package thaumcraft.common.entities.golems;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IItemStackCollapsibleGolem {
    //remember to #discard!
    ItemStack collapseToItemStackWithoutSneaking();
    ItemStack collapseToItemStackWithSneaking();
    boolean canBeCollapsedBy(LivingEntity living);
    ItemStack getCollapseToBasicStack();
}
