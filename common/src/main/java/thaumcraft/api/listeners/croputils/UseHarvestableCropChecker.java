package thaumcraft.api.listeners.croputils;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.SweetBerryBushBlock.AGE;

@AllArgsConstructor
public abstract class UseHarvestableCropChecker implements Comparable<UseHarvestableCropChecker> {
    public static final ListenerManager<UseHarvestableCropChecker> USE_HARVESTABLE_CHECKERS = new ListenerManager<>();
    public final int weight;

    public static boolean isUseBlockHarvestableCrop(Level world, BlockPos pos, @Nullable Entity harvester) {
        var state = world.getBlockState(pos);
        for (var checker : USE_HARVESTABLE_CHECKERS.getListeners()) {
            var result = checker.checkIfUseHarvestable(world, pos, state, harvester);
            if (result != null) {
                return result;
            }
        }
        return false;
    }

    public abstract @Nullable Boolean checkIfUseHarvestable(Level world, BlockPos pos, BlockState state, @Nullable Entity harvester);

    @Override
    public int compareTo(@NotNull UseHarvestableCropChecker o) {
        return Integer.compare(weight, o.weight);
    }

    @AllArgsConstructor
    public enum DefaultImpls {
        SWEET_BERRY(new UseHarvestableCropChecker(100) {
            @Override
            public @Nullable Boolean checkIfUseHarvestable(Level world, BlockPos pos, BlockState state, @Nullable Entity harvester) {
                return checkSweetBerry(world, pos, state, harvester);
            }
        }),
        GLOW_BERRY(new UseHarvestableCropChecker(100) {
            @Override
            public @Nullable Boolean checkIfUseHarvestable(Level world, BlockPos pos, BlockState state, @Nullable Entity harvester) {
                return checkGlowBerry(world, pos, state, harvester);
            }
        }),
        ;

        public static @Nullable("null if dont return") Boolean checkSweetBerry(Level world, BlockPos pos, BlockState state,Entity harvester) {
            if (state.getBlock() instanceof SweetBerryBushBlock && state.getValue(AGE) >= SweetBerryBushBlock.MAX_AGE) {
                return Boolean.TRUE;
            }
            return null;
        }

        public static @Nullable("null if dont return") Boolean checkGlowBerry(Level world, BlockPos pos, BlockState state,Entity harvester) {
            if (state.getBlock() instanceof CaveVines && CaveVines.hasGlowBerries(state)) {
                return Boolean.TRUE;
            }
            return null;
        }

        public final UseHarvestableCropChecker checker;
    }

    public static void init(){
        for (var defaultImpl : UseHarvestableCropChecker.DefaultImpls.values()){
            USE_HARVESTABLE_CHECKERS.registerListener(defaultImpl.checker);
        }
    }
}
