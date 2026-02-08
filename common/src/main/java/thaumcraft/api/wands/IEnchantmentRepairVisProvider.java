package thaumcraft.api.wands;


import net.minecraft.world.item.ItemStack;

public interface IEnchantmentRepairVisProvider {
    default boolean canProvideVisForRepair(ItemStack stack){return true;};

}
