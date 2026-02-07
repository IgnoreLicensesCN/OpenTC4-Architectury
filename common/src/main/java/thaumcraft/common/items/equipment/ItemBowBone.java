package thaumcraft.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemBow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import thaumcraft.api.IRepairEnchantable;
import thaumcraft.common.Thaumcraft;

public class ItemBowBone extends ItemBow implements IRepairEnchantable {
   public static final String[] bowPullIconNameArray = new String[]{"pulling_0", "pulling_1", "pulling_2"};
   @SideOnly(Side.CLIENT)
   private IIcon[] iconArray;

   public ItemBowBone() {
      this.maxStackSize = 1;
      this.setMaxDamage(512);
      this.setCreativeTab(Thaumcraft.tabTC);
   }

   public void onUsingTick(ItemStack stack, Player player, int count) {
      int ticks = this.getMaxItemUseDuration(stack) - count;
      if (ticks > 18) {
         player.stopUsingItem();
      }

   }

   public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, Player par3Player, int par4) {
      int j = this.getMaxItemUseDuration(par1ItemStack) - par4;
      ArrowLooseEvent event = new ArrowLooseEvent(par3Player, par1ItemStack, j);
      MinecraftForge.EVENT_BUS.post(event);
      if (!event.isCanceled()) {
         j = event.charge;
         boolean flag = par3Player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
         if (flag || par3Player.inventory.hasItem(Items.arrow)) {
            float f = (float)j / 10.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if ((double)f < 0.1) {
               return;
            }

            if (f > 1.0F) {
               f = 1.0F;
            }

            EntityArrow entityarrow = new EntityArrow(par2World, par3Player, f * 2.5F);
            entityarrow.setDamage(entityarrow.getDamage() + (double)0.5F);
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
            if (k > 0) {
               entityarrow.setDamage(entityarrow.getDamage() + (double)k * (double)0.5F + (double)0.5F);
            }

            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);
            if (l > 0) {
               entityarrow.setKnockbackStrength(l);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0) {
               entityarrow.setFire(100);
            }

            par1ItemStack.damageItem(1, par3Player);
            par2World.playSoundAtEntity(par3Player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            if (flag) {
               entityarrow.canBePickedUp = 2;
            } else {
               par3Player.inventory.consumeInventoryItem(Items.arrow);
            }

            if (!(Platform.getEnvironment() == Env.CLIENT)) {
               par2World.spawnEntityInWorld(entityarrow);
            }
         }

      }
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, Player par3Player) {
      ArrowNockEvent event = new ArrowNockEvent(par3Player, par1ItemStack);
      MinecraftForge.EVENT_BUS.post(event);
      if (event.isCanceled()) {
         return event.result;
      } else {
         if (par3Player.capabilities.isCreativeMode || par3Player.inventory.hasItem(Items.arrow) || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0) {
            par3Player.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
         }

         return par1ItemStack;
      }
   }

   public int getItemEnchantability() {
      return 3;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.itemIcon = par1IconRegister.registerIcon("thaumcraft:bonebow");
      this.iconArray = new IIcon[bowPullIconNameArray.length];

      for(int i = 0; i < this.iconArray.length; ++i) {
         this.iconArray[i] = par1IconRegister.registerIcon("thaumcraft:bonebow_" + bowPullIconNameArray[i]);
      }

   }

   public IIcon getIcon(ItemStack stack, int renderPass, Player player, ItemStack usingItem, int useRemaining) {
      int j = stack.getMaxItemUseDuration() - useRemaining;
      if (usingItem == null) {
         return this.itemIcon;
      } else if (j >= 13) {
         return this.getItemIconForUseDuration(2);
      } else if (j > 7) {
         return this.getItemIconForUseDuration(1);
      } else {
         return j > 0 ? this.getItemIconForUseDuration(0) : this.itemIcon;
      }
   }

   @SideOnly(Side.CLIENT)
   public IIcon getItemIconForUseDuration(int par1) {
      return this.iconArray[par1];
   }
}
