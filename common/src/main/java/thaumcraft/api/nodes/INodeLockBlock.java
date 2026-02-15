package thaumcraft.api.nodes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import thaumcraft.common.lib.resourcelocations.NodeLockResourceLocation;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;

import java.util.HashMap;
import java.util.Map;

//impl on Block and will tick by node.(so that there would be less BE)
//always considered below a node(unless override AbstractNodeBlockEntity#getNodeLockPos)
//always:considered deactivated if redstone signal charged
public interface INodeLockBlock {
    NodeLockResourceLocation getNodeLockId();

    //returns:should update self
    default boolean nodeLockTick(AbstractNodeBlockEntity thisNode) {
        var level = thisNode.getLevel();
        if (level == null){
            return false;
        }
        if (thisNode.getNodeType() == NodeType.UNSTABLE
                && level.random.nextInt(10000) == 0
        ) {
            thisNode.setNodeType(NodeType.NORMAL);
            return true;
        }
        if (thisNode.getNodeModifier() == NodeModifier.FADING && level.random.nextInt(
                12500) == 0
        ){
            thisNode.setNodeModifier(NodeModifier.PALE);
            return true;
        }
        return false;
    }

    int nodeRegenerationDelayMultiplier(Level atLevel, BlockPos lockAtPos);

    default boolean allowToAttackAnotherNode(Level atLevel, BlockPos lockAtPos){return true;}
    default boolean allowBeingAttackedFromAnotherNode(Level atLevel, BlockPos lockAtPos){return false;}


    Map<NodeLockResourceLocation, INodeLockBlock> nodeLockTypes = new HashMap<>();
    static INodeLockBlock getNodeLock(NodeLockResourceLocation id) {
        return nodeLockTypes.get(id);
    }
    static void registerNodeLock(NodeLockResourceLocation id, INodeLockBlock lock) {
        nodeLockTypes.put(id, lock);
    }
}
