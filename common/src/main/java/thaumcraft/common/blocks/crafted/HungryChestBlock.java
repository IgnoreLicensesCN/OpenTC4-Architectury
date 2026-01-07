package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.HungryChestBlockEntity;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.List;
import java.util.function.Supplier;

public class HungryChestBlock extends ChestBlock implements EntityBlock {
    public HungryChestBlock(Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(properties, supplier);
    }
    public HungryChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        this(BlockBehaviour.Properties.copy(Blocks.CHEST),supplier);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(ChestBlock.TYPE, ChestType.SINGLE);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2).setValue(TYPE, ChestType.SINGLE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HungryChestBlockEntity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType != ThaumcraftBlockEntities.HUNGRY_CHEST){
            return null;
        }
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (level1 == null || level1.isClientSide()) return;
            if (!(blockEntity instanceof HungryChestBlockEntity hungryChest)){return;}
            if (!hungryChest.eatingCooldown()){return;}

            BlockPos above = blockPos.above();

            List<ItemEntity> items = level1.getEntitiesOfClass(ItemEntity.class,
                    new AABB(above));

            for (ItemEntity itemEntity : items) {
                ItemStack stack = itemEntity.getItem();
                hungryChest.eating.set(true);
                hungryChest.startOpen(null);
                ItemStack leftover = hungryChest.addItem(stack.copy());
                if (leftover.isEmpty()) {
                    itemEntity.remove(Entity.RemovalReason.DISCARDED);
                } else {
                    itemEntity.setItem(leftover);
                }
                hungryChest.stopOpen(null);
                hungryChest.addEatingCooldownForEating();
                break;
            }
        };
    }

}
