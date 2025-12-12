package thaumcraft.common.lib.events;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.StatCollector;
import com.linearity.opentc4.utils.vanilla1710.BiomeType;
import com.linearity.opentc4.utils.vanilla1710.BiomeWithTypes;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.Difficulty;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRepairableExtended;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.wands.EnchantmentRepairVisProvider;
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
import thaumcraft.common.items.misc.ItemBathSalts;
import thaumcraft.common.items.misc.ItemCrystalEssence;
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
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.linearity.opentc4.OpenTC4.LOGGER;
import static thaumcraft.api.expands.warp.WarpEventManager.getWarpEventDelayForPlayer;
import static thaumcraft.common.lib.utils.EntityUtils.CHAMPION_MOD_BASE_VALUE_ATTACHED_NOT_AFFECTED;
import static thaumcraft.common.lib.utils.EntityUtils.CHAMPION_MOD_BASE_VALUE_NOT_ATTACHED;


//TODO
public class EventHandlerEntity {
   public static HashMap<String,Float> prevStep = new HashMap<>();
   public static HashMap<String,ArrayList<WeakReference<Entity>>> linkedEntities = new HashMap<>();
   public static Set<Entity> championMobs = (Set<Entity>)(Object) new MapMaker().weakKeys().weakValues().concurrencyLevel(2).makeMap().keySet();

   public static File getThaumcraftPlayersDirectory(MinecraftServer server) {
      File saveRootDir = server.getServerDirectory();
      File thaumcraftPlayerDir = new File(saveRootDir, "thaumcraft_players");
      if (!thaumcraftPlayerDir.exists()) {
         thaumcraftPlayerDir.mkdirs();
      }
      return thaumcraftPlayerDir;
   }
   public static final Map<ItemEntity,Player> itemDropByPlayer = new MapMaker().weakKeys().weakValues().concurrencyLevel(2).makeMap();
   public static void init() {
      if (Platform.getEnvironment() == Env.SERVER) {
         //wdym we have architectury
         PlayerEvent.DROP_ITEM.register(((player, entity) -> {
            itemDropByPlayer.put(entity,player);
            return EventResult.pass();
         }));
         PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            File thaumcraftPlayerDir = getThaumcraftPlayersDirectory(serverPlayer.server);
             Thaumcraft.playerKnowledge.wipePlayerKnowledge(serverPlayer.getName().getString());
            File playerThaumFile = getPlayerFile("thaum", thaumcraftPlayerDir, serverPlayer.getName().getString());
            boolean legacy = false;
            if (!playerThaumFile.exists()) {
               try {
                  playerThaumFile.createNewFile();
               }catch (Exception e) {
                  LOGGER.error(e);
                  throw new RuntimeException(e);
               }
//               File filep = event.getPlayerFile("thaum");
//               if (filep.exists()) {
//                  try {
//                     Files.copy(filep, playerThaumFile);
//                     Thaumcraft.log.info("Using and converting UUID Thaumcraft savefile for {}", serverPlayer.getName().getString());
//                     legacy = true;
//                     filep.delete();
//                     File fb = event.getPlayerFile("thaumback");
//                     if (fb.exists()) {
//                        fb.delete();
//                     }
//                  } catch (IOException ignored) {
//                  }
//               } 
            }

            ResearchManager.loadPlayerData(serverPlayer.getName().getString(), playerThaumFile, getPlayerFile("thaumback", thaumcraftPlayerDir, serverPlayer.getName().getString()), legacy);

            for(ResearchCategoryList cat : ResearchCategories.researchCategories.values()) {
               for(ResearchItem ri : cat.research.values()) {
                  if (ri.isAutoUnlock()) {
                     Thaumcraft.researchManager.completeResearch(serverPlayer, ri.key);
                  }
               }
            }
         });
         PlayerEvent.PLAYER_QUIT.register(serverPlayer -> {
            File thaumcraftPlayerDir = getThaumcraftPlayersDirectory(serverPlayer.server);
            ResearchManager.savePlayerData(serverPlayer.getName().getString(),
                    getPlayerFile("thaum", thaumcraftPlayerDir, serverPlayer.getName().getString()),
                    getPlayerFile("thaumback", thaumcraftPlayerDir, serverPlayer.getName().getString()));
         });
         TickEvent.PLAYER_PRE.register(player -> {
            var world = player.level();
            var playerName = player.getName().getString();
            boolean hoverFlag = Hover.getHover(playerName);
            if (world.dimension() == Config.dimensionOuter
                    && !player.isCreative()
                    && player.tickCount % 20 == 0
                    && (player.getAbilities().flying || hoverFlag)
            ) {
               player.getAbilities().flying = false;
               player.onUpdateAbilities();
               Hover.setHover(playerName, false);
               player.sendSystemMessage(Component.literal(ChatFormatting.ITALIC + "" + ChatFormatting.GRAY + StatCollector.translateToLocal("tc.break.fly")));
            }
            ItemStack chestArmor = player.getItemBySlot(EquipmentSlot.CHEST);
            if (hoverFlag && !(chestArmor.getItem() instanceof ItemHoverHarness)
            ) {
               Hover.setHover(playerName, false);
               player.getAbilities().flying = false;
               player.onUpdateAbilities();
            }

            if (Platform.getEnvironment() != Env.CLIENT) {
               if (!Config.wuss && player.tickCount > 0 && player.tickCount % getWarpEventDelayForPlayer(player) == 0) {
                  WarpEvents.checkWarpEvent(player);
               }

               if (player.tickCount % 10 == 0 && player.hasEffect(Config.potionDeathGaze)) {
                  WarpEvents.checkDeathGaze(player);
               }

               if (player.tickCount % 40 == 0) {
                  int a = 0;
                  Consumer<ItemStack> repairItemStack = stack -> {
                     if (stack.getDamageValue() > 0 && stack.getItem() instanceof IRepairable && !player.isCreative()) {
                        doRepair(stack, player);
                     }
                  };
                  player.getInventory().items.forEach(repairItemStack);
                  player.getInventory().offhand.forEach(repairItemStack);
                  player.getInventory().armor.forEach(repairItemStack);
               }
            }

            updateSpeed(player);
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
            if (world.isClientSide() && (player.isCrouching() || feet.getItem() != ConfigItems.itemBootsTraveller) && prevStep.containsKey(playerName)) {
               player.setMaxUpStep(prevStep.get(playerName));
               prevStep.remove(playerName);
            }
         });
//         EntityEvent.ADD.register((entity, world) -> {
//            if (entity instanceof LivingEntity livingEntity
//                    && !livingEntity.isDeadOrDying()
//            ) {
//                var entityLevel = livingEntity.level();// i think it should be the same with world
//                AttributeInstance championModInstance = livingEntity.getAttribute(EntityUtils.CHAMPION_MOD);
//                if (championModInstance != null) {
//                    if (championModInstance.getBaseValue() == CHAMPION_MOD_BASE_VALUE_NOT_ATTACHED) {
//                        int championChance = world.getRandom().nextInt(100);
//                        if (world.getDifficulty() == Difficulty.EASY || !Config.championMobs) {
//                            championChance += 2;
//                        }
//
//                        if (world.getDifficulty() == Difficulty.HARD) {
//                            championChance -= Config.championMobs ? 2 : 0;
//                        }
//
//                        if (world.dimension() == Config.dimensionOuter) {
//                            championChance -= 3;
//                        }
//
//                        Holder<Biome> biomeHolder = entityLevel.getBiome(
//                                livingEntity.blockPosition()
//                        );
//                       AtomicReference<ResourceKey<Biome>> biomeResKeyRef = new AtomicReference<>();
//                       biomeHolder.unwrapKey().ifPresent(biomeResKeyRef::set);
//                       if (biomeResKeyRef.get() == null) {
//                          biomeResKeyRef.set(BiomeWithTypes.getBiomeResKey(biomeHolder.value()));
//                       }
//                       Collection<BiomeType> biomeTypes = BiomeWithTypes.getBiomeTypes(biomeResKeyRef.get());
//
//                        if (biomeTypes.contains(BiomeType.SPOOKY)
//                                || biomeTypes.contains(BiomeType.NETHER)
//                                || biomeTypes.contains(BiomeType.END)) {
//                            championChance -= Config.championMobs ? 2 : 1;
//                        }
//
//                        if (isDangerousLocation(entityLevel, livingEntity.blockPosition())){
//                            championChance -= Config.championMobs ? 10 : 3;
//                        }
//
//                        int cc = 0;
//                        boolean whitelisted = false;
//
//                        for (Class<?> clazz : ConfigEntities.championModWhitelist.keySet()) {
//                            if (clazz.isAssignableFrom(livingEntity.getClass())) {
//                                whitelisted = true;
//                                if (Config.championMobs || livingEntity instanceof EntityThaumcraftBoss) {
//                                    cc = Math.max(cc, ConfigEntities.championModWhitelist.get(clazz) - 1);
//                                }
//                            }
//                        }
//
//                        championChance -= cc;
//                        AttributeInstance maxHealthAttr = livingEntity.getAttribute(Attributes.MAX_HEALTH);
//                        if (whitelisted
//                                && championChance <= 0
//                                && maxHealthAttr != null
//                                && maxHealthAttr.getBaseValue() >= (double) 10.0F
//                        ) {
//                            EntityUtils.makeChampion(livingEntity, false);
//                        } else {
//                           championModInstance.setBaseValue(CHAMPION_MOD_BASE_VALUE_ATTACHED_NOT_AFFECTED);
//                        }
//                    }
//                }
//            }
//            return EventResult.pass();
//         });

      }

   }


   public static File getPlayerFile(String suffix, File playerDirectory, String playername) {
      if ("dat".equals(suffix)) {
         throw new IllegalArgumentException("The suffix 'dat' is reserved");
      } else {
         return new File(playerDirectory, playername + "." + suffix);
      }
   }

   public static final Function<ItemStack,Boolean> checkIfCanConsumeForRepair = itemStack -> (itemStack.getItem() instanceof EnchantmentRepairVisProvider provider) && provider.canProvideVisForRepair();
   public static void doRepair(ItemStack is, Player player) {

      int level = EnchantmentHelper.getEnchantments(is).getOrDefault(Config.enchRepair,0);
      if (level > 0) {
         if (level > 2) {
            level = 2;
         }

         AspectList cost = ThaumcraftCraftingManager.getObjectTags(is);
         if (cost != null && cost.size() != 0) {
            cost = ResearchManager.reduceToPrimals(cost);
            AspectList finalCost = new AspectList();

            for(Aspect a : cost.getAspectTypes()) {
               if (a != null) {
                  finalCost.merge(a, (int)Math.sqrt(cost.getAmount(a) * 2) * level);
               }
            }
            boolean doRepair = false;
            if (is.getItem() instanceof IRepairableExtended repairable) {
               if (repairable.doRepair(is, player, level) && WandManager.consumeVisFromInventory(player, finalCost, checkIfCanConsumeForRepair)) {
//                  is.damageItem(-level, player);
                  doRepair = true;
               }
            } else if (WandManager.consumeVisFromInventory(player, finalCost,checkIfCanConsumeForRepair)) {
//               is.damageItem(-level, player);
               doRepair = true;
            }
            if (doRepair) {
               is.hurtAndBreak(-level,player,(p) -> {});
            }
         }
      }
   }

//   @SubscribeEvent
//   public void livingTick(LivingEvent.LivingUpdateEvent event) {
//      if (event.entity instanceof EntityMob && !event.entity.isDead) {
//         EntityMob mob = (EntityMob)event.entity;
//         int t = (int)mob.getAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue();
//         if (t >= 0 && ChampionModifier.mods[t].type == 0) {
//            ChampionModifier.mods[t].effect.performEffect(mob, null, null, 0.0F);
//         }
//      }
//   }

   private static void updateSpeed(Player player) {
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
      if (event.entity instanceof Player
              && ((Player)event.entity).inventory.armorItemInSlot(0) != null
              && ((Player)event.entity).inventory.armorItemInSlot(0).getItem() == ConfigItems.itemBootsTraveller
      ) {
         LivingEntity var10000 = event.entityLiving;
         var10000.motionY += 0.275F;
      }
   }

   @SubscribeEvent
   public void playerInteract(EntityInteractEvent event) {
      if (event.target instanceof EntityGolemBase
              && !((EntityGolemBase) event.target).getOwnerName().isEmpty()
              && !((EntityGolemBase)event.target).getOwnerName().equals(event.Player.getName().getString())
      ) {
         if (Platform.getEnvironment() != Env.CLIENT) {
            event.Player.displayClientMessage(new ChatComponentTranslation("You are not my Master!"));
         }

         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void entitySpawns(EntityJoinWorldEvent event) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (event.entity instanceof EntityEnderPearl) {
            int x = MathHelper.floor_double(event.entity.posX);
            int y = MathHelper.floor_double(event.entity.posY);
            int z = MathHelper.floor_double(event.entity.posZ);

            label138:
            for(int xx = -5; xx <= 5; ++xx) {
               for(int yy = -5; yy <= 5; ++yy) {
                  for(int zz = -5; zz <= 5; ++zz) {
                     BlockEntity tile = event.world.getBlockEntity(x + xx, y + yy, z + zz);
                     if (tile instanceof TileOwned) {
                        if (((EntityEnderPearl)event.entity).getThrower() instanceof Player) {
                           ((Player)((EntityEnderPearl)event.entity).getThrower()).displayClientMessage(Component.literal("§5§oThe magic of a nearby warded object destroys the ender pearl."));
                        }

                        event.entity.setDead();
                        break label138;
                     }
                  }
               }
            }
         }

         if (event.entity instanceof Player) {
            ArrayList<WeakReference<Entity>> dudes = linkedEntities.get(event.entity.getName().getString());
            if (dudes != null) {
               for(WeakReference dude : dudes) {
                  if (dude.get() != null && ((Entity)dude.get()).timeUntilPortal == 0) {
                     ((Entity)dude.get()).timeUntilPortal = ((Entity)dude.get()).getPortalCooldown();
                     ((Entity)dude.get()).travelToDimension(event.world.dimension());
                  }
               }
            }
         } else if (event.entity instanceof LivingEntity mob) {
            AttributeInstance championMobInstance = mob.getAttribute(EntityUtils.CHAMPION_MOD);
            if (championMobInstance == null) {return;}
         }
      }

   }


//   @SubscribeEvent
//   public void entityConstuct(EntityEvent.EntityConstructing event) {
//      if (event.entity instanceof EntityMob) {
//         EntityMob mob = (EntityMob)event.entity;
//         mob.getAttributeMap().registerAttribute(EntityUtils.CHAMPION_MOD).setBaseValue(-2.0F);
//      }
//
//   }

   @SubscribeEvent
   public void itemPickup(EntityItemPickupEvent event) {
      if (event.Player.getName().getString().startsWith("FakeThaumcraft")) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void livingDrops(LivingDropsEvent event) {
      boolean fakePlayerFlag = event.source.getEntity() != null && event.source.getEntity() instanceof FakePlayer;
      if (Platform.getEnvironment() != Env.CLIENT
              && event.recentlyHit
              && !fakePlayerFlag
              && event.entity instanceof EntityMob
              && !(event.entity instanceof EntityThaumcraftBoss)
              && ((EntityMob)event.entity).getAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue() >= (double)0.0F) {
         int i = 5 + event.entity.getRandom().nextInt(3);

         while(i > 0) {
            int j = EntityXPOrb.getXPSplit(i);
            i -= j;
            event.entity.level().spawnEntityInWorld(
                    new EntityXPOrb(event.entity.level(), event.entity.posX, event.entity.posY, event.entity.posZ, j
                    ));
         }

         int lb = Math.min(2, MathHelper.floor_float((float)(event.entity.getRandom().nextInt(9) + event.lootingLevel) / 5.0F));
         event.drops.add(
                 new EntityItem(event.entity.level(),
                         event.entityLiving.posX,
                         event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(),
                         event.entityLiving.posZ,
                         new ItemStack(ConfigItems.itemLootbag, 1, lb)));
      }

      if (event.entityLiving instanceof EntityZombie && !(event.entityLiving instanceof EntityBrainyZombie) && event.recentlyHit && event.entity.getRandom().nextInt(10) - event.lootingLevel < 1) {
         event.drops.add(new EntityItem(event.entity.level(), event.entityLiving.posX, event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(), event.entityLiving.posZ, new ItemStack(ConfigItems.itemZombieBrain)));
      }

      if (event.entityLiving instanceof EntityVillager && event.entity.getRandom().nextInt(10) - event.lootingLevel < 1) {
         event.drops.add(new EntityItem(event.entity.level(), event.entityLiving.posX, event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(), event.entityLiving.posZ, new ItemStack(ThaumcraftItems.GOLD_COIN)));
      }

      if (event.source == DamageSourceThaumcraft.dissolve) {
         AspectList aspects = ScanManager.generateEntityAspects(event.entityLiving);
         if (aspects != null && aspects.size() > 0) {
            for(Aspect aspect : aspects.getAspectTypes()) {
               if (!event.entity.getRandom().nextBoolean()) {
                  int size = 1 + event.entity.getRandom().nextInt(aspects.getAmount(aspect));
                  size = Math.max(1, size / 2);
                  ItemStack stack = new ItemStack(ConfigItems.itemCrystalEssence, size, 0);
                  ((ItemCrystalEssence)stack.getItem()).setAspects(stack, (new AspectList()).add(aspect, 1));
                  event.drops.add(new EntityItem(event.entity.level(), event.entityLiving.posX, event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(), event.entityLiving.posZ, stack));
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void livingTick(LivingDeathEvent event) {
      if (Platform.getEnvironment() != Env.CLIENT && !(event.entityLiving instanceof ITaintedMob) && event.entityLiving.isPotionActive(Config.potionTaintPoisonID)) {
         Entity entity = null;
         if (event.entityLiving instanceof EntityCreeper) {
            entity = new EntityTaintCreeper(event.entityLiving.level());
         } else if (event.entityLiving instanceof EntitySheep) {
            entity = new EntityTaintSheep(event.entityLiving.level());
         } else if (event.entityLiving instanceof EntityCow) {
            entity = new EntityTaintCow(event.entityLiving.level());
         } else if (event.entityLiving instanceof EntityPig) {
            entity = new EntityTaintPig(event.entityLiving.level());
         } else if (event.entityLiving instanceof EntityChicken) {
            entity = new EntityTaintChicken(event.entityLiving.level());
         } else if (event.entityLiving instanceof EntityVillager) {
            entity = new EntityTaintVillager(event.entityLiving.level());
         } else {
            entity = new EntityThaumicSlime(event.entityLiving.level());
            if (entity != null) {
               ((EntityThaumicSlime)entity).setSlimeSize((int)(1.0F + Math.min(event.entityLiving.getMaxHealth() / 10.0F, 6.0F)));
            }
         }

         if (entity != null) {
            entity.setLocationAndAngles(event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, event.entityLiving.rotationYaw, 0.0F);
            event.entityLiving.level().spawnEntityInWorld(entity);
            event.entityLiving.setDead();
         }
      } else if (Platform.getEnvironment() != Env.CLIENT && EntityUtils.getRecentlyHit(event.entityLiving) > 0) {
         AspectList aspectsCompound = ScanManager.generateEntityAspects(event.entityLiving);
         if (aspectsCompound != null && aspectsCompound.size() > 0) {
            AspectList aspects = ResearchManager.reduceToPrimals(aspectsCompound);

            for(Aspect aspect : aspects.getAspectTypes()) {
               if (event.entityLiving.getRandom().nextBoolean()) {
                  EntityAspectOrb orb = new EntityAspectOrb(event.entityLiving.level(), event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, aspect, 1 + event.entityLiving.getRandom().nextInt(aspects.getAmount(aspect)));
                  event.entityLiving.level().spawnEntityInWorld(orb);
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
               type = event.Player.inventory.mainInventory[j].getDamageValue();
               break;
            }
         }

         EntityPrimalArrow entityarrow = new EntityPrimalArrow(event.Player.level(), event.Player, f * dam, type);
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
         event.Player.level().playSoundAtEntity(event.Player, "random.bow", 1.0F, 1.0F / (event.Player.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
         boolean flag = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, event.bow) > 0 && event.Player.getRandom().nextFloat() < 0.33F;

          if (!event.Player.capabilities.isCreativeMode || !flag) {
            InventoryUtils.consumeInventoryItem(event.Player, ConfigItems.itemPrimalArrow, type);
         }

         if (Platform.getEnvironment() != Env.CLIENT) {
            event.Player.level().spawnEntityInWorld(entityarrow);
         }

         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   public void finishedUsingItem(PlayerUseItemEvent.Finish event) {
      if (Platform.getEnvironment() != Env.CLIENT && event.Player.isPotionActive(Config.potionUnHungerID)) {
         if (!event.item.isItemEqual(new ItemStack(Items.rotten_flesh))
                 && !event.item.isItemEqual(new ItemStack(ConfigItems.itemZombieBrain))
         ) {
            if (event.item.getItem() instanceof ItemFood) {
               event.Player.displayClientMessage(Component.literal("§4§o" + StatCollector.translateToLocal("warp.text.hunger.1")));
            }
         } else {
            MobEffectInstance pe = event.Player.getActiveMobEffectInstance(Potion.potionTypes[Config.potionUnHungerID]);
            int amp = pe.getAmplifier() - 1;
            int duration = pe.getDuration() - 600;
            event.Player.removeMobEffectInstance(Config.potionUnHungerID);
            if (duration > 0 && amp >= 0) {
               pe = new MobEffectInstance(Config.potionUnHungerID, duration, amp, true);
               pe.getCurativeItems().clear();
               pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
               event.Player.addEffect(pe);
            }

            event.Player.displayClientMessage(Component.literal("§2§o" + StatCollector.translateToLocal("warp.text.hunger.2")));
         }
      }

   }

   @SubscribeEvent
   public void itemExpire(ItemExpireEvent event) {
      if (event.entityItem.getEntityItem() != null
              && event.entityItem.getEntityItem().getItem() != null
              && event.entityItem.getEntityItem().getItem() instanceof ItemBathSalts
      ) {
         int x = MathHelper.floor_double(event.entityItem.posX);
         int y = MathHelper.floor_double(event.entityItem.posY);
         int z = MathHelper.floor_double(event.entityItem.posZ);
         if (event.entityItem.level().getBlock(x, y, z) == Blocks.water && event.entityItem.level().getBlockMetadata(x, y, z) == 0) {
            event.entityItem.level().setBlock(x, y, z, ConfigBlocks.blockFluidPure);
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
