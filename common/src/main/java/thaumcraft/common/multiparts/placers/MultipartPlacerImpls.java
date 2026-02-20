package thaumcraft.common.multiparts.placers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceAlembicBlock;
import thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceBaseCornerBlock;
import thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceNozzleBlock;
import thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceUpperFenceBlock;
import thaumcraft.common.blocks.multipartcomponent.infernalfurnace.AbstractInfernalFurnaceComponent;

import static thaumcraft.common.blocks.multipartcomponent.infernalfurnace.InfernalFurnaceCornerBlock.*;
import static thaumcraft.common.blocks.multipartcomponent.infernalfurnace.InfernalFurnaceEdgeXAxisBlock.*;
import static thaumcraft.common.blocks.multipartcomponent.infernalfurnace.InfernalFurnaceEdgeYAxisBlock.*;
import static thaumcraft.common.blocks.multipartcomponent.infernalfurnace.InfernalFurnaceEdgeZAxisBlock.*;
import static thaumcraft.common.blocks.multipartcomponent.infernalfurnace.InfernalFurnaceSideBlock.*;

public class MultipartPlacerImpls {
    public static class InfernalFurnaceBlockPlacerImpls {
        @Contract(pure = true)
        public static @NotNull IBlockPlacer getCornerPlacerWithState(int stateValue) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.INFERNAL_FURNACE_CORNER.defaultBlockState().setValue(
                        CORNER_TYPE, stateValue);
                state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
                level.setBlockAndUpdate(pos, state);
            });
        }
        @Contract(pure = true)
        public static @NotNull IBlockPlacer getXAxisPlacerWithState(int stateValue) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.INFERNAL_FURNACE_X_AXIS.defaultBlockState().setValue(
                        EDGE_TYPE_X_AXIS, stateValue);
                state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
                level.setBlockAndUpdate(pos, state);
            });
        }
        @Contract(pure = true)
        public static @NotNull IBlockPlacer getYAxisPlacerWithState(int stateValue) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.INFERNAL_FURNACE_Y_AXIS.defaultBlockState().setValue(
                        EDGE_TYPE_Y_AXIS, stateValue);
                state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
                level.setBlockAndUpdate(pos, state);
            });
        }
        @Contract(pure = true)
        public static @NotNull IBlockPlacer getZAxisPlacerWithState(int stateValue) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.INFERNAL_FURNACE_Z_AXIS.defaultBlockState().setValue(
                        EDGE_TYPE_Z_AXIS, stateValue);
                state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
                level.setBlockAndUpdate(pos, state);
            });
        }
        @Contract(pure = true)
        public static @NotNull IBlockPlacer getSidePlacerWithState(int stateValue) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.INFERNAL_FURNACE_SIDE.defaultBlockState().setValue(
                        SIDE_TYPE, stateValue);
                state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
                level.setBlockAndUpdate(pos, state);
            });
        }
        public static final IBlockPlacer BOTTOM_PLACER = ((level, pos, multipartMatchInfo) -> {
            BlockState state = ThaumcraftBlocks.INFERNAL_FURNACE_BOTTOM.defaultBlockState();
            state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
            level.setBlockAndUpdate(pos, state);
        });
        public static final IBlockPlacer LAVA_PLACER = ((level, pos, multipartMatchInfo) -> {
            BlockState state = ThaumcraftBlocks.INFERNAL_FURNACE_LAVA.defaultBlockState();
            state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
            level.setBlockAndUpdate(pos, state);
        });
        public static final IBlockPlacer BAR_PLACER =
            ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.INFERNAL_FURNACE_BAR.defaultBlockState();
                state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
                level.setBlockAndUpdate(pos, state);
            });

        public static final IBlockPlacer[][][] PLACER = new IBlockPlacer[][][]{
            {
                {getCornerPlacerWithState(CORNER_0_0_0), getZAxisPlacerWithState(EDGE_TYPE_0_0_1), getCornerPlacerWithState(CORNER_0_0_2)},
                {getXAxisPlacerWithState(EDGE_TYPE_1_0_0), BOTTOM_PLACER, getXAxisPlacerWithState(EDGE_TYPE_1_0_2),},
                {getCornerPlacerWithState(CORNER_2_0_0), getZAxisPlacerWithState(EDGE_TYPE_2_0_1), getCornerPlacerWithState(CORNER_2_0_2)},
            },
            {
                {getYAxisPlacerWithState(EDGE_TYPE_0_1_0), getSidePlacerWithState(SIDE_TYPE_0_1_1), getYAxisPlacerWithState(EDGE_TYPE_0_1_2),},
                {getSidePlacerWithState(SIDE_TYPE_1_1_0), LAVA_PLACER, getSidePlacerWithState(SIDE_TYPE_1_1_2),},
                {getYAxisPlacerWithState(EDGE_TYPE_2_1_0), BAR_PLACER, getYAxisPlacerWithState(EDGE_TYPE_2_1_2)},
            },
            {
                {getCornerPlacerWithState(CORNER_0_2_0), getZAxisPlacerWithState(EDGE_TYPE_0_2_1), getCornerPlacerWithState(CORNER_0_2_2)},
                {getXAxisPlacerWithState(EDGE_TYPE_1_2_0), null, getXAxisPlacerWithState(EDGE_TYPE_1_2_2),},
                {getCornerPlacerWithState(CORNER_2_2_0), getZAxisPlacerWithState(EDGE_TYPE_2_2_1), getCornerPlacerWithState(CORNER_2_2_2)},
            },
        };
    }
    public static class AdvancedAlchemicalPlacerImpls {
        public static IBlockPlacer getCornerPlacer(int cornerType) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_BASE_CORNER.defaultBlockState().setValue(
                        AdvancedAlchemicalFurnaceBaseCornerBlock.AT_CORNER, cornerType);
                level.setBlockAndUpdate(pos, state);
            });
        }
        public static IBlockPlacer getAlembicPlacer(int cornerType) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_ALEMBIC.defaultBlockState().setValue(
                        AdvancedAlchemicalFurnaceAlembicBlock.AT_CORNER, cornerType);
                level.setBlockAndUpdate(pos, state);
            });
        }
        public static IBlockPlacer getFencePlacer(Direction direction) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_UPPER_FENCE.defaultBlockState().setValue(
                        AdvancedAlchemicalFurnaceUpperFenceBlock.FACING, direction);
                level.setBlockAndUpdate(pos, state);
            });
        }
        public static IBlockPlacer getNozzlePlacer(Direction direction) {
            return ((level, pos, multipartMatchInfo) -> {
                BlockState state = ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_NOZZLE.defaultBlockState().setValue(
                        AdvancedAlchemicalFurnaceNozzleBlock.FACING, direction);
                level.setBlockAndUpdate(pos, state);
            });
        }
        public static final IBlockPlacer BASE_PLACER = (level, pos, multipartMatchInfo) -> {
            BlockState state = ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_BASE.defaultBlockState();
            state = AbstractInfernalFurnaceComponent.setRotation(state, multipartMatchInfo.usingRotation());
            level.setBlockAndUpdate(pos, state);
        };
        //↑x-(W)
        //←z-(N)→z+(S)
        //↓x+E
        public static final IBlockPlacer[][][] PLACER = new IBlockPlacer[][][]{
                {
                        {getCornerPlacer(AdvancedAlchemicalFurnaceBaseCornerBlock.CORNER_NORTH_WEST),getNozzlePlacer(Direction.WEST),getCornerPlacer(AdvancedAlchemicalFurnaceBaseCornerBlock.CORNER_SOUTH_WEST)},
                        {getNozzlePlacer(Direction.NORTH),BASE_PLACER,getNozzlePlacer(Direction.SOUTH)},
                        {getCornerPlacer(AdvancedAlchemicalFurnaceBaseCornerBlock.CORNER_NORTH_EAST),getNozzlePlacer(Direction.EAST),getCornerPlacer(AdvancedAlchemicalFurnaceBaseCornerBlock.CORNER_SOUTH_EAST)},
                },
                {
                        {getAlembicPlacer(AdvancedAlchemicalFurnaceAlembicBlock.CORNER_NORTH_WEST),getFencePlacer(Direction.WEST),getAlembicPlacer(AdvancedAlchemicalFurnaceAlembicBlock.CORNER_SOUTH_WEST)},
                        {getFencePlacer(Direction.NORTH),null,getNozzlePlacer(Direction.SOUTH)},
                        {getAlembicPlacer(AdvancedAlchemicalFurnaceAlembicBlock.CORNER_NORTH_EAST),getNozzlePlacer(Direction.EAST),getFencePlacer(Direction.SOUTH),getAlembicPlacer(AdvancedAlchemicalFurnaceAlembicBlock.CORNER_SOUTH_EAST)},
                }
        };
    }
    public static final IMultipartPlacer INFERNAL_FURNACE_PLACER = new SimpleMultipartPlacer(
            InfernalFurnaceBlockPlacerImpls.PLACER
            ,new BlockPos(2,1,1));
    public static final IMultipartPlacer ADVANCED_ALCHEMICAL_FURNACE_PLACER = new SimpleMultipartPlacer(
            AdvancedAlchemicalPlacerImpls.PLACER,
            new BlockPos(1,0,1)
    );
}
