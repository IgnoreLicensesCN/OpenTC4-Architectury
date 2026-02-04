package thaumcraft.api.nodes;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;

import java.util.HashMap;
import java.util.Map;

public interface INodeLock {
    ResourceLocation getNodeLockId();

    //returns:should update self
    default boolean nodeLockTick(AbstractNodeBlockEntity thisNode) {
        var level = thisNode.getLevel();
        if (level == null){
            return false;
        }
        if (thisNode.getNodeType() == NodeType.UNSTABLE
                && level.random.nextInt(10000) == 0//TODO:Impl(higher level->5000 not 10000)
        ) {
            thisNode.setNodeType(NodeType.NORMAL);
            return true;
        }
        if (thisNode.getNodeModifier() == NodeModifier.FADING && level.random.nextInt(
                12500) == 0//TODO:Impl(higher level->6750 not 12500)
        ){
            thisNode.setNodeModifier(NodeModifier.PALE);
            return true;
        }
        return false;
    }

    int nodeRegenerationDelayMultiplier();

    boolean allowToAttackAnotherNode();


    final Map<ResourceLocation,INodeLock> nodeLockTypes = new HashMap<ResourceLocation,INodeLock>();
    static INodeLock getNodeLock(ResourceLocation id) {
        return nodeLockTypes.get(id);
    }
    static void registerNodeLock(ResourceLocation id,INodeLock lock) {
        nodeLockTypes.put(id, lock);
    }
}
