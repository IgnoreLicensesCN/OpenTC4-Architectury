package thaumcraft.common.lib.world.dim.cell.features;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.world.dim.cell.CellState;
//12
public class CrabSpawnerPassage extends CellFeature {
    public static final CrabSpawnerPassage INSTANCE = new CrabSpawnerPassage(false);
    protected CrabSpawnerPassage(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }
    @Override
    public void specialPlacement(WorldGenLevel world, RandomSource random, int x, int y, int z, CellState cellState) {
        var pos = new BlockPos.MutableBlockPos();
        for(int xOffset = -4; xOffset <= 4; ++xOffset) {
            for(int yOffset = -4; yOffset < 5; ++yOffset) {
                for(int zOffset = -4; zOffset <= 4; ++zOffset) {
                    var currentState = world.getBlockState(pos.set(x + 8 + xOffset, y + 4 + yOffset, z + 8 + zOffset));
                    if ((currentState.isAir()
                            || currentState.is(S.getBlock())
                            || currentState.is(STAIR_LU.getBlock()))
                            && random.nextBoolean()) {
                        //CRUST
//                            placeBlock(world, x + 8 + xOffset, y + 4 + yOffset, z + 8 + zOffset, 21, cellState);
                        if (!currentState.is(V.getBlock()) && currentState.getBlock().defaultDestroyTime() > 0) {
                            currentState = ThaumcraftBlocks.ThaumcraftBlockInstances.CRUSTED_STONE().defaultBlockState();
                            if (random.nextInt(25) == 0){
                                currentState =  ThaumcraftBlocks.ThaumcraftBlockInstances.GLOWING_CRUSTED_STONE().defaultBlockState();
                            } else if (random.nextInt(25) == 0){
                                if (world.getRandom().nextBoolean() || world.getRandom().nextInt(25) == 0){
                                    var newState = getCrabSpawnerForPlacement(world, random, x, y, z, cellState);
                                    if (newState != null){
                                        currentState = newState;
                                    }
                                }
                            }
                            world.setBlock(pos, currentState, 3);
//                                block = ConfigBlocks.blockCosmeticSolid;
//                                meta = 14;
//                                if (world.getRandom().nextInt(25) == 0) {
//                                    block = ConfigBlocks.blockEldritch;
//                                    meta = 4;
//                                } else if (world.getRandom().nextInt(25) == 0) {
//                                    /*boolean*/ /*crab = cellState.feature == 7 ||*/
//                                            (/*cellState.feature == 12 &&*/ world.getRandom().nextBoolean() || world.getRandom().nextInt(25) == 0)
//                                            ;
//                                    if (crab && cellState.feature == 0 || crab && cellState.feature == 7 || crab && cellState.feature == 12) {
//                                        crabSpawner.add(new ChunkPos(x, y, z));
//                                    }
//                                }
                        }
                    }
                }
            }
        }
    }
}
