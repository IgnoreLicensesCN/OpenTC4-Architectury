package thaumcraft.api.listeners.aspects.item.basic.interfaces;

import net.minecraft.world.item.Item;
import thaumcraft.api.listeners.aspects.item.basic.RecipeResolveContext;

import java.util.Collection;

public interface IRecipeAspectFinder {
    //return newlyResolved after execute this
    void resolveItems(RecipeResolveContext context);
}
