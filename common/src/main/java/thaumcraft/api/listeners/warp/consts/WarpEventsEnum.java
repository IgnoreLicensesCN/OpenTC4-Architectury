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
            grantResearchLow(warpContext, living);
        }
    }),
    NOISE_AND_FOLLOWING(new WarpEvent(4, 8) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            noiseAndFollowing(warpContext, living);
        }
    }),

    VIS_EXHAUST(new WarpEvent(4, 16) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            visExhaust(warpContext, living);
        }
    }),
    THAUMARHIA(new WarpEvent(4, 20) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            thaumarhia(warpContext, living);
        }
    }),
    STRANGE_HUNGER(new WarpEvent(4, 24) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            strangeHunger(warpContext, living);
        }
    }),
    FOLLOWING(new WarpEvent(4, 28) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            following(warpContext, living);
        }
    }),
    SPAWN_A_GUARD(new WarpEvent(4, 32) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            spawnAGuard(warpContext, living);
        }
    }),
    BLURRED(new WarpEvent(4, 36) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            blurred(warpContext, living);
        }
    }),
    SUN_SCORNED(new WarpEvent(4, 40) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            sunScorned(warpContext, living);
        }
    }),
    SLOW_DIGGING(new WarpEvent(4, 44) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            slowDigging(warpContext, living);
        }
    }),
    INF_VIS_EXHAUST(new WarpEvent(4, 48) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            infVisExhaust(warpContext, living);
        }
    }),
    NIGHT_VISION(new WarpEvent(4, 52) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            nightVision(warpContext, living);
        }
    }),
    DEATH_GAZE(new WarpEvent(4, 56) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            deathGaze(warpContext, living);
        }
    }),
    FAKE_SPIDERS(new WarpEvent(4, 60) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity player) {
            fakeSpiders(warpContext, player);
        }
    }),
    BEING_WATCHED(new WarpEvent(4, 64) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            beingWatched(warpContext, living);
        }
    }),
    SPAWN_SOME_GUARDS(new WarpEvent(4, 68) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity player) {
            spawnSomeGuards(warpContext, player);
        }
    }),
    BLINDNESS(new WarpEvent(4, 72) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity player) {
            blindness(warpContext, player);
        }
    }),
    DECREASE_A_STICKY_WARP(new WarpEvent(1, 76) {//azanor may get something wrong.
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            decreaseAStickyWarp(warpContext, living);
        }
    }),
    STRANGE_HUNGER_2(new WarpEvent(4, 80) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {

            strangeHunger2(warpContext, living);
        }
    }),
    GRANT_RESEARCH_HIGH(new WarpEvent(4, 84) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            grantResearchHigh(warpContext, living);
        }
    }),
    REAL_SPIDERS(new WarpEvent(4, 92) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, LivingEntity living) {
            realSpiders(warpContext, living);
        }
    }),
    SPAWN_LOTS_OF_GUARDS(new WarpEvent(4, 96) {
        @Override
        public void onEventTriggered(PickWarpEventContext context, LivingEntity living) {
            spawnLotsOfGuards(context, living);
        }
    });

    public static final Component COMPONENT_VIS_EXHAUST = Component.translatable("warp.text.1").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_STRANGE_HUNGER = Component.translatable("warp.text.2").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_GRANT_RESEARCH = Component.translatable("warp.text.3").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_DEATH_GAZE = Component.translatable("warp.text.4").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_SUN_SCORNED = Component.translatable("warp.text.5").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_SPAWN_GUARD = Component.translatable("warp.text.6").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_SPAWN_SPIDER = Component.translatable("warp.text.7").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_NEED_SOAP = Component.translatable("warp.text.8").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_SLOW_DIGGING = Component.translatable("warp.text.9").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_NIGHT_VISION = Component.translatable("warp.text.10").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_NOISE_AND_FOLLOWING = Component.translatable("warp.text.11").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_FOLLOWING = Component.translatable("warp.text.12").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_BEING_WATCHED = Component.translatable("warp.text.13").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_DECREASE_STICKY_WARP = Component.translatable("warp.text.14").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    public static final Component COMPONENT_THAUMARHIA = Component.translatable("warp.text.15").withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);

    public static void spawnLotsOfGuards(PickWarpEventContext context, LivingEntity living) {
        spawnMist(living, context.warp, context.warp / 15);
    }

    public static void realSpiders(PickWarpEventContext warpContext, LivingEntity living) {
        suddenlySpiders(living, warpContext.warp, true);
    }

    public static void grantResearchHigh(PickWarpEventContext warpContext, LivingEntity living) {
        grantResearch(living, warpContext.warp / 10);
    }

    public static void strangeHunger2(PickWarpEventContext warpContext, LivingEntity living) {
        MobEffectInstance pe = new MobEffectInstance(
                ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER(), 6000, Math.min(3, warpContext.warp / 15), true, true);
        living.addEffect(pe);
        living.sendSystemMessage(COMPONENT_STRANGE_HUNGER);
    }

    public static void decreaseAStickyWarp(PickWarpEventContext warpEventContext, LivingEntity living) {
        WarpInfo info = WarpInfo.getFromLivingEntity(living);
        if (info != null) {
            if (info.getStickyWarp() > 0) {
                info.setStickyWarp(-1);
                if (living instanceof ServerPlayer serverPlayer) {
                    new PacketChangeWarpS2C((byte) 1, -1).sendTo(serverPlayer);
                }
            }

            living.sendSystemMessage(COMPONENT_DECREASE_STICKY_WARP);
        }
    }

    public static void blindness(PickWarpEventContext warpContext, LivingEntity player) {
        player.addEffect(
                new MobEffectInstance(MobEffects.BLINDNESS, Math.min(32000, 5 * warpContext.warp), 0, true, true));
    }

    public static void spawnSomeGuards(PickWarpEventContext warpContext, LivingEntity player) {
        spawnMist(player, warpContext.warp, warpContext.warp / 30);
    }

    public static void beingWatched(PickWarpEventContext warpContext, LivingEntity living) {
        living.sendSystemMessage(COMPONENT_BEING_WATCHED);
    }

    public static void fakeSpiders(PickWarpEventContext warpContext, LivingEntity player) {
        suddenlySpiders(player, warpContext.warp, false);
    }

    public static void deathGaze(PickWarpEventContext warpContext, LivingEntity living) {
        MobEffectInstance pe = new MobEffectInstance(
                ThaumcraftEffects.ThaumcraftEffectTypeInstances.DEATH_GAZE(), 6000, Math.min(3, warpContext.warp / 15), true, true);
        living.addEffect(pe);
        living.sendSystemMessage(COMPONENT_DEATH_GAZE);
    }

    public static void nightVision(PickWarpEventContext warpContext, LivingEntity living) {
        living.addEffect(
                new MobEffectInstance(
                        MobEffects.NIGHT_VISION, Math.min(40 * warpContext.warp, 6000), 0, true,
                        true
                ));
        living.sendSystemMessage(COMPONENT_NIGHT_VISION);
    }

    public static void infVisExhaust(PickWarpEventContext warpContext, LivingEntity living) {
        MobEffectInstance pe = new MobEffectInstance(
                ThaumcraftEffects.ThaumcraftEffectTypeInstances.INFECTIOUS_VIS_EXHAUST(), 6000, Math.min(3, warpContext.warp / 15), true, false);
        living.addEffect(pe);
        living.sendSystemMessage(COMPONENT_VIS_EXHAUST);
    }

    public static void slowDigging(PickWarpEventContext warpContext, LivingEntity living) {
        living.addEffect(
                new MobEffectInstance(
                        MobEffects.DIG_SLOWDOWN, 1200, Math.min(3, warpContext.warp / 15), true,
                        true
                ));
        living.sendSystemMessage(COMPONENT_SLOW_DIGGING);
    }

    public static void sunScorned(PickWarpEventContext warpContext, LivingEntity living) {
        MobEffectInstance pe = new MobEffectInstance(
                ThaumcraftEffects.ThaumcraftEffectTypeInstances.SUN_SCORNED(), 5000, Math.min(3, warpContext.warp / 15), true, true);
        living.addEffect(pe);
        living.sendSystemMessage(COMPONENT_SUN_SCORNED);
    }

    public static void blurred(PickWarpEventContext warpContext, LivingEntity living) {
        living.addEffect(
                new MobEffectInstance(
                        ThaumcraftEffects.ThaumcraftEffectTypeInstances.BLURRED_VISION(), Math.min(32000, 10 * warpContext.warp), 0,
                        true, true
                ));
    }

    public static void spawnAGuard(PickWarpEventContext warpContext, LivingEntity living) {
        spawnMist(living, warpContext.warp, 1);
    }

    public static void following(PickWarpEventContext warpContext, LivingEntity living) {
        living.sendSystemMessage(COMPONENT_FOLLOWING);
    }

    public static void strangeHunger(PickWarpEventContext warpContext, LivingEntity living) {
        MobEffectInstance pe = new MobEffectInstance(
                ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER(),
                5000, Math.min(3, warpContext.warp / 15), true, true
        );
        living.addEffect(pe);
        living.sendSystemMessage(COMPONENT_STRANGE_HUNGER);
    }

    public static void thaumarhia(PickWarpEventContext warpContext, LivingEntity living) {
        MobEffectInstance pe = new MobEffectInstance(
                ThaumcraftEffects.ThaumcraftEffectTypeInstances.THAUMARHIA(), Math.min(32000, 10 * warpContext.warp), 0, true, true);
        living.addEffect(pe);
        living.sendSystemMessage(COMPONENT_THAUMARHIA);
    }

    public static void visExhaust(PickWarpEventContext warpContext, LivingEntity living) {
        MobEffectInstance pe = new MobEffectInstance(
                ThaumcraftEffects.ThaumcraftEffectTypeInstances.VIS_EXHAUST(), 5000, Math.min(3, warpContext.warp / 15), true, true);
        living.addEffect(pe);
        living.sendSystemMessage(COMPONENT_VIS_EXHAUST);
    }

    public static void noiseAndFollowing(PickWarpEventContext warpContext, LivingEntity living) {
        living.sendSystemMessage(COMPONENT_NOISE_AND_FOLLOWING);
    }

    public static void grantResearchLow(PickWarpEventContext warpContext, LivingEntity living) {
        grantResearch(living, 1);
    }

    public static void grantResearch(LivingEntity living, int amount) {
        grantResearchAspect(living, amount);
        living.sendSystemMessage(COMPONENT_GRANT_RESEARCH);
    }

    public final WarpEvent event;

    WarpEventsEnum(WarpEvent warpEvent) {
        this.event = warpEvent;
    }
}
