package thaumcraft.common.lib.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import thaumcraft.common.items.equipment.armor.thaumaturge.ThaumostaticHarnessItem;

public class EnchantmentDowsing extends Enchantment {
    public EnchantmentDowsing() {
        super(Rarity.VERY_RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[] { EquipmentSlot.CHEST });
    }
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

    @Override
    public boolean canEnchant(ItemStack stack) {
        if (stack.getItem() instanceof DiggerItem) {
            return true;
        }

        if (stack.getItem() instanceof ThaumostaticHarnessItem) {
            return true;
        }//TODO:tag this

        return stack.isEnchantable();
    }


    public boolean isTradeable() {
        return false;
    }
    public boolean isDiscoverable() {
        return false;
    }
}
