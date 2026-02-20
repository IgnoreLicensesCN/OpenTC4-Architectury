package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import com.linearity.opentc4.VecTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.AbstractMultipartComponentBlock;
import thaumcraft.common.multiparts.formedmatch.IFormedMultipartMatcher;
import thaumcraft.common.multiparts.MultipartMatchInfo;

import static thaumcraft.common.multiparts.formedmatch.FormedMultipartMatcherImpls.INFERNAL_FURNACE_FORMED;

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

    public static final BlockPos MULTIPART_CHECKER_POS = new BlockPos(2,1,1);
    @Override
    public @NotNull BlockPos findTransformBasePosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return MULTIPART_CHECKER_POS;
    }

    @Override
    public @NotNull IFormedMultipartMatcher getMultipartMatcher(Level level, BlockState state, BlockPos pos) {
        return INFERNAL_FURNACE_FORMED;
    }
    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_0);
    }

    public static final MultipartMatchInfo MATCH_INFO_Y_0 = MultipartMatchInfo.of(
            VecTransformations.Rotation3D.NONE,
            VecTransformations.Mirror3D.NONE
    );
    public static final MultipartMatchInfo MATCH_INFO_Y_90 = MultipartMatchInfo.of(
            VecTransformations.Rotation3D.Y_90,
            VecTransformations.Mirror3D.NONE
    );
    public static final MultipartMatchInfo MATCH_INFO_Y_180 = MultipartMatchInfo.of(
            VecTransformations.Rotation3D.Y_180,
            VecTransformations.Mirror3D.NONE
    );
    public static final MultipartMatchInfo MATCH_INFO_Y_270 = MultipartMatchInfo.of(
            VecTransformations.Rotation3D.Y_270,
            VecTransformations.Mirror3D.NONE
    );

    @Override
    public @NotNull MultipartMatchInfo getMatchInfo(Level level, BlockState state, BlockPos pos) {
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
    @Nullable
    public VecTransformations.Rotation3D getRotation(BlockState state) {
        if (!state.hasProperty(ROTATION_Y_AXIS)){
            return null;
        }
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

    public static BlockState setRotation(BlockState state,VecTransformations.Rotation3D rotation) {
        if (rotation == VecTransformations.Rotation3D.NONE) {
            return state.setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_0);
        }
        if (rotation == VecTransformations.Rotation3D.Y_90) {
            return state.setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_90);
        }
        if (rotation == VecTransformations.Rotation3D.Y_180) {
            return state.setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_180);
        }
        if (rotation == VecTransformations.Rotation3D.Y_270) {
            return state.setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_270);
        }
        throw new IllegalArgumentException("Invalid rotation state: " + rotation);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ROTATION_Y_AXIS);
    }
}
