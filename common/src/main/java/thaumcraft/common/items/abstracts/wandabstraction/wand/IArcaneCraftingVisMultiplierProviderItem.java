package thaumcraft.common.items.abstracts.wandabstraction.wand;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
//additional
public interface IArcaneCraftingVisMultiplierProviderItem {

    float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect);

}
