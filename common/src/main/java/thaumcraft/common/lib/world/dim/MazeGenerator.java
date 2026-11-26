package thaumcraft.common.lib.world.dim;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.world.dim.cell.CellState;

import java.util.*;
import java.util.function.Predicate;

import static thaumcraft.common.lib.world.dim.CellFeature.*;
import static thaumcraft.common.lib.world.dim.cell.CellState.*;

//TODO:[maybe wont finished]Language candy(from kt?) for better performance...but readable first
public class MazeGenerator {
    public final int xTotal;
    public final int zTotal;
    public final long seed;
    public final Random rand;
    public final @Nullable CellState[][] grid;//[z][x]
    //element:
    //XXXX XXXX XXXX XXXX XXXX XXXX XXDD DDDD
    //D:Direction bits(Direction#ordinal())


    public static final byte BIT8 = 1;//portal
    public static final byte BIT9 = 1 << 1;//boss
    public static final byte BIT10 = 1 << 2;
    public static final byte BIT11 = 1 << 3;
    public static final byte BIT12 = 1 << 4;
    public static final byte BIT13 = 1 << 5;
    public static final byte BIT14 = 1 << 6;
    public static final byte BITS_25344 = 99;

    public static Direction getOpposite(Direction in) {
        return in.getOpposite();
    }

    public MazeGenerator(int xTotal, int zTotal, long seed) {
        this.xTotal = xTotal;
        this.zTotal = zTotal;
        this.seed = seed;
        this.rand = new Random(seed);
        this.grid = new CellState[zTotal][xTotal];
    }

    protected final void clearGrid() {
        for (CellState[] inner : this.grid) {
            for (int x = 0; x < inner.length; x++) {
                inner[x] = new CellState();
            }
        }
    }

    protected int getCenterX() {
        return 1 + this.xTotal / 2;
    }

    protected int getCenterZ() {
        return 1 + this.zTotal / 2;
    }

    protected Direction pickRandomDirection() {
        return Direction.Plane.HORIZONTAL.getRandomDirection(RandomSource.create(rand.nextLong()));
    }

    protected CellLoc pickNearCenterPosAtStart() {
        var pickedDir = pickRandomDirection();
        var centerX = getCenterX();
        var centerZ = getCenterZ();
        int nearCenterX = getCenterX() + pickedDir.getStepX();
        int nearCenterZ = getCenterZ() + pickedDir.getStepZ();
        if (this.grid[nearCenterZ][nearCenterX] == null) {
            this.grid[nearCenterZ][nearCenterX] = new CellState();
        }
        connectTo(centerX,centerZ,pickedDir,c -> true);
        cellsToExpand.clear();
        var picked = new CellLoc(nearCenterX, nearCenterZ);
        cellsToExpand.add(picked);
        return picked;
    }

    protected List<CellLoc> cellsToExpand = new ArrayList<>();

    protected boolean xzInMazeRange(int x, int z) {
        return 0 < x && x < this.xTotal - 1
                && 0 < z && z < this.zTotal - 1;
    }

    protected boolean xzInMazeRange(int x, int z, Direction dir) {
        x += dir.getStepX();
        z += dir.getStepZ();
        return xzInMazeRange(x, z);
    }

    public boolean generate() {
        clearGrid();
        int pickedCornerX = this.rand.nextBoolean() ? 0 : this.xTotal - 2;
        int pickedCornerZ = this.rand.nextBoolean() ? 0 : this.zTotal - 2;

        assert this.grid[pickedCornerZ][pickedCornerX] != null;
        assert this.grid[pickedCornerZ][pickedCornerX + 1] != null;
        assert this.grid[pickedCornerZ + 1][pickedCornerX] != null;
        assert this.grid[pickedCornerZ + 1][pickedCornerX + 1] != null;
        setCellFeature(this.grid[pickedCornerZ][pickedCornerX],PORTAL);
        setCellFeature(this.grid[pickedCornerZ][pickedCornerX + 1],BOSS_ROOM);
        setCellFeature(this.grid[pickedCornerZ + 1][pickedCornerX],BOSS_ROOM_2);
        setCellFeature(this.grid[pickedCornerZ + 1][pickedCornerX + 1],BOSS_ROOM_3);

        int centerX = 1 + this.xTotal / 2;
        int centerZ = 1 + this.zTotal / 2;
        setCellFeature(this.grid[centerZ][centerX],NONE);


//        blockItself();

        var picked = pickNearCenterPosAtStart();

        int nearCenterX = picked.x;
        int nearCenterZ = picked.z;
        var xArrayInRandomZ = this.grid[nearCenterZ];


        //cells.size():1

        if (!expandFirstRound()) {
            return false;
        }
        //====================================================================================

//        ensureAllNotNull();//since we dont have #blockItself();
        expandCenter(centerX, centerZ);
        //getWayFromCornerToAlreadyExpandedPath
        if (!getWayFromCornerToAlreadyExpandedPath(pickedCornerX, pickedCornerZ)) {
            return false;
        }
//        recoverInvalid();//since we dont mark invalid
        List<thaumcraft.common.lib.world.dim.CellLoc> deadEnds = getDeadEnds();
        if (deadEnds.isEmpty()) {
            return false;
        }
        placeKeyRoom(deadEnds);
        placeFeatureOnDeadEnds(deadEnds);
        placeFeatureOnAnyPath();
        return true;
    }

    protected void placeFeatureOnAnyPath() {
        for (int aa = 0; aa < this.zTotal; ++aa) {
            for (int bb = 0; bb < this.xTotal; ++bb) {
                CellState c = this.grid[aa][bb];
                boolean hasAnyDir = false;
                for (var dir : Direction.Plane.HORIZONTAL) {
                    if (c != null && cellConnectedTo(c,dir)) {
                        hasAnyDir = true;
                        break;
                    }
                }
                if (c != null && getCellFeature(c) == NONE && hasAnyDir && this.rand.nextInt(25) == 0) {
                    setCellFeature(c, FEATURE_SET_B[this.rand.nextInt(FEATURE_SET_B.length)]);
                }
            }
        }
    }

    protected void placeKeyRoom(List<thaumcraft.common.lib.world.dim.CellLoc> deadEnds) {
        thaumcraft.common.lib.world.dim.CellLoc ll = deadEnds.remove(this.rand.nextInt(deadEnds.size()));
        CellState c = this.grid[ll.z][ll.x];
        setCellFeature(c,KEY_ROOM);
    }

    protected void placeFeatureOnDeadEnds(List<thaumcraft.common.lib.world.dim.CellLoc> deadEnds) {
        int places = deadEnds.size() / 2;
        for (var runs = 0;runs < places; runs++) {
            int rInternal = this.rand.nextInt(deadEnds.size());
            thaumcraft.common.lib.world.dim.CellLoc llInternal = deadEnds.get(rInternal);
            CellState cInternal = this.grid[llInternal.z][llInternal.x];
            if (getCellFeature(cInternal) == NONE) {
                setCellFeature(cInternal, FEATURE_SET_A[this.rand.nextInt(FEATURE_SET_A.length)]);
                deadEnds.remove(rInternal);
                ++rInternal;
            }
        }
    }

    protected @NotNull List<thaumcraft.common.lib.world.dim.CellLoc> getDeadEnds() {
        var deadEndsAndCouldBe = getDeadEndsAndCouldBeDeadEnds();
        var deadEnds = deadEndsAndCouldBe[0];
        var couldBeDeadEnds = deadEndsAndCouldBe[1];

        if (deadEnds.isEmpty()) {
            if (!couldBeDeadEnds.isEmpty()) {
                var pickedDeadEnd = couldBeDeadEnds.remove(rand.nextInt(couldBeDeadEnds.size()));
                for (var dir : Direction.Plane.HORIZONTAL) {
                    connectTo(pickedDeadEnd.x,pickedDeadEnd.z,dir,c -> !c.isEmpty() && !getCellFeature(c).preventInitCorePathConnection);
                }
                deadEnds.add(pickedDeadEnd);
            }
        }
        return deadEnds;
    }

    protected @NotNull List<thaumcraft.common.lib.world.dim.CellLoc>[] getDeadEndsAndCouldBeDeadEnds() {
        List<thaumcraft.common.lib.world.dim.CellLoc> deadEndsLoc = new ArrayList<>();
        List<thaumcraft.common.lib.world.dim.CellLoc> couldBeDeadEndsLoc = new ArrayList<>();

        for (int aa = 0; aa < this.zTotal; ++aa) {
            for (int bb = 0; bb < this.xTotal; ++bb) {
                CellState c = this.grid[aa][bb];
                int exits = 0;
                int nearRoom = 0;
                for (var dir : Direction.Plane.HORIZONTAL) {
                    if (c != null && cellConnectedTo(c,dir)) {
                        exits++;
                    }

                    if (xzInMazeRange(aa, bb,dir)) {
                        var nearRoomInMaze = this.grid[aa + dir.getStepX()][bb + dir.getStepZ()];
                        if (nearRoomInMaze != null && !getCellFeature(nearRoomInMaze).preventInitCorePathConnection) {
                            nearRoom++;
                        }
                    }
                }
                if (exits == 1 && getCellFeature(c) == NONE) {
                    deadEndsLoc.add(new thaumcraft.common.lib.world.dim.CellLoc(aa, bb));
                }
                if (c != null && c.isEmpty()) {
                    if (nearRoom > 0){
                        couldBeDeadEndsLoc.add(new thaumcraft.common.lib.world.dim.CellLoc(aa, bb));
                    }
                }
            }
        }
        return new List[]{deadEndsLoc, couldBeDeadEndsLoc};
    }

    protected void recoverInvalid() {
        for (int aa = 0; aa < this.zTotal; ++aa) {
            for (int bb = 0; bb < this.xTotal; ++bb) {
                CellState c = this.grid[aa][bb];
                if (c != null && getCellFeature(c) == INVALID) {
                    setCellFeature(c,NONE);
                }
            }
        }
    }

    //BFS
    protected boolean getWayFromCornerToAlreadyExpandedPath(int pickedCornerX, int pickedCornerZ) {
        var traceBack = new Direction[zTotal][xTotal];
        ArrayDeque<CellLoc> neighboursToCheck = new ArrayDeque<>();
        neighboursToCheck.add(new CellLoc(pickedCornerX, pickedCornerZ));
        while (!neighboursToCheck.isEmpty()) {
            CellLoc toCheck = neighboursToCheck.poll();
            Direction[] directions1 = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
            ArrayUtils.shuffle(directions1, this.rand);
            for (var dir : directions1) {
                var nextX = toCheck.x + dir.getStepX();
                var nextZ = toCheck.z + dir.getStepZ();
                if (xzInMazeRange(nextX, nextZ)
                        && traceBack[nextZ][nextX] == null
                        && !(nextX == pickedCornerX && nextZ == pickedCornerZ)
                ) {
                    CellState next = grid[nextZ][nextX];
                    if (next == null) {
                        continue;
                    }
                    if (getCellFeature(next).preventInitCorePathConnection) {
                        continue;
                    }
                    traceBack[nextZ][nextX] = dir;
                    if (cellHasConnectedDirection(next)) {
                        connectTo(toCheck.x, toCheck.z, dir, c -> true);
                        int cx = toCheck.x;
                        int cz = toCheck.z;
                        while (traceBack[cz][cx] != null) {
                            Direction backDir = traceBack[cz][cx].getOpposite();
                            connectTo(cz, cx, backDir, c -> true);
                            cx += backDir.getStepX();
                            cz += backDir.getStepZ();
                        }
                        return true;
                    }
                    neighboursToCheck.add(new CellLoc(nextX, nextZ));
                }
            }
        }
        return false;
    }

    private boolean connectPickedConnerWithoutSearching(Direction[] directions, int pickedCornerX, int pickedCornerZ) {
        boolean connected = false;
        label338:
        for (int xOffset = 0; xOffset < 2; ++xOffset) {
            for (int zOffset = 0; zOffset < 2; ++zOffset) {
                for (var dir : directions) {
                    var pickedX = pickedCornerX + xOffset;
                    var pickedZ = pickedCornerZ + zOffset;
                    if (connectTo(
                            pickedX, pickedZ, dir, c -> !c.isEmpty()
                                    && getCellFeature(c) == NONE
                    )
                    ) {
                        connected = true;
                        break label338;
                    }
                }
            }
        }
        return connected;
    }

    protected void expandCenter(int centerX, int centerZ) {
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        ArrayUtils.shuffle(directions, this.rand);
        for (var dir : directions) {
            if (this.rand.nextBoolean()) {
                connectTo(centerX, centerZ, dir, c -> !c.isEmpty());
            }
        }
    }

    private void ensureAllNotNull() {
        for (int aa = 0; aa < this.zTotal; ++aa) {
            for (int bb = 0; bb < this.xTotal; ++bb) {
                if (this.grid[aa][bb] == null) {
                    this.grid[aa][bb] = new CellState();
                }
            }
        }
    }

    protected boolean connectTo(int x, int z, Direction dir, Predicate<@NotNull CellState> cellPredicate) {
        if (!xzInMazeRange(x, z, dir)) {
            return false;
        }
        var nearXZ = this.grid[z + dir.getStepZ()][x + dir.getStepX()];
        if (nearXZ == null) {
            return false;
        }
        if (!cellPredicate.test(nearXZ)) {
            return false;
        }
        addCellConnectedDirection(this.grid[z][x],dir);
        addCellConnectedDirection(nearXZ,dir.getOpposite());
        return true;
    }

    protected boolean expandFirstRound() {
        boolean success = false;
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        while (!cellsToExpand.isEmpty()) {
            int index = this.getNextIndex(cellsToExpand.size());
            int x = cellsToExpand.get(index).x;
            int z = cellsToExpand.get(index).z;
            ArrayUtils.shuffle(directions, this.rand);
            boolean carved = false;

            for (var dir : directions) {
                if (connectTo(x, z, dir, CellState::isEmpty)) {
                    cellsToExpand.add(new CellLoc(x, z, dir));
                    carved = true;
                    success = true;
                    break;
                }
            }

            if (!carved) {
                cellsToExpand.remove(index);
            }
        }
        return success;
    }

    protected void blockItself() {
        //foolish idea...
        int l = (this.xTotal + this.zTotal) / 4;

        for (int z = 0; z < l; ++z) {
            int w = 1 + this.rand.nextInt(3);
            if (w > 2) {
                --l;
            }

            int qq = this.rand.nextInt(this.xTotal - w);
            int ww = this.rand.nextInt(this.zTotal - w);

            for (int a = qq; a < qq + w; ++a) {
                for (int b = ww; b < ww + w; ++b) {
                    if (this.grid[b][a].isEmpty()) {
                        this.grid[b][a] = null;
                    }
                }
            }
        }
    }

    protected int getNextIndex(int ceil) {
        float r = this.rand.nextFloat();
        if (r <= 0.45F) {
            return ceil - 1;
        } else {
            return r <= 0.9F ? this.rand.nextInt(ceil) : 0;
        }
    }

//    public void print() {
//        HashMap<CellState, String[]> tiles = new HashMap<>();
//        tiles.put(new CellState(), new String[]{"...", "...", "..."});
//        tiles.put(new CellState(Direction.NORTH), new String[]{"# #", "# #", "###"});
//        tiles.put(new CellState(Direction.SOUTH), new String[]{"###", "# #", "# #"});
//        tiles.put(new CellState(Direction.EAST), new String[]{"###", "#  ", "###"});
//        tiles.put(new CellState(Direction.WEST), new String[]{"###", "  #", "###"});
//        tiles.put(new CellState(Direction.NORTH, Direction.SOUTH), new String[]{"# #", "# #", "# #"});
//        tiles.put(new CellState(Direction.NORTH, Direction.WEST), new String[]{"# #", "  #", "###"});
//        tiles.put(new CellState(Direction.NORTH, Direction.EAST), new String[]{"# #", "#  ", "###"});
//        tiles.put(new CellState(Direction.SOUTH, Direction.WEST), new String[]{"###", "  #", "# #"});
//        tiles.put(new CellState(Direction.SOUTH, Direction.EAST), new String[]{"###", "#  ", "# #"});
//        tiles.put(new CellState(Direction.WEST, Direction.EAST), new String[]{"###", "   ", "###"});
//        tiles.put(new CellState(Direction.NORTH, Direction.SOUTH, Direction.EAST), new String[]{"# #", "#  ", "# #"});
//        tiles.put(new CellState(Direction.NORTH, Direction.SOUTH, Direction.WEST), new String[]{"# #", "  #", "# #"});
//        tiles.put(new CellState(Direction.NORTH, Direction.WEST, Direction.EAST), new String[]{"# #", "   ", "###"});
//        tiles.put(new CellState(Direction.SOUTH, Direction.WEST, Direction.EAST), new String[]{"###", "   ", "# #"});
//        tiles.put(new CellState(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST), new String[]{"# #", "   ", "# #"});
//        tiles.put(new CellState(NONE, Direction.NORTH, Direction.SOUTH), new String[]{"#-#", " A ", "#-#"});
//        tiles.put(new CellState(NONE, Direction.WEST, Direction.EAST), new String[]{"# #", "|A|", "# #"});
//        tiles.put(new CellState(PORTAL, Direction.NORTH, Direction.SOUTH), new String[]{"#-#", " B ", "#-#"});
//        tiles.put(new CellState(PORTAL, Direction.WEST, Direction.EAST), new String[]{"# #", "|B|", "# #"});
//
//        for (int y = 0; y < this.zTotal; ++y) {
//            for (var q : FEATURE_SET_C) {
//                for (int x = 0; x < this.xTotal; ++x) {
//                    CellState c = this.grid[y][x];
//                    if (c != null && tiles.containsKey(c)) {
////                        TODO:What is this?
////                        System.out.print(((String[]) tiles.get(c))[q.ordinal()]);
//                    } else if (c != null && c.feature == PORTAL) {
//                        if ((q != NONE
//                                ||
//                                !c.availableDirs.contains(Direction.NORTH))
//                                && (q != PORTAL || !c.availableDirs.contains(Direction.SOUTH))
//                        ) {
//                            if (q == NONE
//                                    && c.availableDirs.contains(Direction.WEST)
//                                    && !c.availableDirs.contains(Direction.EAST)
//                            ) {
//                                System.out.print("-PP");
//                            } else if (q == NONE
//                                    && c.availableDirs.contains(Direction.EAST)
//                                    && !c.availableDirs.contains(Direction.WEST)
//                            ) {
//                                System.out.print("PP-");
//                            } else if (
//                                    q == NONE
//                                            && c.availableDirs.contains(Direction.WEST)
//                                            && c.availableDirs.contains(Direction.EAST)
//                            ) {
//                                System.out.print("-P-");
//                            } else {
//                                System.out.print("PPP");
//                            }
//                        } else {
//                            System.out.print("P|P");
//                        }
//                    } else if (c != null && c.feature != NONE) {
//                        System.out.print("FFF");
//                    }
//                }
//
//                System.out.println();
//            }
//        }
//
//    }
}
