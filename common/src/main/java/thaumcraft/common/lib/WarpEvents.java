package thaumcraft.common.lib;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import com.linearity.opentc4.utils.StatCollector;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;
import thaumcraft.common.lib.network.misc.PacketMiscEventS2C;
import thaumcraft.common.lib.network.playerdata.PacketAspectPoolS2C;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.concurrent.atomic.AtomicInteger;

import static com.linearity.opentc4.simpleutils.bauble.BaubleUtils.forEachBauble;
import static thaumcraft.api.listeners.warp.WarpEventManager.tryTriggerRandomWarpEvent;

public class WarpEvents {

   public static void checkWarpEvent(ServerPlayer player) {
      tryTriggerRandomWarpEvent(player);
//      int warp = Thaumcraft.playerKnowledge.getWarpTotal(player.getGameProfile().getName());
//      int actualwarp = Thaumcraft.playerKnowledge.getWarpPerm(player.getGameProfile().getName())
//              + Thaumcraft.playerKnowledge.getWarpSticky(player.getGameProfile().getName());
//      warp += getWarpFromGear(player);
//      int warpCounter = Thaumcraft.playerKnowledge.getWarpCounter(player.getGameProfile().getName());
//      int r = player.getRandom().nextInt(100);
//      if (warpCounter > 0 && warp > 0 && (double)r <= Math.sqrt(warpCounter)) {
//         warp = Math.min(100, (warp + warp + warpCounter) / 3);
//         warpCounter = (int)((double)warpCounter - Math.max(5.0F, Math.sqrt(warpCounter) * (double)2.0F));
//         Thaumcraft.playerKnowledge.setWarpCounter(player.getGameProfile().getName(), warpCounter);
//         int eff = player.getRandom().nextInt(warp);
//         ItemStack helm = player.inventory.armorInventory[3];
//         if (helm != null
//                 && helm.getItem() instanceof ItemFortressArmor
//                 && helm.hasTagCompound() && helm.stackTagCompound.hasKey("mask")
//                 && helm.stackTagCompound.getInteger("mask") == 0) {
//            eff -= 2 + player.getRandom().nextInt(4);
//         }
//
//         PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((short)0), (ServerPlayer)player);
//         if (eff > 0) {
//            if (eff <= 4) {
//               grantResearch(player, 1);
//               player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.3")));
//            }
//            else if (eff > 8) {
//               if (eff <= 12) {
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.11")));
//               }
//               else if (eff <= 16) {
//                  MobEffectInstance pe = new MobEffectInstance(Config.potionVisExhaustID, 5000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.1")));
//               }
//               else if (eff <= 20) {
//                  MobEffectInstance pe = new MobEffectInstance(Config.potionThaumarhiaID, Math.min(32000, 10 * warp), 0, true);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.15")));
//               }
//               else if (eff <= 24) {
//                  MobEffectInstance pe = new MobEffectInstance(Config.potionUnHungerID, 5000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//                  pe.addCurativeItem(new ItemStack(ThaumcraftItems.rotten_flesh));
//                  pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));
//
//                  try {
//                     player.addEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.2")));
//               }
//               else if (eff <= 28) {
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.12")));
//               }
//               else if (eff <= 32) {
//                  spawnMist(player, warp, 1);
//               }
//               else if (eff <= 36) {
//                  try {
//                     player.addEffect(new MobEffectInstance(Config.potionBlurredID, Math.min(32000, 10 * warp), 0, true));
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//               }
//               else if (eff <= 40) {
//                  MobEffectInstance pe = new MobEffectInstance(Config.potionSunScornedID, 5000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.5")));
//               }
//               else if (eff <= 44) {
//                  try {
//                     player.addEffect(new MobEffectInstance(Potion.digSlowdown.id, 1200, Math.min(3, warp / 15), true));
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.9")));
//               }
//               else if (eff <= 48) {
//                  MobEffectInstance pe = new MobEffectInstance(Config.potionInfVisExhaustID, 6000, Math.min(3, warp / 15), false);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.1")));
//               }
//               else if (eff <= 52) {
//                  player.addEffect(new MobEffectInstance(Potion.nightVision.id, Math.min(40 * warp, 6000), 0, true));
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.10")));
//               }
//               else if (eff <= 56) {
//                  MobEffectInstance pe = new MobEffectInstance(Config.potionDeathGazeID, 6000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.4")));
//               }
//               else if (eff <= 60) {
//                  suddenlySpiders(player, warp, false);
//               }
//               else if (eff <= 64) {
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.13")));
//               }
//               else if (eff <= 68) {
//                  spawnMist(player, warp, warp / 30);
//               }
//               else if (eff <= 72) {
//                  try {
//                     player.addEffect(new MobEffectInstance(Potion.blindness.id, Math.min(32000, 5 * warp), 0, true));
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//               }
//               else if (eff == 76) {//??? "=="?
//                  if (Thaumcraft.playerKnowledge.getWarpSticky(player.getGameProfile().getName()) > 0) {
//                     Thaumcraft.playerKnowledge.addWarpSticky(player.getGameProfile().getName(), -1);
//                     PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)1), (ServerPlayer)player);
//                     PacketHandler.INSTANCE.sendTo(new PacketWarpMessageS2C(player, (byte)1, -1), (ServerPlayer)player);
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.14")));
//               }
//               else if (eff <= 80) {
//                  MobEffectInstance pe = new MobEffectInstance(Config.potionUnHungerID, 6000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//                  pe.addCurativeItem(new ItemStack(ThaumcraftItems.rotten_flesh));
//                  pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));
//
//                  try {
//                     player.addEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.2")));
//               }
//               else if (eff <= 84) {
//                  grantResearch(player, warp / 10);
//                  player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.3")));
//               }
//               else if (eff > 88) {
//                  if (eff <= 92) {
//                     suddenlySpiders(player, warp, true);
//                  } else {
//                     spawnMist(player, warp, warp / 15);
//                  }
//               }
//            }
//         }
//
//         if (actualwarp > 10 && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), "BATHSALTS") && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), "@BATHSALTS")) {
//            player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.8")));
//            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("@BATHSALTS"), (ServerPlayer)player);
//            Thaumcraft.researchManager.completeResearch(player, "@BATHSALTS");
//         }
//
//         if (actualwarp > 25 && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), "ELDRITCHMINOR")) {
//            grantResearch(player, 10);
//            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ELDRITCHMINOR"), (ServerPlayer)player);
//            Thaumcraft.researchManager.completeResearch(player, "ELDRITCHMINOR");
//         }
//
//         if (actualwarp > 50 && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), "ELDRITCHMAJOR")) {
//            grantResearch(player, 20);
//            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ELDRITCHMAJOR"), (ServerPlayer)player);
//            Thaumcraft.researchManager.completeResearch(player, "ELDRITCHMAJOR");
//         }
//      }
//
//      Thaumcraft.playerKnowledge.addWarpTemp(player.getGameProfile().getName(), -1);
//      PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)2), (ServerPlayer)player);
   }

   public static void spawnMist(ServerPlayer player, int warp, int guardian) {
      new PacketMiscEventS2C((short)1).sendTo(player);
      if (guardian > 0) {
         guardian = Math.min(8, guardian);

         for(int a = 0; a < guardian; ++a) {
            spawnGuardian(player);
         }
      }

      player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.6")),true);
   }

   public static void grantResearch(Player player, int times) {
      int amt = 1 + player.getRandom().nextInt(times);

      for(int a = 0; a < amt; ++a) {
         Aspect aspect = Aspects.getPrimalAspects().get(player.getRandom().nextInt(6));
         Thaumcraft.playerKnowledge.addAspectPool(player.getGameProfile().getName(), aspect, (short)1);

         new PacketAspectPoolS2C(aspect.getAspectKey(), (short) 1, Thaumcraft.playerKnowledge.getAspectPoolFor(player.getGameProfile().getName(), aspect)).sendTo((ServerPlayer)player);
      }

      ResearchManager.scheduleSave(player.getGameProfile().getName());
   }

   public static void spawnGuardian(Player player) {
      EntityEldritchGuardian eg = new EntityEldritchGuardian(player.level());
      int i = MathHelper.floor_double(player.posX);
      int j = MathHelper.floor_double(player.posY);
      int k = MathHelper.floor_double(player.posZ);

      for(int l = 0; l < 50; ++l) {
         int i1 = i + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
         int j1 = j + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
         int k1 = k + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24) * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
         if (World.doesBlockHaveSolidTopSurface(player.level(), i1, j1 - 1, k1)) {
            eg.setPosition(i1, j1, k1);
            if (player.level().checkNoEntityCollision(eg.boundingBox) && player.level().getCollidingBoundingBoxes(eg, eg.boundingBox).isEmpty() && !player.level().isAnyLiquid(eg.boundingBox)) {
               eg.setTarget(player);
               eg.setAttackTarget(player);
               player.level().spawnEntityInWorld(eg);
               break;
            }
         }
      }

   }

   public static void suddenlySpiders(Player player, int warp, boolean real) {
      int spawns = Math.min(50, warp);

      for(int a = 0; a < spawns; ++a) {
         EntityMindSpider spider = new EntityMindSpider(player.level());
         int i = MathHelper.floor_double(player.posX);
         int j = MathHelper.floor_double(player.posY);
         int k = MathHelper.floor_double(player.posZ);
         boolean success = false;

         for(int l = 0; l < 50; ++l) {
            int i1 = i + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
            int j1 = j + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
            int k1 = k + MathHelper.getRandomIntegerInRange(player.getRandom(), 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.getRandom(), -1, 1);
            if (World.doesBlockHaveSolidTopSurface(player.level(), i1, j1 - 1, k1)) {
               spider.setPosition(i1, j1, k1);
               if (player.level().checkNoEntityCollision(spider.boundingBox) && player.level().getCollidingBoundingBoxes(spider, spider.boundingBox).isEmpty() && !player.level().isAnyLiquid(spider.boundingBox)) {
                  success = true;
                  break;
               }
            }
         }

         if (success) {
            spider.setTarget(player);
            spider.setAttackTarget(player);
            if (!real) {
               spider.setViewer(player.getGameProfile().getName());
               spider.setHarmless(true);
            }

            player.level().spawnEntityInWorld(spider);
         }
      }

      player.displayClientMessage(Component.literal("§5§o" + StatCollector.translateToLocal("warp.text.7")));
   }

//   public static void checkDeathGaze(Player player) {
//      MobEffectInstance pe = player.getActiveMobEffectInstance(Potion.potionTypes[Config.potionDeathGazeID]);
//      if (pe != null) {
//         int level = pe.getAmplifier();
//         int range = Math.min(8 + level * 3, 24);
//         List<Entity> list = (List<Entity>)player.level().getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(range, range, range));
//
//          for (Entity entity : list) {
//              if (entity.canBeCollidedWith()
//                      && entity instanceof LivingEntity
//                      && entity.isAlive()
//                      && EntityUtils.isVisibleTo(0.75F, player, entity, (float) range)
//                      && player.canEntityBeSeen(entity)
//                      && (!(entity instanceof Player)
//                      || MinecraftServer.getServer().isPVPEnabled())
//                      && !((LivingEntity) entity).isPotionActive(Potion.wither.getId()))
//              {
//                 LivingEntity living = (LivingEntity) entity;
//                  living.setRevengeTarget(player);
//                  living.setLastAttacker(player);
//                  if (entity instanceof EntityCreature) {
//                      ((EntityCreature) entity).setTarget(player);
//                  }
//
//                  living.addEffect(new MobEffectInstance(Potion.wither.getId(), 80));
//              }
//          }
//
//      }
//   }

   public static int getWarpFromGear(Player player) {
      AtomicInteger w = new AtomicInteger(EventHandlerRunic.getFinalWarp(player.getMainHandItem(), player));
      player.getArmorSlots().forEach(armorInSlot -> w.addAndGet(EventHandlerRunic.getFinalWarp(armorInSlot, player)));
      forEachBauble(player,(slot, stack, item) -> {
         w.addAndGet(EventHandlerRunic.getFinalWarp(stack, player));
         return false;
      });
      return w.addAndGet(EventHandlerRunic.getFinalWarp(player.getOffhandItem(), player));
   }
}
