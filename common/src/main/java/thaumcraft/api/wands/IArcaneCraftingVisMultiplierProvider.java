package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
//additional
public interface IArcaneCraftingVisMultiplierProvider {

    float getCraftingVisMultiplier(ItemStack usingWand, Aspect aspect);

}
