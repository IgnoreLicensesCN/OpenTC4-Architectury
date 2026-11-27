package thaumcraft.common.lib.world.dim.cell.features;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.world.dim.cell.CellState;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

//14
public class MindSpiderSpawnerPassage extends CellFeature {
    public static final MindSpiderSpawnerPassage INSTANCE = new MindSpiderSpawnerPassage(false);
    protected MindSpiderSpawnerPassage(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    @Override
    public void specialPlacement(WorldGenLevel world, RandomSource random, int x, int y, int z, CellState cellState) {

        var pos = new BlockPos.MutableBlockPos();
        for(int w = -3; w <= 3; ++w) {
            for(int h = -3; h <= 3; ++h) {
                for(int j = -3; j <= 3; ++j) {
                    if (world.getBlockState(pos.set(x + 8 + w, y + 4 + h, z + 8 + j)).isAir() && random.nextFloat() < 0.35F) {
                        world.setBlock(pos, Blocks.COBWEB.defaultBlockState(),3);
                    }

                    world.setBlock(pos,Blocks.SPAWNER.defaultBlockState(),3);
                    var be = getExistingBlockEntity(world,pos);
                    if (be instanceof SpawnerBlockEntity spawner) {
                        spawner.setEntityId(ThaumcraftEntities.ThaumcraftEntityTypeInstances.MIND_SPIDER(), RandomSource.create(random.nextLong()));
                    }
                }
            }
        }
    }
}
