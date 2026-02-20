package thaumcraft.common.blocks.crafted;

import com.linearity.colorannotation.annotation.RGBColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.crafting.interfaces.IInfusionStabiliser;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

public class TallowCandleBlock extends SuppressedWarningBlock implements IInfusionStabiliser {
    public static final IntegerProperty WAX = IntegerProperty.create("wax", 1, 5);
    public final int color;
    public TallowCandleBlock(Properties properties,int color) {
        super(properties);
        this.color = color;
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(WAX, 0)
        );
    }
    public TallowCandleBlock(@RGBColor int color) {
        super(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)
                .strength(0.1F,0.f)
                .lightLevel(s -> 1)
        );
        this.color = color;
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(WAX, 0)
        );
    }


    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return canSupportCenter(levelReader, blockPos.below(), Direction.UP);
    }//torch

    @Override
    public @NotNull BlockState updateShape(
            BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2
    ) {
        return direction == Direction.DOWN && !this.canSurvive(blockState, levelAccessor, blockPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }//torch

    public static final VoxelShape SHAPE = Block.box(6.0f, 0.0F, 6.0f, 10.0f, 8, 10.0f);
    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }//torch

    @Override
    public void animateTick(BlockState arg, Level arg2, BlockPos arg3, RandomSource arg4) {
        double d = arg3.getX() + 0.5;
        double e = arg3.getY() + 0.7;
        double f = arg3.getZ() + 0.5;
        arg2.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        arg2.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
    }//torch

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(WAX);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        RandomSource r = ctx.getLevel().getRandom();
        return this.defaultBlockState()
                .setValue(WAX, 1 + r.nextInt(5));
    }
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos,
                        BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (oldState.getBlock() != this){

            level.setBlock(pos,state.setValue(WAX, 1 + level.random.nextInt(5)),3);
        }
    }


    public boolean canStabaliseInfusion(Level world, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
}
