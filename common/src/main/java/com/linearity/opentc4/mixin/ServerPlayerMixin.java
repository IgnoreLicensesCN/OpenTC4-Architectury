package com.linearity.opentc4.mixin;

import com.linearity.opentc4.playerdata.AdditionalPlayerDataManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.lib.events.RunicShieldHandler;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void opentc4$beforeServerPlayerTick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object)this;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void opentc4$afterServerPlayerTick(CallbackInfo ci) {
        opentc4$runicShieldTickForPlayer();
    }

    @Unique
    private void opentc4$runicShieldTickForPlayer(){
        ServerPlayer player = (ServerPlayer)(Object)this;
        RunicShieldHandler.updateRunicShieldForPlayer(player);
    }

    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void opentc4$restoreFrom(ServerPlayer serverPlayer, boolean cloningForTeleport, CallbackInfo ci) {
        var toPlayer = (ServerPlayer) (Object) this;
        AdditionalPlayerDataManager.syncDataFromBeingClonedToCloning(
                serverPlayer,toPlayer,cloningForTeleport
        );
    }


    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void opentc4$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        AdditionalPlayerDataManager.writePlayerDataIntoTag((ServerPlayer)(Object)this,tag);
    }
    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void opentc4$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        AdditionalPlayerDataManager.readPlayerDataFromTag((ServerPlayer)(Object)this,tag);
    }
}

