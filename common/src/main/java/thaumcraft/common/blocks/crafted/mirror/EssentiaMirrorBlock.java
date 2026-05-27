package thaumcraft.common.blocks.crafted.mirror;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.mirror.EssentiaMirrorBlockEntity;

public class EssentiaMirrorBlock extends AbstractMirrorBlock {
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaMirrorBlockEntity(blockPos,blockState);
    }
}
