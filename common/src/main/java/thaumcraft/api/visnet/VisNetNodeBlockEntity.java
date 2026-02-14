package thaumcraft.api.visnet;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tc4tweak.ConfigurationHandler;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.lib.resourcelocations.VisNetNodeTypeResourceLocation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.linearity.opentc4.Consts.TileVisNodeCompoundTagAccessors.TILE_VIS_NODE_LINKS_ACCESSOR;
import static thaumcraft.api.visnet.VisNetHandler.cache;
import static thaumcraft.api.visnet.VisNetHandler.nearbyNodes;

public abstract class VisNetNodeBlockEntity extends TileThaumcraft {
    public VisNetNodeBlockEntity(BlockEntityType<? extends VisNetNodeBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    @NotNull WeakReference<VisNetNodeBlockEntity> parent = new WeakReference<>(null);
    @NotNull List<WeakReference<VisNetNodeBlockEntity>> children = new ArrayList<>();
    @NotNull List<BlockPos> loadedLink = new ArrayList<>();
    protected int nodeCounter = 0;
    private boolean nodeRegged = false;
    public boolean nodeRefresh = false;

    /**
     * @return the number ofAspectVisList blocks away this node will check for parent nodes to connect to.
     */
    public abstract int getRange();

    /**
     * @return true if this is the source or root node ofAspectVisList the vis network.
     */
    public abstract boolean isSource();

    /**
     * This method should never be called directly. Use {@link VisNetHandler#drainVis(Level, int, int, int, Aspect, int)} instead
     * @param aspect what aspect to drain
     * @param vis how much to drain
     * @return how much was actually drained
     */
    public int consumeCentiVis(Aspect aspect, int vis) {
        var parent = getParent();
        if (parent != null) {
            int out = parent.consumeCentiVis(aspect, vis);
            if (out>0) {
                triggerConsumeEffect(aspect);
            }
            return out;
        }
        return 0;
    }
    public void triggerConsumeEffect(Aspect aspect) {	}
    /**
     * @return
     */
    @Nullable
    public VisNetNodeBlockEntity getParent() {
        return parent.get();
    }

    public VisNetNodeBlockEntity getRootSource() {
        var parent = getParent();
        return parent != null ?
                parent.getRootSource() : this.isSource() ?
                this : null;
    }
    public WeakReference<VisNetNodeBlockEntity> getRootSourceRef() {
        return new WeakReference<>(getRootSource());
    }
    public void setParent(WeakReference<VisNetNodeBlockEntity> parent) {
        if (parent != null) {
            this.parent = parent;
        }else {
            this.parent.clear();
        }
    }
    public void setParent(VisNetNodeBlockEntity parent) {
        setParent(new WeakReference<>(parent));
    }
    public void removeParent(){
        setParent(new WeakReference<>(null));
    }
    public List<WeakReference<VisNetNodeBlockEntity>> getChildren() {
        return children;
    }
    public WorldCoordinates getLocation() {
        return new WorldCoordinates(this);
    }
    public void tick() {
        if (processSavedLink()){return;}
        if (level == null) {return;}
        if (Platform.getEnvironment() != Env.CLIENT && ((nodeCounter++) % 40==0 || nodeRefresh)) {
            var level = getLevel();
            if (level == null) {return;}
            //check for changes
            if (!nodeRefresh
                    && !children.isEmpty()) {
                for (var n:children) {
                    if (n==null || n.get()==null || !VisNetHandler.canNodeBeSeen(this, n.get())) {
                        nodeRefresh=true;
                        break;
                    }
                }
            }

            //refresh linked nodes
            if (nodeRefresh) {
                for (var toRefreshRef:children) {
                    var toRefresh = toRefreshRef.get();
                    if (toRefresh!=null) {
                        toRefresh.nodeRefresh=true;
                    }
                }
                this.children.clear();
                setParent(new WeakReference<>(null));
            }

            //redo stuff
            if (isSource() && !nodeRegged) {
                VisNetHandler.addSource(level, this);
                nodeRegged = true;
            } else if (!isSource() && getParent() != null) {
                setParent(VisNetHandler.addNode(level, this));
                nodeRefresh=true;
            }

            if (nodeRefresh) {
                level.blockUpdated(this.worldPosition,this.getBlockState().getBlock());
                parentChanged();
            }
            nodeRefresh=false;
        }

    }
    
    public void parentChanged() { }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (this.isSource()
                || !TILE_VIS_NODE_LINKS_ACCESSOR.compoundTagHasKey(tag)
                || !ConfigurationHandler.INSTANCE.isSavedLinkEnabled()
        ) {
            this.loadedLink =  new ArrayList<>();
            return;
        }
        this.loadedLink =  TILE_VIS_NODE_LINKS_ACCESSOR.readFromCompoundTag(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {

        if (this.isSource() || !ConfigurationHandler.INSTANCE.isSavedLinkEnabled()) return;
        VisNetNodeBlockEntity root = this.getRootSource();
        if (root == null)
            return;
        List<BlockPos> path = new ArrayList<>();
        VisNetNodeBlockEntity node = this.getRootSource();
        // historically we store the whole path up to source node (hence the name link
        // but it turns out we only use 2 nodes. more ancient ancestors are prone to all kinds ofAspectVisList weirdness
        // due to unloading order, but 2 nodes seem to stable enough
        while (node != null && (path.size() <= 1 || ConfigurationHandler.INSTANCE.isSavedLinkSaveWholeLink())) {
            path.add(node.getBlockPos());
            node = node.getParent();
        }

        TILE_VIS_NODE_LINKS_ACCESSOR.writeToCompoundTag(tag,path);
        var pos = this.getBlockPos();
        OpenTC4.LOGGER.trace("Written link for node {} at {}. {} element.", getVisNetNodeType(this),
                pos, path.size()
        );
    }
    
    protected boolean processSavedLink() {
        WorldCoordinates c = new WorldCoordinates(this);
        Action action;
        try {
            action = processSavedLink0();
        } catch (Exception e) {
            OpenTC4.LOGGER.error("Failed to process saved link. Defaulting to no saved link!", e);
//            visNode.clearSavedLink();
            return false;
        }
        if (ConfigurationHandler.INSTANCE.isSavedLinkDebugEnabled() && action != Action.DISABLED) {
            OpenTC4.LOGGER.info("Processed saved link for node {} at {},{},{}: {}", getVisNetNodeType(this), c.x, c.y, c.z, action);
        }
        return action.returnValue();
    }

    public enum Action {
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
    protected Action processSavedLink0() {
        List<BlockPos> link = this.getSavedLink();
        if (link == null) return Action.DISABLED;
        BlockPos c = link.get(0);
        Level w = this.getLevel();
        if (w == null) return Action.RETURN;
        BlockState bs = w.getBlockState(c);
        if (bs.isAir()) {
            return Action.RETURN;
        }
        BlockEntity tile = w.getBlockEntity(c);
        if (!(tile instanceof VisNetNodeBlockEntity visNetNodeBlockEntity && canConnect(visNetNodeBlockEntity))) {
            // ThE uses a fake TE for cv p2p that is not retrievable via getBlockEntity
            // however it's accessible via VisNetHandler.sources
            HashMap<WorldCoordinates, WeakReference<VisNetNodeBlockEntity>> sourcelist = VisNetHandler.sources.get(w.dimension());
            VisNetNodeBlockEntity sourcenode = sourcelist.get(new WorldCoordinates(c.getX(),c.getY(),c.getZ(), w.dimension().toString())).get();
            if (sourcenode == null) {
                this.clearSavedLink();
                return Action.CLEAR_CONTINUE;
            }
            tile = sourcenode;
        }
        VisNetNodeBlockEntity next = (VisNetNodeBlockEntity) tile;
        if (next.isSource()) {
            setParentAndClear(next);
            w.sendBlockUpdated(this.getBlockPos(),this.getBlockState(),this.getBlockState(),3);
            this.parentChanged();
            this.clearSavedLink();
            return Action.SET_PARENT_RETURN;
        }
        List<BlockPos> nextLink = next.getSavedLink();
        if (nextLink == null) {
            this.clearSavedLink();
            var rootSource = next.getRootSource();
            if (rootSource != null) {
                setParentAndClear(next);
                w.sendBlockUpdated(this.getBlockPos(),this.getBlockState(),this.getBlockState(),3);
//                w.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                this.parentChanged();
                return Action.SET_PARENT_RETURN;
            } else {
                return Action.CLEAR_CONTINUE;
            }
        }
        if (link.size() == 1 || nextLink.get(0).equals(link.get(1))) {
            return Action.RETURN;
        }
        clearSavedLink();
        return Action.CLEAR_CONTINUE;
    }

    /**
     * only affects VisNetNode connection and color(not really for aspect)
     * like channel.but put 0 for any "channel".
     * --IgnoreLicensesCN
     * @return the type ofAspectVisList shard this is attuned to:
     * none 0, <s>air 1, fire 2, water 3, earth 4, order 5, entropy 6
     * Should return -1 for most implementations</s>
     * (wont make sure which aspect it shows anymore.depends on BE behavior)
     */
    public int getAttunement() {
        return 0;
    }
    
    protected boolean canConnect(VisNetNodeBlockEntity anotherNode) {
        if (VisNetHandler.canNodeBeSeen(this, anotherNode)) return true;
        return this.getAttunement() == -1 || anotherNode.getAttunement() == -1 || anotherNode.getAttunement() == this.getAttunement();
    }

    protected VisNetNodeTypeResourceLocation getVisNetNodeType(VisNetNodeBlockEntity node) {
        return node.isSource() ? SOURCE : RELAY;
    }

    public List<BlockPos> getSavedLink() {
        return loadedLink;
    }

    public void clearSavedLink() {
        this.loadedLink = new ArrayList<>();
    }
    protected void setParentAndClear(VisNetNodeBlockEntity parent) {
        OpenTC4.LOGGER.trace("Force set parent ofAspectVisList {} ({}) to {} ({})", this.getClass().getSimpleName(), this.getBlockPos(), parent.getClass().getSimpleName(), parent.getBlockPos());
        WeakReference<VisNetNodeBlockEntity> ref = new WeakReference<>(this);
        this.setParent(new WeakReference<>(parent));
        parent.getChildren().add(ref);
        nearbyNodes.clear();
        cache.clear();
//        CommonUtils.sendSupplementaryS35(parent);
        if (level != null){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
    
    public static final VisNetNodeTypeResourceLocation SOURCE = VisNetNodeTypeResourceLocation.of("thaumcraft","source");
    public static final VisNetNodeTypeResourceLocation RELAY = VisNetNodeTypeResourceLocation.of("thaumcraft","relay");

    @Override
    public void setRemoved() {
        super.setRemoved();
        removeThisNode();
    }

    public void removeThisNode() {
        for (var ref:getChildren()) {
            var child = ref.get();
            if (child!=null) {
                child.removeThisNode();
            }
        }

        this.children = new ArrayList<>();
        var parent = this.getParent();
        if (parent != null) {
            parent.nodeRefresh=true;
        }
        this.removeParent();
        this.parentChanged();
        Level worldObj = getLevel();
        if (worldObj == null) {
            throw new NullPointerException("worldObj is null");
        }
        if (this.isSource()) {
            ResourceKey<Level> key = worldObj.dimension();
            var sourcelist = VisNetHandler.sources.get(key);
            if (sourcelist==null) {
                sourcelist = new HashMap<>();
            }
            sourcelist.remove(getLocation());
            VisNetHandler.sources.put( key, sourcelist );
        }
        BlockPos pos = this.worldPosition;
        BlockState state = worldObj.getBlockState(pos);
        worldObj.sendBlockUpdated(pos,state,state,3);
    }

}
