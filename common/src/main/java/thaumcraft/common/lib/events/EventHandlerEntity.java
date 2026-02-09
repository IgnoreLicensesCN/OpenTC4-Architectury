package thaumcraft.common.lib.events;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.api.IRepairable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.api.wands.IEnchantmentRepairVisProvider;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntityAspectOrb;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.monster.*;
import thaumcraft.common.entities.monster.boss.EntityThaumcraftBoss;
import thaumcraft.common.entities.projectile.EntityPrimalArrow;
import thaumcraft.common.items.misc.ItemBathSalts;
import thaumcraft.common.items.misc.ItemCrystalEssence;
import thaumcraft.common.items.armor.Hover;
import thaumcraft.common.items.armor.ItemHoverHarness;
import thaumcraft.common.items.equipment.ItemBowBone;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.WarpEvents;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.effects.effectshaderhandlers.BlurredVisionShaderHandler;
import thaumcraft.common.lib.effects.effectshaderhandlers.DeathGazeShaderHandler;
import thaumcraft.common.lib.effects.effectshaderhandlers.SunScornedShaderHandler;
import thaumcraft.common.lib.effects.effectshaderhandlers.UnnaturalHungerShaderHandler;
import thaumcraft.common.lib.enchantment.ThaumcraftEnchantments;
import thaumcraft.common.lib.network.gamedata.PacketSyncItemAspectsS2C;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.linearity.opentc4.OpenTC4.LOGGER;
import static thaumcraft.api.listeners.warp.WarpEventManager.getWarpEventDelayForPlayer;


//TODO
public class EventHandlerEntity {
   public static HashMap<String,Float> prevStep = new HashMap<>();
   public static HashMap<String,ArrayList<WeakReference<Entity>>> linkedEntities = new HashMap<>();

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
            new PacketSyncItemAspectsS2C().sendTo(serverPlayer);
            File thaumcraftPlayerDir = getThaumcraftPlayersDirectory(serverPlayer.server);
             Thaumcraft.playerKnowledge.wipePlayerKnowledge(serverPlayer.getGameProfile().getName());
            File playerThaumFile = getPlayerFile("thaum", thaumcraftPlayerDir, serverPlayer.getGameProfile().getName());
            boolean legacy = false;
            if (!playerThaumFile.exists()) {
               try {
                  playerThaumFile.createNewFile();
               }catch (Exception e) {
                  LOGGER.error(e);
                  throw new RuntimeException(e);
               }
            }

            ResearchManager.loadPlayerData(serverPlayer.getGameProfile().getName(), playerThaumFile, getPlayerFile("thaumback", thaumcraftPlayerDir, serverPlayer.getGameProfile().getName()), legacy);

            //since research unlock check migrated to ResearchItem,this is not needed
//            for(ResearchCategory cat : ResearchCategory.researchCategories.values()) {
//               for(ResearchItem ri : cat.researches.values()) {
//                  if (ri.isAutoUnlock()) {
//                     Thaumcraft.researchManager.completeResearch(serverPlayer, ri.key);
//                  }
//               }
//            }
         });
         PlayerEvent.PLAYER_QUIT.register(serverPlayer -> {
            File thaumcraftPlayerDir = getThaumcraftPlayersDirectory(serverPlayer.server);
            ResearchManager.savePlayerData(serverPlayer.getGameProfile().getName(),
                    getPlayerFile("thaum", thaumcraftPlayerDir, serverPlayer.getGameProfile().getName()),
                    getPlayerFile("thaumback", thaumcraftPlayerDir, serverPlayer.getGameProfile().getName()));
         });

         ClientTickEvent.CLIENT_POST.register(mc -> {
            var player = mc.player;
            if (player == null){return;}
            DeathGazeShaderHandler.INSTANCE.tick(player);
            BlurredVisionShaderHandler.INSTANCE.tick(player);
            SunScornedShaderHandler.INSTANCE.tick(player);
            UnnaturalHungerShaderHandler.INSTANCE.tick(player);
         });

         TickEvent.PLAYER_PRE.register(player -> {
            var world = player.level();
            var playerName = player.getGameProfile().getName();
            boolean hoverFlag = Hover.getHover(playerName);
            if (world.dimension() == Config.dimensionOuter
                    && !player.isCreative()
                    && player.tickCount % 20 == 0
                    && (player.getAbilities().flying || hoverFlag)
            ) {
               player.getAbilities().flying = false;
               player.onUpdateAbilities();
               Hover.setHover(playerName, false);
               player.sendSystemMessage(Component.literal("tc.break.fly").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            }
            ItemStack chestArmor = player.getItemBySlot(EquipmentSlot.CHEST);
            if (hoverFlag && !(chestArmor.getItem() instanceof ItemHoverHarness)
            ) {
               Hover.setHover(playerName, false);
               player.getAbilities().flying = false;
               player.onUpdateAbilities();
            }

            if (player instanceof ServerPlayer serverPlayer) {
               if (!Config.wuss && player.tickCount > 0 && player.tickCount % getWarpEventDelayForPlayer(serverPlayer) == 0) {
                  WarpEvents.checkWarpEvent(serverPlayer);
               }
               //migrated to DeathGazeEffect#applyEffectTick
//               if (player.tickCount % 10 == 0 && player.hasEffect(
//                       Config.potionDeathGaze
//               )) {
//                  WarpEvents.checkDeathGaze(serverPlayer);
//               }

               if (serverPlayer.tickCount % 40 == 0) {
                  int a = 0;
                  Consumer<ItemStack> repairItemStack = stack -> {
                     if (stack.getDamageValue() > 0 && (stack.getItem() instanceof IRepairable || EnchantmentHelper.getItemEnchantmentLevel(ThaumcraftEnchantments.REPAIR,stack) > 0) && !serverPlayer.isCreative()) {
                        doRepair(stack, serverPlayer);
                     }
                  };
                  serverPlayer.getInventory().items.forEach(repairItemStack);
                  serverPlayer.getInventory().offhand.forEach(repairItemStack);
                  serverPlayer.getInventory().armor.forEach(repairItemStack);
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

   public static final Function<ItemStack,Boolean> checkIfCanConsumeForRepair = itemStack -> (itemStack.getItem() instanceof IEnchantmentRepairVisProvider provider) && provider.canProvideVisForRepair(itemStack);
   public static void doRepair(ItemStack is, ServerPlayer player) {

      int level = EnchantmentHelper.getEnchantments(is).getOrDefault(ThaumcraftEnchantments.REPAIR,0);
      var item = is.getItem();
      if (item instanceof IRepairable repairable){
         repairable.doRepair(is,player,level);
         return;
      }
      if (level > 0) {
         AspectList<Aspect>cost = ThaumcraftCraftingManager.getObjectTags(is);
         if (cost != null && !cost.isEmpty()) {
            cost = ResearchManager.reduceToPrimalsAndCast(cost);
            CentiVisList<Aspect> finalCost = new CentiVisList<>();

            for(Aspect a : cost.getAspectTypes()) {
               if (a != null) {
                  finalCost.mergeWithHighest(a, (int)Math.sqrt(cost.getAmount(a) * 2) * level);
               }
            }
            boolean doRepair = WandManager.consumeCentiVisFromInventory(player, finalCost, checkIfCanConsumeForRepair);
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
         if (!player.getAbilities().flying
                 && player.inventory.armorItemInSlot(0) != null
                 && player.moveForward > 0.0F) {
            int haste = EnchantmentHelper.getEnchantmentLevel(ThaumcraftEnchantments.HASTE.effectId, player.inventory.armorItemInSlot(0));
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
              && !((EntityGolemBase)event.target).getOwnerName().equals(event.Player.getGameProfile().getName())
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
                           ((Player)((EntityEnderPearl)event.entity).getThrower()).displayClientMessage(Component.literal("§5§oThe magic ofAspectVisList a nearby warded object destroys the ender pearl."));
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
      if (event.Player.getGameProfile().getName().startsWith("FakeThaumcraft")) {
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
         AspectList<Aspect>aspects = ScanManager.generateEntityAspects(event.entityLiving);
         if (aspects != null && aspects.size() > 0) {
            for(Aspect aspect : aspects.getAspectTypes()) {
               if (!event.entity.getRandom().nextBoolean()) {
                  int size = 1 + event.entity.getRandom().nextInt(aspects.getAmount(aspect));
                  size = Math.max(1, size / 2);
                  ItemStack stack = new ItemStack(ConfigItems.itemCrystalEssence, size, 0);
                  ((ItemCrystalEssence)stack.getItem()).setAspects(stack, (new AspectList<>()).addAll(aspect, 1));
                  event.drops.add(new EntityItem(event.entity.level(), event.entityLiving.posX, event.entityLiving.posY + (double)event.entityLiving.getEyeHeight(), event.entityLiving.posZ, stack));
               }
            }
         }
      }

   }

   @SubscribeEvent
   public void livingTick(LivingDeathEvent event) {
      if (Platform.getEnvironment() != Env.CLIENT && !(event.entityLiving instanceof ITaintedMob) && event.entityLiving.isPotionActive(ThaumcraftEffects.FLUX_TAINT)) {
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
         AspectList<Aspect>aspectsCompound = ScanManager.generateEntityAspects(event.entityLiving);
         if (aspectsCompound != null && !aspectsCompound.isEmpty()) {
            AspectList<Aspect>aspects = ResearchManager.reduceToPrimalsAndCast(aspectsCompound);

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

//   @SubscribeEvent
//   public void finishedUsingItem(PlayerUseItemEvent.Finish event) {
//      if (Platform.getEnvironment() != Env.CLIENT && event.Player.isPotionActive(ThaumcraftEffects.UNNATURAL_HUNGER)) {
//         if (!event.item.isItemEqual(new ItemStack(Items.rotten_flesh))
//                 && !event.item.isItemEqual(new ItemStack(ConfigItems.itemZombieBrain))
//         ) {
//            if (event.item.getItem() instanceof ItemFood) {
//               event.Player.displayClientMessage(Component.literal("§4§o" + StatCollector.translateToLocal("warp.text.hunger.1")));
//            }
//         } else {
//            MobEffectInstance pe = event.Player.getActiveMobEffectInstance(ThaumcraftEffects.UNNATURAL_HUNGER);
//            int amp = pe.getAmplifier() - 1;
//            int duration = pe.getDuration() - 600;
//            event.Player.removeMobEffectInstance(ThaumcraftEffects.UNNATURAL_HUNGER);
//            if (duration > 0 && amp >= 0) {
//               pe = new MobEffectInstance(ThaumcraftEffects.UNNATURAL_HUNGER, duration, amp, true,true);
//               pe.getCurativeItems().clear();
//               pe.addCurativeItem(new ItemStack(Items.rotten_flesh));
//               event.Player.addEffect(pe);
//            }
//
//            event.Player.displayClientMessage(Component.literal("§2§o" + StatCollector.translateToLocal("warp.text.hunger.2")));
//         }
//      }
//
//   }

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
