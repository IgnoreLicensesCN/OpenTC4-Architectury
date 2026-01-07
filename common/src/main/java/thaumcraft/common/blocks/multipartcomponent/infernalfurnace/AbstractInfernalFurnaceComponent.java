package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import com.linearity.opentc4.VecTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.abstracts.AbstractMultipartComponentBlock;
import thaumcraft.common.multiparts.matchers.IFormedMultipartMatcher;
import thaumcraft.common.multiparts.matchers.MultipartMatchInfo;

import static thaumcraft.common.multiparts.matchers.FormedMultipartMatcherImpls.INFERNAL_FURNACE_FORMED;

public abstract class AbstractInfernalFurnaceComponent extends AbstractMultipartComponentBlock {
    public AbstractInfernalFurnaceComponent(Properties properties) {
        super(properties);
    }
    public AbstractInfernalFurnaceComponent() {
        super(Properties
                .copy(Blocks.OBSIDIAN)
                .strength(10.0f,500.f)
                .lightLevel(s -> 1)
        );
    }

    @Override
    public IFormedMultipartMatcher getMultipartMatcher(Level level, BlockState state, BlockPos pos) {
        return INFERNAL_FURNACE_FORMED;
    }
    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_0);
    }

    public static final MultipartMatchInfo MATCH_INFO_Y_0 = new MultipartMatchInfo(
            VecTransformations.Rotation3D.NONE,
            VecTransformations.Mirror3D.NONE
    );
    public static final MultipartMatchInfo MATCH_INFO_Y_90 = new MultipartMatchInfo(
            VecTransformations.Rotation3D.Y_90,
            VecTransformations.Mirror3D.NONE
    );
    public static final MultipartMatchInfo MATCH_INFO_Y_180 = new MultipartMatchInfo(
            VecTransformations.Rotation3D.Y_180,
            VecTransformations.Mirror3D.NONE
    );
    public static final MultipartMatchInfo MATCH_INFO_Y_270 = new MultipartMatchInfo(
            VecTransformations.Rotation3D.Y_270,
            VecTransformations.Mirror3D.NONE
    );

    @Override
    public MultipartMatchInfo getMatchInfo(Level level, BlockState state, BlockPos pos) {
        int yAxis = state.getValue(ROTATION_Y_AXIS);
        if (yAxis == ROTATION_DEGREE_0) {
            return MATCH_INFO_Y_0;
        }
        if (yAxis == ROTATION_DEGREE_90) {
            return MATCH_INFO_Y_90;
        }
        if (yAxis == ROTATION_DEGREE_180) {
            return MATCH_INFO_Y_180;
        }
        if (yAxis == ROTATION_DEGREE_270) {
            return MATCH_INFO_Y_270;
        }
        throw new IllegalArgumentException("Invalid yAxis state: " + yAxis);
    }

    @Override
    public VecTransformations.Rotation3D getRotation(BlockState state) {
        int yAxis = state.getValue(ROTATION_Y_AXIS);
        if (yAxis == ROTATION_DEGREE_0) {
            return VecTransformations.Rotation3D.NONE;
        }
        if (yAxis == ROTATION_DEGREE_90) {
            return VecTransformations.Rotation3D.Y_90;
        }
        if (yAxis == ROTATION_DEGREE_180) {
            return VecTransformations.Rotation3D.Y_180;
        }
        if (yAxis == ROTATION_DEGREE_270) {
            return VecTransformations.Rotation3D.Y_270;
        }
        throw new IllegalArgumentException("Invalid yAxis state: " + yAxis);
    }
}
