package thaumcraft.api.listeners.aspects.item.basic.reciperesolver;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.RecipeResolveContext;

public abstract class CalculateStage implements Comparable<CalculateStage> {
    public final int weight;

    public CalculateStage(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull CalculateStage o) {
        return Integer.compare(this.weight, o.weight);
    }

    public abstract void startStage(RecipeResolveContext context);
}
