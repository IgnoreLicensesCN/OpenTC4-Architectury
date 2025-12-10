package thaumcraft.common.lib.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.Player;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.items.armor.ItemFortressArmor;
import thaumcraft.common.items.baubles.ItemAmuletRunic;
import thaumcraft.common.items.baubles.ItemGirdleRunic;
import thaumcraft.common.items.baubles.ItemRingRunic;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXShield;
import thaumcraft.common.lib.network.playerdata.PacketRunicCharge;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.HashMap;

import static simpleutils.bauble.BaubleUtils.forEachBauble;

public class EventHandlerRunic {
   public HashMap<Integer,Integer> runicCharge = new HashMap<>();
   private HashMap<Integer,Long> nextCycle = new HashMap<>();
   private HashMap<Integer,Integer> lastCharge = new HashMap<>();
   public HashMap<Integer,Integer[]> runicInfo = new HashMap<>();
   private HashMap<String,Long> upgradeCooldown = new HashMap<>();
   public boolean isDirty = true;
   private int rechargeDelay = 0;

   @SubscribeEvent
   public void livingTick(LivingEvent.LivingUpdateEvent event) {
      if (!event.entity.worldObj.isRemote && event.entity instanceof Player) {
         Player player = (Player)event.entity;
         if (this.isDirty || player.ticksExisted % 40 == 0) {
            final int[] max = {0};
            final int[] charged = {0};
            final int[] kinetic = {0};
            final int[] healing = {0};
            final int[] emergency = {0};
            this.isDirty = false;

            for(int a = 0; a < 4; ++a) {
               if (player.inventory.armorItemInSlot(a) != null && player.inventory.armorItemInSlot(a).getItem() instanceof IRunicArmor) {
                  int amount = getFinalCharge(player.inventory.armorItemInSlot(a));
                  max[0] += amount;
               }
            }

            forEachBauble(player,(a,stack,item) -> {
               if (item instanceof IRunicArmor){
                  IRunicArmor armor = (IRunicArmor)stack.getItem();
                  int amount = getFinalCharge(stack);
                  if (item instanceof ItemRingRunic) {
                     switch (stack.getItemDamage()) {
                        case 2:
                           ++charged[0];
                           break;
                        case 3:
                           ++healing[0];
                     }
                  } else if (item instanceof ItemAmuletRunic && stack.getItemDamage() == 1) {
                     ++emergency[0];
                  } else if (item instanceof ItemGirdleRunic && stack.getItemDamage() == 1) {
                     ++kinetic[0];
                  }

                  max[0] += amount;
               }
               return false;
            });

            if (max[0] > 0) {
               this.runicInfo.put(player.getEntityId(), new Integer[]{max[0], charged[0], kinetic[0], healing[0], emergency[0]});
               if (this.runicCharge.containsKey(player.getEntityId())) {
                  int charge = this.runicCharge.get(player.getEntityId());
                  if (charge > max[0]) {
                     this.runicCharge.put(player.getEntityId(), max[0]);
                     PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short) max[0], max[0]), (ServerPlayer)player);
                  }
               }
            } else {
               this.runicInfo.remove(player.getEntityId());
               this.runicCharge.put(player.getEntityId(), 0);
               PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short) 0, 0), (ServerPlayer)player);
            }
         }

         if (this.rechargeDelay > 0) {
            --this.rechargeDelay;
         } else if (this.runicInfo.containsKey(player.getEntityId())) {
            if (!this.lastCharge.containsKey(player.getEntityId())) {
               this.lastCharge.put(player.getEntityId(), -1);
            }

            if (!this.runicCharge.containsKey(player.getEntityId())) {
               this.runicCharge.put(player.getEntityId(), 0);
            }

            if (!this.nextCycle.containsKey(player.getEntityId())) {
               this.nextCycle.put(player.getEntityId(), 0L);
            }

            long time = System.currentTimeMillis();
            int charge = this.runicCharge.get(player.getEntityId());
            if (charge > ((Integer[])this.runicInfo.get(player.getEntityId()))[0]) {
               charge = ((Integer[])this.runicInfo.get(player.getEntityId()))[0];
            } else if (charge < ((Integer[])this.runicInfo.get(player.getEntityId()))[0] && this.nextCycle.get(player.getEntityId()) < time && WandManager.consumeVisFromInventory(player, (new AspectList()).add(Aspect.AIR, Config.shieldCost).add(Aspect.EARTH, Config.shieldCost))) {
               long interval = Config.shieldRecharge - ((Integer[])this.runicInfo.get(player.getEntityId()))[1] * 500;
               this.nextCycle.put(player.getEntityId(), time + interval);
               ++charge;
               this.runicCharge.put(player.getEntityId(), charge);
            }

            if (this.lastCharge.get(player.getEntityId()) != charge) {
               PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short)charge, ((Integer[])this.runicInfo.get(player.getEntityId()))[0]), (ServerPlayer)player);
               this.lastCharge.put(player.getEntityId(), charge);
            }
         }
      }

   }

   @SubscribeEvent
   public void entityHurt(LivingHurtEvent event) {
      if (event.source.getSourceOfDamage() != null && event.source.getSourceOfDamage() instanceof Player) {
         Player leecher = (Player)event.source.getSourceOfDamage();
         ItemStack helm = leecher.inventory.armorInventory[3];
         if (helm != null && helm.getItem() instanceof ItemFortressArmor && helm.hasTagCompound() && helm.stackTagCompound.hasKey("mask") && helm.stackTagCompound.getInteger("mask") == 2 && leecher.worldObj.rand.nextFloat() < event.ammount / 12.0F) {
            leecher.heal(1.0F);
         }
      }

      if (event.entity instanceof Player) {
         long time = System.currentTimeMillis();
         Player player = (Player)event.entity;
         if (event.source.getSourceOfDamage() != null && event.source.getSourceOfDamage() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase)event.source.getSourceOfDamage();
            ItemStack helm = player.inventory.armorInventory[3];
            if (helm != null && helm.getItem() instanceof ItemFortressArmor && helm.hasTagCompound() && helm.stackTagCompound.hasKey("mask") && helm.stackTagCompound.getInteger("mask") == 1 && player.worldObj.rand.nextFloat() < event.ammount / 10.0F) {
               try {
                  attacker.addPotionEffect(new PotionEffect(Potion.wither.getId(), 80));
               } catch (Exception ignored) {
               }
            }
         }

         if (event.source == DamageSource.drown || event.source == DamageSource.wither || event.source == DamageSource.outOfWorld || event.source == DamageSource.starve) {
            return;
         }

         if (this.runicInfo.containsKey(player.getEntityId()) && this.runicCharge.containsKey(player.getEntityId()) && this.runicCharge.get(player.getEntityId()) > 0) {
            int target = -1;
            if (event.source.getEntity() != null) {
               target = event.source.getEntity().getEntityId();
            }

            if (event.source == DamageSource.fall) {
               target = -2;
            }

            if (event.source == DamageSource.fallingBlock) {
               target = -3;
            }

            PacketHandler.INSTANCE.sendToAllAround(new PacketFXShield(event.entity.getEntityId(), target), new NetworkRegistry.TargetPoint(event.entity.worldObj.provider.dimensionId, event.entity.posX, event.entity.posY, event.entity.posZ, 64.0F));
            int charge = this.runicCharge.get(player.getEntityId());
            if ((float)charge > event.ammount) {
               charge = (int)((float)charge - event.ammount);
               event.ammount = 0.0F;
            } else {
               event.ammount -= (float)charge;
               charge = 0;
            }

            String key = player.getEntityId() + ":" + 2;
            if (charge <= 0 && ((Integer[])this.runicInfo.get(player.getEntityId()))[2] > 0 && (!this.upgradeCooldown.containsKey(key) || this.upgradeCooldown.get(key) < time)) {
               this.upgradeCooldown.put(key, time + 20000L);
               player.worldObj.newExplosion(player, player.posX, player.posY + (double)(player.height / 2.0F), player.posZ, 1.5F + (float)((Integer[])this.runicInfo.get(player.getEntityId()))[2] * 0.5F, false, false);
            }

            key = player.getEntityId() + ":" + 3;
            if (charge <= 0 && ((Integer[])this.runicInfo.get(player.getEntityId()))[3] > 0 && (!this.upgradeCooldown.containsKey(key) || this.upgradeCooldown.get(key) < time)) {
               this.upgradeCooldown.put(key, time + 20000L);
               synchronized(player) {
                  try {
                     player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 240, ((Integer[])this.runicInfo.get(player.getEntityId()))[3]));
                  } catch (Exception ignored) {
                  }
               }

               player.worldObj.playSoundAtEntity(player, "thaumcraft:runicShieldEffect", 1.0F, 1.0F);
            }

            key = player.getEntityId() + ":" + 4;
            if (charge <= 0 && ((Integer[])this.runicInfo.get(player.getEntityId()))[4] > 0 && (!this.upgradeCooldown.containsKey(key) || this.upgradeCooldown.get(key) < time)) {
               this.upgradeCooldown.put(key, time + 60000L);
               int t = 8 * ((Integer[])this.runicInfo.get(player.getEntityId()))[4];
               charge = Math.min(((Integer[])this.runicInfo.get(player.getEntityId()))[0], t);
               this.isDirty = true;
               player.worldObj.playSoundAtEntity(player, "thaumcraft:runicShieldCharge", 1.0F, 1.0F);
            }

            if (charge <= 0) {
               this.rechargeDelay = Config.shieldWait;
            }

            this.runicCharge.put(player.getEntityId(), charge);
            PacketHandler.INSTANCE.sendTo(new PacketRunicCharge(player, (short)charge, ((Integer[])this.runicInfo.get(player.getEntityId()))[0]), (ServerPlayer)player);
         }
      } else if (event.entity instanceof EntityMob && (((EntityMob)event.entity).getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue() >= (double)0.0F || event.entity instanceof IEldritchMob)) {
         EntityMob mob = (EntityMob)event.entity;
         int t = (int)((EntityMob)event.entity).getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue();
         if ((t == 5 || event.entity instanceof IEldritchMob) && mob.getAbsorptionAmount() > 0.0F) {
            int target = -1;
            if (event.source.getEntity() != null) {
               target = event.source.getEntity().getEntityId();
            }

            if (event.source == DamageSource.fall) {
               target = -2;
            }

            if (event.source == DamageSource.fallingBlock) {
               target = -3;
            }

            PacketHandler.INSTANCE.sendToAllAround(new PacketFXShield(mob.getEntityId(), target), new NetworkRegistry.TargetPoint(event.entity.worldObj.provider.dimensionId, event.entity.posX, event.entity.posY, event.entity.posZ, 32.0F));
            event.entity.worldObj.playSoundEffect(event.entity.posX, event.entity.posY, event.entity.posZ, "thaumcraft:runicShieldEffect", 0.66F, 1.1F + event.entity.worldObj.rand.nextFloat() * 0.1F);
         } else if (t >= 0 && ChampionModifier.mods[t].type == 2 && event.source.getSourceOfDamage() != null && event.source.getSourceOfDamage() instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase)event.source.getSourceOfDamage();
            event.ammount = ChampionModifier.mods[t].effect.performEffect(mob, attacker, event.source, event.ammount);
         }
      }

      if (event.ammount > 0.0F && event.source.getSourceOfDamage() != null && event.entity instanceof EntityLivingBase && event.source.getSourceOfDamage() instanceof EntityMob && ((EntityMob)event.source.getSourceOfDamage()).getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue() >= (double)0.0F) {
         EntityMob mob = (EntityMob)event.source.getSourceOfDamage();
         int t = (int)mob.getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue();
         if (ChampionModifier.mods[t].type == 1) {
            event.ammount = ChampionModifier.mods[t].effect.performEffect(mob, (EntityLivingBase)event.entity, event.source, event.ammount);
         }
      }

   }

   @SubscribeEvent
   public void tooltipEvent(ItemTooltipEvent event) {
      int charge = getFinalCharge(event.itemStack);
      if (charge > 0) {
         event.toolTip.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("item.runic.charge") + " +" + charge);
      }

      int warp = getFinalWarp(event.itemStack, event.Player);
      if (warp > 0) {
         event.toolTip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("item.warping") + " " + warp);
      }

   }

   public static int getFinalCharge(ItemStack stack) {
      if (!(stack.getItem() instanceof IRunicArmor)) {
         return 0;
      } else {
         IRunicArmor armor = (IRunicArmor)stack.getItem();
         int base = armor.getRunicCharge(stack);
         if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("RS.HARDEN")) {
            base += stack.stackTagCompound.getByte("RS.HARDEN");
         }

         return base;
      }
   }

   public static int getFinalWarp(ItemStack stack, Player player) {
      if (stack != null && stack.getItem() instanceof IWarpingGear) {
         IWarpingGear armor = (IWarpingGear)stack.getItem();
         return armor.getWarp(stack, player);
      } else {
         return 0;
      }
   }

   public static int getHardening(ItemStack stack) {
      if (!(stack.getItem() instanceof IRunicArmor)) {
         return 0;
      } else {
         int base = 0;
         if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("RS.HARDEN")) {
            base += stack.stackTagCompound.getByte("RS.HARDEN");
         }

         return base;
      }
   }
}
