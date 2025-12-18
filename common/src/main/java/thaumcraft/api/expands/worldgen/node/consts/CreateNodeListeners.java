package thaumcraft.api.expands.worldgen.node.consts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import thaumcraft.api.expands.worldgen.node.CreateNodeContext;
import thaumcraft.api.expands.worldgen.node.listeners.CreateNodeListener;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;

public class CreateNodeListeners {
    public static CreateNodeListener DEFAULT_NODE_CREATOR = new CreateNodeListener(0) {
        @Override
        public boolean onCreateNode(Level world, CreateNodeContext context) {
            if (world.getBlockState(new BlockPos(context.x, context.y, context.z)).isAir()) {
                world.setBlock(context.x, context.y, context.z, ConfigBlocks.blockAiry, 0, 0);
            }

            BlockEntity te = world.getBlockEntity(new BlockPos(context.x, context.y, context.z));
            if (te == null) {return }
            if (te instanceof AbstractNodeBlockEntity) {
                AbstractNodeBlockEntity nodeBlockEntity = (AbstractNodeBlockEntity) te;
                nodeBlockEntity.setNodeType(context.nodeType);
                nodeBlockEntity.setNodeModifier(context.nodeModifier);
                nodeBlockEntity.setAspects(context.aspects);
            }

            world.sendBlockUpdated(
                    new BlockPos(context.x, context.y, context.z),
                    te.getBlockState(),
                    te.getBlockState(),
                    3);//idk what to use just put 3
            return false;
        }
    };
}
