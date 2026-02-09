package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls;

import com.linearity.opentc4.simpleutils.ListenerManager;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.CalculateStage;

public class SimpleCalculateStageImpl extends CalculateStage {

    private final ListenerManager<AbstractRecipeResolver> recipeAspectResolvers;

    public SimpleCalculateStageImpl(
            int weight,
            ListenerManager<AbstractRecipeResolver> recipeAspectResolvers
    ) {
        super(weight);
        this.recipeAspectResolvers = recipeAspectResolvers;
    }

    @Override
    public void startStage(RecipeResolveContext recipeResolveContext) {
        recipeAspectResolvers.getListeners().forEach(AbstractRecipeResolver::reloadRecipes);
        do {
            recipeAspectResolvers.getListeners()
                    .forEach(l -> l.resolveItems(recipeResolveContext));
        } while (recipeResolveContext.prepareForNextRun());
    }
}
