package thaumcraft.api.listeners.warp.consts;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
import static thaumcraft.api.listeners.warp.consts.WarpEventsEnum.COMPONENT_NEED_SOAP;
import static thaumcraft.api.research.ThaumcraftResearches.*;

public class AfterWarpEventListeners {
    public static final WarpEventListenerAfter CHECK_RESEARCH = new WarpEventListenerAfter(0) {
        @Override
        public void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull LivingEntity living) {
            checkResearch(warpContext, e, living);
        }
    };

    public static void checkResearch(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull LivingEntity living) {
        if (warpContext.actualWarp > 10
                && !BATH_SALTS.livingHasClue(living)) {
            BATH_SALTS.giveClueToLiving(living);
            if (living instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(COMPONENT_NEED_SOAP, false);
                new PacketClueCompleteS2C(BATH_SALTS.getNeededClue()).sendTo(serverPlayer);
            }
        }

        if (warpContext.actualWarp > 25
                && !ELDRITCH_MINOR.isLivingEntityCompletedResearch(living)) {

            grantResearchAspect(living, 10);
            if (living instanceof ServerPlayer serverPlayer) {
                ELDRITCH_MINOR.completeResearchFor(serverPlayer);
                new PacketResearchCompleteS2C(EldritchMinorResearch.ID).sendTo(serverPlayer);
            }
        }

        if (warpContext.actualWarp > 50
                && !ELDRITCH_MAJOR.isLivingEntityCompletedResearch(living)) {
            grantResearchAspect(living, 20);
            if (living instanceof ServerPlayer serverPlayer) {
                ELDRITCH_MAJOR.completeResearchFor(serverPlayer);
                new PacketResearchCompleteS2C(EldritchMajorResearch.ID).sendTo(serverPlayer);
            }
        }
    }

    public static final WarpEventListenerAfter DECREASE_A_TEMP_WARP = new WarpEventListenerAfter(1) {
        @Override
        public void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull LivingEntity living) {
            decreaseATempWarp(warpContext, e, living);
        }
    };

    public static void decreaseATempWarp(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull LivingEntity living) {
        var warpInfo = WarpInfo.getFromLivingEntity(living);
        if (warpInfo != null) {
            warpInfo.addTempWarp(-1);
        }
    }

    public static final WarpEventListenerAfter DONT_SEND_MIST_FOR_EMPTY = new WarpEventListenerAfter(2) {
        @Override
        public void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull LivingEntity living) {
            dontSendMistForEmpty(warpContext, e, living);
        }
    };

    private static void dontSendMistForEmpty(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull LivingEntity living) {
        if (e == WarpEvent.EMPTY) {
            e.sendMistPacket = false;
        }
    }
}
