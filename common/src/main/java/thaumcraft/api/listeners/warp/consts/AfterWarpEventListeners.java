package thaumcraft.api.listeners.warp.consts;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.warp.PickWarpEventContext;
import thaumcraft.api.listeners.warp.listeners.WarpEvent;
import thaumcraft.api.listeners.warp.listeners.WarpEventListenerAfter;
import thaumcraft.api.research.impl.eldritch.EldritchMajorResearch;
import thaumcraft.api.research.impl.eldritch.EldritchMinorResearch;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketClueCompleteS2C;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketResearchCompleteS2C;


import static thaumcraft.api.listeners.warp.consts.WarpEvents.grantResearchAspect;
import static thaumcraft.api.research.ThaumcraftResearches.*;

public class AfterWarpEventListeners {
    public static final WarpEventListenerAfter CHECK_RESEARCH = new WarpEventListenerAfter(0) {
        @Override
        public void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull Player player) {
            if (warpContext.actualWarp > 10
                    && !BATH_SALTS.playerHasClue(player)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.sendSystemMessage(Component.translatable("warp.text.8")
                            .withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC),false);
                    BATH_SALTS.giveClueToPlayer(player);
                    new PacketClueCompleteS2C(BATH_SALTS.getNeededClue()).sendTo(serverPlayer);
                }
            }

            if (warpContext.actualWarp > 25
                    && !ELDRITCH_MINOR.isPlayerCompletedResearch(player)) {

                if (player instanceof ServerPlayer serverPlayer) {
                    grantResearchAspect(serverPlayer, 10);
                    ELDRITCH_MINOR.completeResearchFor(serverPlayer);
                    new PacketResearchCompleteS2C(EldritchMinorResearch.ID).sendTo(serverPlayer);
                }
            }

            if (warpContext.actualWarp > 50
                    && !ELDRITCH_MAJOR.isPlayerCompletedResearch(player)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    grantResearchAspect(serverPlayer, 20);
                    ELDRITCH_MAJOR.completeResearchFor(serverPlayer);
                    new PacketResearchCompleteS2C(EldritchMajorResearch.ID).sendTo(serverPlayer);
                }
            }
        }
    };
    public static final WarpEventListenerAfter DECREASE_A_TEMP_WARP = new WarpEventListenerAfter(1) {
        @Override
        public void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull Player player) {
            WarpInfo.getFromPlayer(player).addTempWarp(-1);
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
