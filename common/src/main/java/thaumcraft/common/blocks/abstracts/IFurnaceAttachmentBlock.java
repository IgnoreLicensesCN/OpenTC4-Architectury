package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface IFurnaceAttachmentBlock {
    void onFurnaceBurning(
            Level level,
            AbstractFurnaceBlockEntity furnaceBlockEntity,
            BlockState furnaceState,
            BlockPos furnacePos,
            BlockState attachmentState,
            BlockPos attachmentPos,
            CallbackInfo ci
    );
}
