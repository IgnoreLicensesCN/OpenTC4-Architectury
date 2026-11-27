package thaumcraft.common.lib.world.dim.cell.features;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.world.dim.cell.CellState;

public class PassageWithTrap extends CellFeature {
    public static final PassageWithTrap INSTANCE = new PassageWithTrap(false);
    protected PassageWithTrap(boolean preventInitCorePathConnection) {
        super(preventInitCorePathConnection);
    }
    @Override
    public @Nullable BlockState getTrapStone(WorldGenLevel world, RandomSource random, int x, int y, int z, CellState cellState) {
        return random.nextInt(3) == 0 ? T : null;
    }
}
