package thaumcraft.common.lib.world.dim.cell.features;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeLookups;
import thaumcraft.common.lib.world.dim.cell.CellState;

import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.FIBROUS_TAINT;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_GRASS;
//13
public class FibrePassage extends CellFeature {
    public static final FibrePassage INSTANCE = new FibrePassage(false);
    protected FibrePassage(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    @Override
    public void specialPlacement(WorldGenLevel world, RandomSource random, int x, int y, int z, CellState cellState) {
        var pos = new BlockPos.MutableBlockPos();
        for(int w = -4; w <= 4; ++w) {
            for(int h = -3; h <= 3; ++h) {
                for(int j = -4; j <= 4; ++j) {
                    if (world.getBlockState(pos.set(x + 8 + w, y + 4 + h, z + 8 + j)).isAir()
                            && BlockUtils.isAdjacentToSolidBlock(world, pos)) {
                        if (random.nextInt(3) != 0) {

                            if (world instanceof ServerLevel serverLevel){
                                Utils.setBiomeAt(serverLevel, pos, ThaumcraftBiomeLookups.biomeHolderForLevel(world, ThaumcraftBiomeIDs.TAINT_KEY));
                            }
                            world.setBlock(pos, (random.nextInt(4) == 0 ? TAINTED_GRASS():FIBROUS_TAINT()).defaultBlockState(), 3);
                        }

                    }
                }
            }
        }
    }
}
