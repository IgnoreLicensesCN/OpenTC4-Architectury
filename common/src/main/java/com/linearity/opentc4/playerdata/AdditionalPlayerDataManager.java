package com.linearity.opentc4.playerdata;

import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata.RunicShieldInfoTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata.WarpInfoTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

public class AdditionalPlayerDataManager {
    public static final RunicShieldInfoTagAccessor SHIELD_ACCESSOR = new RunicShieldInfoTagAccessor("player_runic_shield");
    public static final WarpInfoTagAccessor PLAYER_WARP_INFO = new WarpInfoTagAccessor("player_warp_info");
    public static void syncDataFromBeingClonedToCloning(ServerPlayer beingCloned,ServerPlayer cloningInto,boolean cloningForTeleport) {
        //Oh i will hard code things here
        var shieldInfo = EntityRunicShieldInfo.getFromPlayer(beingCloned);
        EntityRunicShieldInfo.setForPlayer(cloningInto,shieldInfo);
        shieldInfo.syncChargeSendPacket(cloningInto);
        shieldInfo.syncCapacitySendPacket(cloningInto);

        var warpInfo = WarpInfo.getFromPlayer(beingCloned);
        WarpInfo.setForPlayer(cloningInto,warpInfo);
        warpInfo.syncTo(cloningInto);
    }
    //readFromCompoundTag
    public static void readPlayerDataFromTag(ServerPlayer player, CompoundTag tag) {
        EntityRunicShieldInfo.setForPlayer(player,SHIELD_ACCESSOR.readFromCompoundTag(tag));
        WarpInfo.setForPlayer(player,PLAYER_WARP_INFO.readFromCompoundTag(tag));

    }
    //writeToCompoundTag
    public static void writePlayerDataIntoTag(ServerPlayer player, CompoundTag tag) {
        SHIELD_ACCESSOR.writeToCompoundTag(tag,EntityRunicShieldInfo.getFromPlayer(player));
        PLAYER_WARP_INFO.writeToCompoundTag(tag,WarpInfo.getFromPlayer(player));
    }

    public static void syncDataForJoinedPlayer(ServerPlayer player) {
        var shieldInfo = EntityRunicShieldInfo.getFromPlayer(player);
        shieldInfo.syncChargeSendPacket(player);
        shieldInfo.syncCapacitySendPacket(player);

        var warpInfo = WarpInfo.getFromPlayer(player);
        warpInfo.syncTo(player);

    }
}
