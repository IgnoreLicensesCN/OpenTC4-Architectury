package thaumcraft.api.listeners.warp;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.listeners.warp.consts.WarpEventsEnum;
import thaumcraft.api.listeners.warp.listeners.*;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.misc.PacketMiscEventS2C;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketChangeWarpS2C;


import static thaumcraft.api.listeners.warp.consts.AfterPickEventListeners.SPAWN_GUARD_IF_NO_EVENT;
import static thaumcraft.api.listeners.warp.consts.AfterWarpEventListeners.*;
import static thaumcraft.api.listeners.warp.consts.BeforePickEventListeners.*;
import static thaumcraft.api.listeners.warp.consts.WarpConditions.NO_WARP_WARD;
import static thaumcraft.api.listeners.warp.consts.WarpConditions.WARP_AND_COUNTER;
import static thaumcraft.api.listeners.warp.consts.WarpEvents.*;


public class WarpEventManager {
    /**
     * I may update those register methods,so please don't access these lists directly.
     */
    public static final ListenerManager<WarpEvent> warpEventManager = new ListenerManager<>();
    public static final ListenerManager<WarpEventListenerBefore> warpEventListenerBeforeManager = new ListenerManager<>();
    public static final ListenerManager<WarpEventListenerAfter> warpEventListenerAfterManager = new ListenerManager<>();
    public static final ListenerManager<PickWarpEventListenerBefore> pickWarpEventListenerBeforeManager = new ListenerManager<>();
    public static final ListenerManager<PickWarpEventListenerAfter> pickWarpEventListenerAfterManager = new ListenerManager<>();
    public static final ListenerManager<WarpConditionChecker> warpConditionCheckerManager = new ListenerManager<>();
    public static final ListenerManager<GettingWarpDelayListener> gettingWarpDelayListenerManager = new ListenerManager<>();

    public static void init() {
        for (var eventEnum : WarpEventsEnum.values()) {
            warpEventManager.registerListener(eventEnum.event);
        }
//        pickWarpEventListenerBeforeManager.registerListener(THAUMIC_FORTRESS_MASK_DISCOUNT);
        pickWarpEventListenerBeforeManager.registerListener(CALCULATE_WARP_AND_COUNTER);
        pickWarpEventListenerAfterManager.registerListener(SPAWN_GUARD_IF_NO_EVENT);

        warpEventListenerAfterManager.registerListener(CHECK_RESEARCH);
        warpEventListenerAfterManager.registerListener(DECREASE_A_TEMP_WARP);
        warpEventListenerAfterManager.registerListener(DONT_SEND_MISC_FOR_EMPTY);

        warpConditionCheckerManager.registerListener(WARP_AND_COUNTER);
        warpConditionCheckerManager.registerListener(NO_WARP_WARD);

    }

    public static WarpEvent pickWarpEventWithListener(PickWarpEventContext warpContext, LivingEntity living) {
        for (PickWarpEventListenerBefore listener : pickWarpEventListenerBeforeManager.getListeners()) {
            listener.beforePickEvent(warpContext, living);
        }
        if (warpContext.warp <= 0 || warpContext.actualWarp <= 0) {
            return WarpEvent.EMPTY;
        }
        warpContext.randWithWarp = living.getRandom().nextInt(warpContext.warp);
        WarpEvent picked = WarpEvent.EMPTY;

        for (WarpEvent pickEvent : warpEventManager.getListeners()) {
            if (warpContext.actualWarp >= pickEvent.warpRequired) {
                if (warpContext.randWithWarp >= pickEvent.weight) {
                    warpContext.randWithWarp -= pickEvent.weight;
                } else {
                    picked = pickEvent;
                    break;
                }
            } else {
                break;
            }
        }

        for (PickWarpEventListenerAfter listener : pickWarpEventListenerAfterManager.getListeners()) {
            picked = listener.afterPickEvent(warpContext, picked, living);
        }
        return picked;
    }

    public static void triggerRandomWarpEvent(PickWarpEventContext warpContext, LivingEntity living) {
        triggerWarpEvent(warpContext, pickWarpEventWithListener(warpContext, living), living);
    }

    public static void triggerWarpEvent(PickWarpEventContext warpContext, WarpEvent e, LivingEntity living) {
        e.enable();
        for (WarpEventListenerBefore listener : warpEventListenerBeforeManager.getListeners()) {
            listener.onWarpEvent(warpContext, e, living);
        }
        if (e.enabledFlag) {
            e.onEventTriggered(warpContext, living);
            if (e.retryAnotherFlag) {
                triggerRandomWarpEvent(warpContext, living);
                return;
            }
            for (WarpEventListenerAfter listener : warpEventListenerAfterManager.getListeners()) {
                listener.onWarpEvent(warpContext, e, living);
            }
            if (e.sendMiscPacket) {
                if (living instanceof ServerPlayer serverPlayer) {
                    new PacketMiscEventS2C((short) 0).sendTo(serverPlayer);
                }
            }
            if (living instanceof ServerPlayer serverPlayer) {
                var warpInfo =  WarpInfo.getFromLivingEntity(living);
                if (warpInfo != null) {
                    warpInfo.syncSendPacket(serverPlayer);
                }
            }
        }
    }

    public static void tryTriggerRandomWarpEvent(@NotNull LivingEntity living) {
        WarpInfo warpInfo = WarpInfo.getFromLivingEntity(living);
        if (warpInfo != null) {
            PickWarpEventContext warpContext = new PickWarpEventContext(
                    warpInfo.getTotalWarp()
                            + getWarpFromGear(living),
                    living,
                    warpInfo.getPermWarp()
                            + warpInfo.getStickyWarp(),
                    warpInfo.getWarpEventCounter()
            );
            for (WarpConditionChecker condition : warpConditionCheckerManager.getListeners()) {
                if (!condition.check(warpContext, living)) {
                    return;
                }
            }
            triggerRandomWarpEvent(warpContext, living);
        }
    }

    public static final int defaultCheckWarpEventDelay = 2000;

    //unit:tick
    public static int getWarpEventDelayForPlayer(LivingEntity living) {
        int result = defaultCheckWarpEventDelay;
        for (GettingWarpDelayListener listener : gettingWarpDelayListenerManager.getListeners()) {
            result = listener.onGettingWarpEventDelayForLiving(living);
        }
        return result;
    }

    public static int getFinalWarp(ItemStack stack,@Nullable Entity entityEquipped) {
        if (stack != null && stack.getItem() instanceof IWarpingGear armor) {
            return armor.getWarp(stack, entityEquipped);
        }
        //need some map to lookup?
        return 0;
    }


    public static void addResearchWarpTo(LivingEntity living, int warp) {
        var info = WarpInfo.getFromLivingEntity(living);
        if (info == null) {
            return;
        }
        if (warp > 0 && !Config.wuss && !living.level().isClientSide) {
            if (warp > 1) {
                int w2 = warp / 2;
                if (warp - w2 > 0) {
                    info.addPermWarp(warp - w2);
                    if (living instanceof ServerPlayer serverPlayer) {
                        new PacketChangeWarpS2C((byte)0, warp - w2).sendTo(serverPlayer);
                    }
                }

                info.addStickyWarp(warp - w2);
                if (living instanceof ServerPlayer serverPlayer) {
                    new PacketChangeWarpS2C((byte) 1, w2).sendTo(serverPlayer);
                }
            } else {
                info.addPermWarp(warp);
                if (living instanceof ServerPlayer serverPlayer) {
                    new PacketChangeWarpS2C((byte)0, warp).sendTo(serverPlayer);
                }
            }
        }
    }
}
