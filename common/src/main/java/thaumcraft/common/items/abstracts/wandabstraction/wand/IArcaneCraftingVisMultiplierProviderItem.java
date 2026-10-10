package thaumcraft.common.items.abstracts.wandabstraction.wand;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
//additional
public interface IArcaneCraftingVisMultiplierProviderItem {

    //"consumption cost decrease percent" in fact
    float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect);

}
