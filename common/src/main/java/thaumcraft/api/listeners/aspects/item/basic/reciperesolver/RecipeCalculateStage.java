package thaumcraft.api.listeners.aspects.item.basic.reciperesolver;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage.RecipeResolveContext;

//maybe it's not needed but i want to calculate faster.
//for example,if we calculate stone cutter first we can tryReduce lots of recipes in crafting table
public abstract class RecipeCalculateStage implements Comparable<RecipeCalculateStage> {
    public final int weight;

    public RecipeCalculateStage(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull RecipeCalculateStage o) {
        return Integer.compare(this.weight, o.weight);
    }

    public abstract void startStage(RecipeResolveContext context);
}
