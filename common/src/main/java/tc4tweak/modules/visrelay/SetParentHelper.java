package tc4tweak.modules.visrelay;

import com.linearity.opentc4.OpenTC4;
import tc4tweak.CommonUtils;
import thaumcraft.api.visnet.TileVisNode;

import java.lang.ref.WeakReference;

import static thaumcraft.api.visnet.VisNetHandler.cache;
import static thaumcraft.api.visnet.VisNetHandler.nearbyNodes;


public class SetParentHelper {

    public static void setParent(TileVisNode parent, TileVisNode child) {
        OpenTC4.LOGGER.trace("Force set parent of {} ({}) to {} ({})", child.getClass().getSimpleName(), child.getBlockPos(), parent.getClass().getSimpleName(), parent.getBlockPos());
        WeakReference<TileVisNode> ref = new WeakReference<>(child);
        child.setParent(new WeakReference<>(parent));
        parent.getChildren().add(ref);
        nearbyNodes.clear();
        cache.clear();
        CommonUtils.sendSupplementaryS35(parent);
    }
}
