package thaumcraft.common.items.abstracts.harness;

import net.minecraft.world.item.ItemStack;

public interface IHarnessFlyingSpeedModifier {//TODO:[maybe wont finished] use Attribute to make this part
    //will add to total multiplier
    float getHarnessSpeedMultiplier(ItemStack stack);
}
