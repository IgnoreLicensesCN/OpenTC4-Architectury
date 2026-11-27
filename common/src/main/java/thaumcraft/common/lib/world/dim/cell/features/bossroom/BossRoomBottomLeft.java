package thaumcraft.common.lib.world.dim.cell.features.bossroom;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.common.lib.world.dim.cell.CellState;
import thaumcraft.common.lib.world.dim.cell.features.AbstractBossRoom;

public class BossRoomBottomLeft extends AbstractBossRoom {
    public static final BossRoomBottomLeft INSTANCE = new BossRoomBottomLeft(true);
    protected BossRoomBottomLeft(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    @Override
    protected int getBossRoomIndex() {
        return 3;
    }

    @Override
    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        placeRoomMainPart(world,random,cx,cz,y,Direction.WEST,cellState);
        super.placeFeatureAt(world, random, cx, cz, y, cellState);
    }

}
