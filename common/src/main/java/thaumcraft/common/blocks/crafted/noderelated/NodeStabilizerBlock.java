package thaumcraft.common.blocks.crafted.noderelated;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import thaumcraft.api.nodes.INodeLockBlock;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.abstracts.AbstractNodeLockBlock;
import thaumcraft.common.lib.resourcelocations.NodeLockResourceLocation;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;

//TODO:Render(use client-local map to keep state)
public class NodeStabilizerBlock extends AbstractNodeLockBlock {
    public static final NodeLockResourceLocation NODE_LOCK_ID = NodeLockResourceLocation.of(Thaumcraft.MOD_ID,"node_stabilizer");
    public NodeStabilizerBlock(Properties properties) {
        super(properties);
    }
    public NodeStabilizerBlock() {
        super();
        INodeLockBlock.registerNodeLock(NODE_LOCK_ID,this);
    }

    @Override
    public NodeLockResourceLocation getNodeLockId() {
        return NODE_LOCK_ID;
    }

    @Override
    public int nodeRegenerationDelayMultiplier(Level atLevel, BlockPos lockAtPos) {
        return 2;
    }
    @Override
    public boolean nodeLockTick(AbstractNodeBlockEntity thisNode) {
        var level = thisNode.getLevel();
        if (level == null){
            return false;
        }
        if (thisNode.getNodeType() == NodeType.UNSTABLE
                && level.random.nextInt(5000) == 0
        ) {
            thisNode.setNodeType(NodeType.NORMAL);
            return true;
        }
        if (thisNode.getNodeModifier() == NodeModifier.FADING && level.random.nextInt(
                6750) == 0
        ){
            thisNode.setNodeModifier(NodeModifier.PALE);
            return true;
        }
        return false;
    }


}
