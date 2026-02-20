package thaumcraft.api.listeners.warp.consts;


import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.listeners.warp.PickWarpEventContext;
import thaumcraft.api.listeners.warp.listeners.WarpEvent;
import thaumcraft.api.listeners.warp.listeners.WarpEventListenerAfter;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.PacketResearchCompleteS2C;


import static thaumcraft.common.lib.WarpEvents.grantResearch;

public class AfterWarpEventListeners {
    public static final WarpEventListenerAfter CHECK_RESEARCH = new WarpEventListenerAfter(0) {
        @Override
        public void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull Player player) {
            if (warpContext.actualWarp > 10
                    && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), "BATHSALTS")
                    && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), "@BATHSALTS")) {
                player.displayClientMessage(Component.literal("§5§o" + Component.translatable("warp.text.8")),false);
                if (player instanceof ServerPlayer serverPlayer) {
                    new PacketResearchCompleteS2C("@BATHSALTS").sendTo(serverPlayer);
                }
                Thaumcraft.researchManager.completeResearch(player, "@BATHSALTS");
            }

            if (warpContext.actualWarp > 25
                    && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), "ELDRITCHMINOR")) {
                grantResearch(player, 10);
                if (player instanceof ServerPlayer serverPlayer) {
                    new PacketResearchCompleteS2C("ELDRITCHMINOR").sendTo(serverPlayer);
                }
                Thaumcraft.researchManager.completeResearch(player, "ELDRITCHMINOR");
            }

            if (warpContext.actualWarp > 50
                    && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), "ELDRITCHMAJOR")) {
                grantResearch(player, 20);
                if (player instanceof ServerPlayer serverPlayer) {
                    new PacketResearchCompleteS2C("ELDRITCHMAJOR").sendTo(serverPlayer);
                }
                Thaumcraft.researchManager.completeResearch(player, "ELDRITCHMAJOR");
            }
        }
    };
    public static final WarpEventListenerAfter DECREASE_A_TEMP_WARP = new WarpEventListenerAfter(1) {
        @Override
        public void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull Player player) {
            Thaumcraft.playerKnowledge.addWarpTemp(player.getGameProfile().getName(), -1);
        }
    };
    public static final WarpEventListenerAfter DONT_SEND_MISC_FOR_EMPTY = new WarpEventListenerAfter(2) {
        @Override
        public void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull Player player) {
            if (e == WarpEvent.EMPTY){
                e.sendMiscPacket = false;
            }
        }
    };
}
