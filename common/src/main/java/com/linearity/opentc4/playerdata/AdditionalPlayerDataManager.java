package com.linearity.opentc4.playerdata;

import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata.ResearchAndScannedInfoTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata.RunicShieldInfoTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata.WarpInfoTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.common.lib.network.gamedata.PacketSyncItemAspectsS2C;
import thaumcraft.common.runicshield.RunicShieldInfo;

//Oh i will hard code things here
//but you may mixin this
public class AdditionalPlayerDataManager {
    public static final RunicShieldInfoTagAccessor SHIELD_ACCESSOR = new RunicShieldInfoTagAccessor("player_runic_shield");
    public static final WarpInfoTagAccessor PLAYER_WARP_INFO = new WarpInfoTagAccessor("player_warp_info");
    public static final ResearchAndScannedInfoTagAccessor PLAYER_COMPLETED_RESEARCH_INFO = new ResearchAndScannedInfoTagAccessor("player_research_and_scanned_info");

    public static void syncDataFromBeingClonedToCloningServer(ServerPlayer beingCloned, ServerPlayer cloningInto, boolean cloningForTeleport) {

        var shieldInfo = RunicShieldInfo.getFromLiving(beingCloned);
        if (shieldInfo != null){
            RunicShieldInfo.setForLiving(cloningInto,shieldInfo);
            shieldInfo.syncChargeSendPacket(cloningInto);
            shieldInfo.syncCapacitySendPacket(cloningInto);
        }
        var warpInfo = WarpInfo.getFromLivingEntity(beingCloned);
        if (warpInfo != null) {
            WarpInfo.setForLivingEntity(cloningInto, warpInfo);
            warpInfo.syncSendPacket(cloningInto);
        }

        var researchAndClueInfo = ResearchAndScannedInfo.getFromLiving(beingCloned);
        if (researchAndClueInfo != null) {
            ResearchAndScannedInfo.setForLiving(cloningInto, researchAndClueInfo);
            researchAndClueInfo.syncAllSendPacket(cloningInto);
        }

    }
    //readFromCompoundTag
    public static void readPlayerDataFromTag(ServerPlayer player, CompoundTag tag) {
        RunicShieldInfo.setForLiving(player,SHIELD_ACCESSOR.readFromCompoundTag(tag));
        WarpInfo.setForLivingEntity(player,PLAYER_WARP_INFO.readFromCompoundTag(tag));
        ResearchAndScannedInfo.setForLiving(player,PLAYER_COMPLETED_RESEARCH_INFO.readFromCompoundTag(tag));
    }
    //writeToCompoundTag
    public static void writePlayerDataIntoTag(ServerPlayer player, CompoundTag tag) {
        var shieldInfo = RunicShieldInfo.getFromLiving(player);
        if (shieldInfo != null){
            SHIELD_ACCESSOR.writeToCompoundTag(tag, shieldInfo);
        }
        var warpInfo = WarpInfo.getFromLivingEntity(player);
        if (warpInfo != null) {
            PLAYER_WARP_INFO.writeToCompoundTag(tag, warpInfo);
        }
        var researchInfo = ResearchAndScannedInfo.getFromLiving(player);
        if (researchInfo != null) {
            PLAYER_COMPLETED_RESEARCH_INFO.writeToCompoundTag(tag, researchInfo);
        }
    }

    public static void syncDataForJoinedPlayer(ServerPlayer player) {
        new PacketSyncItemAspectsS2C().sendTo(player);

        var shieldInfo = RunicShieldInfo.getFromLiving(player);
        if (shieldInfo != null){
            shieldInfo.syncChargeSendPacket(player);
            shieldInfo.syncCapacitySendPacket(player);
        }

        var warpInfo = WarpInfo.getFromLivingEntity(player);
        if (warpInfo != null) {
            warpInfo.syncSendPacket(player);
        }

        var researchInfo = ResearchAndScannedInfo.getFromLiving(player);
        if (researchInfo != null) {
            researchInfo.syncAllSendPacket(player);
        }

    }

    public static void syncDataFromBeingClonedToCloningClient(Player beingCloned,Player cloningInto) {
        var shieldInfo = RunicShieldInfo.getFromLiving(beingCloned);
        if (shieldInfo != null){
            RunicShieldInfo.setForLiving(cloningInto,shieldInfo);
        }

        var warpInfo = WarpInfo.getFromLivingEntity(beingCloned);
        if (warpInfo != null){
            WarpInfo.setForLivingEntity(cloningInto, warpInfo);
        }

        var researchAndClueInfo = ResearchAndScannedInfo.getFromLiving(beingCloned);
        ResearchAndScannedInfo.setForLiving(cloningInto,researchAndClueInfo);
    }
}
