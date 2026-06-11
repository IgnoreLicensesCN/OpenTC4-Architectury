package thaumcraft.common.items.abstracts;


import net.minecraft.world.item.ItemStack;

public interface IInfinityEnchantmentChanceCostArrowItem {
    default float getCostChance(ItemStack stack){
        return 0.6666667f;
    };
}
