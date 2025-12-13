package thaumcraft.api.expands.warp.consts;

import com.linearity.opentc4.utils.StatCollector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpEvent;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarpS2C;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessageS2C;

import static thaumcraft.common.lib.WarpEvents.*;

/**
 * I placed events here so that you can unregister them easily.
 */
public class WarpEvents {

    public static final WarpEvent GRANT_RESEARCH_LOW = new WarpEvent(4,4) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            grantResearch(player, 1);
            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.3")),true);
        }
    };
    public static final WarpEvent NOISE_AND_FOLLOWING = new WarpEvent(4,8) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.11")),true);
        }
    };
    public static final WarpEvent VIS_EXHAUST = new WarpEvent(4,16) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            MobEffectInstance pe = new MobEffectInstance(Config.potionVisExhaust, 5000, Math.min(3, warpContext.warp / 15), true);
//            pe.getCurativeItems().clear();

            try {
                player.addEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.1")),true);
        }
    };
    public static final WarpEvent THAUMARHIA = new WarpEvent(4,20) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            MobEffectInstance pe = new MobEffectInstance(Config.potionThaumarhia, Math.min(32000, 10 * warpContext.warp), 0, true);
//            pe.getCurativeItems().clear();

            try {
                player.addEffect(pe);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.15")),true);
        }
    };
    public static final WarpEvent STRANGE_HUNGER = new WarpEvent(4,24) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            //TODO:Add curative item for potionUnHunger
            MobEffectInstance pe = new MobEffectInstance(Config.potionUnHunger, 5000, Math.min(3, warpContext.warp / 15),true, true);
//            pe.getCurativeItems().clear();
//            pe.addCurativeItem(new ItemStack(ThaumcraftItems.rotten_flesh));
//            pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));

            try {
                player.addEffect(pe);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.2")),true);
        }
    };
    public static final WarpEvent FOLLOWING = new WarpEvent(4,28) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.12")),true);
        }
    };
    public static final WarpEvent SPAWN_A_GUARD = new WarpEvent(4,32) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            spawnMist(player, warpContext.warp, 1);
        }
    };
    public static final WarpEvent BLURRED = new WarpEvent(4,36) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            try {
                player.addEffect(new MobEffectInstance(Config.potionBlurredID, Math.min(32000, 10 * warpContext.warp), 0,true, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public static final WarpEvent SUN_SCORNED = new WarpEvent(4,40) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            MobEffectInstance pe = new MobEffectInstance(Config.potionSunScorned, 5000, Math.min(3, warpContext.warp / 15),true, true);
//            pe.getCurativeItems().clear();

            try {
                player.addEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.5")),true);
        }
    };
    public static final WarpEvent SLOW_DIGGING = new WarpEvent(4,44) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {

            try {
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1200, Math.min(3, warpContext.warp / 15),true, true));
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.9")),true);
        }
    };
    public static final WarpEvent INF_VIS_EXHAUST = new WarpEvent(4,48) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            MobEffectInstance pe = new MobEffectInstance(Config.potionInfVisExhaustID, 6000, Math.min(3, warpContext.warp / 15),true, false);
//            pe.getCurativeItems().clear();

            try {
                player.addEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.1")),true);
        }
    };
    public static final WarpEvent NIGHT_VISION = new WarpEvent(4,52) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Math.min(40 * warpContext.warp, 6000), 0,true, true));
            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.10")),true);
        }
    };
    public static final WarpEvent DEATH_GAZE = new WarpEvent(4,56) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            MobEffectInstance pe = new MobEffectInstance(Config.potionDeathGaze, 6000, Math.min(3, warpContext.warp / 15),true, true);
            pe.getCurativeItems().clear();

            try {
                player.addEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.4")),true);
        }
    };
    public static final WarpEvent FAKE_SPIDERS = new WarpEvent(4,60) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            suddenlySpiders(player, warpContext.warp, false);
        }
    };
    public static final WarpEvent BEING_WATCHED = new WarpEvent(4,64) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.13")),true);
        }
    };
    public static final WarpEvent SPAWN_SOME_GUARDS = new WarpEvent(4,68) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            spawnMist(player, warpContext.warp, warpContext.warp / 30);
        }
    };
    public static final WarpEvent BLINDNESS = new WarpEvent(4,72) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            try {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, Math.min(32000, 5 * warpContext.warp), 0,true, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public static final WarpEvent DECREASE_A_STICKY_WARP = new WarpEvent(1,76) {//anazor may get something wrong.
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {

            if (Thaumcraft.playerKnowledge.getWarpSticky(player.getName().getString()) > 0) {
                Thaumcraft.playerKnowledge.addWarpSticky(player.getName().getString(), -1);
                if (player instanceof ServerPlayer serverPlayer) {
                    new PacketSyncWarpS2C(player, (byte) 1).sendTo(serverPlayer);
                    new PacketWarpMessageS2C( (byte) 1, -1).sendTo(serverPlayer);
                }
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.14")),true);
        }
    };
    public static final WarpEvent STRANGE_HUNGER_2 = new WarpEvent(4,80) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            MobEffectInstance pe = new MobEffectInstance(Config.potionUnHunger, 6000, Math.min(3, warpContext.warp / 15),true , true);
//            pe.getCurativeItems().clear();
//            pe.addCurativeItem(new ItemStack(ThaumcraftItems.rotten_flesh));
//            pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));

            try {
                player.addEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.2")),true);
        }
    };
    public static final WarpEvent GRANT_RESEARCH_HIGH = new WarpEvent(4,84) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            grantResearch(player, warpContext.warp / 10);
            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.3")),true);
        }
    };
    public static final WarpEvent REAL_SPIDERS = new WarpEvent(4,92) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, ServerPlayer player) {
            suddenlySpiders(player, warpContext.warp, true);
        }
    };
    public static final WarpEvent SPAWN_LOTS_OF_GUARDS = new WarpEvent(4,96) {
        @Override
        public void onEventTriggered(PickWarpEventContext context, ServerPlayer player) {
            spawnMist(player, context.warp, context.warp / 15);
        }
    };
}
