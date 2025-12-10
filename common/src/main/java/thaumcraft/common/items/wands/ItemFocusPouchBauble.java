package thaumcraft.common.items.wands;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemFocusPouchBauble extends ItemFocusPouch implements IBauble {
   public BaubleType getBaubleType(ItemStack itemstack) {
      return BaubleType.BELT;
   }

   public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
   }

   public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }
}
