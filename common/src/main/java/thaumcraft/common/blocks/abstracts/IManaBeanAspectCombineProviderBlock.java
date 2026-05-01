package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

public interface IManaBeanAspectCombineProviderBlock {
    @NotNull Aspect getAspectCanProvide(Level level, BlockPos selfPos, BlockState selfState);
}
