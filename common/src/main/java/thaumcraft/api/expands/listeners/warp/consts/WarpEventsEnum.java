package thaumcraft.api.expands.listeners.warp.consts;

import com.linearity.opentc4.utils.StatCollector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import thaumcraft.api.expands.listeners.warp.PickWarpEventContext;
import thaumcraft.api.expands.listeners.warp.listeners.WarpEvent;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarpS2C;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessageS2C;

import static thaumcraft.common.lib.WarpEvents.*;

/**
 * I placed events here so that you can unregister them easily.
 */
public enum WarpEventsEnum {

    GRANT_RESEARCH_LOW(new WarpEvent(4, 4) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            grantResearch(player, 1);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.3")), true);
        }
    }),
    NOISE_AND_FOLLOWING(new WarpEvent(4, 8) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.11")), true);
        }
    }),

    VIS_EXHAUST(new WarpEvent(4, 16) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.VIS_EXHAUST, 5000, Math.min(3, warpContext.warp / 15), true, true);
            player.addEffect(pe);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.1")), true);
        }
    }),
    THAUMARHIA(new WarpEvent(4, 20) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {

            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.THAUMARHIA, Math.min(32000, 10 * warpContext.warp), 0, true, true);
            player.addEffect(pe);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.15")), true);
        }
    }),
    STRANGE_HUNGER(new WarpEvent(4, 24) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.UNNATURAL_HUNGER, 5000, Math.min(3, warpContext.warp / 15), true, true);
            player.addEffect(pe);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.2")), true);
        }
    }),
    FOLLOWING(new WarpEvent(4, 28) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.12")), true);
        }
    }),
    SPAWN_A_GUARD(new WarpEvent(4, 32) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            spawnMist(player, warpContext.warp, 1);
        }
    }),
    BLURRED(new WarpEvent(4, 36) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.addEffect(
                    new MobEffectInstance(
                            ThaumcraftEffects.BLURRED_VISION, Math.min(32000, 10 * warpContext.warp), 0,
                            true, true
                    ));
        }
    }),
    SUN_SCORNED(new WarpEvent(4, 40) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {

            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.SUN_SCORNED, 5000, Math.min(3, warpContext.warp / 15), true, true);
            player.addEffect(pe);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.5")), true);
        }
    }),
    SLOW_DIGGING(new WarpEvent(4, 44) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.DIG_SLOWDOWN, 1200, Math.min(3, warpContext.warp / 15), true,
                            true
                    ));
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.9")), true);
        }
    }),
    INF_VIS_EXHAUST(new WarpEvent(4, 48) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {

            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.INFECTIOUS_VIS_EXHAUST, 6000, Math.min(3, warpContext.warp / 15), true, false);
            player.addEffect(pe);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.1")), true);
        }
    }),
    NIGHT_VISION(new WarpEvent(4, 52) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.NIGHT_VISION, Math.min(40 * warpContext.warp, 6000), 0, true,
                            true
                    ));
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.10")), true);
        }
    }),
    DEATH_GAZE(new WarpEvent(4, 56) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {

            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.DEATH_GAZE, 6000, Math.min(3, warpContext.warp / 15), true, true);
            player.addEffect(pe);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.4")), true);
        }
    }),
    FAKE_SPIDERS(new WarpEvent(4, 60) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            suddenlySpiders(player, warpContext.warp, false);
        }
    }),
    BEING_WATCHED(new WarpEvent(4, 64) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.13")), true);
        }
    }),
    SPAWN_SOME_GUARDS(new WarpEvent(4, 68) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            spawnMist(player, warpContext.warp, warpContext.warp / 30);
        }
    }),
    BLINDNESS(new WarpEvent(4, 72) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.addEffect(
                    new MobEffectInstance(MobEffects.BLINDNESS, Math.min(32000, 5 * warpContext.warp), 0, true, true));
        }
    }),
    DECREASE_A_STICKY_WARP(new WarpEvent(1, 76) {//anazor may get something wrong.
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {

            if (Thaumcraft.playerKnowledge.getWarpSticky(player.getName()
                    .getString()) > 0) {
                Thaumcraft.playerKnowledge.addWarpSticky(
                        player.getName()
                                .getString(), -1
                );
                if (player instanceof ServerPlayer serverPlayer) {
                    new PacketSyncWarpS2C(player, (byte) 1).sendTo(serverPlayer);
                    new PacketWarpMessageS2C((byte) 1, -1).sendTo(serverPlayer);
                }
            }

            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.14")), true);
        }
    }),
    STRANGE_HUNGER_2(new WarpEvent(4, 80) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {

            MobEffectInstance pe = new MobEffectInstance(
                    ThaumcraftEffects.UNNATURAL_HUNGER, 6000, Math.min(3, warpContext.warp / 15), true, true);
            player.addEffect(pe);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.2")), true);
        }
    }),
    GRANT_RESEARCH_HIGH(new WarpEvent(4, 84) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            grantResearch(player, warpContext.warp / 10);
            player.displayClientMessage(
                    Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.3")), true);
        }
    }),
    REAL_SPIDERS(new WarpEvent(4, 92) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            suddenlySpiders(player, warpContext.warp, true);
        }
    }),
    SPAWN_LOTS_OF_GUARDS(new WarpEvent(4, 96) {
        @Override
        public void onEventTriggered(PickWarpEventContext context, ServerPlayer player) {
            spawnMist(player, context.warp, context.warp / 15);
        }
    });
    public final WarpEvent event;

    WarpEventsEnum(WarpEvent warpEvent) {
        this.event = warpEvent;
    }
}
