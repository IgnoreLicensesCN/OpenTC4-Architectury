package thaumcraft.common.items.abstracts.harness;

import net.minecraft.world.item.ItemStack;

public interface IHarnessFuelDurationMultiplier {//TODO:[maybe wont finished] use Attribute to make this part
    float getHarnessFuelDurationMultiplier(ItemStack stack);
}
