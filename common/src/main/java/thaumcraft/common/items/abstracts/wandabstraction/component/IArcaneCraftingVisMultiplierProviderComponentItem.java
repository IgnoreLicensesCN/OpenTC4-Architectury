package thaumcraft.common.items.abstracts.wandabstraction.component;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

//additional
public interface IArcaneCraftingVisMultiplierProviderComponentItem {

    float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect);

}
