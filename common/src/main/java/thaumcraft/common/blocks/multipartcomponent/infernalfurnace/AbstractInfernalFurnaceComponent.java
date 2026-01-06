package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.abstracts.AbstractMultipartComponentBlock;
import thaumcraft.common.multiparts.matchers.AbstractMultipartMatcher;

import static thaumcraft.common.multiparts.matchers.MultipartMatcherImpls.INFERNAL_FURNACE_FORMED;

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
    public AbstractMultipartMatcher getMultipartMatcher(Level level, BlockState state, BlockPos pos) {
        return INFERNAL_FURNACE_FORMED;
    }
    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_0);
    }
}
