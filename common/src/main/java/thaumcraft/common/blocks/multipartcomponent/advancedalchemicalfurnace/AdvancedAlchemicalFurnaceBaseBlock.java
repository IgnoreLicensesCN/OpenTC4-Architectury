package thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.fx.migrated.particles.FXSlimyBubble;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceBlockEntity;

public class AdvancedAlchemicalFurnaceBaseBlock extends AbstractAdvancedAlchemicalFurnaceComponent
        implements EntityBlock {
    private static final VoxelShape SHAPE_FULL =
            Shapes.block(); // 等价 0~16 全立方体

    private static final VoxelShape SHAPE_LOW =
            Block.box(0, 0, 0, 16, 11.2, 16); // 0.7 * 16 = 11.2

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (collisionContext instanceof EntityCollisionContext entityCollisionContext) {
            if (entityCollisionContext.getEntity() instanceof ItemEntity) {
                return SHAPE_LOW;
            }
        }
        return SHAPE_FULL;
    }

    public static final BlockPos SELF_POS_1_0_1 = new BlockPos(1, 0, 1);

    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return SELF_POS_1_0_1;
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.setBlock(pos, ThaumcraftBlocks.ALCHEMICAL_FURNACE.defaultBlockState(), 3);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this) {
            return new AdvancedAlchemicalFurnaceBlockEntity(blockPos, blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.ADVANCED_ALCHEMICAL_FURNACE && blockState.getBlock() == this && !level.isClientSide()) {
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof AdvancedAlchemicalFurnaceBlockEntity furnace) {
                    furnace.serverTick();
                }
            });
        }
        return null;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);
        if (entity instanceof ItemEntity itemEntity && level.getBlockEntity(
                blockPos) instanceof AdvancedAlchemicalFurnaceBlockEntity furnace) {
            furnace.handleItemEntity(itemEntity);
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource rand) {
        if (level.getBlockEntity(
                blockPos) instanceof AdvancedAlchemicalFurnaceBlockEntity furnace && furnace.aspects.visSize() > 0 && level.isClientSide) {
            if (level instanceof ClientLevel clientLevel) {
                var x = blockPos.getX();
                var y = blockPos.getY();
                var z = blockPos.getZ();
                FXSlimyBubble ef = new FXSlimyBubble(
                        clientLevel,
                        (float) x + rand.nextFloat(),
                        y + 1,
                        (float) z + rand.nextFloat(),
                        0.06F + rand.nextFloat() * 0.06F
                );
                ef.setAlphaF(0.8F);
                ef.setRBGColorF(0.6F - rand.nextFloat() * 0.2F, 0.0F, 0.6F + rand.nextFloat() * 0.2F);
                Minecraft.getInstance().particleEngine.add(ef);

                if (rand.nextInt(50) == 0) {
                    double var21 = (float) x + rand.nextFloat();
                    double var22 = (double) y + 1;
                    double var23 = (float) z + rand.nextFloat();
                    level.playSound(
                            null,
                            var21,
                            var22,
                            var23,
                            SoundEvents.LAVA_POP,
                            SoundSource.BLOCKS,
                            0.1F + rand.nextFloat() * 0.1F,
                            0.9F + rand.nextFloat() * 0.15F
                    );
                }

                int q = rand.nextInt(2);
                int w = rand.nextInt(2);
                FXSlimyBubble ef2 = new FXSlimyBubble(
                        clientLevel,
                        (double) x - 0.6 + (double) rand.nextFloat() * 0.2 + (double) (q * 2),
                        y + 2,
                        (double) z - 0.6 + (double) rand.nextFloat() * 0.2 + (double) (w * 2),
                        0.06F + rand.nextFloat() * 0.06F
                );
                ef2.setAlphaF(0.8F);
                ef2.setRBGColorF(0.6F - rand.nextFloat() * 0.2F, 0.0F, 0.6F + rand.nextFloat() * 0.2F);
                Minecraft.getInstance().particleEngine.add(ef2);
            }

        }
    }
}
