package thaumcraft.api.listeners.aspects.item.basic;//sorry guys but i need it here to access package-private method

import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetContext;
import thaumcraft.api.listeners.aspects.item.basic.getters.listeners.ItemBasicAspectGetListener;

import static thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration.getCalculatedRegisteredBasicAspect;

public enum ItemBasicAspectGetListeners {
    REGISTERED_BASIC(
            new ItemBasicAspectGetListener(0) {
                @Override
                public void getBasicAspects(ItemBasicAspectGetContext context) {
                    var registeredResult = getCalculatedRegisteredBasicAspect(context.itemToGet);
                    context.pickNotEmptyMinimizedOneAsResult(registeredResult);
                    if (!registeredResult.isEmpty()) {
                        context.doFinishGetting = true;
                    }
                }
            }
    ),
    VANILLA_RECIPE(
            new ItemBasicAspectGetListener(1000) {

                @Override
                public void getBasicAspects(ItemBasicAspectGetContext context) {
                    var fromRecipe = ItemBasicAspectCalculator.getBasicAspectsFromCalculated(context.itemToGet);
                    context.pickNotEmptyMinimizedOneAsResult(fromRecipe);
                    if (!fromRecipe.isEmpty()) {
                        context.doFinishGetting = true;
                    }
                }
            }
    ),
    //TODO:TC4 Recipes
    //TODO:[maybe wont finished](at least maybe wont finished in this mod)Mek recipes and more?
    ;

    public final ItemBasicAspectGetListener listener;
    ItemBasicAspectGetListeners(ItemBasicAspectGetListener listener) {
        this.listener = listener;
    }
}
