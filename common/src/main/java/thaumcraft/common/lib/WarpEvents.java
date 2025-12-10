package thaumcraft.common.lib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.Player;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static simpleutils.bauble.BaubleUtils.forEachBauble;
import static thaumcraft.api.expands.warp.WarpEventManager.tryTriggerRandomWarpEvent;

public class WarpEvents {

   public static void checkWarpEvent(Player player) {
      tryTriggerRandomWarpEvent(player);
//      int warp = Thaumcraft.proxy.getPlayerKnowledge().getWarpTotal(player.getCommandSenderName());
//      int actualwarp = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName())
//              + Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName());
//      warp += getWarpFromGear(player);
//      int warpCounter = Thaumcraft.proxy.getPlayerKnowledge().getWarpCounter(player.getCommandSenderName());
//      int r = player.worldObj.rand.nextInt(100);
//      if (warpCounter > 0 && warp > 0 && (double)r <= Math.sqrt(warpCounter)) {
//         warp = Math.min(100, (warp + warp + warpCounter) / 3);
//         warpCounter = (int)((double)warpCounter - Math.max(5.0F, Math.sqrt(warpCounter) * (double)2.0F));
//         Thaumcraft.proxy.getPlayerKnowledge().setWarpCounter(player.getCommandSenderName(), warpCounter);
//         int eff = player.worldObj.rand.nextInt(warp);
//         ItemStack helm = player.inventory.armorInventory[3];
//         if (helm != null
//                 && helm.getItem() instanceof ItemFortressArmor
//                 && helm.hasTagCompound() && helm.stackTagCompound.hasKey("mask")
//                 && helm.stackTagCompound.getInteger("mask") == 0) {
//            eff -= 2 + player.worldObj.rand.nextInt(4);
//         }
//
//         PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((short)0), (ServerPlayer)player);
//         if (eff > 0) {
//            if (eff <= 4) {
//               grantResearch(player, 1);
//               player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.3")));
//            }
//            else if (eff > 8) {
//               if (eff <= 12) {
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.11")));
//               }
//               else if (eff <= 16) {
//                  PotionEffect pe = new PotionEffect(Config.potionVisExhaustID, 5000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addPotionEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.1")));
//               }
//               else if (eff <= 20) {
//                  PotionEffect pe = new PotionEffect(Config.potionThaumarhiaID, Math.min(32000, 10 * warp), 0, true);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addPotionEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.15")));
//               }
//               else if (eff <= 24) {
//                  PotionEffect pe = new PotionEffect(Config.potionUnHungerID, 5000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//                  pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
//                  pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));
//
//                  try {
//                     player.addPotionEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.2")));
//               }
//               else if (eff <= 28) {
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.12")));
//               }
//               else if (eff <= 32) {
//                  spawnMist(player, warp, 1);
//               }
//               else if (eff <= 36) {
//                  try {
//                     player.addPotionEffect(new PotionEffect(Config.potionBlurredID, Math.min(32000, 10 * warp), 0, true));
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//               }
//               else if (eff <= 40) {
//                  PotionEffect pe = new PotionEffect(Config.potionSunScornedID, 5000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addPotionEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.5")));
//               }
//               else if (eff <= 44) {
//                  try {
//                     player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1200, Math.min(3, warp / 15), true));
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.9")));
//               }
//               else if (eff <= 48) {
//                  PotionEffect pe = new PotionEffect(Config.potionInfVisExhaustID, 6000, Math.min(3, warp / 15), false);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addPotionEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.1")));
//               }
//               else if (eff <= 52) {
//                  player.addPotionEffect(new PotionEffect(Potion.nightVision.id, Math.min(40 * warp, 6000), 0, true));
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.10")));
//               }
//               else if (eff <= 56) {
//                  PotionEffect pe = new PotionEffect(Config.potionDeathGazeID, 6000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//
//                  try {
//                     player.addPotionEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.4")));
//               }
//               else if (eff <= 60) {
//                  suddenlySpiders(player, warp, false);
//               }
//               else if (eff <= 64) {
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.13")));
//               }
//               else if (eff <= 68) {
//                  spawnMist(player, warp, warp / 30);
//               }
//               else if (eff <= 72) {
//                  try {
//                     player.addPotionEffect(new PotionEffect(Potion.blindness.id, Math.min(32000, 5 * warp), 0, true));
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//               }
//               else if (eff == 76) {//??? "=="?
//                  if (Thaumcraft.proxy.getPlayerKnowledge().getWarpSticky(player.getCommandSenderName()) > 0) {
//                     Thaumcraft.proxy.getPlayerKnowledge().addWarpSticky(player.getCommandSenderName(), -1);
//                     PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)1), (ServerPlayer)player);
//                     PacketHandler.INSTANCE.sendTo(new PacketWarpMessage(player, (byte)1, -1), (ServerPlayer)player);
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.14")));
//               }
//               else if (eff <= 80) {
//                  PotionEffect pe = new PotionEffect(Config.potionUnHungerID, 6000, Math.min(3, warp / 15), true);
//                  pe.getCurativeItems().clear();
//                  pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
//                  pe.addCurativeItem(new ItemStack(ConfigItems.itemZombieBrain));
//
//                  try {
//                     player.addPotionEffect(pe);
//                  } catch (Exception e) {
//                     e.printStackTrace();
//                  }
//
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.2")));
//               }
//               else if (eff <= 84) {
//                  grantResearch(player, warp / 10);
//                  player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.3")));
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
//         if (actualwarp > 10 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "BATHSALTS") && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "@BATHSALTS")) {
//            player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.8")));
//            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("@BATHSALTS"), (ServerPlayer)player);
//            Thaumcraft.proxy.getResearchManager().completeResearch(player, "@BATHSALTS");
//         }
//
//         if (actualwarp > 25 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ELDRITCHMINOR")) {
//            grantResearch(player, 10);
//            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ELDRITCHMINOR"), (ServerPlayer)player);
//            Thaumcraft.proxy.getResearchManager().completeResearch(player, "ELDRITCHMINOR");
//         }
//
//         if (actualwarp > 50 && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "ELDRITCHMAJOR")) {
//            grantResearch(player, 20);
//            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("ELDRITCHMAJOR"), (ServerPlayer)player);
//            Thaumcraft.proxy.getResearchManager().completeResearch(player, "ELDRITCHMAJOR");
//         }
//      }
//
//      Thaumcraft.proxy.getPlayerKnowledge().addWarpTemp(player.getCommandSenderName(), -1);
//      PacketHandler.INSTANCE.sendTo(new PacketSyncWarp(player, (byte)2), (ServerPlayer)player);
   }

   public static void spawnMist(Player player, int warp, int guardian) {
      PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((short)1), (ServerPlayer)player);
      if (guardian > 0) {
         guardian = Math.min(8, guardian);

         for(int a = 0; a < guardian; ++a) {
            spawnGuardian(player);
         }
      }

      player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.6")));
   }

   public static void grantResearch(Player player, int times) {
      int amt = 1 + player.worldObj.rand.nextInt(times);

      for(int a = 0; a < amt; ++a) {
         Aspect aspect = Aspect.getPrimalAspects().get(player.worldObj.rand.nextInt(6));
         Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspect, (short)1);
         PacketHandler.INSTANCE.sendTo(new PacketAspectPool(aspect.getTag(), (short) 1, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), aspect)), (ServerPlayer)player);
      }

      ResearchManager.scheduleSave(player);
   }

   public static void spawnGuardian(Player player) {
      EntityEldritchGuardian eg = new EntityEldritchGuardian(player.worldObj);
      int i = MathHelper.floor_double(player.posX);
      int j = MathHelper.floor_double(player.posY);
      int k = MathHelper.floor_double(player.posZ);

      for(int l = 0; l < 50; ++l) {
         int i1 = i + MathHelper.getRandomIntegerInRange(player.worldObj.rand, 7, 24) * MathHelper.getRandomIntegerInRange(player.worldObj.rand, -1, 1);
         int j1 = j + MathHelper.getRandomIntegerInRange(player.worldObj.rand, 7, 24) * MathHelper.getRandomIntegerInRange(player.worldObj.rand, -1, 1);
         int k1 = k + MathHelper.getRandomIntegerInRange(player.worldObj.rand, 7, 24) * MathHelper.getRandomIntegerInRange(player.worldObj.rand, -1, 1);
         if (World.doesBlockHaveSolidTopSurface(player.worldObj, i1, j1 - 1, k1)) {
            eg.setPosition(i1, j1, k1);
            if (player.worldObj.checkNoEntityCollision(eg.boundingBox) && player.worldObj.getCollidingBoundingBoxes(eg, eg.boundingBox).isEmpty() && !player.worldObj.isAnyLiquid(eg.boundingBox)) {
               eg.setTarget(player);
               eg.setAttackTarget(player);
               player.worldObj.spawnEntityInWorld(eg);
               break;
            }
         }
      }

   }

   public static void suddenlySpiders(Player player, int warp, boolean real) {
      int spawns = Math.min(50, warp);

      for(int a = 0; a < spawns; ++a) {
         EntityMindSpider spider = new EntityMindSpider(player.worldObj);
         int i = MathHelper.floor_double(player.posX);
         int j = MathHelper.floor_double(player.posY);
         int k = MathHelper.floor_double(player.posZ);
         boolean success = false;

         for(int l = 0; l < 50; ++l) {
            int i1 = i + MathHelper.getRandomIntegerInRange(player.worldObj.rand, 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.worldObj.rand, -1, 1);
            int j1 = j + MathHelper.getRandomIntegerInRange(player.worldObj.rand, 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.worldObj.rand, -1, 1);
            int k1 = k + MathHelper.getRandomIntegerInRange(player.worldObj.rand, 7, 24)
                    * MathHelper.getRandomIntegerInRange(player.worldObj.rand, -1, 1);
            if (World.doesBlockHaveSolidTopSurface(player.worldObj, i1, j1 - 1, k1)) {
               spider.setPosition(i1, j1, k1);
               if (player.worldObj.checkNoEntityCollision(spider.boundingBox) && player.worldObj.getCollidingBoundingBoxes(spider, spider.boundingBox).isEmpty() && !player.worldObj.isAnyLiquid(spider.boundingBox)) {
                  success = true;
                  break;
               }
            }
         }

         if (success) {
            spider.setTarget(player);
            spider.setAttackTarget(player);
            if (!real) {
               spider.setViewer(player.getCommandSenderName());
               spider.setHarmless(true);
            }

            player.worldObj.spawnEntityInWorld(spider);
         }
      }

      player.addChatMessage(new ChatComponentText("§5§o" + StatCollector.translateToLocal("warp.text.7")));
   }

   public static void checkDeathGaze(Player player) {
      PotionEffect pe = player.getActivePotionEffect(Potion.potionTypes[Config.potionDeathGazeID]);
      if (pe != null) {
         int level = pe.getAmplifier();
         int range = Math.min(8 + level * 3, 24);
         List<Entity> list = (List<Entity>)player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(range, range, range));

          for (Entity entity : list) {
              if (entity.canBeCollidedWith()
                      && entity instanceof EntityLivingBase
                      && entity.isEntityAlive()
                      && EntityUtils.isVisibleTo(0.75F, player, entity, (float) range)
                      && player.canEntityBeSeen(entity)
                      && (!(entity instanceof Player)
                      || MinecraftServer.getServer().isPVPEnabled())
                      && !((EntityLivingBase) entity).isPotionActive(Potion.wither.getId()))
              {
                 EntityLivingBase living = (EntityLivingBase) entity;
                  living.setRevengeTarget(player);
                  living.setLastAttacker(player);
                  if (entity instanceof EntityCreature) {
                      ((EntityCreature) entity).setTarget(player);
                  }

                  living.addPotionEffect(new PotionEffect(Potion.wither.getId(), 80));
              }
          }

      }
   }

   public static int getWarpFromGear(Player player) {
      AtomicInteger w = new AtomicInteger(EventHandlerRunic.getFinalWarp(player.getCurrentEquippedItem(), player));

      for(int a = 0; a < 4; ++a) {
         w.addAndGet(EventHandlerRunic.getFinalWarp(player.inventory.armorItemInSlot(a), player));
      }
      forEachBauble(player,(slot, stack, item) -> {
         w.addAndGet(EventHandlerRunic.getFinalWarp(stack, player));
         return false;
      });
      return w.get();
   }
}
