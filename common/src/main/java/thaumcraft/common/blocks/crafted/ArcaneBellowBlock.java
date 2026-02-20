package thaumcraft.common.blocks.crafted;

import com.linearity.opentc4.mixinaccessors.AbstractFurnaceBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.blocks.abstracts.IFurnaceAttachmentBlock;
import thaumcraft.common.blocks.abstracts.IInfernalFurnaceTickDiscounter;
import thaumcraft.common.tiles.abstracts.IThaumcraftFurnace;

import java.util.Objects;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

//TODO:Render(BlockEntity may required)
public class ArcaneBellowBlock extends SuppressedWarningBlock implements IInfernalFurnaceTickDiscounter, IFurnaceAttachmentBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public ArcaneBellowBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING,Direction.NORTH)
        );
    }
    public ArcaneBellowBlock() {
        super(
                Properties.of()
                        .strength(2.5F, 10.F)
                        .sound(SoundType.WOOD)
        );

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING,Direction.NORTH)
        );
    }

    public static final VoxelShape SHAPE = Block.box(16*0.1, 0, 16*0.1, 16*0.9, 16, 16*0.9);
    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // 设置放置时的朝向
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // 让方块朝向玩家背后
        Direction dir = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, dir);
    }

    @Override
    public int getInfernalFurnaceTickDiscount(Level level, BlockState state, BlockPos pos, Direction furnaceBlockExposedDirection) {
        if (level.hasNeighborSignal(pos)){
            return 0;
        }
        if (!(state.getBlock() instanceof ArcaneBellowBlock)) {
            return 0;
        }
        var facingDir = state.getValue(FACING);
        if (facingDir != furnaceBlockExposedDirection.getOpposite()){
            return 0;
        }
        return 20;
    }

    protected boolean attachedToBlock(Level level,BlockPos attachmentPos,BlockState attachmentState,BlockPos toAttachPos){
        if (level.hasNeighborSignal(attachmentPos)){
            return false;
        }
        if (!(attachmentState.getBlock() == this)) {
            return false;
        }
        var facingDir = attachmentState.getValue(FACING);
        if (!Objects.equals(attachmentPos.relative(facingDir),toAttachPos)){
            return false;
        }
        return true;
    }

    @Override
    public void onVanillaFurnaceBurning(Level level, AbstractFurnaceBlockEntity furnaceBlockEntity, BlockState furnaceState, BlockPos furnacePos, BlockState attachmentState, BlockPos attachmentPos, CallbackInfo ci) {
        if (!attachedToBlock(level, attachmentPos, attachmentState, furnacePos)) {
            return;
        }
        AbstractFurnaceBlockEntityAccessor accessor = (AbstractFurnaceBlockEntityAccessor) furnaceBlockEntity;
        var tickCount = accessor.opentc4$getTickCount();
        if ((tickCount) % 3 == 0 && accessor.opentc4$isLit()) {
            var currentProgress = accessor.opentc4$getCookingProgress();
            var currentMaxProgress = accessor.opentc4$getCookingTotalTime();
            accessor.opentc4$setCookingProgress(Math.min(currentMaxProgress-1, currentProgress + 1));
//            AbstractFurnaceBlockEntity.serverTick(level,furnacePos,furnaceState,furnaceBlockEntity);
        }
    }

    @Override
    public void attachmentOnCalculatingRequiredCookTime(Level level, IThaumcraftFurnace be, BlockState furnaceState, BlockPos furnacePos, BlockState attachmentState, BlockPos attachmentPos, int defaultFurnaceRequiredCookTime) {
        if (!attachedToBlock(level, attachmentPos, attachmentState, furnacePos)) {
            return;
        }
        be.setRequiredCookTime((int)(be.getRequiredCookTime() - defaultFurnaceRequiredCookTime*0.125));
    }
}
