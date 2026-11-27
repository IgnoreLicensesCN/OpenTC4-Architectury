package thaumcraft.common.lib.world.dim.cell.features.bossroom;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.common.lib.world.dim.cell.CellState;
import thaumcraft.common.lib.world.dim.cell.features.AbstractBossRoom;

public class BossRoomUpperRight extends AbstractBossRoom {
    public static final BossRoomUpperRight INSTANCE = new BossRoomUpperRight(true);
    protected BossRoomUpperRight(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    @Override
    protected int getBossRoomIndex() {
        return 1;
    }

    @Override
    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        placeRoomMainPart(world,random,cx,cz,y,Direction.EAST,cellState);
        super.placeFeatureAt(world, random, cx, cz, y, cellState);
    }

}
