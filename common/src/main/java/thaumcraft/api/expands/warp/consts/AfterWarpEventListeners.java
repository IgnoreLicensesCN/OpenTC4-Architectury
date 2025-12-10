package thaumcraft.api.expands.warp.consts;

import net.minecraft.entity.player.Player;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpEvent;
import thaumcraft.api.expands.warp.listeners.WarpEventListenerAfter;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;

import javax.annotation.Nonnull;

import static thaumcraft.common.lib.WarpEvents.grantResearch;

public class AfterWarpEventListeners {
    public static final WarpEventListenerAfter CHECK_RESEARCH = new WarpEventListenerAfter(0) {
        @Override
        public void onWarpEvent(@Nonnull PickWarpEventContext warpContext, @Nonnull WarpEvent e, @Nonnull Player player) {
            if (warpContext.actualWarp > 10
                    && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "BATHSALTS")
                    && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "@BATHSALTS")) {
                player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.8")));
                if (player instanceof ServerPlayer) {
                    PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("@BATHSALTS"), (ServerPlayer) player);
                }
                Thaumcraft.proxy.getResearchManager().completeResearch(player, "@BATHSALTS");
            }

            if (warpContext.actualWarp > 25
                    && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ELDRITCHMINOR")) {
                grantResearch(player, 10);
                if (player instanceof ServerPlayer) {
                    PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ELDRITCHMINOR"), (ServerPlayer) player);
                }
                Thaumcraft.proxy.getResearchManager().completeResearch(player, "ELDRITCHMINOR");
            }

            if (warpContext.actualWarp > 50
                    && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ELDRITCHMAJOR")) {
                grantResearch(player, 20);
                if (player instanceof ServerPlayer) {
                    PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ELDRITCHMAJOR"), (ServerPlayer) player);
                }
                Thaumcraft.proxy.getResearchManager().completeResearch(player, "ELDRITCHMAJOR");
            }
        }
    };
    public static final WarpEventListenerAfter DECREASE_A_TEMP_WARP = new WarpEventListenerAfter(1) {
        @Override
        public void onWarpEvent(@Nonnull PickWarpEventContext warpContext, @Nonnull WarpEvent e, @Nonnull Player player) {
            Thaumcraft.proxy.getPlayerKnowledge().addWarpTemp(player.getCommandSenderName(), -1);
        }
    };
    public static final WarpEventListenerAfter DONT_SEND_MISC_FOR_EMPTY = new WarpEventListenerAfter(2) {
        @Override
        public void onWarpEvent(@Nonnull PickWarpEventContext warpContext, @Nonnull WarpEvent e, @Nonnull Player player) {
            if (e == WarpEvent.EMPTY){
                e.sendMiscPacket = false;
            }
        }
    };
}
