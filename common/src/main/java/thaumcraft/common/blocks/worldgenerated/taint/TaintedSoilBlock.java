package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TaintedSoilBlock extends AbstractTaintBlock {
    public TaintedSoilBlock(Properties properties) {
        super(properties);
    }
    public TaintedSoilBlock() {
        super();
    }

    @Override
    public void onBlockOutOfTaintBiome(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
         if (random.nextInt(10) == 0) {
            world.setBlock(blockPos, Blocks.DIRT.defaultBlockState(), 3);
        }
    }
}
