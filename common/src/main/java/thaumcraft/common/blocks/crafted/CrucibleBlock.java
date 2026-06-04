package thaumcraft.common.blocks.crafted;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableBlockOrBlockEntity;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.abstracts.AbstractLiquidFillInBlock;
import thaumcraft.common.tiles.crafted.CrucibleBlockEntity;

public class CrucibleBlock
        extends AbstractLiquidFillInBlock
        implements IWandInteractableBlockOrBlockEntity
{
    public CrucibleBlock(Properties properties) {
        super(properties);
    }

    public CrucibleBlock() {
        this(Properties.of()
                .strength(3,17)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
        );
    }


    public static final VoxelShape CRUCIBLE_SHAPE = Shapes.join(
            Shapes.box(0.0, 0.0, 0.0, 1, 0.85, 1),
            Shapes.box(0.125, 5.0/16., 2.0/16., 14.0/16., 0.85, 14.0/16.),
            BooleanOp.ONLY_FIRST);

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return CRUCIBLE_SHAPE;
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this) {
            return new CrucibleBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == null) return null;
        if (!level.isClientSide) {
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof CrucibleBlockEntity crucible) {
                    crucible.serverTick();
                }
            });
        }
        return ((level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof CrucibleBlockEntity crucible) {
                crucible.clientTick();
            }
        });
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);
        if (!level.isClientSide && level.getBlockEntity(blockPos) instanceof CrucibleBlockEntity crucible) {
            crucible.entityInside(entity);
        }
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var player = useOnContext.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        var sneaking = player.isCrouching();
        if (!sneaking) {
            return InteractionResult.PASS;
        }
        var level = useOnContext.getLevel();
        var pos = useOnContext.getClickedPos();
        if (level.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
            crucible.spillRemnants();
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(10) == 0) {
            if (level.getBlockEntity(blockPos) instanceof CrucibleBlockEntity crucible
                    && crucible.getFluidAmount() > 0
                    && crucible.boiled()) {
                level.playSound(
                        null,
                        blockPos,
                        SoundEvents.LAVA_POP,
                        SoundSource.BLOCKS,
                        0.1F + randomSource.nextFloat() * 0.1F,
                        1.2F + randomSource.nextFloat() * 0.2F);
            }
        }
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (level.isClientSide){
            if (level instanceof ClientLevel clientLevel) {
                if (i == 1){
                    ClientFXUtils.blockSparkle(
                            clientLevel,
                            blockPos.getX(),
                            blockPos.getY(),
                            blockPos.getZ(),
                            -9999,
                            5
                    );
                    return true;
                } else if (i == 2) {
                    ClientFXUtils.crucibleBoilSound(
                            clientLevel,
                            blockPos.getX(),
                            blockPos.getY(),
                            blockPos.getZ()
                    );

                    if (level.getBlockEntity(blockPos) instanceof CrucibleBlockEntity crucible) {
                        for(int q = 0; q < 10; ++q) {
                            ClientFXUtils.crucibleBoil(clientLevel,
                                    blockPos.getX(),
                                    blockPos.getY(),
                                    blockPos.getZ(),
                                    crucible,
                                    j);
                        }
                    }
                    return true;
                }
            }
        }

        return super.triggerEvent(blockState, level, blockPos, i, j);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof CrucibleBlockEntity crucible) {
                crucible.spillRemnants();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
