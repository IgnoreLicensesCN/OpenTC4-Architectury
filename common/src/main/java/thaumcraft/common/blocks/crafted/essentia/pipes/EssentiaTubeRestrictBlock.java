package thaumcraft.common.blocks.crafted.essentia.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.essentiabe.pipes.EssentiaTubeRestrictBlockEntity;

public class EssentiaTubeRestrictBlock extends EssentiaTubeBlock {
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaTubeRestrictBlockEntity(blockPos, blockState);
    }
}
