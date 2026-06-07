package com.linearity.opentc4.mixin.client;

import com.linearity.opentc4.annotations.StoleFrom;
import com.linearity.opentc4.playerdata.AdditionalPlayerDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Unique
    private LocalPlayer opentc4$tmpPlayer = null;
    @Shadow
    @Final
    private Minecraft minecraft;

    @StoleFrom("architectury")
    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void opentc4$handleRespawnPre(ClientboundRespawnPacket packet, CallbackInfo ci) {
        this.opentc4$tmpPlayer = minecraft.player;
    }

    //changed target earlier
    @StoleFrom("architectury")
    @Inject(method = "handleRespawn",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/client/Minecraft;player:Lnet/minecraft/client/player/LocalPlayer;"
            )
    )
    private void opentc4$handleRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
        AdditionalPlayerDataManager.syncDataFromBeingClonedToCloningClient(opentc4$tmpPlayer, minecraft.player);
        this.opentc4$tmpPlayer = null;
    }
}
