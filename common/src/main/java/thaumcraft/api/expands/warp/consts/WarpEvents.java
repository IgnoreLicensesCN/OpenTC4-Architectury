package thaumcraft.api.expands.warp.consts;

import net.minecraft.entity.player.Player;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpEvent;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarp;
import thaumcraft.common.lib.network.playerdata.PacketWarpMessage;

import static thaumcraft.common.lib.WarpEvents.*;

/**
 * I placed events here so that you can unregister them easily.
 */
public class WarpEvents {

    public static final WarpEvent GRANT_RESEARCH_LOW = new WarpEvent(4,4) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            grantResearch(player, 1);
            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.3")));
        }
    };
    public static final WarpEvent NOISE_AND_FOLLOWING = new WarpEvent(4,8) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext,Player player) {
            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.11")));
        }
    };
    public static final WarpEvent VIS_EXHAUST = new WarpEvent(4,16) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext,Player player) {
            PotionEffect pe = new PotionEffect(Config.potionVisExhaustID, 5000, Math.min(3, warpContext.warp / 15), true);
            pe.getCurativeItems().clear();

            try {
                player.addPotionEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.1")));
        }
    };
    public static final WarpEvent THAUMARHIA = new WarpEvent(4,20) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            PotionEffect pe = new PotionEffect(Config.potionThaumarhiaID, Math.min(32000, 10 * warpContext.warp), 0, true);
            pe.getCurativeItems().clear();

            try {
                player.addPotionEffect(pe);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.15")));
        }
    };
    public static final WarpEvent STRANGE_HUNGER = new WarpEvent(4,24) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            PotionEffect pe = new PotionEffect(Config.potionUnHungerID, 5000, Math.min(3, warpContext.warp / 15), true);
            pe.getCurativeItems().clear();
            pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
            pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));

            try {
                player.addPotionEffect(pe);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.2")));
        }
    };
    public static final WarpEvent FOLLOWING = new WarpEvent(4,28) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.12")));
        }
    };
    public static final WarpEvent SPAWN_A_GUARD = new WarpEvent(4,32) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            spawnMist(player, warpContext.warp, 1);
        }
    };
    public static final WarpEvent BLURRED = new WarpEvent(4,36) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            try {
                player.addPotionEffect(new PotionEffect(Config.potionBlurredID, Math.min(32000, 10 * warpContext.warp), 0, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public static final WarpEvent SUN_SCORNED = new WarpEvent(4,40) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            PotionEffect pe = new PotionEffect(Config.potionSunScornedID, 5000, Math.min(3, warpContext.warp / 15), true);
            pe.getCurativeItems().clear();

            try {
                player.addPotionEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.5")));
        }
    };
    public static final WarpEvent SLOW_DIGGING = new WarpEvent(4,44) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {

            try {
                player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1200, Math.min(3, warpContext.warp / 15), true));
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.9")));
        }
    };
    public static final WarpEvent INF_VIS_EXHAUST = new WarpEvent(4,48) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            PotionEffect pe = new PotionEffect(Config.potionInfVisExhaustID, 6000, Math.min(3, warpContext.warp / 15), false);
            pe.getCurativeItems().clear();

            try {
                player.addPotionEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.1")));
        }
    };
    public static final WarpEvent NIGHT_VISION = new WarpEvent(4,52) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            player.addPotionEffect(new PotionEffect(Potion.nightVision.id, Math.min(40 * warpContext.warp, 6000), 0, true));
            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.10")));
        }
    };
    public static final WarpEvent DEATH_GAZE = new WarpEvent(4,56) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            PotionEffect pe = new PotionEffect(Config.potionDeathGazeID, 6000, Math.min(3, warpContext.warp / 15), true);
            pe.getCurativeItems().clear();

            try {
                player.addPotionEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.4")));
        }
    };
    public static final WarpEvent FAKE_SPIDERS = new WarpEvent(4,60) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            suddenlySpiders(player, warpContext.warp, false);
        }
    };
    public static final WarpEvent BEING_WATCHED = new WarpEvent(4,64) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.13")));
        }
    };
    public static final WarpEvent SPAWN_SOME_GUARDS = new WarpEvent(4,68) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            spawnMist(player, warpContext.warp, warpContext.warp / 30);
        }
    };
    public static final WarpEvent BLINDNESS = new WarpEvent(4,72) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            try {
                player.addPotionEffect(new PotionEffect(Potion.blindness.id, Math.min(32000, 5 * warpContext.warp), 0, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public static final WarpEvent DECREASE_A_STICKY_WARP = new WarpEvent(1,76) {//anazor may get something wrong.
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {

            if (Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName()) > 0) {
                Thaumcraft.proxy.getPlayerKnowledge().addWarpSticky(player.getCommandSenderName(), -1);
                if (player instanceof ServerPlayer) {
                    PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte) 1), (ServerPlayer) player);
                    PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte) 1, -1), (ServerPlayer) player);
                }
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.14")));
        }
    };
    public static final WarpEvent STRANGE_HUNGER_2 = new WarpEvent(4,80) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            PotionEffect pe = new PotionEffect(Config.potionUnHungerID, 6000, Math.min(3, warpContext.warp / 15), true);
            pe.getCurativeItems().clear();
            pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
            pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));

            try {
                player.addPotionEffect(pe);
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.2")));
        }
    };
    public static final WarpEvent GRANT_RESEARCH_HIGH = new WarpEvent(4,84) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            grantResearch(player, warpContext.warp / 10);
            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.3")));
        }
    };
    public static final WarpEvent REAL_SPIDERS = new WarpEvent(4,92) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext, Player player) {
            suddenlySpiders(player, warpContext.warp, true);
        }
    };
    public static final WarpEvent SPAWN_LOTS_OF_GUARDS = new WarpEvent(4,96) {
        @Override
        public void onEventTriggered(PickWarpEventContext context, Player player) {
            spawnMist(player, context.warp, context.warp / 15);
        }
    };
}
