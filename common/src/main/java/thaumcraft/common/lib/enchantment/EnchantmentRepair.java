package thaumcraft.common.lib.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.IRepairable;

public class EnchantmentRepair extends Enchantment {

   public EnchantmentRepair() {
      super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, new EquipmentSlot[]{});
   }

   @Override
   public int getMinCost(int level) {
      return 20 + (level - 1) * 10;
   }

   @Override
   public int getMaxCost(int level) {
      return getMinCost(level) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 2;
   }

   @Override
   public boolean canEnchant(ItemStack stack) {
      // 可以作用于可损坏物品或者附魔书
      return stack.isDamageableItem() && (stack.getItem() instanceof IRepairable || stack.is(Items.ENCHANTED_BOOK));
   }

   @Override
   public boolean checkCompatibility(Enchantment other) {
      // 不能和Unbreaking共存
      return super.checkCompatibility(other) && other != Enchantments.UNBREAKING;
   }
//   public EnchantmentRepair(int par1, int par2) {
//      super(par1, par2, EnumEnchantmentType.all);
//      this.setName("repair");
//   }
//
//   public int getMinEnchantability(int par1) {
//      return 20 + (par1 - 1) * 10;
//   }
//
//   public int getMaxEnchantability(int par1) {
//      return super.getMinEnchantability(par1) + 50;
//   }
//
//   public int getMaxLevel() {
//      return 2;
//   }
//
//   public boolean canApply(ItemStack stack) {
//      return stack.isItemStackDamageable() && (stack.getItem() instanceof IRepairable || stack.getItem() instanceof ItemBook);
//   }
//
//   public boolean canApplyAtEnchantingTable(ItemStack stack) {
//      return this.canApply(stack);
//   }
//
//   public boolean canApplyTogether(Enchantment par1Enchantment) {
//      return super.canApplyTogether(par1Enchantment) && par1Enchantment.effectId != Enchantment.unbreaking.effectId;
//   }
}
