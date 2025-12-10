package thaumcraft.common.lib.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.items.armor.ItemHoverHarness;

import java.util.Objects;

public class EnchantmentHaste extends Enchantment {
   public EnchantmentHaste() {
      super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[] { EquipmentSlot.CHEST });
   }
//   public EnchantmentHaste(int par1, int par2) {
//      super(par1, par2, EnumEnchantmentType.armor);
//      this.setName("haste");
//   }

   @Override
   public int getMinCost(int level) {
      return 15 + (level - 1) * 9;
   }

   @Override
   public int getMaxCost(int level) {
      return getMinCost(level) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }
//   public int getMinEnchantability(int par1) {
//      return 15 + (par1 - 1) * 9;
//   }
//
//   public int getMaxEnchantability(int par1) {
//      return super.getMinEnchantability(par1) + 50;
//   }

//   public int getMaxLevel() {
//      return 3;
//   }

//   public boolean canApply(ItemStack is) {
//      return is != null && (is.getItem() instanceof ItemArmor && (((ItemArmor)is.getItem()).armorType == 3 || is.getItem() instanceof ItemHoverHarness) || is.getItem() instanceof ItemBook);
//   }
//
//   public boolean canApplyAtEnchantingTable(ItemStack stack) {
//      return this.canApply(stack);
//   }
   @Override
   public boolean canEnchant(ItemStack stack) {
   // 允许胸甲
      if (stack.getItem() instanceof ArmorItem armor && Objects.equals(armor.getEquipmentSlot(),EquipmentSlot.CHEST)) {
         return true;
      }

      // 允许 Hover Harness（你自己的物品）
      if (stack.getItem() instanceof ItemHoverHarness) {
         return true;
      }

      // 允许书
      return stack.isEnchantable();
   }
}
