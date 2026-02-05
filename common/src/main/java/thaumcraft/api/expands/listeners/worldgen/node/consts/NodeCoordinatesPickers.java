package thaumcraft.api.expands.listeners.worldgen.node.consts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import thaumcraft.api.expands.listeners.worldgen.node.PickNodeCoordinateContext;
import thaumcraft.api.expands.listeners.worldgen.node.listeners.PickNodeCoordinatesListener;
import thaumcraft.common.lib.utils.Utils;


import static net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG;

public class NodeCoordinatesPickers {
    public static final PickNodeCoordinateContext[] EMPTY_CONTEXTS = new PickNodeCoordinateContext[0];
    public static final PickNodeCoordinatesListener defaultPicker = new PickNodeCoordinatesListener(0) {
        @Override
        
        public PickNodeCoordinateContext[] pickNodeCoordinates(Level world, RandomSource random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            int selectedY = Utils.getFirstUncoveredY(world, x, z);

            int avgGroundLvl = 64;
            if (world instanceof ServerLevel serverLevel
            && serverLevel.getChunkSource().getGenerator() instanceof FlatLevelSource flatSource
            ) {
                avgGroundLvl = flatSource.getBaseHeight(
                        x,
                        z,
                        WORLD_SURFACE_WG,//idk how to get
                        serverLevel,
                        serverLevel.getChunkSource().randomState()
                );
            }

            if (selectedY < 2) {
                selectedY = avgGroundLvl
                        + random.nextInt(64) - 32
                        + Utils.getFirstUncoveredY(world, x, z);
            }

            if (selectedY < 2) {
                selectedY = 32 + random.nextInt(64);
            }

            if (world.isEmptyBlock(new BlockPos(x, selectedY + 1, z))) {
                ++selectedY;
            }

            int p = random.nextInt(4);
            BlockState b = world.getBlockState(new BlockPos(x, selectedY + p, z));

            if (b.isAir() || b.canBeReplaced()) {
                selectedY += p;
            }
            if (selectedY > world.getMinBuildHeight()) {
                return EMPTY_CONTEXTS;
            }
            return new PickNodeCoordinateContext[]{new PickNodeCoordinateContext(new BlockPos(x, selectedY, z), false, false, false)};
        }
    };
}
