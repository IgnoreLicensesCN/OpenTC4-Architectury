package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IThaumatoriumAttachmentBlock {
    int getAdditionalRecipeSize(
            Level atlevel,
            BlockPos attachmentSelfPos,
            BlockState attachmentSelfState,
            BlockPos beingAttachedPos
    );
}
