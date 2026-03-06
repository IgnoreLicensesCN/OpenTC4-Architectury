package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.CrucibleBlockEntity;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class CrucibleBlock
        extends SuppressedWarningBlock
        implements
        EntityBlock,
        IWandInteractableBlock
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
        // metadata == 0 对应的逻辑
        return CRUCIBLE_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
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
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        if (level.getBlockEntity(pos) instanceof CrucibleBlockEntity be) {
            ItemStack stack = player.getItemInHand(hand);
            var fluidStack = platformUtils.copyFluidStackFromItemStack(stack);
            if (fluidStack != null) {
                var inserted = be.insertFluid(fluidStack.getFluid(),fluidStack.getAmount());
                if (inserted != 0){
                    fluidStack.setAmount(fluidStack.getAmount()-inserted);
                    platformUtils.decreaseFluidStackToItemStack(stack,fluidStack);
                    return InteractionResult.CONSUME;
                }
            }

        }
        return InteractionResult.PASS;
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
        return ;//TODO:Clear
    }
}
