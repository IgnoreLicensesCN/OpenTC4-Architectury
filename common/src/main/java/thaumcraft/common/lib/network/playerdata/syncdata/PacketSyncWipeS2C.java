package thaumcraft.common.lib.network.playerdata.syncdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;
import thaumcraft.api.research.ResearchAndScannedInfo;

public class PacketSyncWipeS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_wipe";

    public PacketSyncWipeS2C() {
    }

    public static PacketSyncWipeS2C decode(FriendlyByteBuf buf) {
        return new PacketSyncWipeS2C();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {

            var researchAndScannedInfo = ResearchAndScannedInfo.getFromLiving(player);
            if (researchAndScannedInfo != null) {
                researchAndScannedInfo.completedResearches.clear();
                researchAndScannedInfo.completedClues.clear();
                researchAndScannedInfo.owningResearchAspect.clear();
                researchAndScannedInfo.scannedThings.clear();
            }
            var warpInfo = WarpInfo.getFromLivingEntity(player);
            if (warpInfo != null) {
                warpInfo.setStickyWarp(0);
                warpInfo.setTempWarp(0);
                warpInfo.setPermWarp(0);
                warpInfo.setWarpEventCounter(0);
            }
        }
    }

    public static MessageType messageType;

    @Override
    public MessageType getType() {
        return messageType;
    }

}
