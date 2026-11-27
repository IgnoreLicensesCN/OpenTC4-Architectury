package thaumcraft.common.lib.world.dim.cell.features;

import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.internal.WeightedRandomCollection;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.world.dim.cell.CellState;
import thaumcraft.common.lib.world.dim.cell.features.bossroom.BossRoomBottomLeft;
import thaumcraft.common.lib.world.dim.cell.features.bossroom.BossRoomBottomRight;
import thaumcraft.common.lib.world.dim.cell.features.bossroom.BossRoomUpperLeft;
import thaumcraft.common.lib.world.dim.cell.features.bossroom.BossRoomUpperRight;

import static net.minecraft.world.level.block.StairBlock.FACING;
import static net.minecraft.world.level.block.StairBlock.HALF;
import static net.minecraft.world.level.block.state.properties.Half.BOTTOM;
import static net.minecraft.world.level.block.state.properties.Half.TOP;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.ELDRITCH_BLOCK;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.*;
import static thaumcraft.common.lib.world.dim.cell.CellState.cellConnectedTo;

@AllArgsConstructor
public class CellFeature {
    public static CellFeature NONE = new CellFeature(false);//0
    public static PortalRoom PORTAL;
    public static BossRoomUpperLeft BOSS_ROOM_UPPER_LEFT;
    public static BossRoomUpperRight BOSS_ROOM_UPPER_RIGHT;
    public static BossRoomBottomRight BOSS_ROOM_BOTTOM_RIGHT;
    public static BossRoomBottomLeft BOSS_ROOM_BOTTOM_LEFT;
    public static CellFeature KEY_ROOM = new CellFeature(true);
    public static CellFeature NEST_ROOM = new CellFeature(false);
    public static CellFeature LIBRARY_ROOM = new CellFeature(false);//8
    public static CellFeature PASSAGE = new CellFeature(false);
    public static CellFeature PASSAGE_1 = new CellFeature(false);//10
    public static PassageWithTrap PASSAGE_WITH_TRAP;
    public static CrabSpawnerPassage CRAB_SPAWNER_PASSAGE;//12
    public static FibrePassage FIBRE_PASSAGE;//13
    public static MindSpiderSpawnerPassage MIND_SPIDER_SPAWNER_PASSAGE;//14
    public static CellFeature INVALID = new CellFeature(false);//99

    public static final BlockState A = Blocks.AIR.defaultBlockState();//0,9
    public static final BlockState B = Blocks.BEDROCK.defaultBlockState();//1,99
    public static final BlockState G = ThaumcraftBlocks.ThaumcraftBlockInstances.GLOWING_CRUSTED_STONE().defaultBlockState();//7
    public static final BlockState V = ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_VOID().defaultBlockState();//8
    public static final BlockState S = ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE().defaultBlockState();
    public static final BlockState T = ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE().defaultBlockState();
    public static final BlockState GL = ThaumcraftBlocks.ThaumcraftBlockInstances.GLYPHED_STONE().defaultBlockState();
    public static final BlockState R = ANCIENT_ROCK().defaultBlockState();
    public static final BlockState STAIR_LU = ThaumcraftBlocks.ThaumcraftBlockInstances
            .ANCIENT_STONE_STAIRS().defaultBlockState()
            .setValue(FACING,Direction.WEST)
            .setValue(HALF,TOP)
            ;//11
    public static final BlockState STAIR_RU = ThaumcraftBlocks.ThaumcraftBlockInstances
            .ANCIENT_STONE_STAIRS().defaultBlockState()
            .setValue(FACING,Direction.EAST)
            .setValue(HALF,TOP)
            ;
    public static final BlockState STAIR_LD = ThaumcraftBlocks.ThaumcraftBlockInstances
            .ANCIENT_STONE_STAIRS().defaultBlockState()
            .setValue(FACING,Direction.WEST)
            .setValue(HALF,BOTTOM)
            ;//10
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
    public static @Nullable BlockState getCrabSpawnerForPlacement(WorldGenLevel world, RandomSource random, int x, int y, int z, CellState cellState){
        int exp = BlockUtils.countExposedSides(world, x,y,z);
        var pos = new BlockPos.MutableBlockPos(x, y, z);
        if (exp == 1 && !BlockUtils.isBlockAdjacentToAtleast(world, x,y,z, ELDRITCH_BLOCK, 1)) {
            for (var dir:Direction.values()) {
                if (world.getBlockState(pos.set(x,y,z).move(dir)).isAir()){
                    return ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_CRAB_SPAWNER().defaultBlockState().setValue(FACING,dir);
                }
            }
        }
        return null;
    }
    public void generateConnections(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState, int depth, boolean collapseConnection){
        generateConnectionDefault(world, cx, cz, y, depth, collapseConnection);
    }

    public static void generateConnectionDefault(WorldGenLevel world, int cx, int cz, int y, int depth, boolean collapseConnection) {
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

    public void specialPlacement(WorldGenLevel world, RandomSource random, int x, int y, int z, CellState cellState){

    }

    public @Nullable BlockState getTrapStone(WorldGenLevel world, RandomSource random, int x, int y, int z, CellState cellState){
        return null;
    }

    public void placeFeatureBefore(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState){

    }
    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState){
        int x = cx * 16;
        int z = cz * 16;
        var mutable = new BlockPos.MutableBlockPos(x, y, z);
        generateConnections(world, random, cx, cz, y, cellState, 4, false);
        boolean considerConnectedAllDirections = cellConnectedTo(cellState, Direction.NORTH)
                && cellConnectedTo(cellState, Direction.SOUTH)
                && cellConnectedTo(cellState, Direction.EAST)
                && cellConnectedTo(cellState, Direction.WEST)
                && random.nextBoolean();

        for(int xOffset = 1; xOffset < 8; ++xOffset) {
            for(int zOffset = 1; zOffset < 8; ++zOffset) {
                var dontPlaceTrapFlag = (xOffset == 4 && zOffset == 4 && considerConnectedAllDirections);
                var trapStone = getTrapStone(world,random,x + 4 + xOffset, y + 2, z + 4 + zOffset,cellState);
                world.setBlock(mutable.set(x + 4 + xOffset, y + 2, z + 4 + zOffset),dontPlaceTrapFlag?G:trapStone != null ? trapStone : S,3);
                trapStone = getTrapStone(world,random,x + 4 + xOffset, y + 8, z + 4 + zOffset,cellState);
                world.setBlock(mutable.set(x + 4 + xOffset, y + 8, z + 4 + zOffset),dontPlaceTrapFlag?G:trapStone != null ? trapStone : S,3);

                world.setBlock(mutable.set(x + 4 + xOffset, y, z + 4 + zOffset),B,3);
                world.setBlock(mutable.set(x + 4 + xOffset, y + 10, z + 4 + zOffset),B,3);
                world.setBlock(mutable.set(x + 4 + xOffset, y + 1, z + 4 + zOffset),V,3);
                world.setBlock(mutable.set(x + 4 + xOffset, y + 9, z + 4 + zOffset),V,3);
            }
        }

        pathConnectionFeatureDetectConnection(world, random, cellState, mutable, x, y,z,considerConnectedAllDirections,Direction.NORTH);
        pathConnectionFeatureDetectConnection(world, random, cellState, mutable, x, y,z,considerConnectedAllDirections,Direction.SOUTH);
        pathConnectionFeatureDetectConnection(world, random, cellState, mutable, x, y,z,considerConnectedAllDirections,Direction.EAST);
        pathConnectionFeatureDetectConnection(world, random, cellState, mutable, x, y,z,considerConnectedAllDirections,Direction.WEST);

        if (considerConnectedAllDirections) {
            world.setBlock(mutable.set(x + 5, y + 3, z + 5),STAIR_LD.rotate(rotationFromNorth(Direction.EAST)),3);
            world.setBlock(mutable.set(x + 5, y + 7, z + 5),STAIR_LU.rotate(rotationFromNorth(Direction.EAST)),3);
            world.setBlock(mutable.set(x + 5, y + 3, z + 6),STAIR_LD.rotate(rotationFromNorth(Direction.NORTH)),3);
            world.setBlock(mutable.set(x + 5, y + 7, z + 6),STAIR_LU.rotate(rotationFromNorth(Direction.NORTH)),3);
            world.setBlock(mutable.set(x + 11, y + 3, z + 5),STAIR_LD.rotate(rotationFromNorth(Direction.EAST)),3);
            world.setBlock(mutable.set(x + 11, y + 7, z + 5),STAIR_LU.rotate(rotationFromNorth(Direction.EAST)),3);
            world.setBlock(mutable.set(x + 11, y + 3, z + 6),STAIR_RD.rotate(rotationFromNorth(Direction.NORTH)),3);
            world.setBlock(mutable.set(x + 11, y + 7, z + 6),STAIR_RU.rotate(rotationFromNorth(Direction.NORTH)),3);
            world.setBlock(mutable.set(x + 5, y + 3, z + 11),STAIR_LD.rotate(rotationFromNorth(Direction.NORTH)),3);
            world.setBlock(mutable.set(x + 5, y + 7, z + 11),STAIR_LU.rotate(rotationFromNorth(Direction.NORTH)),3);
            world.setBlock(mutable.set(x + 6, y + 3, z + 11),STAIR_RD.rotate(rotationFromNorth(Direction.EAST)),3);
            world.setBlock(mutable.set(x + 6, y + 7, z + 11),STAIR_RU.rotate(rotationFromNorth(Direction.EAST)),3);
            world.setBlock(mutable.set(x + 11, y + 3, z + 11),STAIR_RD.rotate(rotationFromNorth(Direction.NORTH)),3);
            world.setBlock(mutable.set(x + 11, y + 7, z + 11),STAIR_RU.rotate(rotationFromNorth(Direction.NORTH)),3);
            world.setBlock(mutable.set(x + 10, y + 3, z + 11),STAIR_RD.rotate(rotationFromNorth(Direction.EAST)),3);
            world.setBlock(mutable.set(x + 10, y + 7, z + 11),STAIR_RU.rotate(rotationFromNorth(Direction.EAST)),3);
        }
        specialPlacement(world, random, x, y, z, cellState);
    }
    public void placeFeatureAfter(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState){

    }
    protected void placeDecorationAt(WorldGenLevel world, RandomSource random, BlockPos pos, CellState cellState){
        int exp = BlockUtils.countExposedSides(world, pos.getX(),pos.getY(),pos.getZ());
        if (exp > 0 && (exp == 1 || !isBedrockShowing(world,  pos.getX(),pos.getY(),pos.getZ()))
                && !BlockUtils.isBlockAdjacentToAtleast(world,  pos.getX(),pos.getY(),pos.getZ(),
                ELDRITCH_BLOCK,
                1
        )) {
            var state = random.nextInt(3) != 0 ? G : (random.nextInt(8) != 0 ? GL : T);
            world.setBlock(pos, state, 3);
            if (state == G && random.nextInt(12) == 0) {
                var mutable = new BlockPos.MutableBlockPos();
                for (var dir:Direction.values()){
                    if (world.getBlockState(mutable.setWithOffset(pos,dir)).isAir()){
                        world.setBlock(mutable,STRANGE_CRYSTALS().defaultBlockState().setValue(FACING,dir),3);
                        break;
                    }
                }
            }
        }
    }
    public static boolean isBedrockShowing(BlockGetter world, int x, int y, int z) {
        var mutable = new BlockPos.MutableBlockPos(x, y, z);
        for(Direction dir : Direction.values()) {
            var state = world.getBlockState(mutable.set(x, y, z).move(dir));
            var stateOpposite = world.getBlockState(mutable.set(x, y, z).move(dir.getOpposite()));
            if (!state.isCollisionShapeFullBlock(world, mutable)
                    && (stateOpposite.is(Blocks.BEDROCK) || stateOpposite.is(ELDRITCH_VOID()))) {
                return true;
            }
        }

        return false;
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

    public void pathConnectionFeatureDetectConnection(
            WorldGenLevel world, RandomSource random,
            CellState cellState,
            BlockPos.MutableBlockPos cache,
            int x, int y, int z, boolean connectedAllDirections, Direction toCheck
    ) {
        int mod = connectedAllDirections ? 1 : 0;
        if (cellConnectedTo(cellState,toCheck)) {
            for(int xIndex = 2 + mod; xIndex < 9 - mod; ++xIndex) {
                for(int zIndex = 2 + mod; zIndex < 9 - mod; ++zIndex) {
                    var offsetX = toCheck.getStepX() < 0 ? 2 : toCheck.getStepX() == 0 ? xIndex : 8;
                    var offsetZ = toCheck.getStepZ() < 0 ? 2 : toCheck.getStepZ() == 0 ? xIndex : 8;
                    world.setBlock(cache.set(x + 3 + offsetX, y + 10 - zIndex, z + 3 + offsetZ),PATTERN_CONNECT[zIndex][xIndex].rotate(rotationFromNorth(toCheck)),3);
                }
            }

            if (!connectedAllDirections) {
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
                    var trapStone = getTrapStone(
                            world,
                            random,
                            x + 4 + (toCheck.getStepX() == 0 ? w : 1) + offsetXAddition,
                            y + 9 - h,
                            z + 4 + (toCheck.getStepZ() == 0 ? w : 1) + offsetZAddition,
                            cellState
                    );
                    world.setBlock(cache.set(
                            x + 4 + (toCheck.getStepX() == 0 ? w : 1) + offsetXAddition,
                            y + 9 - h,
                            z + 4 + (toCheck.getStepZ() == 0 ? w : 1) + offsetZAddition
                    ),trapStone != null ? trapStone : S,3);
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
                        var zOffset = (toCheck.getStepZ() <= 0 ? 0 : toCheck.getStepZ() == 0 ? w : 8);
                        world.setBlock(cache.set(x + 4 + xOffset, y + 1, z + 4 + zOffset),B,3);
                        world.setBlock(cache.set(x + 4 + xOffset, y + 9, z + 4 + zOffset),B,3);
                    }

                    if (w == 7) {
                        var xOffsetA = (toCheck.getStepX() <= 0 ? 0 : toCheck.getStepX() == 0 ? 0 : 8);
                        var zOffsetA = (toCheck.getStepZ() <= 0 ? 0 : toCheck.getStepZ() == 0 ? 0 : 8);
                        var xOffsetB = (toCheck.getStepX() <= 0 ? 0 : toCheck.getStepX() == 0 ? 0 : 8);
                        var zOffsetB = (toCheck.getStepZ() <= 0 ? 0 : toCheck.getStepZ() == 0 ? 0 : 8);
                        world.setBlock(cache.set(x + 4 + xOffsetA, y + 9 - h, z + 4 + zOffsetA),B,3);
                        world.setBlock(cache.set(x + 4 + xOffsetB, y + 9 - h, z + 4 + zOffsetB),B,3);
                    }
                }
            }

            for(int sameDirOffset = 2; sameDirOffset < 7; ++sameDirOffset) {
                var addOffsetX = toCheck.getStepX() == 0
                        ? sameDirOffset
                        : toCheck.getStepX()<0?2:8 //orthDirOffset
                        ;
                var addOffsetZ = toCheck.getStepZ() == 0
                        ? sameDirOffset
                        : toCheck.getStepZ()<0?2:8 //orthDirOffset
                        ;
                world.setBlock(cache.set(x + 4 + addOffsetX, y + 3, z + 4 + addOffsetZ),STAIR_LD.rotate(rotationFromNorth(Rotation.CLOCKWISE_90.rotate(toCheck))),3);
                world.setBlock(cache.set(x + 4 + addOffsetX, y + 7, z + 4 + addOffsetZ),STAIR_LU.rotate(rotationFromNorth(Rotation.CLOCKWISE_90.rotate(toCheck))),3);
            }
        }
    }

    public static void placeNone(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {

    }


    @Deprecated(forRemoval = true)
    protected void placeBlock(WorldGenLevel world, int x, int y, int z, int blockToPlace, CellState cellState) {
        placeBlock(world, x,y,z, blockToPlace, null, cellState);
    }
    @Deprecated(forRemoval = true)
    protected void placeBlock(WorldGenLevel world, int x, int y, int z, int blockToPlace, @Nullable Direction dir, CellState cellState) {
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

    private final boolean preventInitCorePathConnection;
    public static final WeightedRandomCollection<CellFeature> FEATURE_SET_A = new WeightedRandomCollection<>();
    public static final WeightedRandomCollection<CellFeature> FEATURE_SET_B = new WeightedRandomCollection<>();
//    public static final CellFeature[] FEATURE_SET_C = {
//            NONE,
//            PORTAL,
//            BOSS_ROOM_UPPER_LEFT,
//    };

    public boolean preventInitCorePathConnection() {
        return preventInitCorePathConnection;
    }

    private static boolean inited = false;
    public static void init(){
        if (inited) {
            return;
        }
        inited = true;
        PORTAL = PortalRoom.INSTANCE;
        BOSS_ROOM_UPPER_LEFT = BossRoomUpperLeft.INSTANCE;
        BOSS_ROOM_UPPER_RIGHT = BossRoomUpperRight.INSTANCE;
        BOSS_ROOM_BOTTOM_LEFT = BossRoomBottomLeft.INSTANCE;
        BOSS_ROOM_BOTTOM_RIGHT = BossRoomBottomRight.INSTANCE;

        PASSAGE_WITH_TRAP = PassageWithTrap.INSTANCE;
        CRAB_SPAWNER_PASSAGE = CrabSpawnerPassage.INSTANCE;
        FIBRE_PASSAGE = FibrePassage.INSTANCE;
        MIND_SPIDER_SPAWNER_PASSAGE = MindSpiderSpawnerPassage.INSTANCE;
        initFeatureSetA();
        initFeatureSetB();

    }

    private static void initFeatureSetA() {
        FEATURE_SET_A.add(NEST_ROOM,1);
        FEATURE_SET_A.add(LIBRARY_ROOM,1);
        FEATURE_SET_A.add(PASSAGE,1);
    }

    private static void initFeatureSetB() {
        FEATURE_SET_B.add(LIBRARY_ROOM,1);
        FEATURE_SET_B.add(PASSAGE_1,1);
        FEATURE_SET_B.add(PASSAGE_WITH_TRAP,2);
        FEATURE_SET_B.add(CRAB_SPAWNER_PASSAGE,2);
        FEATURE_SET_B.add(FIBRE_PASSAGE,1);
        FEATURE_SET_B.add(MIND_SPIDER_SPAWNER_PASSAGE,1);
    }
}
