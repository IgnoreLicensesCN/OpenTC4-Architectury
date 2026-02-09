package thaumcraft.api.listeners.aspects.item.basic.reciperesolver;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.aspects.item.basic.interfaces.IRecipeAspectFinder;
import thaumcraft.api.listeners.aspects.item.basic.interfaces.IRecipesLoader;

public abstract class AbstractRecipeResolver implements IRecipesLoader, IRecipeAspectFinder,Comparable<AbstractRecipeResolver> {
    public final int weight;

    public AbstractRecipeResolver(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull AbstractRecipeResolver o) {
        return Integer.compare(this.weight, o.weight);
    }
}
