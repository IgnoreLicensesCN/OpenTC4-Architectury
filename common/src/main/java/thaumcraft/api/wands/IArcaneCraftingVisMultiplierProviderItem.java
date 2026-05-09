package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

public interface IArcaneCraftingVisMultiplierProviderItem {

    float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect);

}
