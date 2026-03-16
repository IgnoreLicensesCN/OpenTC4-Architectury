package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IThaumatoriumAttachment {
    int getAdditionalRecipeSize(
            Level atlevel,
            BlockPos selfPos,
            BlockState selfState,
            BlockPos beingAttachedPos
    );
}
