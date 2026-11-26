package thaumcraft.common.lib.world.dim;

import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.lib.world.dim.cell.CellState;

import java.util.Random;

import static net.minecraft.world.level.block.StairBlock.FACING;
import static net.minecraft.world.level.block.StairBlock.HALF;
import static net.minecraft.world.level.block.state.properties.Half.BOTTOM;
import static net.minecraft.world.level.block.state.properties.Half.TOP;
import static thaumcraft.common.lib.world.dim.cell.CellState.cellConnectedTo;
import static thaumcraft.common.lib.world.dim.cell.CellState.getCellFeature;

@AllArgsConstructor
public class CellFeature {
    public static final CellFeature NONE = new CellFeature(false);//0
    public static final CellFeature PORTAL = new CellFeature(true);
    public static final CellFeature BOSS_ROOM = new CellFeature(true);
    public static final CellFeature BOSS_ROOM_1 = new CellFeature(true);
    public static final CellFeature BOSS_ROOM_2 = new CellFeature(true);
    public static final CellFeature BOSS_ROOM_3 = new CellFeature(true);//5
    public static final CellFeature KEY_ROOM = new CellFeature(true);
    public static final CellFeature NEST_ROOM = new CellFeature(false);
    public static final CellFeature LIBRARY_ROOM = new CellFeature(false);//8
    public static final CellFeature PASSAGE = new CellFeature(false);
    public static final CellFeature PASSAGE_1 = new CellFeature(false);//10
    public static final CellFeature PASSAGE_WITH_TRAP = new CellFeature(false);
    public static final CellFeature PASSAGE_3 = new CellFeature(false);//12
    public static final CellFeature PASSAGE_4 = new CellFeature(false);
    public static final CellFeature PASSAGE_5 = new CellFeature(false);
    public static final CellFeature INVALID = new CellFeature(false);//99

    public static final BlockState B = Blocks.BEDROCK.defaultBlockState();
    public static final BlockState A = Blocks.AIR.defaultBlockState();
    public static final BlockState V = ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_VOID().defaultBlockState();
    public static final BlockState S = ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE().defaultBlockState();
    public static final BlockState T = ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE().defaultBlockState();
    public static final BlockState G = ThaumcraftBlocks.ThaumcraftBlockInstances.GLOWING_CRUSTED_STONE().defaultBlockState();//7
    public static final BlockState STAIR_LU = ThaumcraftBlocks.ThaumcraftBlockInstances
            .ANCIENT_STONE_STAIRS().defaultBlockState()
            .setValue(FACING,Direction.WEST)
            .setValue(HALF,TOP)
            ;
    public static final BlockState STAIR_RU = ThaumcraftBlocks.ThaumcraftBlockInstances
            .ANCIENT_STONE_STAIRS().defaultBlockState()
            .setValue(FACING,Direction.EAST)
            .setValue(HALF,TOP)
            ;
    public static final BlockState STAIR_LD = ThaumcraftBlocks.ThaumcraftBlockInstances
            .ANCIENT_STONE_STAIRS().defaultBlockState()
            .setValue(FACING,Direction.WEST)
            .setValue(HALF,BOTTOM)
            ;
    public static final BlockState STAIR_RD = ThaumcraftBlocks.ThaumcraftBlockInstances
            .ANCIENT_STONE_STAIRS().defaultBlockState()
            .setValue(FACING,Direction.EAST)
            .setValue(HALF,BOTTOM)
            ;

    public static final BlockState[][] PATTERN_CONNECT = new BlockState[][]{
            {A, B, B, B, B, B, B, B, B, B, A},
            {B, V, V, V, V, V, V, V, V, V, B},
            {B, V, V, S, S, S, S, S, V, V, B},
            {B, V, S, STAIR_LU, A, A, A, STAIR_RU, S, V, B},
            {B, V, S, A, A, A, A, A, S, V, B},
            {B, V, S, A, A, A, A, A, S, V, B},
            {B, V, S, A, A, A, A, A, S, V, B},
            {B, V, S, STAIR_LD, A, A, A, STAIR_RD, S, V, B},
            {B, V, V, S, S, S, S, S, V, V, B},
            {B, V, V, V, V, V, V, V, V, V, B},
            {A, B, B, B, B, B, B, B, B, B, A},
    };
    public static Rotation rotationFromNorth(Direction dir) {
        return switch (dir){
            case Direction.SOUTH -> Rotation.CLOCKWISE_180;
            case Direction.EAST -> Rotation.CLOCKWISE_90;
            case Direction.WEST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }
    public void generateConnections(Level world, Random random, int cx, int cz, int y, CellState cellState, int depth, boolean collapseConnection){
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos(x, y, z);
        for (var dir:Direction.Plane.HORIZONTAL){
            var stepX = dir.getStepX();
            var stepZ = dir.getStepZ();
            var rotation = rotationFromNorth(dir);
            if (stepZ != 0){
                for(int zOffset = 0; zOffset <= depth; ++zOffset) {
                    int placeAtZ;
                    if (stepZ < 0){
                        placeAtZ = z + zOffset;
                    }
                    else {
                        placeAtZ = z + 16 - zOffset;
                    }

                    boolean flag1 = zOffset == depth && collapseConnection;
                    boolean flag2 = zOffset == depth - 1 && collapseConnection;
                    int offsetStart = flag1 ? 2 : (flag2 ? 1 : 0);
                    int offsetEnd = flag1 ? 9 : (flag2 ? 10 : 11);
                    for(int xOffset = offsetStart; xOffset < offsetEnd; ++xOffset) {
                        for(int yOffset = offsetStart; yOffset < offsetEnd; ++yOffset) {
                            if (zOffset != depth || !collapseConnection || PATTERN_CONNECT[yOffset][xOffset] != V) {
                                world.setBlock(mutable.set(x + 3 + xOffset, y + 10 - yOffset, placeAtZ),PATTERN_CONNECT[yOffset][xOffset].rotate(rotation),3);
                            }
                        }
                    }
                }
            }else {
                for(int xOffset = 0; xOffset <= depth; ++xOffset) {
                    int placeAtX;
                    if (stepX < 0){
                        placeAtX = x + xOffset;
                    }
                    else {
                        placeAtX = x + 16 - xOffset;
                    }
                    boolean collapseFlag1 = xOffset == depth && collapseConnection;
                    boolean collapseFlag2 = xOffset == depth - 1 && collapseConnection;
                    int offsetStart = collapseFlag1 ? 2 : (collapseFlag2 ? 1 : 0);
                    int offsetEnd = collapseFlag1 ? 9 : (collapseFlag2 ? 10 : 11);
                    for(int zOffset = offsetStart; zOffset < offsetEnd; ++zOffset) {
                        for(int yOffset = offsetStart; yOffset < offsetEnd; ++yOffset) {
                            if (xOffset != depth || !collapseConnection || PATTERN_CONNECT[yOffset][zOffset] != V) {
                                world.setBlock(mutable.set(placeAtX, y + 10 - yOffset, z + 3 + zOffset), PATTERN_CONNECT[yOffset][zOffset].rotate(rotation), 3);
                            }
                        }
                    }
                }
            }
        }

    }
    public void placeFeatureAt(Level world, Random random, int cx, int cz, int y, CellState cellState){
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos(x, y, z);
        generateConnections(world, random, cx, cz, y, cellState, 4, false);
        int mod = 0;
        if (cellConnectedTo(cellState,Direction.NORTH)
                &&cellConnectedTo(cellState,Direction.SOUTH)
                &&cellConnectedTo(cellState,Direction.EAST)
                &&cellConnectedTo(cellState,Direction.WEST)
                && random.nextBoolean()) {
            mod = 1;
        }

        for(int w = 1; w < 8; ++w) {
            for(int h = 1; h < 8; ++h) {
                var dontPlaceTrapFlag = (w == 4 && h == 4 && mod == 1);
                world.setBlock(mutable.set(x + 4 + w, y + 2, z + 4 + h),dontPlaceTrapFlag?G:getCellFeature(cellState) == PASSAGE_WITH_TRAP && random.nextInt(3) == 0 ? T : S,3);
                world.setBlock(mutable.set(x + 4 + w, y + 8, z + 4 + h),dontPlaceTrapFlag?G:getCellFeature(cellState) == PASSAGE_WITH_TRAP && random.nextInt(3) == 0 ? T : S,3);

                world.setBlock(mutable.set(x + 4 + w, y, z + 4 + h),B,3);
                world.setBlock(mutable.set(x + 4 + w, y + 10, z + 4 + h),B,3);
                world.setBlock(mutable.set(x + 4 + w, y + 1, z + 4 + h),V,3);
                world.setBlock(mutable.set(x + 4 + w, y + 9, z + 4 + h),V,3);
            }
        }

        connectionFeatureDetectConnection(world, random, cellState, mutable, x, y,z,mod,Direction.NORTH);
        connectionFeatureDetectConnection(world, random, cellState, mutable, x, y,z,mod,Direction.SOUTH);
        connectionFeatureDetectConnection(world, random, cellState, mutable, x, y,z,mod,Direction.EAST);
        connectionFeatureDetectConnection(world, random, cellState, mutable, x, y,z,mod,Direction.WEST);

        if (mod == 1) {
            world.setBlock(mutable.set(x + 5, y + 3, z + 5),STAIR_LD.rotate(rotationFromNorth(Direction.EAST)),3);;
            world.setBlock(mutable.set(x + 5, y + 7, z + 5),STAIR_LU.rotate(rotationFromNorth(Direction.EAST)),3);;
            world.setBlock(mutable.set(x + 5, y + 3, z + 6),STAIR_LD.rotate(rotationFromNorth(Direction.NORTH)),3);;
            world.setBlock(mutable.set(x + 5, y + 7, z + 6),STAIR_LU.rotate(rotationFromNorth(Direction.NORTH)),3);;
            world.setBlock(mutable.set(x + 11, y + 3, z + 5),STAIR_LD.rotate(rotationFromNorth(Direction.EAST)),3);;
            world.setBlock(mutable.set(x + 11, y + 7, z + 5),STAIR_LU.rotate(rotationFromNorth(Direction.EAST)),3);;
            world.setBlock(mutable.set(x + 11, y + 3, z + 6),STAIR_RD.rotate(rotationFromNorth(Direction.NORTH)),3);;
            world.setBlock(mutable.set(x + 11, y + 7, z + 6),STAIR_RU.rotate(rotationFromNorth(Direction.NORTH)),3);;
            world.setBlock(mutable.set(x + 5, y + 3, z + 11),STAIR_LD.rotate(rotationFromNorth(Direction.NORTH)),3);;
            world.setBlock(mutable.set(x + 5, y + 7, z + 11),STAIR_LU.rotate(rotationFromNorth(Direction.NORTH)),3);;
            world.setBlock(mutable.set(x + 6, y + 3, z + 11),STAIR_RD.rotate(rotationFromNorth(Direction.EAST)),3);;
            world.setBlock(mutable.set(x + 6, y + 7, z + 11),STAIR_RU.rotate(rotationFromNorth(Direction.EAST)),3);;
            world.setBlock(mutable.set(x + 11, y + 3, z + 11),STAIR_RD.rotate(rotationFromNorth(Direction.NORTH)),3);;
            world.setBlock(mutable.set(x + 11, y + 7, z + 11),STAIR_RU.rotate(rotationFromNorth(Direction.NORTH)),3);;
            world.setBlock(mutable.set(x + 10, y + 3, z + 11),STAIR_RD.rotate(rotationFromNorth(Direction.EAST)),3);;
            world.setBlock(mutable.set(x + 10, y + 7, z + 11),STAIR_RU.rotate(rotationFromNorth(Direction.EAST)),3);;
        }

        if (cellState.feature == 12) {
            for(int w = -4; w <= 4; ++w) {
                for(int h = -4; h < 5; ++h) {
                    for(int j = -4; j <= 4; ++j) {
                        if ((world.isAirBlock(x + 8 + w, y + 4 + h, z + 8 + j) || world.getBlock(x + 8 + w, y + 4 + h, z + 8 + j) == ConfigBlocks.blockCosmeticSolid || world.getBlock(x + 8 + w, y + 4 + h, z + 8 + j) == ConfigBlocks.blockStairsEldritch) && random.nextBoolean()) {
                            placeBlock(world, x + 8 + w, y + 4 + h, z + 8 + j, 21, cellState);
                        }
                    }
                }
            }
        }

        if (cellState.feature == 13) {
            for(int w = -4; w <= 4; ++w) {
                for(int h = -3; h <= 3; ++h) {
                    for(int j = -4; j <= 4; ++j) {
                        if (world.isAirBlock(x + 8 + w, y + 4 + h, z + 8 + j) && BlockUtils.isAdjacentToSolidBlock(world, x + 8 + w, y + 4 + h, z + 8 + j)) {
                            if (random.nextInt(3) != 0) {
                                world.setBlock(x + 8 + w, y + 4 + h, z + 8 + j, ConfigBlocks.blockTaintFibres, random.nextInt(4) == 0 ? 1 : 0, 3);
                            }

                            Utils.setBiomeAt(world, x + 8 + w, z + 8 + j, ThaumcraftWorldGenerator.biomeTaint);
                        }
                    }
                }
            }
        }

        if (cellState.feature == 14) {
            for(int w = -3; w <= 3; ++w) {
                for(int h = -3; h <= 3; ++h) {
                    for(int j = -3; j <= 3; ++j) {
                        if (world.isAirBlock(x + 8 + w, y + 4 + h, z + 8 + j) && random.nextFloat() < 0.35F) {
                            world.setBlock(x + 8 + w, y + 4 + h, z + 8 + j, Blocks.web);
                        }

                        world.setBlock(x + 8, y + 4, z + 8, Blocks.mob_spawner);
                        BlockEntityMobSpawner var12 = (BlockEntityMobSpawner)world.getBlockEntity(x + 8, y + 4, z + 8);
                        if (var12 != null) {
                            var12.func_145881_a().setEntityName("Thaumcraft.MindSpider");
                        }
                    }
                }
            }
        }

    }

    private static final Direction[] DIRECTIONS_NEIGHBOUR_N_S = {Direction.WEST,Direction.EAST};
    private static final Direction[] DIRECTIONS_NEIGHBOUR_W_E = {Direction.WEST,Direction.EAST};
    public static Direction[] getPlaneNeighbours(Direction direction){
        return switch (direction){
            case NORTH,SOUTH -> DIRECTIONS_NEIGHBOUR_N_S;
            case EAST,WEST -> DIRECTIONS_NEIGHBOUR_W_E;
            default -> throw new IllegalStateException("only for plane dir");
        };
    }

    public static void connectionFeatureDetectConnection(
            Level world, Random random,
            CellState cellState,
            BlockPos.MutableBlockPos cache,
            int x, int y, int z, int mod, Direction toCheck
    ) {
        if (cellConnectedTo(cellState,toCheck)) {
            for(int w = 2 + mod; w < 9 - mod; ++w) {
                for(int h = 2 + mod; h < 9 - mod; ++h) {
                    var offsetX = toCheck.getStepX() < 0 ? 2 : toCheck.getStepX() == 0 ? w : 8;
                    var offsetZ = toCheck.getStepZ() < 0 ? 2 : toCheck.getStepZ() == 0 ? w : 8;
                    world.setBlock(cache.set(x + 3 + offsetX, y + 10 - h, z + 3 + offsetZ),PATTERN_CONNECT[h][w].rotate(rotationFromNorth(toCheck)),3);
                }
            }

            if (mod == 0) {
                for (var dir: getPlaneNeighbours(toCheck)){
                    var addOffsetX = (toCheck.getStepX() + dir.getStepX())*2;
                    var addOffsetZ = (toCheck.getStepZ() + dir.getStepZ())*2;
                    world.setBlock(cache.set(x + 8 + addOffsetX, y + 3, z + 8 + addOffsetZ),STAIR_LD.rotate(rotationFromNorth(dir)),3);
                    world.setBlock(cache.set(x + 8 + addOffsetX, y + 7, z + 8 + addOffsetZ),STAIR_LU.rotate(rotationFromNorth(dir)),3);
                }
            }
        }
        else {
            for(int w = 1; w < 8; ++w) {
                for(int h = 1; h < 8; ++h) {
                    var offsetXAddition = toCheck.getStepX() > 0 ? 8 : 0;
                    var offsetZAddition = toCheck.getStepZ() > 0 ? 8 : 0;
                    world.setBlock(cache.set(
                            x + 4 + (toCheck.getStepX() == 0 ? w : 1) + offsetXAddition,
                            y + 9 - h,
                            z + 4 + (toCheck.getStepZ() == 0 ? w : 1) + offsetZAddition
                    ),getCellFeature(cellState) == PASSAGE_WITH_TRAP && random.nextInt(3) == 0 ? T : S,3);
                    world.setBlock(cache.set(
                            x + 4 + (toCheck.getStepX() == 0 ? w : 0) + offsetXAddition,
                            y + 9 - h,
                            z + 4 + (toCheck.getStepZ() == 0 ? w : 0) + offsetZAddition
                    ),V,3);
                    world.setBlock(cache.set(
                            x + 4 + (toCheck.getStepX() == 0 ? w : -1) + offsetXAddition,
                            y + 9 - h,
                            z + 4 + (toCheck.getStepZ() == 0 ? w : -1) + offsetZAddition
                    ),B,3);

                    if (h == 7) {
                        var xOffset = (toCheck.getStepX() <= 0 ? 0 : toCheck.getStepX() == 0 ? w : 8);
                        var zOffset = (toCheck.getStepX() <= 0 ? 0 : toCheck.getStepX() == 0 ? w : 8);
                        world.setBlock(cache.set(x + 4 + xOffset, y + 1, z + 4 + zOffset),B,3);
                        world.setBlock(cache.set(x + 4 + xOffset, y + 9, z + 4 + zOffset),B,3);
                    }

                    if (w == 7) {
                        var xOffsetA = (toCheck.getStepX() <= 0 ? 0 : toCheck.getStepX() == 0 ? 0 : 8);
                        var zOffsetA = (toCheck.getStepX() <= 0 ? 0 : toCheck.getStepX() == 0 ? 0 : 8);
                        var xOffsetB = (toCheck.getStepX() <= 0 ? 0 : toCheck.getStepX() == 8 ? 0 : 8);
                        var zOffsetB = (toCheck.getStepX() <= 0 ? 0 : toCheck.getStepX() == 8 ? 0 : 8);
                        world.setBlock(cache.set(x + 4 + xOffsetA, y + 9 - h, z + 4 + zOffsetA),B,3);
                        world.setBlock(cache.set(x + 4 + xOffsetB, y + 9 - h, z + 4 + zOffsetB),B,3);
                    }
                }
            }

            for(int w = 2; w < 7; ++w) {
                var addOffsetX = toCheck.getStepX() == 0 ? w : (toCheck.getStepX()+3)*2;
                var addOffsetZ = toCheck.getStepZ() == 0 ? w : (toCheck.getStepZ()+3)*2;
                world.setBlock(cache.set(x + 4 + addOffsetX, y + 3, z + 4 + addOffsetZ),STAIR_LD.rotate(rotationFromNorth(Rotation.CLOCKWISE_90.rotate(toCheck))),3);
                world.setBlock(cache.set(x + 4 + addOffsetX, y + 7, z + 4 + addOffsetZ),STAIR_LU.rotate(rotationFromNorth(Rotation.CLOCKWISE_90.rotate(toCheck))),3);
            }
        }
    }

    public static void placeNone(Level world, Random random, int cx, int cz, int y, CellState cellState) {

    }



    protected void placeBlock(Level world, int x, int y, int z, int blockToPlace, CellState cellState) {
        placeBlock(world, x,y,z, blockToPlace, null, cellState);
    }

    protected void placeBlock(Level world, int x, int y, int z, int blockToPlace, @Nullable Direction dir, CellState cellState) {
//        Block block;
//        int meta;
        label233: {
            block = null;
            meta = 0;
            switch (blockToPlace) {
                case B:
                    if (world.isAirBlock(x, y, z)) {
                        block = Blocks.bedrock;
                    }
                    break label233;
                case 2:
                    if (cellState.feature == 7 && world.getRandom().nextInt(3) == 0) {
                        break;
                    }

                    if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                        if (world.getRandom().nextInt(25) == 0) {
                            boolean crab = cellState.feature == 7 || world.getRandom().nextInt(50) == 0;
                            if ((!crab || cellState.feature != 0) && (!crab || cellState.feature != 7)) {
                                decoCommon.add(new ChunkPos(x, y, z));
                            } else {
                                crabSpawner.add(new ChunkPos(x, y, z));
                            }
                        }

                        block = ConfigBlocks.blockCosmeticSolid;
                        meta = 11;
                    }
                    break label233;
                case 3:
                    if ((double)world.getRandom().nextFloat() < 0.005) {
                        decoUrn.add(new ChunkPos(x, y, z));
                    }

                    block = ConfigBlocks.blockStairsEldritch;
                    switch (dir.ordinal()) {
                        case 2:
                        case 3:
                            meta = 1;
                            break label233;
                        case 4:
                        case 5:
                            meta = 3;
                        default:
                            break label233;
                    }
                case 4:
                    if ((double)world.getRandom().nextFloat() < 0.005) {
                        decoUrn.add(new ChunkPos(x, y, z));
                    }

                    block = ConfigBlocks.blockStairsEldritch;
                    switch (dir.ordinal()) {
                        case 2:
                        case 3:
                            meta = 0;
                            break label233;
                        case 4:
                        case 5:
                            meta = 2;
                        default:
                            break label233;
                    }
                case 5:
                    block = ConfigBlocks.blockStairsEldritch;
                    switch (dir.ordinal()) {
                        case 2:
                        case 3:
                            meta = 5;
                            break label233;
                        case 4:
                        case 5:
                            meta = 7;
                        default:
                            break label233;
                    }
                case 6:
                    block = ConfigBlocks.blockStairsEldritch;
                    switch (dir.ordinal()) {
                        case 2:
                        case 3:
                            meta = 4;
                            break label233;
                        case 4:
                        case 5:
                            meta = 6;
                        default:
                            break label233;
                    }
                case 7:
                    block = ConfigBlocks.blockEldritch;
                    meta = 4;
                    break label233;
                case V:
                    block = ConfigBlocks.blockEldritchNothing;
                    break label233;
                case A:
                    block = Blocks.air;
                    decoCommon.remove(new ChunkPos(x, y, z));
                    crabSpawner.remove(new ChunkPos(x, y, z));
                    decoUrn.remove(new ChunkPos(x, y, z));
                    break label233;
                case STD:
                    block = ConfigBlocks.blockStairsEldritch;
                    switch (dir) {
                        case NORTH:
                            meta = 3;
                            break label233;
                        case SOUTH:
                            meta = 2;
                            break label233;
                        case EAST:
                            meta = 0;
                            break label233;
                        case WEST:
                            meta = 1;
                        default:
                            break label233;
                    }
                case 11:
                    block = ConfigBlocks.blockStairsEldritch;
                    switch (dir) {
                        case NORTH:
                            meta = 7;
                            break label233;
                        case SOUTH:
                            meta = 6;
                            break label233;
                        case EAST:
                            meta = 4;
                            break label233;
                        case WEST:
                            meta = 5;
                        default:
                            break label233;
                    }
                case 15:
                    block = ConfigBlocks.blockEldritch;
                    meta = 7;
                    decoCommon.remove(new ChunkPos(x, y, z));
                    crabSpawner.remove(new ChunkPos(x, y, z));
                    decoUrn.remove(new ChunkPos(x, y, z));
                    break label233;
                case 16:
                    block = ConfigBlocks.blockEldritch;
                    meta = 8;
                    decoCommon.remove(new ChunkPos(x, y, z));
                    crabSpawner.remove(new ChunkPos(x, y, z));
                    decoUrn.remove(new ChunkPos(x, y, z));
                    break label233;
                case 17:
                    block = ConfigBlocks.blockAiry;
                    meta = 12;
                    break label233;
                case 18:
                    if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                        block = ConfigBlocks.blockCosmeticSolid;
                        meta = 12;
                    }
                    break label233;
                case SN:
                    if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                        block = ConfigBlocks.blockCosmeticSolid;
                        meta = 13;
                    }
                    break label233;
                case T:
                    if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                        block = ConfigBlocks.blockEldritch;
                        meta = 10;
                    }
                    break label233;
                case 21:
                    break;
                case 99:
                    block = Blocks.bedrock;
                default:
                    break label233;
            }

            if (world.getBlock(x, y, z) != ConfigBlocks.blockEldritchNothing) {
                block = ConfigBlocks.blockCosmeticSolid;
                meta = 14;
                if (world.getRandom().nextInt(25) == 0) {
                    block = ConfigBlocks.blockEldritch;
                    meta = 4;
                } else if (world.getRandom().nextInt(25) == 0) {
                    boolean crab = cellState.feature == 7 || (cellState.feature == 12 && world.getRandom().nextBoolean() || world.getRandom().nextInt(25) == 0);
                    if (crab && cellState.feature == 0 || crab && cellState.feature == 7 || crab && cellState.feature == 12) {
                        crabSpawner.add(new ChunkPos(x, y, z));
                    }
                }
            }
        }

        if (block != null) {
            world.setBlock(x, y, z, block, meta, block != ConfigBlocks.blockEldritchNothing && block != Blocks.bedrock && block != Blocks.air ? 3 : 0);
        }

    }

    public final boolean preventInitCorePathConnection;
    public static final CellFeature[] FEATURE_SET_A = {
            NEST_ROOM,
            LIBRARY_ROOM,
            PASSAGE
    };
    public static final CellFeature[] FEATURE_SET_B = {
            LIBRARY_ROOM,
            PASSAGE_1,
            PASSAGE_WITH_TRAP,
            PASSAGE_WITH_TRAP,
            PASSAGE_3,
            PASSAGE_3,
            PASSAGE_4,
            PASSAGE_5,
    };
    public static final CellFeature[] FEATURE_SET_C = {
            NONE,
            PORTAL,
            BOSS_ROOM,
    };
}
