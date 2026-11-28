package thaumcraft.common.lib.world.dim.cell.features.bossroom;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.common.lib.world.dim.cell.CellState;

public class BossRoomBottomRight extends AbstractBossRoom {
    public static final BossRoomBottomRight INSTANCE = new BossRoomBottomRight(true);
    protected BossRoomBottomRight(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }

    @Override
    protected int getBossRoomIndex() {
        return 2;
    }

    @Override
    public void placeFeatureAt(WorldGenLevel world, RandomSource random, int cx, int cz, int y, CellState cellState) {
        placeRoomMainPart(world,random,cx,cz,y,Direction.SOUTH,cellState);
        super.placeFeatureAt(world, random, cx, cz, y, cellState);
    }

}
