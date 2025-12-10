package thaumcraft.common.lib.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import thaumcraft.common.items.armor.ItemHoverHarness;

public class EnchantmentHaste extends Enchantment {
   public EnchantmentHaste(int par1, int par2) {
      super(par1, par2, EnumEnchantmentType.armor);
      this.setName("haste");
   }

   public int getMinEnchantability(int par1) {
      return 15 + (par1 - 1) * 9;
   }

   public int getMaxEnchantability(int par1) {
      return super.getMinEnchantability(par1) + 50;
   }

   public int getMaxLevel() {
      return 3;
   }

   public boolean canApply(ItemStack is) {
      return is != null && (is.getItem() instanceof ItemArmor && (((ItemArmor)is.getItem()).armorType == 3 || is.getItem() instanceof ItemHoverHarness) || is.getItem() instanceof ItemBook);
   }

   public boolean canApplyAtEnchantingTable(ItemStack stack) {
      return this.canApply(stack);
   }
}
