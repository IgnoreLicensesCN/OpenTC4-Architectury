package thaumcraft.common.blocks.crafted.noderelated;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import thaumcraft.api.nodes.INodeLockBlock;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.abstracts.AbstractNodeLockBlock;
import thaumcraft.common.lib.resourcelocations.NodeLockResourceLocation;

//TODO:Render(use client-local map to keep state)
public class AdvancedNodeStabilizerBlock extends AbstractNodeLockBlock {
    public static final NodeLockResourceLocation NODE_LOCK_ID = NodeLockResourceLocation.of(Thaumcraft.MOD_ID,"advanced_node_stabilizer");
    public AdvancedNodeStabilizerBlock(Properties properties) {
        super(properties);
    }
    public AdvancedNodeStabilizerBlock() {
        super();
        INodeLockBlock.registerNodeLock(NODE_LOCK_ID,this);
    }

    @Override
    public NodeLockResourceLocation getNodeLockId() {
        return NODE_LOCK_ID;
    }

    @Override
    public int nodeRegenerationDelayMultiplier(Level atLevel, BlockPos lockAtPos) {
        return 20;
    }
}
