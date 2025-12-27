package thaumcraft.api.nodes;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;

import java.util.HashMap;
import java.util.Map;

public interface INodeLock {
    ResourceLocation getNodeLockId();

    //returns:should update self
    boolean nodeLockTick(AbstractNodeBlockEntity node);

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
