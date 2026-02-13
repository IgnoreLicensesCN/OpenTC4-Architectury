package tc4tweak.modules.visrelay;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tc4tweak.CommonUtils;
import tc4tweak.ConfigurationHandler;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.visnet.VisNetNodeBlockEntity;
import thaumcraft.api.visnet.VisNetHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.linearity.opentc4.Consts.TileVisNodeCompoundTagAccessors.TILE_VIS_NODE_LINKS_ACCESSOR;
import static com.linearity.opentc4.Consts.VisNetNodeBlockEntityCompoundTagAccessors.*;

@Deprecated(forRemoval = true)
public class SavedLinkHandler {
    private enum Action {
        RETURN(true),
        SET_PARENT_RETURN(true),
        CLEAR_CONTINUE(false),
        DISABLED(false),
        ;
        private final boolean decision;
        Action(boolean decision) {
            this.decision = decision;
        }

        public boolean returnValue() {
            return decision;
        }
    }

    private static String getNodeType(VisNetNodeBlockEntity node) {
        return node.isSource() ? "Source" : "Relay";
    }

    // true -> RET, false -> resume
    public static boolean processSavedLink(VisNetNodeBlockEntity visNode) {
        WorldCoordinates c = new WorldCoordinates(visNode);
        Action action;
        try {
            action = processSavedLink0(visNode);
        } catch (Exception e) {
            OpenTC4.LOGGER.error("Failed to process saved link. Defaulting to no saved link!", e);
//            visNode.clearSavedLink();
            return false;
        }
        if (ConfigurationHandler.INSTANCE.isSavedLinkDebugEnabled() && action != Action.DISABLED) {
            OpenTC4.LOGGER.info("Processed saved link for node {} at {},{},{}: {}", getNodeType(visNode), c.x, c.y, c.z, action);
        }
        return action.returnValue();
    }

    private static Action processSavedLink0(VisNetNodeBlockEntity visNode) {
        List<BlockPos> link = visNode.getSavedLink();
        if (link == null) return Action.DISABLED;
        BlockPos c = link.get(0);
        Level w = visNode.getLevel();
        if (w == null) return Action.RETURN;
        BlockState bs = w.getBlockState(c);
        if (bs.isAir()) {
            return Action.RETURN;
        }
        BlockEntity tile = w.getBlockEntity(c);
        if (!canConnect(visNode, tile)) {
            // ThE uses a fake TE for cv p2p that is not retrievable via getBlockEntity
            // however it's accessible via VisNetHandler.sources
            HashMap<WorldCoordinates, WeakReference<VisNetNodeBlockEntity>> sourcelist = VisNetHandler.sources.get(w.dimension());
            VisNetNodeBlockEntity sourcenode = CommonUtils.deref(sourcelist.get(new WorldCoordinates(c.getX(),c.getY(),c.getZ(), w.dimension().toString())));
            if (sourcenode == null) {
                visNode.clearSavedLink();
                return Action.CLEAR_CONTINUE;
            }
            tile = sourcenode;
        }
        VisNetNodeBlockEntity next = (VisNetNodeBlockEntity) tile;
        if (next.isSource()) {
            SetParentHelper.setParent(next, visNode);
            w.sendBlockUpdated(visNode.getBlockPos(),visNode.getBlockState(),visNode.getBlockState(),3);
//            w.markBlockForUpdate(visNode.xCoord, visNode.yCoord, visNode.zCoord);
            visNode.parentChanged();
            visNode.clearSavedLink();
            return Action.SET_PARENT_RETURN;
        }
        List<BlockPos> nextLink = next.getSavedLink();
        if (nextLink == null) {
            visNode.clearSavedLink();
            if (VisNetHandler.isNodeValid(next.getRootSource())) {
                SetParentHelper.setParent(next, visNode);
                w.sendBlockUpdated(visNode.getBlockPos(),visNode.getBlockState(),visNode.getBlockState(),3);
//                w.markBlockForUpdate(visNode.xCoord, visNode.yCoord, visNode.zCoord);
                visNode.parentChanged();
                return Action.SET_PARENT_RETURN;
            } else {
                return Action.CLEAR_CONTINUE;
            }
        }
        if (link.size() == 1 || nextLink.get(0).equals(link.get(1))) {
            return Action.RETURN;
        }
        visNode.clearSavedLink();
        return Action.CLEAR_CONTINUE;
    }

    private static boolean canConnect(VisNetNodeBlockEntity node, BlockEntity tile) {
        if (!(tile instanceof VisNetNodeBlockEntity next)) return false;
        if (VisNetHandler.canNodeBeSeen(node, next)) return true;
        return node.getAttunement() == -1 || next.getAttunement() == -1 || next.getAttunement() == node.getAttunement();
    }

    public static void saveAdditional(VisNetNodeBlockEntity thiz, CompoundTag tag) {
        if (thiz.isSource() || !ConfigurationHandler.INSTANCE.isSavedLinkEnabled()) return;
        VisNetNodeBlockEntity root = thiz.getRootSource();
        if (root == null)
            return;
        List<BlockPos> path = new ArrayList<>();
        VisNetNodeBlockEntity node = thiz.getRootSource();
        // historically we store the whole path up to source node (hence the name link
        // but it turns out we only use 2 nodes. more ancient ancestors are prone to all kinds ofAspectVisList weirdness
        // due to unloading order, but 2 nodes seem to stable enough
        while (node != null && (path.size() <= 1 || ConfigurationHandler.INSTANCE.isSavedLinkSaveWholeLink())) {
            path.add(node.getBlockPos());
            node = node.getParent();
        }

        TILE_VIS_NODE_LINKS_ACCESSOR.writeToCompoundTag(tag,path);
        var pos = thiz.getBlockPos();
        OpenTC4.LOGGER.trace("Written link for node {} at {}. {} element.", getNodeType(thiz),
                pos, path.size()
        );
    }
}
