package com.linearity.opentc4.playerdata;

import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata.ResearchAndScannedInfoTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata.RunicShieldInfoTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata.WarpInfoTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.researches.ResearchAndScannedInfo;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;

//Oh i will hard code things here
//but you may mixin this
public class AdditionalPlayerDataManager {
    public static final RunicShieldInfoTagAccessor SHIELD_ACCESSOR = new RunicShieldInfoTagAccessor("player_runic_shield");
    public static final WarpInfoTagAccessor PLAYER_WARP_INFO = new WarpInfoTagAccessor("player_warp_info");
    public static final ResearchAndScannedInfoTagAccessor PLAYER_COMPLETED_RESEARCH_INFO = new ResearchAndScannedInfoTagAccessor("player_research_and_scanned_info");

    public static void syncDataFromBeingClonedToCloningServer(ServerPlayer beingCloned, ServerPlayer cloningInto, boolean cloningForTeleport) {

        var shieldInfo = EntityRunicShieldInfo.getFromPlayer(beingCloned);
        EntityRunicShieldInfo.setForPlayer(cloningInto,shieldInfo);
        shieldInfo.syncChargeSendPacket(cloningInto);
        shieldInfo.syncCapacitySendPacket(cloningInto);

        var warpInfo = WarpInfo.getFromPlayer(beingCloned);
        WarpInfo.setForPlayer(cloningInto,warpInfo);
        warpInfo.syncSendPacket(cloningInto);

        var researchAndClueInfo = ResearchAndScannedInfo.getFromPlayer(beingCloned);
        ResearchAndScannedInfo.setForPlayer(cloningInto,researchAndClueInfo);
        researchAndClueInfo.syncAllSendPacket(cloningInto);

    }
    //readFromCompoundTag
    public static void readPlayerDataFromTag(ServerPlayer player, CompoundTag tag) {
        EntityRunicShieldInfo.setForPlayer(player,SHIELD_ACCESSOR.readFromCompoundTag(tag));
        WarpInfo.setForPlayer(player,PLAYER_WARP_INFO.readFromCompoundTag(tag));
        ResearchAndScannedInfo.setForPlayer(player,PLAYER_COMPLETED_RESEARCH_INFO.readFromCompoundTag(tag));
    }
    //writeToCompoundTag
    public static void writePlayerDataIntoTag(ServerPlayer player, CompoundTag tag) {
        SHIELD_ACCESSOR.writeToCompoundTag(tag,EntityRunicShieldInfo.getFromPlayer(player));
        PLAYER_WARP_INFO.writeToCompoundTag(tag,WarpInfo.getFromPlayer(player));
        PLAYER_COMPLETED_RESEARCH_INFO.writeToCompoundTag(tag, ResearchAndScannedInfo.getFromPlayer(player));
    }

    public static void syncDataForJoinedPlayer(ServerPlayer player) {
        var shieldInfo = EntityRunicShieldInfo.getFromPlayer(player);
        shieldInfo.syncChargeSendPacket(player);
        shieldInfo.syncCapacitySendPacket(player);

        var warpInfo = WarpInfo.getFromPlayer(player);
        warpInfo.syncSendPacket(player);

        var researchInfo = ResearchAndScannedInfo.getFromPlayer(player);
        researchInfo.syncAllSendPacket(player);
    }

    public static void syncDataFromBeingClonedToCloningClient(Player beingCloned,Player cloningInto) {
        var shieldInfo = EntityRunicShieldInfo.getFromPlayer(beingCloned);
        EntityRunicShieldInfo.setForPlayer(cloningInto,shieldInfo);

        var warpInfo = WarpInfo.getFromPlayer(beingCloned);
        WarpInfo.setForPlayer(cloningInto,warpInfo);

        var researchAndClueInfo = ResearchAndScannedInfo.getFromPlayer(beingCloned);
        ResearchAndScannedInfo.setForPlayer(cloningInto,researchAndClueInfo);
    }
}
