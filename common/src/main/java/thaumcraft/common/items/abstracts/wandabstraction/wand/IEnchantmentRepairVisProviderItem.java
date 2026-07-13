package thaumcraft.common.items.abstracts.wandabstraction.wand;


import net.minecraft.world.item.ItemStack;

public interface IEnchantmentRepairVisProviderItem {
    default boolean canProvideVisForRepair(ItemStack stack){return true;};

}
