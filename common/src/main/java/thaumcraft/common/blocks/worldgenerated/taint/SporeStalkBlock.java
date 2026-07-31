package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.tainted.EntityTaintSpore;
import thaumcraft.common.entities.monster.tainted.TaintSporeEntity;

//blocktaintfibres:3
public class SporeStalkBlock extends AbstractTaintFibreBlock{
    public SporeStalkBlock(Properties properties) {
        super(properties);
    }
    public SporeStalkBlock() {
        super();
    }

    @Override
    protected void onSpreadFibresFailed(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (Config.spawnTaintSpore && random.nextInt(10) == 0 && world.getBlockState(blockPos.above()).isAir()) {
            world.setBlockAndUpdate(blockPos, ThaumcraftBlocks.ThaumcraftBlockInstances.MATURE_SPORE_STALK().defaultBlockState());
            var spore = new TaintSporeEntity(world);
            spore.setPos(blockPos.getCenter().add(0,1,0));
            world.addFreshEntity(spore);
        }
    }
}
