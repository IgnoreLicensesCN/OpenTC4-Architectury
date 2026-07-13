package com.linearity.opentc4.mixin;

import com.linearity.opentc4.annotations.StoleFrom;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.linearity.opentc4.playerdata.AdditionalPlayerDataManager.syncDataForJoinedPlayer;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @StoleFrom("architectury")
    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    private void opentc4$placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        syncDataForJoinedPlayer(serverPlayer);
    }
}
