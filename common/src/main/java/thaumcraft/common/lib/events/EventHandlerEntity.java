package thaumcraft.common.lib.events;

import com.google.common.io.Files;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.Player;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.*;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRepairableExtended;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigEntities;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.monster.*;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.entities.projectile.EntityPrimalArrow;
import thaumcraft.common.items.ItemBathSalts;
import thaumcraft.common.items.ItemCrystalEssence;
import thaumcraft.common.items.armor.Hover;
import thaumcraft.common.items.armor.ItemHoverHarness;
import thaumcraft.common.items.equipment.ItemBowBone;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.WarpEvents;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.lib.world.dim.Cell;
import thaumcraft.common.lib.world.dim.CellLoc;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.tiles.TileOwned;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import static thaumcraft.api.expands.warp.WarpEventManager.getWarpEventDelayForPlayer;

public class EventHandlerEntity {
   public HashMap<Integer,Float> prevStep = new HashMap<>();
   public static HashMap<String,ArrayList<WeakReference<Entity>>> linkedEntities = new HashMap<>();

   @SubscribeEvent
   public void droppedItem(ItemTossEvent event) {
      NBTTagCompound itemData = event.entityItem.getEntityData();
      itemData.setString("thrower", event.player.getCommandSenderName());
   }

   @SubscribeEvent
   public void playerLoad(PlayerEvent.LoadFromFile event) {
      Player p = event.Player;
      Thaumcraft.proxy.getPlayerKnowledge().wipePlayerKnowledge(p.getCommandSenderName());
      File file1 = this.getPlayerFile("thaum", event.playerDirectory, p.getCommandSenderName());
      boolean legacy = false;
      if (!file1.exists()) {
         File filep = event.getPlayerFile("thaum");
         if (filep.exists()) {
            try {
               Files.copy(filep, file1);
                Thaumcraft.log.info("Using and converting UUID Thaumcraft savefile for {}", p.getCommandSenderName());
               legacy = true;
               filep.delete();
               File fb = event.getPlayerFile("thaumback");
               if (fb.exists()) {
                  fb.delete();
               }
            } catch (IOException ignored) {
            }
         } else {
            File filet = this.getLegacyPlayerFile(p);
            if (filet.exists()) {
               try {
                  Files.copy(filet, file1);
                   Thaumcraft.log.info("Using pre MC 1.7.10 Thaumcraft savefile for {}", p.getCommandSenderName());
                  legacy = true;
               } catch (IOException ignored) {
               }
            }
         }
      }

      ResearchManager.loadPlayerData(p, file1, this.getPlayerFile("thaumback", event.playerDirectory, p.getCommandSenderName()), legacy);

      for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
         for(ResearchItem ri : cat.research.values()) {
            if (ri.isAutoUnlock()) {
               Thaumcraft.proxy.getResearchManager().completeResearch(p, ri.key);
            }
         }
      }

   }

   public File getLegacyPlayerFile(Player player) {
      try {
         File playersDirectory = new File(player.worldObj.getSaveHandler().getWorldDirectory(), "players");
         return new File(playersDirectory, player.getCommandSenderName() + ".thaum");
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public File getPlayerFile(String suffix, File playerDirectory, String playername) {
      if ("dat".equals(suffix)) {
         throw new IllegalArgumentException("The suffix 'dat' is reserved");
      } else {
         return new File(playerDirectory, playername + "." + suffix);
      }
   }

   @SubscribeEvent
   public void playerSave(PlayerEvent.SaveToFile event) {
      Player p = event.Player;
      ResearchManager.savePlayerData(p, this.getPlayerFile("thaum", event.playerDirectory, p.getCommandSenderName()), this.getPlayerFile("thaumback", event.playerDirectory, p.getCommandSenderName()));
   }

   public static void doRepair(ItemStack is, Player player) {
      int level = EnchantmentHelper.getEnchantmentLevel(Config.enchRepair.effectId, is);
      if (level > 0) {
         if (level > 2) {
            level = 2;
         }

         AspectList cost = ThaumcraftCraftingManager.getObjectTags(is);
         if (cost != null && cost.size() != 0) {
            cost = ResearchManager.reduceToPrimals(cost);
            AspectList finalCost = new AspectList();

            for(Aspect a : cost.getAspects()) {
               if (a != null) {
                  finalCost.merge(a, (int)Math.sqrt(cost.getAmount(a) * 2) * level);
               }
            }

            if (is.getItem() instanceof IRepairableExtended) {
               if (((IRepairableExtended)is.getItem()).doRepair(is, player, level) && WandManager.consumeVisFromInventory(player, finalCost)) {
                  is.damageItem(-level, player);
               }
            } else if (WandManager.consumeVisFromInventory(player, finalCost)) {
               is.damageItem(-level, player);
            }

         }
      }
   }

   @SubscribeEvent
   public void livingTick(LivingEvent.LivingUpdateEvent event) {
      if (event.entity instanceof Player) {
         Player player = (Player)event.entity;
         if (event.entity.worldObj.provider.dimensionId == Config.dimensionOuterId && !player.capabilities.isCreativeMode && player.ticksExisted % 20 == 0 && (player.capabilities.isFlying || Hover.getHover(player.getEntityId()))) {
            player.capabilities.isFlying = false;
            Hover.setHover(player.getEntityId(), false);
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY + StatCollector.translateToLocal("tc.break.fly")));
         }

         if (Hover.getHover(player.getEntityId()) && (player.inventory.armorItemInSlot(2) == null || !(player.inventory.armorItemInSlot(2).getItem() instanceof ItemHoverHarness))) {
            Hover.setHover(player.getEntityId(), false);
            player.capabilities.isFlying = false;
         }

         if (!event.entity.worldObj.isRemote) {
            if (!Config.wuss && player.ticksExisted > 0 && player.ticksExisted % getWarpEventDelayForPlayer(player) == 0) {
               WarpEvents.checkWarpEvent(player);
            }

            if (player.ticksExisted % 10 == 0 && player.isPotionActive(Config.potionDeathGazeID)) {
               WarpEvents.checkDeathGaze(player);
            }

            if (player.ticksExisted % 40 == 0) {
               int a = 0;

               while(true) {
                  InventoryPlayer var10001 = player.inventory;
                  if (a >= InventoryPlayer.getHotbarSize()) {
                     for(/*int */a = 0; a < 4; ++a) {
                        if (player.inventory.armorItemInSlot(a) != null) {
                           ItemStack is = player.inventory.armorItemInSlot(a);
                           if (is.getItemDamage() > 0 && is.getItem() instanceof IRepairable && !player.capabilities.isCreativeMode) {
                              doRepair(is, player);
                           }
                        }
                     }
                     break;
                  }

                  if (player.inventory.mainInventory[a] != null) {
                     ItemStack is = player.inventory.mainInventory[a];
                     if (is.getItemDamage() > 0 && is.getItem() instanceof IRepairable && !player.capabilities.isCreativeMode && !(is.getItem() instanceof ItemHoverHarness)) {
                        doRepair(is, player);
                     }
                  }

                  ++a;
               }
            }
         }

         this.updateSpeed(player);
         if (player.worldObj.isRemote && (player.isSneaking() || player.inventory.armorItemInSlot(0) == null || player.inventory.armorItemInSlot(0).getItem() != ConfigItems.itemBootsTraveller) && this.prevStep.containsKey(player.getEntityId())) {
            player.stepHeight = this.prevStep.get(player.getEntityId());
            this.prevStep.remove(player.getEntityId());
         }
      }

      if (event.entity instanceof EntityMob && !event.entity.isDead) {
         EntityMob mob = (EntityMob)event.entity;
         int t = (int)mob.getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue();
         if (t >= 0 && ChampionModifier.mods[t].type == 0) {
            ChampionModifier.mods[t].effect.performEffect(mob, null, null, 0.0F);
         }
      }

   }

   private void updateSpeed(Player player) {
      try {
         if (!player.capabilities.isFlying && player.inventory.armorItemInSlot(0) != null && player.moveForward > 0.0F) {
            int haste = EnchantmentHelper.getEnchantmentLevel(Config.enchHaste.effectId, player.inventory.armorItemInSlot(0));
            if (haste > 0) {
               float bonus = (float)haste * 0.015F;
               if (player.isAirBorne) {
                  bonus /= 2.0F;
               }

               if (player.isInWater()) {
                  bonus /= 2.0F;
               }

               player.moveFlying(0.0F, 1.0F, bonus);
            }
         }
      } catch (Exception ignored) {
      }

   }

   @SubscribeEvent
   public void playerJumps(LivingEvent.LivingJumpEvent event) {
      if (event.entity instanceof Player && ((Player)event.entity).inventory.armorItemInSlot(0) != null && ((Player)event.entity).inventory.armorItemInSlot(0).getItem() == ConfigItems.itemBootsTraveller) {
         EntityLivingBase var10000 = event.entityLiving;
         var10000.motionY += 0.275F;
      }

   }

   @SubscribeEvent
   public void playerInteract(EntityInteractEvent event) {
      if (event.target instanceof EntityGolemBase && !((EntityGolemBase) event.target).getOwnerName().isEmpty() && !((EntityGolemBase)event.target).getOwnerName().equals(event.Player.getCommandSenderName())) {
         if (!event.Player.worldObj.isRemote) {
            event.Player.addChatMessage(new ChatComponentTranslation("You are not my Master!"));
         }

         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void entitySpawns(EntityJoinWorldEvent event) {
      if (!event.world.isRemote) {
         if (event.entity instanceof EntityEnderPearl) {
            int x = MathHelper.floor_double(event.entity.posX);
            int y = MathHelper.floor_double(event.entity.posY);
            int z = MathHelper.floor_double(event.entity.posZ);

            label138:
            for(int xx = -5; xx <= 5; ++xx) {
               for(int yy = -5; yy <= 5; ++yy) {
                  for(int zz = -5; zz <= 5; ++zz) {
                     TileEntity tile = event.world.getTileEntity(x + xx, y + yy, z + zz);
                     if (tile instanceof TileOwned) {
                        if (((EntityEnderPearl)event.entity).getThrower() instanceof Player) {
                           ((Player)((EntityEnderPearl)event.entity).getThrower()).addChatMessage(new ChatComponentText("§5§oThe magic of a nearby warded object destroys the ender pearl."));
                        }

                        event.entity.setDead();
                        break label138;
                     }
                  }
               }
            }
         }

         if (event.entity instanceof Player) {
            ArrayList<WeakReference<Entity>> dudes = linkedEntities.get(event.entity.getCommandSenderName());
            if (dudes != null) {
               for(WeakReference dude : dudes) {
                  if (dude.get() != null && ((Entity)dude.get()).timeUntilPortal == 0) {
                     ((Entity)dude.get()).timeUntilPortal = ((Entity)dude.get()).getPortalCooldown();
                     ((Entity)dude.get()).travelToDimension(event.world.provider.dimensionId);
                  }
               }
            }
         } else if (event.entity instanceof EntityMob) {
            EntityMob mob = (EntityMob)event.entity;
            if (mob.getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue() < (double)-1.0F) {
               int championChance = event.world.rand.nextInt(100);
               if (event.world.difficultySetting == EnumDifficulty.EASY || !Config.championMobs) {
                  championChance += 2;
               }

               if (event.world.difficultySetting == EnumDifficulty.HARD) {
                  championChance -= Config.championMobs ? 2 : 0;
               }

               if (event.world.provider.dimensionId == Config.dimensionOuterId) {
                  championChance -= 3;
               }

               BiomeGenBase bg = mob.worldObj.getBiomeGenForCoords(
                       MathHelper.ceiling_double_int(mob.posX),
                       MathHelper.ceiling_double_int(mob.posZ)
               );
               if (BiomeDictionary.isBiomeOfType(bg, Type.SPOOKY)
                       || BiomeDictionary.isBiomeOfType(bg, Type.NETHER)
                       || BiomeDictionary.isBiomeOfType(bg, Type.END)) {
                  championChance -= Config.championMobs ? 2 : 1;
               }

               if (this.isDangerousLocation(
                       mob.worldObj, MathHelper.ceiling_double_int(mob.posX),
                       MathHelper.ceiling_double_int(mob.posY),
                       MathHelper.ceiling_double_int(mob.posZ))) {
                  championChance -= Config.championMobs ? 10 : 3;
               }

               int cc = 0;
               boolean whitelisted = false;

               for(Class<?> clazz : ConfigEntities.championModWhitelist.keySet()) {
                  if (clazz.isAssignableFrom(event.entity.getClass())) {
                     whitelisted = true;
                     if (Config.championMobs || event.entity instanceof EntityThaumcraftBoss) {
                        cc = Math.max(cc, ConfigEntities.championModWhitelist.get(clazz) - 1);
                     }
                  }
               }

               championChance -= cc;
               if (whitelisted && championChance <= 0
                       && mob.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() >= (double)10.0F) {
                  EntityUtils.makeChampion(mob, false);
               } else {
                  IAttributeInstance modai = mob.getEntityAttribute(EntityUtils.CHAMPION_MOD);
                  modai.removeModifier(ChampionModifier.ATTRIBUTE_MOD_NONE);
                  modai.applyModifier(ChampionModifier.ATTRIBUTE_MOD_NONE);
               }
            }
         }
      }

   }

   private boolean isDangerousLocation(World world, int x, int y, int z) {
      if (world.provider.dimensionId == Config.dimensionOuterId) {
         int xx = x >> 4;
         int zz = z >> 4;
         Cell c = MazeHandler.getFromHashMap(new CellLoc(xx, zz));
          return c != null && (c.feature == 6 || c.feature == 8);
      }

      return false;
   }

   @SubscribeEvent
   public void entityConstuct(EntityEvent.EntityConstructing event) {
      if (event.entity instanceof EntityMob) {
         EntityMob mob = (EntityMob)event.entity;
         mob.getAttributeMap().registerAttribute(EntityUtils.CHAMPION_MOD).setBaseValue(-2.0F);
      }

   }

   @SubscribeEvent
   public void itemPickup(EntityItemPickupEvent event) {
      if (event.Player.getCommandSenderName().startsWith("FakeThaumcraft")) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void livingDrops(LivingDropsEvent event) {
      boolean fakePlayerFlag = event.source.getEntity() != null && event.source.getEntity() instanceof FakePlayer;
      if (!event.entity.worldObj.isRemote
              && event.recentlyHit
              && !fakePlayerFlag
              && event.entity instanceof EntityMob
              && !(event.entity instanceof EntityThaumcraftBoss)
              && ((EntityMob)event.entity).getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue() >= (double)0.0F) {
         int i = 5 + event.entity.worldObj.rand.nextInt(3);

         while(i > 0) {
            int j = EntityXPOrb.getXPSplit(i);
            i -= j;
            event.entity.worldObj.spawnEntityInWorld(
                    new EntityXPOrb(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, j
                    ));
         }

         int lb = Math.min(2, MathHelper.floor_float((float)(event.entity.worldObj.rand.nextInt(9) + event.lootingLevel) / 5.0F));
         event.drops.add(
                 new EntityItem(event.entity.worldObj,
                         event.entityLiving.posX,
                         event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(),
                         event.entityLiving.posZ,
                         new ItemStack(ConfigItems.itemLootbag, 1, lb)));
      }

      if (event.entityLiving instanceof EntityZombie && !(event.entityLiving instanceof EntityBrainyZombie) && event.recentlyHit && event.entity.worldObj.rand.nextInt(10) - event.lootingLevel < 1) {
         event.drops.add(new EntityItem(event.entity.worldObj, event.entityLiving.posX, event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(), event.entityLiving.posZ, new ItemStack(ConfigItems.itemZombieBrain)));
      }

      if (event.entityLiving instanceof EntityVillager && event.entity.worldObj.rand.nextInt(10) - event.lootingLevel < 1) {
         event.drops.add(new EntityItem(event.entity.worldObj, event.entityLiving.posX, event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(), event.entityLiving.posZ, new ItemStack(ConfigItems.itemResource, 1, 18)));
      }

      if (event.source == DamageSourceThaumcraft.dissolve) {
         AspectList aspects = ScanManager.generateEntityAspects(event.entityLiving);
         if (aspects != null && aspects.size() > 0) {
            for(Aspect aspect : aspects.getAspects()) {
               if (!event.entity.worldObj.rand.nextBoolean()) {
                  int size = 1 + event.entity.worldObj.rand.nextInt(aspects.getAmount(aspect));
                  size = Math.max(1, size / 2);
                  ItemStack stack = new ItemStack(ConfigItems.itemCrystalEssence, size, 0);
                  ((ItemCrystalEssence)stack.getItem()).setAspects(stack, (new AspectList()).add(aspect, 1));
                  event.drops.add(new EntityItem(event.entity.worldObj, event.entityLiving.posX, event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(), event.entityLiving.posZ, stack));
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void livingTick(LivingDeathEvent event) {
      if (!event.entityLiving.worldObj.isRemote && !(event.entityLiving instanceof ITaintedMob) && event.entityLiving.isPotionActive(Config.potionTaintPoisonID)) {
         Entity entity = null;
         if (event.entityLiving instanceof EntityCreeper) {
            entity = new EntityTaintCreeper(event.entityLiving.worldObj);
         } else if (event.entityLiving instanceof EntitySheep) {
            entity = new EntityTaintSheep(event.entityLiving.worldObj);
         } else if (event.entityLiving instanceof EntityCow) {
            entity = new EntityTaintCow(event.entityLiving.worldObj);
         } else if (event.entityLiving instanceof EntityPig) {
            entity = new EntityTaintPig(event.entityLiving.worldObj);
         } else if (event.entityLiving instanceof EntityChicken) {
            entity = new EntityTaintChicken(event.entityLiving.worldObj);
         } else if (event.entityLiving instanceof EntityVillager) {
            entity = new EntityTaintVillager(event.entityLiving.worldObj);
         } else {
            entity = new EntityThaumicSlime(event.entityLiving.worldObj);
            if (entity != null) {
               ((EntityThaumicSlime)entity).setSlimeSize((int)(1.0F + Math.min(event.entityLiving.getMaxHealth() / 10.0F, 6.0F)));
            }
         }

         if (entity != null) {
            entity.setLocationAndAngles(event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, event.entityLiving.rotationYaw, 0.0F);
            event.entityLiving.worldObj.spawnEntityInWorld(entity);
            event.entityLiving.setDead();
         }
      } else if (!event.entityLiving.worldObj.isRemote && EntityUtils.getRecentlyHit(event.entityLiving) > 0) {
         AspectList aspectsCompound = ScanManager.generateEntityAspects(event.entityLiving);
         if (aspectsCompound != null && aspectsCompound.size() > 0) {
            AspectList aspects = ResearchManager.reduceToPrimals(aspectsCompound);

            for(Aspect aspect : aspects.getAspects()) {
               if (event.entityLiving.worldObj.rand.nextBoolean()) {
                  EntityAspectOrb orb = new EntityAspectOrb(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, aspect, 1 + event.entityLiving.worldObj.rand.nextInt(aspects.getAmount(aspect)));
                  event.entityLiving.worldObj.spawnEntityInWorld(orb);
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void bowNocked(ArrowNockEvent event) {
      if (event.Player.inventory.hasItem(ConfigItems.itemPrimalArrow)) {
         event.Player.setItemInUse(event.result, event.result.getItem().getMaxItemUseDuration(event.result));
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void bowShot(ArrowLooseEvent event) {
      if (event.Player.inventory.hasItem(ConfigItems.itemPrimalArrow)) {
         float f = 0.0F;
         float dam = 2.0F;
         if (event.bow.getItem() instanceof ItemBowBone) {
            f = (float)event.charge / 10.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if ((double)f < 0.1) {
               return;
            }

            dam = 2.5F;
         } else {
            f = (float)event.charge / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if ((double)f < 0.1) {
               return;
            }
         }

         if (f > 1.0F) {
            f = 1.0F;
         }

         int type = 0;

         for(int j = 0; j < event.Player.inventory.mainInventory.length; ++j) {
            if (event.Player.inventory.mainInventory[j] != null && event.Player.inventory.mainInventory[j].getItem() == ConfigItems.itemPrimalArrow) {
               type = event.Player.inventory.mainInventory[j].getItemDamage();
               break;
            }
         }

         EntityPrimalArrow entityarrow = new EntityPrimalArrow(event.Player.worldObj, event.Player, f * dam, type);
         if (event.bow.getItem() instanceof ItemBowBone) {
            entityarrow.setDamage(entityarrow.getDamage() + (double)0.5F);
         } else if (f == 1.0F) {
            entityarrow.setIsCritical(true);
         }

         int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, event.bow);
         if (k > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + (double)k * (double)0.5F + (double)0.5F);
         }

         int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, event.bow);
         if (type == 3) {
            ++l;
         }

         if (l > 0) {
            entityarrow.setKnockbackStrength(l);
         }

         if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, event.bow) > 0) {
            entityarrow.setFire(100);
         }

         event.bow.damageItem(1, event.Player);
         event.Player.worldObj.playSoundAtEntity(event.Player, "random.bow", 1.0F, 1.0F / (event.Player.worldObj.rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
         boolean flag = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, event.bow) > 0 && event.Player.worldObj.rand.nextFloat() < 0.33F;

          if (!event.Player.capabilities.isCreativeMode || !flag) {
            InventoryUtils.consumeInventoryItem(event.Player, ConfigItems.itemPrimalArrow, type);
         }

         if (!event.Player.worldObj.isRemote) {
            event.Player.worldObj.spawnEntityInWorld(entityarrow);
         }

         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void finishedUsingItem(PlayerUseItemEvent.Finish event) {
      if (!event.entity.worldObj.isRemote && event.Player.isPotionActive(Config.potionUnHungerID)) {
         if (!event.item.isItemEqual(new ItemStack(Items.rotten_flesh)) && !event.item.isItemEqual(new ItemStack(ConfigItems.itemZombieBrain))) {
            if (event.item.getItem() instanceof ItemFood) {
               event.Player.addChatMessage(new ChatComponentText("§4§o" + StatCollector.translateToLocal("warp.text.hunger.1")));
            }
         } else {
            PotionEffect pe = event.Player.getActivePotionEffect(Potion.potionTypes[Config.potionUnHungerID]);
            int amp = pe.getAmplifier() - 1;
            int duration = pe.getDuration() - 600;
            event.Player.removePotionEffect(Config.potionUnHungerID);
            if (duration > 0 && amp >= 0) {
               pe = new PotionEffect(Config.potionUnHungerID, duration, amp, true);
               pe.getCurativeItems().clear();
               pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
               event.Player.addPotionEffect(pe);
            }

            event.Player.addChatMessage(new ChatComponentText("§2§o" + StatCollector.translateToLocal("warp.text.hunger.2")));
         }
      }

   }

   @SubscribeEvent
   public void itemExpire(ItemExpireEvent event) {
      if (event.entityItem.getEntityItem() != null && event.entityItem.getEntityItem().getItem() != null && event.entityItem.getEntityItem().getItem() instanceof ItemBathSalts) {
         int x = MathHelper.floor_double(event.entityItem.posX);
         int y = MathHelper.floor_double(event.entityItem.posY);
         int z = MathHelper.floor_double(event.entityItem.posZ);
         if (event.entityItem.worldObj.getBlock(x, y, z) == Blocks.water && event.entityItem.worldObj.getBlockMetadata(x, y, z) == 0) {
            event.entityItem.worldObj.setBlock(x, y, z, ConfigBlocks.blockFluidPure);
         }
      }

   }

   @SubscribeEvent
   public void breakSpeedEvent(PlayerEvent.BreakSpeed event) {
      if (!event.Player.onGround && Hover.getHover(event.Player.getEntityId())) {
         event.newSpeed = event.originalSpeed * 5.0F;
      }

   }
}
