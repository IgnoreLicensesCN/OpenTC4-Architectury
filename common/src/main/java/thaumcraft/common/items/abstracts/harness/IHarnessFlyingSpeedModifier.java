package thaumcraft.common.items.abstracts.harness;

import net.minecraft.world.item.ItemStack;

public interface IHarnessFlyingSpeedModifier {
    //will add to total multiplier
    float getHarnessSpeedMultiplier(ItemStack stack);
}
