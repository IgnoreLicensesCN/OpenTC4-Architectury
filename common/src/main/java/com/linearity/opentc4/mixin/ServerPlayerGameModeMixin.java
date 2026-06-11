package com.linearity.opentc4.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import thaumcraft.common.items.abstracts.IRedirectBreakPosItem;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow @Final protected ServerPlayer player;

    @Shadow
    protected ServerLevel level;

    @ModifyVariable(
            method = "destroyBlock",
            at = @At("HEAD"),
            argsOnly = true
    )
    private BlockPos opentc4$redirectBreakPosToPosForTool(BlockPos posPrev) {
        var mainHandStack = player.getMainHandItem();
        if (mainHandStack.getItem() instanceof IRedirectBreakPosItem redirectBreakPosItem){
            this.player.connection.send(new ClientboundBlockUpdatePacket(posPrev,level.getBlockState(posPrev)));
            return redirectBreakPosItem.redirectBreakPosToPos(posPrev,player,mainHandStack);
        }
        return posPrev;
    }
}
