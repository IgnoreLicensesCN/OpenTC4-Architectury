package thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace;

import com.linearity.opentc4.VecTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.abstracts.AbstractMultipartComponentBlock;
import thaumcraft.common.multiparts.MultipartMatchInfo;
import thaumcraft.common.multiparts.formedmatch.IFormedMultipartMatcher;

import static thaumcraft.common.multiparts.formedmatch.FormedMultipartMatcherImpls.ADVANCED_ALCHEMICAL_FURNACE_FORMED;

public abstract class AbstractAdvancedAlchemicalFurnaceComponent extends AbstractMultipartComponentBlock {
    public static final BlockPos MULTIPART_CHECKER_POS = new BlockPos(1,0,1);
    public AbstractAdvancedAlchemicalFurnaceComponent(Properties properties) {
        super(properties);
    }
    public AbstractAdvancedAlchemicalFurnaceComponent() {
        super(Properties
                .copy(Blocks.IRON_BLOCK)
                .strength(3,17)
        );
    }
    @Override
    public @NotNull BlockPos findTransformBasePosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        return MULTIPART_CHECKER_POS;
    }

    @Override
    public @NotNull IFormedMultipartMatcher getMultipartMatcher(Level level, BlockState state, BlockPos pos) {
        return ADVANCED_ALCHEMICAL_FURNACE_FORMED;
    }

    @Override
    public VecTransformations.Rotation3D getRotation(BlockState state) {
        return VecTransformations.Rotation3D.NONE;
    }

    @Override
    public @NotNull MultipartMatchInfo getMatchInfo(Level level, BlockState state, BlockPos pos) {
        return null;
    }
}
