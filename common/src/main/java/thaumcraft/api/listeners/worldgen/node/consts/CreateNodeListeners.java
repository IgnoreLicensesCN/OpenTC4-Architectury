package thaumcraft.api.listeners.worldgen.node.consts;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import thaumcraft.api.listeners.worldgen.node.CreateNodeContext;
import thaumcraft.api.listeners.worldgen.node.listeners.CreateNodeListener;
import thaumcraft.api.nodes.INodeBlockEntity;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;

public class CreateNodeListeners {
    public static CreateNodeListener DEFAULT_NODE_CREATOR = new CreateNodeListener(0) {
        @Override
        public boolean onCreateNode(Level world, CreateNodeContext context) {
            onCreateNode((WorldGenLevel) world, context);
            BlockEntity te = world.getBlockEntity(context.pos);
            if (te == null) {
                return false;
            }
            if (te instanceof INodeBlockEntity nodeBlockEntity) {
                nodeBlockEntity.setNodeType(context.nodeType);
                nodeBlockEntity.setNodeModifier(context.nodeModifier);
                nodeBlockEntity.setAspectsWithBase(context.aspects);
            }
            world.setBlockAndUpdate(
                    context.pos,
                    te.getBlockState()
            );
            return false;
        }

        @Override
        public boolean onCreateNode(WorldGenLevel world, CreateNodeContext context) {
            if (world.getBlockState(context.pos).isAir()) {
                world.setBlock(context.pos, ThaumcraftBlocks.AURA_NODE.defaultBlockState(), 3);
            }

            BlockEntity te = world.getBlockEntity(context.pos);
            if (te == null) {return false;}
            if (te instanceof AbstractNodeBlockEntity nodeBlockEntity) {
                nodeBlockEntity.setNodeType(context.nodeType);
                nodeBlockEntity.setNodeModifier(context.nodeModifier);
                nodeBlockEntity.setAspectsWithBase(context.aspects);
            }
            return false;
        }
    };
}
