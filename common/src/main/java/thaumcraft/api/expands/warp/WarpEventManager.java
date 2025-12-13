package thaumcraft.api.expands.warp;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;
import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.expands.warp.listeners.*;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.misc.PacketMiscEventS2C;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarpS2C;
import thaumcraft.common.lib.research.PlayerKnowledge;



import static thaumcraft.api.expands.warp.consts.AfterPickEventListeners.SPAWN_GUARD_IF_NO_EVENT;
import static thaumcraft.api.expands.warp.consts.AfterWarpEventListeners.*;
import static thaumcraft.api.expands.warp.consts.BeforePickEventListeners.CALCULATE_WARP_AND_COUNTER;
import static thaumcraft.api.expands.warp.consts.BeforePickEventListeners.THAUMIC_FORTRESS_MASK_DISCOUNT;
import static thaumcraft.api.expands.warp.consts.WarpConditions.NO_WARP_WARD;
import static thaumcraft.api.expands.warp.consts.WarpConditions.WARP_AND_COUNTER;
import static thaumcraft.api.expands.warp.consts.WarpEvents.*;
import static thaumcraft.common.lib.WarpEvents.*;


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
    
    public static void init(){
        warpEventManager.registerListener(GRANT_RESEARCH_LOW);
        warpEventManager.registerListener(NOISE_AND_FOLLOWING);
        warpEventManager.registerListener(VIS_EXHAUST);
        warpEventManager.registerListener(THAUMARHIA);
        warpEventManager.registerListener(STRANGE_HUNGER);
        warpEventManager.registerListener(FOLLOWING);
        warpEventManager.registerListener(SPAWN_A_GUARD);
        warpEventManager.registerListener(BLURRED);
        warpEventManager.registerListener(SUN_SCORNED);
        warpEventManager.registerListener(SLOW_DIGGING);
        warpEventManager.registerListener(INF_VIS_EXHAUST);
        warpEventManager.registerListener(NIGHT_VISION);
        warpEventManager.registerListener(DEATH_GAZE);
        warpEventManager.registerListener(FAKE_SPIDERS);
        warpEventManager.registerListener(BEING_WATCHED);
        warpEventManager.registerListener(SPAWN_SOME_GUARDS);
        warpEventManager.registerListener(BLINDNESS);
        warpEventManager.registerListener(DECREASE_A_STICKY_WARP);
        warpEventManager.registerListener(STRANGE_HUNGER_2);
        warpEventManager.registerListener(GRANT_RESEARCH_HIGH);
        warpEventManager.registerListener(REAL_SPIDERS);
        pickWarpEventListenerBeforeManager.registerListener(THAUMIC_FORTRESS_MASK_DISCOUNT);
        pickWarpEventListenerBeforeManager.registerListener(CALCULATE_WARP_AND_COUNTER);
        pickWarpEventListenerAfterManager.registerListener(SPAWN_GUARD_IF_NO_EVENT);

        warpEventListenerAfterManager.registerListener(CHECK_RESEARCH);
        warpEventListenerAfterManager.registerListener(DECREASE_A_TEMP_WARP);
        warpEventListenerAfterManager.registerListener(DONT_SEND_MISC_FOR_EMPTY);

        warpConditionCheckerManager.registerListener(WARP_AND_COUNTER);
        warpConditionCheckerManager.registerListener(NO_WARP_WARD);

    }
    
    public static  WarpEvent pickWarpEventWithListener(PickWarpEventContext warpContext,Player player) {
        for (PickWarpEventListenerBefore listener : pickWarpEventListenerBeforeManager.getListeners()) {
            listener.beforePickEvent(warpContext, player);
        }
        if (warpContext.warp <= 0 || warpContext.actualWarp <= 0) {
            return WarpEvent.EMPTY;
        }
        warpContext.randWithWarp = player.getRandom().nextInt(warpContext.warp);
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
            picked = listener.afterPickEvent(warpContext,picked,player);
        }
        return picked;
    }

    public static void triggerRandomWarpEvent(PickWarpEventContext warpContext, ServerPlayer player) {
        triggerWarpEvent(warpContext, pickWarpEventWithListener(warpContext,player),player);
    }

    public static void triggerWarpEvent(PickWarpEventContext warpContext, WarpEvent e, ServerPlayer player) {
        e.enable();
        for (WarpEventListenerBefore listener : warpEventListenerBeforeManager.getListeners()) {
            listener.onWarpEvent(warpContext,e,player);
        }
        if (e.enabledFlag) {
            e.onEventTriggered(warpContext,player);
            if (e.retryAnotherFlag){
                triggerRandomWarpEvent(warpContext,player);
                return;
            }
            for (WarpEventListenerAfter listener : warpEventListenerAfterManager.getListeners()) {
                listener.onWarpEvent(warpContext,e,player);
            }
            if (e.sendMiscPacket){
                if (player instanceof ServerPlayer) {
                    new PacketMiscEventS2C((short)0).sendTo(player);
                }
            }
            if (player instanceof ServerPlayer) {
                new PacketSyncWarpS2C(player, (byte)2).sendTo(player);
            }
        }
    }

    public static void tryTriggerRandomWarpEvent(@NotNull ServerPlayer player) {
        PlayerKnowledge knowledge = Thaumcraft.playerKnowledge;
        PickWarpEventContext warpContext = new PickWarpEventContext(
                knowledge.getWarpTotal(player.getName().getString())
                        + getWarpFromGear(player),
                player,
                knowledge.getWarpPerm(player.getName().getString())
                        + knowledge.getWarpSticky(player.getName().getString()),
                knowledge.getWarpCounter(player.getName().getString())
        );
        for (WarpConditionChecker condition : warpConditionCheckerManager.getListeners()) {
            if (!condition.check(warpContext,player)) {
                return;
            }
        }
        triggerRandomWarpEvent(warpContext,player);
    }

    public static final int defaultCheckWarpEventDelay = 2000;
    //unit:tick
    public static int getWarpEventDelayForPlayer(ServerPlayer player) {
        int result = defaultCheckWarpEventDelay;
        for (GettingWarpDelayListener listener : gettingWarpDelayListenerManager.getListeners()) {
            result = listener.onGettingWarpEventDelayForPlayer(player);
        }
        return result;
    }
}
