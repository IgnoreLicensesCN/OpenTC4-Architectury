package thaumcraft.common.blocks.worldgenerated.taint;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.world.biomes.BiomeUtils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;

public class FibrousTaintBlock extends AbstractTaintFibreBlock{
    public FibrousTaintBlock(Properties properties) {
        super(properties);
    }
    public FibrousTaintBlock() {
        super();
    }
    static final double F = 1.0 / 16.0;
    static final VoxelShape DOWN  = Shapes.box(0, 0, 0, 1, F, 1);
    static final VoxelShape UP    = Shapes.box(0, 1-F, 0, 1, 1, 1);
    static final VoxelShape NORTH = Shapes.box(0, 0, 0, 1, 1, F);
    static final VoxelShape SOUTH = Shapes.box(0, 0, 1-F, 1, 1, 1);
    static final VoxelShape WEST  = Shapes.box(0, 0, 0, F, 1, 1);
    static final VoxelShape EAST  = Shapes.box(1-F, 0, 0, 1, 1, 1);

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        for (Direction side : Direction.values()) {
            BlockPos checkPos = pos.relative(side);
            BlockState other = level.getBlockState(checkPos);

            if (other.isFaceSturdy(level, checkPos, side.getOpposite())) {
                return switch (side) {
                    case DOWN  -> DOWN;
                    case UP    -> UP;
                    case NORTH -> NORTH;
                    case SOUTH -> SOUTH;
                    case WEST  -> WEST;
                    case EAST  -> EAST;
                };
            }
        }
        return DOWN;
    }

    @Override
    protected boolean cancelRandomTickAfterSpread(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (isOnlyAdjacentToTaint(world, blockPos) || !world.getBiome(blockPos).is(ThaumcraftBiomeIDs.TAINT_KEY)) {
            world.setBlock(blockPos, Blocks.AIR.defaultBlockState(),3);
            return true;
        }
        return false;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            BiomeUtils.taintBiomeSpread(world, blockPos, random, this);
            if (isOnlyAdjacentToTaint(world, blockPos) || !world.getBiome(blockPos).is(ThaumcraftBiomeIDs.TAINT_KEY)) {
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(),3);
                return;
            }
            super.randomTick(blockState, world, blockPos, random);
        }
    }
}
