package thaumcraft.api.blockapi;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IEntityInLavaBlock {
    boolean consideredAsLava(BlockState state, Level level, BlockPos pos,@Nullable Entity entityCollideWith);
}
