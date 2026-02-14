package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

//TODO:i dont remember if it's reliable,verify it
public abstract class FiniteLiquidBlock extends LiquidBlock {

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 8);
    private final Direction gravity;

    public FiniteLiquidBlock(FlowingFluid fluid, Properties props, Direction gravity) {
        super(fluid, props);
        this.gravity = gravity;
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 8));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return false;
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        int myLevel = state.getValue(LEVEL);
        if (myLevel == 0) throw new RuntimeException("Level 0 invalid!");
        int remaining = myLevel;

        // ----------------------------------------
        // 重力方向扩散
        // ----------------------------------------
        BlockPos gravityPos = pos.relative(gravity);
        BlockState gravityState = level.getBlockState(gravityPos);
        if (canFlowInto(level, gravityPos, gravityState, remaining, true)) {
            remaining = flowTo(level, pos, gravityPos, state, true);
            // 流向重力方向后就不进行横向流动
            return;
        }

        // ----------------------------------------
        // 横向扩散（每次传递1）
        // ----------------------------------------
        if (remaining > 1) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos n = pos.relative(dir);
                BlockState nState = level.getBlockState(n);
                if (canFlowInto(level, n, nState, remaining, false)) {
                    remaining = flowTo(level, pos, n, state, false);
                }
            }
        }

    }
    private boolean canFlowInto(Level level, BlockPos pos, BlockState state, int myLevel, boolean isGravityDir) {
        if (state.isAir()) return true;

        if (state.getBlock() instanceof FiniteLiquidBlock fblock) {
            int theirLevel = state.getValue(LEVEL);
            if (isGravityDir) {
                // 重力方向：只要没有满8就可以补 LEVEL
                return theirLevel < 8;
            } else {
                // 横向/斜向：只能向比自己低的方块传递 1
                return myLevel > theirLevel;
            }
        }
        return false;
    }

    private int flowTo(Level level, BlockPos from, BlockPos to, BlockState fromState, boolean isGravityDir) {
        int myLevel = fromState.getValue(LEVEL);
        if (myLevel <= 0) throw new RuntimeException("LEVEL=0 invalid!");

        BlockState dst = level.getBlockState(to);
        int remaining = myLevel;

        if (dst.isAir()) {
            int move = isGravityDir ? myLevel : 1;
            level.setBlock(to, defaultBlockState().setValue(LEVEL, move), 3);
            remaining -= move;
            level.setBlock(from, fromState.setValue(LEVEL, remaining), 3);
        } else if (dst.getBlock() instanceof FiniteLiquidBlock finiteLiquidBlock && (finiteLiquidBlock.arch$getFluid() == this.arch$getFluid())
        ) {
            int theirLevel = dst.getValue(LEVEL);
            if (isGravityDir) {
                int transfer = Math.min(myLevel, 8 - theirLevel);
                level.setBlock(to, dst.setValue(LEVEL, theirLevel + transfer), 3);
                remaining -= transfer;
                level.setBlock(from, fromState.setValue(LEVEL, remaining), 3);
            } else {
                if (myLevel > theirLevel) {
                    level.setBlock(to, dst.setValue(LEVEL, theirLevel + 1), 3);
                    remaining -= 1;
                    level.setBlock(from, fromState.setValue(LEVEL, remaining), 3);
                }
            }
        }

        return remaining;
    }

}

