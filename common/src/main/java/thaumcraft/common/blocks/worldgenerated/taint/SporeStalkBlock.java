package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityTaintSpore;

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
            world.setBlockAndUpdate(blockPos, ThaumcraftBlocks.MATURE_SPORE_STALK.defaultBlockState());
            EntityTaintSpore spore = new EntityTaintSpore(world);//TODO:entity
            spore.setLocationAndAngles((float)x + 0.5F, y + 1, (float)z + 0.5F, 0.0F, 0.0F);
            world.addFreshEntity(spore);
        }
    }
}
