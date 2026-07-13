package com.linearity.opentc4.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.items.abstracts.ISwordLikeItem;

@Mixin(BambooSaplingBlock.class)
public class BambooSaplingBlockMixin {
    @Inject(
            method = "getDestroyProgress",
            at = @At("HEAD"),
            cancellable = true
    )
    private void opentc4$getDestroyProgress(
            BlockState blockState,
            Player player,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CallbackInfoReturnable<Float> cir
    ){
        if (player.getMainHandItem().getItem() instanceof ISwordLikeItem){
            cir.setReturnValue(1.0F);
        }
    }
}
