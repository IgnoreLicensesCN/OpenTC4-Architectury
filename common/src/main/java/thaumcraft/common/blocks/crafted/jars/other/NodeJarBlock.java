package thaumcraft.common.blocks.crafted.jars.other;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.crafted.jars.JarBlock;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.items.misc.jars.NodeJarBlockItem;
import thaumcraft.common.tiles.crafted.jars.NodeJarBlockEntity;
import thaumcraft.common.tiles.node.NodeBlockEntity;

public class NodeJarBlock extends JarBlock implements
        EntityBlock,
        IWandInteractableBlock {
    public NodeJarBlock(Properties properties) {
        super(properties);
    }
    public NodeJarBlock() {
        super(JAR_PROPERTIES);
    }

    public NodeJarBlockItem getNodeJarItem() {
        return ThaumcraftItems.NODE_JAR;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, pos, blockState, livingEntity, itemStack);
        if (level.isClientSide) return;

        if (level.getBlockEntity(pos) instanceof NodeJarBlockEntity jar && itemStack.getItem() instanceof NodeJarBlockItem jarItem) {
            jar.nodeInfo = jarItem.getNodeInfo(itemStack);
            jar.markDirtyAndUpdateSelf();
        }
    }



    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new NodeJarBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        if (!level.isClientSide()) {
            var pos = useOnContext.getClickedPos();
            if (level.getBlockEntity(pos) instanceof NodeJarBlockEntity jar) {
                var nodeInfo = jar.nodeInfo;
                level.setBlockAndUpdate(pos, ThaumcraftBlocks.AURA_NODE.defaultBlockState());
                NodeBlockEntity nodeBlockEntity = (NodeBlockEntity) level.getBlockEntity(pos);
                if (nodeBlockEntity != null) {
                    nodeBlockEntity.readNodeInfo(nodeInfo);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level.getBlockEntity(blockPos) instanceof NodeJarBlockEntity jar) {
            var nodeInfo = jar.nodeInfo;
            var jarItem = getNodeJarItem();
            var stackToDrop = new ItemStack(jarItem);
            jarItem.setNodeInfo(stackToDrop,nodeInfo);
            var posCenter = blockPos.getCenter();
            level.addFreshEntity(new ItemEntity(level,posCenter.x,posCenter.y,posCenter.z,stackToDrop));
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }
}
