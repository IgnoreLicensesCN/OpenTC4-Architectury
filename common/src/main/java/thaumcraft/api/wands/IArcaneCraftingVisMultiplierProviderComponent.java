package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

public interface IArcaneCraftingVisMultiplierProviderComponent {

    float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect);

}
