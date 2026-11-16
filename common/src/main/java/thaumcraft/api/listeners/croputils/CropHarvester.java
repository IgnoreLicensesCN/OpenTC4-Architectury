package thaumcraft.api.listeners.croputils;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.utils.CropUtils;

@SuppressWarnings("UnusedDeclaration")
@AllArgsConstructor
public abstract class CropHarvester implements Comparable<CropHarvester> {
    public static final ListenerManager<CropHarvester> CROP_HARVESTERS = new ListenerManager<>();
    public final int weight;

    public abstract boolean canHarvestWithThis(ServerLevel level, BlockPos pos, @Nullable Entity harvester);

    public abstract void tryHarvest(ServerLevel level, BlockPos pos, @Nullable Entity harvester);

    @Override
    public int compareTo(@NotNull CropHarvester o) {
        return Integer.compare(this.weight, o.weight);
    }

    @AllArgsConstructor
    public enum DefaultImpl {
        EMPTY_HAND_USE(new CropHarvester(100) {
            @Override
            public boolean canHarvestWithThis(ServerLevel level, BlockPos pos, @Nullable Entity harvester) {
                return emptyHandUseCanHarvest(level, pos, harvester);
            }

            @Override
            public void tryHarvest(ServerLevel level, BlockPos pos, @Nullable Entity harvester) {
                harvestWithEmptyHandUse(level, pos, harvester);
            }
        }),
        DIRECTLY_BREAK(new CropHarvester(100) {
            @Override
            public boolean canHarvestWithThis(ServerLevel level, BlockPos pos, @Nullable Entity harvester) {
                return canDirectlyBreakHarvest(level, pos, harvester);
            }

            @Override
            public void tryHarvest(ServerLevel level, BlockPos pos, @Nullable Entity harvester) {
                directlyBreakHarvest(level, pos, harvester);
            }
        }),
        ;
        public final CropHarvester harvester;

        public static boolean emptyHandUseCanHarvest(ServerLevel level, BlockPos pos, @Nullable Entity harvester) {
            return UseHarvestableCropChecker.isUseBlockHarvestableCrop(level, pos, harvester);
        }

        public static void harvestWithEmptyHandUse(ServerLevel level, BlockPos pos, @Nullable Entity harvester) {
            CropUtils.harvestCropUsingRightClick(level, pos, harvester);
        }

        public static boolean canDirectlyBreakHarvest(ServerLevel level, BlockPos pos, @Nullable Entity harvester) {
            return DirectlyBreakHarvestableCropChecker.isDirectlyBreakHarvestableCrop(level, pos, harvester);
        }

        public static void directlyBreakHarvest(ServerLevel level, BlockPos pos, @Nullable Entity harvester) {
            level.destroyBlock(pos, true, harvester);
        }
    }

    public static void init() {
        for (var harvester : DefaultImpl.values()) {
            CROP_HARVESTERS.registerListener(harvester.harvester);
        }
    }
}
