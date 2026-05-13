package com.linearity.opentc4.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.blockapi.IRedstoneWireConnectableBlock;

@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {

    @Inject(method = "shouldConnectTo("+
            "Lnet/minecraft/world/level/block/state/BlockState;"+
            "Lnet/minecraft/core/Direction;" +
            ")"+"Z",at = @At("HEAD"),cancellable = true)
    private static void opentc4$shouldConnectTo(
            BlockState connectToState,
            @Nullable Direction direction,
            CallbackInfoReturnable<Boolean> cir
    ){
        if (connectToState.getBlock() instanceof IRedstoneWireConnectableBlock connectable
                && connectable.canBeConnected(connectToState,direction)){
            cir.setReturnValue(true);
        }
    }
}
