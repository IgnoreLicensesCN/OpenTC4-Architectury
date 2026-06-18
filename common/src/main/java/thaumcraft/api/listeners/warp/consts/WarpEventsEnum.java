package thaumcraft.api.listeners.warp.consts;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.api.listeners.warp.PickWarpEventContext;
import thaumcraft.api.listeners.warp.listeners.WarpEvent;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketChangeWarpS2C;

import static thaumcraft.api.listeners.warp.consts.WarpEvents.*;

/**
 * I placed events here so that you can unregister them easily.
 */
public enum WarpEventsEnum {

    GRANT_RESEARCH_LOW(new WarpEvent(4, 4) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            grantResearchAspect(living, 1);
            living.sendSystemMessage(
                    Component.translatable("warp.text.3").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    NOISE_AND_FOLLOWING(new WarpEvent(4, 8) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            living.sendSystemMessage(
                    Component.translatable("warp.text.11").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),

    VIS_EXHAUST(new WarpEvent(4, 16) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.ThaumcraftEffectTypeInstances.VIS_EXHAUST(), 5000, Math.min(3, warpContext.warp / 15), true, true);
            living.addEffect(pe);
            living.sendSystemMessage(
                    Component.translatable("warp.text.1").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    THAUMARHIA(new WarpEvent(4, 20) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.ThaumcraftEffectTypeInstances.THAUMARHIA(), Math.min(32000, 10 * warpContext.warp), 0, true, true);
            living.addEffect(pe);
            living.sendSystemMessage(
                    Component.translatable("warp.text.15").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    STRANGE_HUNGER(new WarpEvent(4, 24) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER(), 5000, Math.min(3, warpContext.warp / 15), true, true);
            living.addEffect(pe);
            living.sendSystemMessage(
                    Component.translatable("warp.text.2").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    FOLLOWING(new WarpEvent(4, 28) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            living.sendSystemMessage(
                    Component.translatable("warp.text.12").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    SPAWN_A_GUARD(new WarpEvent(4, 32) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            spawnMist(living, warpContext.warp, 1);
        }
    }),
    BLURRED(new WarpEvent(4, 36) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            living.addEffect(
                    new MobEffectInstance(
                            ThaumcraftEffects.ThaumcraftEffectTypeInstances.BLURRED_VISION(), Math.min(32000, 10 * warpContext.warp), 0,
                            true, true
                    ));
        }
    }),
    SUN_SCORNED(new WarpEvent(4, 40) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.ThaumcraftEffectTypeInstances.SUN_SCORNED(), 5000, Math.min(3, warpContext.warp / 15), true, true);
            living.addEffect(pe);
            living.sendSystemMessage(
                    Component.translatable("warp.text.5").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    SLOW_DIGGING(new WarpEvent(4, 44) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            living.addEffect(
                    new MobEffectInstance(
                            MobEffects.DIG_SLOWDOWN, 1200, Math.min(3, warpContext.warp / 15), true,
                            true
                    ));
            living.sendSystemMessage(
                    Component.translatable("warp.text.9").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    INF_VIS_EXHAUST(new WarpEvent(4, 48) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.ThaumcraftEffectTypeInstances.INFECTIOUS_VIS_EXHAUST(), 6000, Math.min(3, warpContext.warp / 15), true, false);
            living.addEffect(pe);
            living.sendSystemMessage(
                    Component.translatable("warp.text.1").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    NIGHT_VISION(new WarpEvent(4, 52) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            living.addEffect(
                    new MobEffectInstance(
                            MobEffects.NIGHT_VISION, Math.min(40 * warpContext.warp, 6000), 0, true,
                            true
                    ));
            living.sendSystemMessage(
                    Component.translatable("warp.text.10").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    DEATH_GAZE(new WarpEvent(4, 56) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            MobEffectInstance pe =new MobEffectInstance(
                    ThaumcraftEffects.ThaumcraftEffectTypeInstances.DEATH_GAZE(), 6000, Math.min(3, warpContext.warp / 15), true, true);
            living.addEffect(pe);
            living.sendSystemMessage(
                    Component.translatable("warp.text.4").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    FAKE_SPIDERS(new WarpEvent(4, 60) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity player) {
            suddenlySpiders(player, warpContext.warp, false);
        }
    }),
    BEING_WATCHED(new WarpEvent(4, 64) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            living.sendSystemMessage(
                    Component.translatable("warp.text.13").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    SPAWN_SOME_GUARDS(new WarpEvent(4, 68) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity player) {
            spawnMist(player, warpContext.warp, warpContext.warp / 30);
        }
    }),
    BLINDNESS(new WarpEvent(4, 72) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity player) {
            player.addEffect(
                    new MobEffectInstance(MobEffects.BLINDNESS, Math.min(32000, 5 * warpContext.warp), 0, true, true));
        }
    }),
    DECREASE_A_STICKY_WARP(new WarpEvent(1, 76) {//azanor may get something wrong.
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            WarpInfo info = WarpInfo.getFromLivingEntity(living);
            if (info != null) {
                if (info.getStickyWarp() > 0) {
                    info.setStickyWarp(-1);
                    if (living instanceof ServerPlayer serverPlayer) {
                        new PacketChangeWarpS2C((byte) 1, -1).sendTo(serverPlayer);
                    }
                }

                living.sendSystemMessage(
                        Component.translatable("warp.text.14").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));}
        }
    }),
    STRANGE_HUNGER_2(new WarpEvent(4, 80) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            MobEffectInstance pe = new MobEffectInstance(
                    ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER(), 6000, Math.min(3, warpContext.warp / 15), true, true);
            living.addEffect(pe);
            living.sendSystemMessage(
                    Component.translatable("warp.text.2").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    GRANT_RESEARCH_HIGH(new WarpEvent(4, 84) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            grantResearchAspect(living, warpContext.warp / 10);
            living.sendSystemMessage(
                    Component.translatable("warp.text.3").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC));
        }
    }),
    REAL_SPIDERS(new WarpEvent(4, 92) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            suddenlySpiders(living, warpContext.warp, true);
        }
    }),
    SPAWN_LOTS_OF_GUARDS(new WarpEvent(4, 96) {
        @Override
        public void onEventTriggered(PickWarpEventContext context, LivingEntity living) {
            spawnMist(living, context.warp, context.warp / 15);
        }
    });
    public final WarpEvent event;

    WarpEventsEnum(WarpEvent warpEvent) {
        this.event = warpEvent;
    }
}
