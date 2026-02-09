package thaumcraft.api.crafting.interfaces;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

//used to change real aspect cost like InfusionEnchantmentRecipe
public interface IInfusionAspectsModifiable {
    AspectList<Aspect> getAspectsModified(ItemStack recipeInput, AspectList<Aspect> basicCostAspects);
}
