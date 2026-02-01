package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

//blocktaint:1
public class TaintedSoilBlock extends AbstractTaintBlock {
    public TaintedSoilBlock(Properties properties) {
        super(properties);
    }
    public TaintedSoilBlock() {
        super(
                BlockBehaviour.Properties.of()
                        .randomTicks()
                        .strength(1.5F,10)
                        .sound(TAINT_BLOCK_SOUND));
    }

    @Override
    public void onBlockOutOfTaintBiome(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
         if (random.nextInt(10) == 0) {
            world.setBlock(blockPos, Blocks.DIRT.defaultBlockState(), 3);
        }
    }
}
