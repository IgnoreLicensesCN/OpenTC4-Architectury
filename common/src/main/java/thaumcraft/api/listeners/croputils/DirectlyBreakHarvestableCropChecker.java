package thaumcraft.api.listeners.croputils;

import com.linearity.opentc4.mixin.accessors.IntegerPropertyAccessor;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

import static thaumcraft.api.listeners.croputils.DirectlyBreakHarvestableCropChecker.DefaultImpls.BREAK_HARVESTABLE_CHECKERS;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.*;

@SuppressWarnings("UnusedDeclaration")
@AllArgsConstructor
public abstract class DirectlyBreakHarvestableCropChecker implements Comparable<DirectlyBreakHarvestableCropChecker> {
    public final int weight;

    public static boolean isDirectlyBreakHarvestableCrop(
            Level world, BlockPos pos, @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester) {
        var state = world.getBlockState(pos);
        for (var checker : BREAK_HARVESTABLE_CHECKERS.getListeners()) {
            var result = checker.checkIfDirectlyBreakHarvestable(world, pos, harvester, state);
            if (result != null) {
                return result;
            }
        }
        return false;
    }

    public abstract @Nullable("null if dont return")
    Boolean checkIfDirectlyBreakHarvestable(
            Level world,
            BlockPos pos,
            @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
            BlockState state);

    @Override
    public int compareTo(@NotNull DirectlyBreakHarvestableCropChecker o) {
        return Integer.compare(this.weight, o.weight);
    }

    @AllArgsConstructor
    public enum DefaultImpls {
        NOT_HARVESTABLE(new DirectlyBreakHarvestableCropChecker(100) {
            @Override
            public @Nullable("null if dont return")
            Boolean checkIfDirectlyBreakHarvestable(
                    Level world,
                    BlockPos pos,
                    @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                    BlockState state) {
                return checkNotHarvestable(world, pos, harvester, state);
            }
        }),
        CROP_BLOCK_MAX_AGE(new DirectlyBreakHarvestableCropChecker(200) {
            @Override
            public @Nullable("null if dont return")
            Boolean checkIfDirectlyBreakHarvestable(
                    Level world,
                    BlockPos pos,
                    @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                    BlockState state) {
                return checkForCropBlock(world, pos, harvester, state);
            }
        }),
        REED_LIKE(new DirectlyBreakHarvestableCropChecker(300) {
            @Override
            public @Nullable("null if dont return")
            Boolean checkIfDirectlyBreakHarvestable(
                    Level world,
                    BlockPos pos,
                    @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                    BlockState state) {
                return checkReedLike(world, pos, harvester, state);
            }
        }),
        MEETS_AGE_REGISTERED(new DirectlyBreakHarvestableCropChecker(400) {
            @Override
            public @Nullable("null if dont return")
            Boolean checkIfDirectlyBreakHarvestable(
                    Level world,
                    BlockPos pos,
                    @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                    BlockState state) {
                return checkMeetsRegisteredAge(world, pos, harvester, state);
            }
        }),
        DIRECTLY_HARVESTABLE(new DirectlyBreakHarvestableCropChecker(600) {
            @Override
            public @Nullable("null if dont return")
            Boolean checkIfDirectlyBreakHarvestable(
                    Level world,
                    BlockPos pos,
                    @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                    BlockState state) {
                return checkDirectlyHarvestable(world, pos, harvester, state);
            }
        }),
        ;
        public static final ListenerManager<DirectlyBreakHarvestableCropChecker> BREAK_HARVESTABLE_CHECKERS = new ListenerManager<>();
        public static final Map<Block, IntegerProperty>
                blockWithAgeProperty = new IdentityHashMap<>();
        public static final Map<Block, ObjectIntPair<IntegerProperty> /*value:age considered fully grown*/>
                blockWithAgePropertyAndExpectedValue = new IdentityHashMap<>();

        public static void init() {
            blockWithAgePropertyAndExpectedValue.put(Blocks.COCOA, new ObjectIntPair<>(CocoaBlock.AGE, CocoaBlock.MAX_AGE));
            blockWithAgePropertyAndExpectedValue.put(Blocks.NETHER_WART, new ObjectIntPair<>(NetherWartBlock.AGE, NetherWartBlock.MAX_AGE));
            blockWithAgePropertyAndExpectedValue.put(Blocks.SWEET_BERRY_BUSH, new ObjectIntPair<>(SweetBerryBushBlock.AGE, Integer.MAX_VALUE));
            blockWithAgePropertyAndExpectedValue.put(Blocks.CAVE_VINES_PLANT, new ObjectIntPair<>(CaveVinesBlock.AGE, Integer.MAX_VALUE));
        }

        public final DirectlyBreakHarvestableCropChecker checker;

        public static @Nullable("null if dont return") Boolean checkNotHarvestable(
                Level world,
                BlockPos pos,
                @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                BlockState state) {
            if (state.is(NOT_BREAK_HARVESTABLE)) {
                return Boolean.FALSE;
            }
            return null;
        }

        public static @Nullable("null if dont return") Boolean checkForCropBlock(
                Level world,
                BlockPos pos,
                @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                BlockState state) {
            if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)) {
                return Boolean.TRUE;
            }
            return null;
        }

        public static @Nullable("null if dont return") Boolean checkReedLike(
                Level world,
                BlockPos pos,
                @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                BlockState state) {
            if (state.is(REED_LIKE_DIRECTLY_BREAK_HARVESTABLE)) {
                return world.getBlockState(pos.below()).getBlock() == state.getBlock();
            }
            return null;
        }

        public static @Nullable("null if dont return") Boolean checkMeetsRegisteredAge(
                Level world,
                BlockPos pos,
                @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                BlockState state) {
            var block = state.getBlock();
            var gotAgePropFromMap = blockWithAgeProperty.get(block);
            if (gotAgePropFromMap != null && state.getValue(gotAgePropFromMap) >= ((IntegerPropertyAccessor) gotAgePropFromMap).getMax()) {
                return true;
            }
            var gotAgePropAndExpectedValueFromMap = blockWithAgePropertyAndExpectedValue.get(block);
            if (gotAgePropAndExpectedValueFromMap != null
                    && state.getValue(gotAgePropAndExpectedValueFromMap.left())
                    >= gotAgePropAndExpectedValueFromMap.rightInt()) {
                return Boolean.TRUE;
            }
            return null;
        }

        public static @Nullable("null if dont return") Boolean checkMeetsAgeDefault(
                Level world,
                BlockPos pos,
                @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                BlockState state) {
            var block = state.getBlock();
            if (blockWithAgeProperty.containsKey(block) || blockWithAgePropertyAndExpectedValue.containsKey(block)) {
                return null;//you may want set Integer.MAX so age wont meet check condition
            }
            for (var property : state.getProperties()) {
                if (property instanceof IntegerProperty integerProperty) {
                    if (property.getName().equals("age")) {
                        blockWithAgeProperty.put(block, integerProperty);
                        return state.getValue(integerProperty) >= ((IntegerPropertyAccessor) integerProperty).getMax() ? Boolean.TRUE : null;
                    }
                }
            }
            return null;
        }

        public static @Nullable("null if dont return") Boolean checkDirectlyHarvestable(
                Level world,
                BlockPos pos,
                @Nullable("golem in vanilla tc4 usage but idk if you want more") Entity harvester,
                BlockState state) {
            if (state.is(DIRECTLY_BREAK_HARVESTABLE)) {
                return Boolean.TRUE;
            }
            return null;
        }
    }

    public static void init() {
        for (var defaultImpl : DefaultImpls.values()) {
            BREAK_HARVESTABLE_CHECKERS.registerListener(defaultImpl.checker);
        }
        DefaultImpls.init();
    }
}
