package com.linearity.opentc4.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.listeners.noteblock.NoteBlockEventManager;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {

    @Inject(
            method = "playNote",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;gameEvent(" +
                            "Lnet/minecraft/world/entity/Entity;" +
                            "Lnet/minecraft/world/level/gameevent/GameEvent;" +
                            "Lnet/minecraft/core/BlockPos;)V"
            )
    )
    private void opentc4$onNotePlayed(Entity entity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        NoteBlockEventManager.onPlayedNote(entity, state, level, pos);
    }
}
