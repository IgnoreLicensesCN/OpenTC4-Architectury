package thaumcraft.common.lib.world.dim.cell.features.bossroom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import thaumcraft.common.lib.world.dim.cell.CellState;
import thaumcraft.common.lib.world.dim.cell.features.CellFeature;

import static net.minecraft.world.level.block.StairBlock.FACING;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.*;
import static thaumcraft.common.lib.world.dim.cell.CellState.cellConnectedTo;

public abstract class AbstractBossRoom extends CellFeature {
    public static final IntegerProperty BOSS_ROOM_INDEX = IntegerProperty.create("boss_room_index", 0, 3);

    protected AbstractBossRoom(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    public static final BlockState E = ANCIENT_LOCK_EMPTY().defaultBlockState();
    public static final BlockState I = IMPASSABLE().defaultBlockState();
    public static final BlockState G = ANCIENT_GATEWAY().defaultBlockState();
    static final BlockState[][] PATTERN_DOORWAY = new BlockState[][]{
            {A, G, G, G, G, G, A,},
            {G, G, I, I, I, G, G,},
            {G, I, I, I, I, I, G,},
            {G, I, I, E, I, I, G,},
            {G, I, I, I, I, I, G,},
            {G, G, I, I, I, G, G,},
            {A, G, G, G, G, G, A,},
    };

    protected abstract int getBossRoomIndex();

    protected void placeRoomMainPart(WorldGenLevel world, RandomSource random, int cx, int cz, int y, Direction dir, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var connectToMainDirection = cellConnectedTo(cellState,dir);
        var connectedToCCW = cellConnectedTo(cellState,dir.getCounterClockWise());
        var stepX = dir.getStepX();
        var stepZ = dir.getStepZ();
        var stepXCCW = dir.getCounterClockWise().getStepX();
        var stepZCCW = dir.getCounterClockWise().getStepZ();
        var stepWithCounterClockwiseX = dir.getCounterClockWise().getStepX() + dir.getStepX();
        var stepWithCounterClockwiseZ = dir.getCounterClockWise().getStepZ() + dir.getStepZ();

        var mutable = new BlockPos.MutableBlockPos(x, y, z);

        //   N(0,-1)
        //W(-1,0)(1,0)E
        //    S(0,1)

        //SELF,COUNTER,CLOCKWISE
        //(0,-1),(-1,-1),(1,-1)N [,,-7]
        //(1,0),(1,-1),(1,1)E[,,7]
        //(0,1),(1,1),(-1,1)S[,,7]
        //(-1,0),(-1,1),(-1,-1)W[,,-7]

        for(int a = stepWithCounterClockwiseX < 0 ? 1 : 0; a <= 15; ++a) {
            for(int b = stepWithCounterClockwiseZ < 0 ? 1 : 0; b <= 15; ++b) {
                for(int c = 0; c < 13; ++c) {
                    if (a == 8 + stepWithCounterClockwiseX * 7 || b == 8 + stepWithCounterClockwiseZ * 7) {
                        world.setBlock(mutable.set(x + a, y + c, z + b),B,3);
                    }
                }
            }
        }

        for(int a = stepWithCounterClockwiseX < 0 ? 2 : 0; a <= (stepWithCounterClockwiseX < 0 ? 15 : 14); ++a) {
            for(int b = stepWithCounterClockwiseZ < 0 ? 2 : 0; b <= (stepWithCounterClockwiseZ < 0 ? 15 : 14); ++b) {

                boolean orFlagSelfDir = (stepX != 0
                        ? (a <= 4 || a >= 12 || b != (8+6*stepWithCounterClockwiseZ))
                        : (b <= 4 || b >= 12 || a != (8+6*stepWithCounterClockwiseX)))
                        || connectToMainDirection;
                boolean orFlagCCWDir = (stepXCCW != 0
                        ? (a <= 4 || a >= 12 || b != (8+6*stepWithCounterClockwiseZ))
                        : (b <= 4 || b >= 12 || a != (8+6*stepWithCounterClockwiseX)))
                        || connectedToCCW;

                for(int c = 1; c < 12; ++c) {
                    if ((a == 8 + 6 * stepWithCounterClockwiseX || b == 8 + 6 * stepWithCounterClockwiseZ)
                            && orFlagSelfDir
                            && orFlagCCWDir ) {
                        world.setBlock(mutable.set(x + a, y + c, z + b),V,3);
                    }
                }
            }
        }

        for(int a = stepWithCounterClockwiseX<0?3:0; a <= (stepWithCounterClockwiseX<0?15:13); ++a) {
            for(int b = stepWithCounterClockwiseZ<0?3:0; b <= (stepWithCounterClockwiseZ<0?15:13); ++b) {
                for(int c = 2; c < 11; ++c) {
                    if (a == 8 + stepWithCounterClockwiseX * 5 || b == 8 + stepWithCounterClockwiseZ * 5) {
                        world.setBlock(mutable.set(x + a, y + c, z + b),R,3);
                    }
                }
            }
        }

        for(int xOffset = stepWithCounterClockwiseX<0?2:0; xOffset <= (stepWithCounterClockwiseX < 0 ? 15 : 14); ++xOffset) {
            for(int zOffset = stepWithCounterClockwiseZ<0?3:0; zOffset <= (stepWithCounterClockwiseZ < 0 ? 15 : 14); ++zOffset) {
                world.setBlock(mutable.set(x + xOffset, y - 1, z + zOffset),B,3);
                world.setBlock(mutable.set(x + xOffset, y, z + zOffset),V,3);
                world.setBlock(mutable.set(x + xOffset, y + 1, z + zOffset),S,3);
                world.setBlock(mutable.set(x + xOffset, y +13, z + zOffset),B,3);
                world.setBlock(mutable.set(x + xOffset, y + 12, z + zOffset),V,3);
                placeDecorationAt(world,random,mutable.set(x + xOffset, y + 11, z + zOffset),cellState);
                world.setBlock(mutable.set(x + xOffset, y + 11, z + zOffset),S,3);
            }
        }

        for(int g = stepWithCounterClockwiseX<0?4:0; g <= (stepWithCounterClockwiseX < 0 ? 15 : 11); ++g) {
            world.setBlock(mutable.set(x + g, y + 2, z + 8 + stepWithCounterClockwiseZ * 4),STAIR_LD.setValue(FACING,dir),3);
            world.setBlock(mutable.set(x + g, y + 10, z + 8 + stepWithCounterClockwiseZ * 4),STAIR_LU.setValue(FACING,dir),3);
        }
        for (int g = stepWithCounterClockwiseZ<0?4:0; g <= (stepWithCounterClockwiseZ < 0 ? 15 : 11); ++g){
            world.setBlock(mutable.set(x + 8 + stepWithCounterClockwiseX * 4, y + 2, z + g),STAIR_LD.setValue(FACING,dir.getClockWise()),3);
            world.setBlock(mutable.set(x + 8 + stepWithCounterClockwiseX * 4, y + 10, z + g),STAIR_LU.setValue(FACING,dir.getClockWise()),3);
        }

        generateConnections(world, random, cx, cz, y, cellState, 3, true);
    }
    @Override
    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos(x, y, z);
        for (var dir : Direction.Plane.HORIZONTAL) {
            if (cellConnectedTo(cellState, dir)) {
                for (int xOffset = 0; xOffset < 7; ++xOffset) {
                    for (int zOffset = 0; zOffset < 7; ++zOffset) {
                        int xx = x + 5 + dir.getStepX() == 0 ? xOffset : dir.getStepX() < 0 ? -2 : 8;
                        int zz = z + 5 + dir.getStepZ() == 0 ? xOffset : dir.getStepZ() < 0 ? -2 : 8;

                        var state = PATTERN_DOORWAY[xOffset][zOffset];
                        if (state.hasProperty(BOSS_ROOM_INDEX)) {
                            state = state.setValue(BOSS_ROOM_INDEX, getBossRoomIndex());
                        }
                        world.setBlock(mutable.set(xx, y + 2 + zOffset, zz), state, 3 /*getBossRoomIndex() * 4 + dir*/);
                    }
                }
                break;
            }
        }
    }
}
