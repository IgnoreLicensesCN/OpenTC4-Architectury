package thaumcraft.common.lib.network;


import dev.architectury.networking.simple.SimpleNetworkManager;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.misc.*;
import thaumcraft.common.lib.network.playerdata.*;

public class PacketHandler {
    public static final SimpleNetworkManager INSTANCE = SimpleNetworkManager.create(Thaumcraft.MOD_ID + "main_net_manager");


    public static void init() {
        PacketWarpMessageS2C.messageType = INSTANCE.registerS2C(PacketWarpMessageS2C.ID, PacketWarpMessageS2C::decode);
        PacketSyncWipeS2C.messageType = INSTANCE.registerS2C(PacketSyncWipeS2C.ID, PacketSyncWipeS2C::decode);
        PacketSyncWarpS2C.messageType = INSTANCE.registerS2C(PacketSyncWarpS2C.ID, PacketSyncWarpS2C::decode);
        PacketSyncScannedPhenomenaS2C.messageType = INSTANCE.registerS2C(PacketSyncScannedPhenomenaS2C.ID, PacketSyncScannedPhenomenaS2C::decode);
        PacketSyncScannedItemsS2C.messageType = INSTANCE.registerS2C(PacketSyncScannedItemsS2C.ID, PacketSyncScannedItemsS2C::decode);
        PacketSyncScannedEntitiesS2C.messageType = INSTANCE.registerS2C(PacketSyncScannedEntitiesS2C.ID, PacketSyncScannedEntitiesS2C::decode);
        PacketSyncResearchS2C.messageType = INSTANCE.registerS2C(PacketSyncResearchS2C.ID, PacketSyncResearchS2C::decode);
        PacketSyncAspectsS2C.messageType = INSTANCE.registerS2C(PacketSyncAspectsS2C.ID, PacketSyncAspectsS2C::decode);
        PacketScannedToServerC2S.messageType = INSTANCE.registerC2S(PacketScannedToServerC2S.ID, PacketScannedToServerC2S::decode);
        PacketRunicChargeS2C.messageType = INSTANCE.registerS2C(PacketRunicChargeS2C.ID, PacketRunicChargeS2C::decode);
        PacketResearchCompleteS2C.messageType = INSTANCE.registerS2C(PacketResearchCompleteS2C.ID, PacketResearchCompleteS2C::decode);
        PacketPlayerCompleteToServerC2S.messageType = INSTANCE.registerC2S(PacketPlayerCompleteToServerC2S.ID, PacketPlayerCompleteToServerC2S::decode);
        PacketAspectPoolS2C.messageType = INSTANCE.registerS2C(PacketAspectPoolS2C.ID, PacketAspectPoolS2C::decode);
        PacketAspectPlaceToServerC2S.messageType = INSTANCE.registerC2S(PacketAspectPlaceToServerC2S.ID, PacketAspectPlaceToServerC2S::decode);
        PacketAspectDiscoveryS2C.messageType = INSTANCE.registerS2C(PacketAspectDiscoveryS2C.ID, PacketAspectDiscoveryS2C::decode);
        PacketAspectCombinationC2S.messageType = INSTANCE.registerC2S(PacketAspectCombinationC2S.ID,PacketAspectCombinationC2S::decode);
        PacketBoreDigS2C.messageType = INSTANCE.registerS2C(PacketBoreDigS2C.ID, PacketBoreDigS2C::decode);

        PacketItemKeyC2S.messageType = INSTANCE.registerC2S(PacketItemKeyC2S.ID,PacketItemKeyC2S::decode);
        PacketMiscEventS2C.messageType = INSTANCE.registerS2C(PacketMiscEventS2C.ID, PacketMiscEventS2C::decode);
        PacketNoteC2S.messageType = INSTANCE.registerC2S(PacketNoteC2S.ID, PacketNoteC2S::decode);
        PacketNoteS2C.messageType = INSTANCE.registerS2C(PacketNoteS2C.ID, PacketNoteS2C::decode);

        int idx = 0;
//      INSTANCE.registerMessage(PacketBiomeChange.class, PacketBiomeChange.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketConfig.class, PacketConfig.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketMiscEvent.class, PacketMiscEvent.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketSyncWipe.class, PacketSyncWipe.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketSyncAspects.class, PacketSyncAspects.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketSyncResearch.class, PacketSyncResearch.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketSyncScannedItems.class, PacketSyncScannedItems.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketSyncScannedEntities.class, PacketSyncScannedEntities.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketSyncScannedPhenomena.class, PacketSyncScannedPhenomena.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketResearchComplete.class, PacketResearchComplete.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketAspectPool.class, PacketAspectPool.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketAspectDiscovery.class, PacketAspectDiscovery.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketScannedToServer.class, PacketScannedToServer.class, idx++, Side.SERVER);
//      INSTANCE.registerMessage(PacketAspectCombinationToServer.class, PacketAspectCombinationToServer.class, idx++, Side.SERVER);
//      INSTANCE.registerMessage(PacketPlayerCompleteToServer.class, PacketPlayerCompleteToServer.class, idx++, Side.SERVER);
//      INSTANCE.registerMessage(PacketAspectPlaceToServer.class, PacketAspectPlaceToServer.class, idx++, Side.SERVER);
//      INSTANCE.registerMessage(PacketRunicCharge.class, PacketRunicCharge.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketBoreDig.class, PacketBoreDig.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketNote.class, PacketNote.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketSyncWarp.class, PacketSyncWarp.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketWarpMessageS2C.class, PacketWarpMessageS2C.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketNote.class, PacketNote.class, idx++, Side.SERVER);
//      INSTANCE.registerMessage(PacketItemKeyToServer.class, PacketItemKeyToServer.class, idx++, Side.SERVER);
//      INSTANCE.registerMessage(PacketFocusChangeToServer.class, PacketFocusChangeToServer.class, idx++, Side.SERVER);
//      INSTANCE.registerMessage(PacketFlyToServer.class, PacketFlyToServer.class, idx++, Side.SERVER);
//      INSTANCE.registerMessage(PacketFXBlockBubble.class, PacketFXBlockBubble.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXBlockDig.class, PacketFXBlockDig.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXBlockSparkle.class, PacketFXBlockSparkle.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXBlockArc.class, PacketFXBlockArc.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXBlockZap.class, PacketFXBlockZap.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXEssentiaSource.class, PacketFXEssentiaSource.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXInfusionSource.class, PacketFXInfusionSource.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXShield.class, PacketFXShield.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXSonic.class, PacketFXSonic.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXWispZap.class, PacketFXWispZap.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXZap.class, PacketFXZap.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXVisDrain.class, PacketFXVisDrain.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXBeamPulse.class, PacketFXBeamPulse.class, idx++, Side.CLIENT);
//      INSTANCE.registerMessage(PacketFXBeamPulseGolemBoss.class, PacketFXBeamPulseGolemBoss.class, idx++, Side.CLIENT);
    }

}
