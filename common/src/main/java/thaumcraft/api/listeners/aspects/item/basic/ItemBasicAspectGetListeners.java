package thaumcraft.api.listeners.aspects.item.basic;//sorry guys but i need it here to access package-private method

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetContext;
import thaumcraft.api.listeners.aspects.item.basic.getters.listeners.ItemBasicAspectGetListener;

import static thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration.getCalculatedRegisteredBasicAspect;

public enum ItemBasicAspectGetListeners {
    REGISTERED_BASIC(
            new ItemBasicAspectGetListener(0) {
                @Override
                public void getBasicAspects(ItemBasicAspectGetContext context) {
                    pickRegisteredBasic(context);
                }
            }
    ),
    CALCULATED(
            new ItemBasicAspectGetListener(1000) {
                @Override
                public void getBasicAspects(ItemBasicAspectGetContext context) {
                    pickCalculated(context);
                }
            }
    ),
    //TODO:[maybe wont finished](at least maybe wont finished in this mod)Mek recipes and more?
    ;


    public static void pickAndTryFinish(ItemBasicAspectGetContext context, UnmodifiableAspectList<Aspect> registeredResult) {
        context.pickNotEmptyMinimizedOneAsResult(registeredResult);
        if (!registeredResult.isEmpty()) {
            context.doFinishGetting = true;
        }
    }

    public static void pickRegisteredBasic(ItemBasicAspectGetContext context) {
        var registeredResult = getCalculatedRegisteredBasicAspect(context.itemToGet);
        pickAndTryFinish(context, registeredResult);
    }

    public static void pickCalculated(ItemBasicAspectGetContext context) {
        var fromRecipe = ItemBasicAspectCalculator.getBasicAspectsFromCalculated(context.itemToGet);
        pickAndTryFinish(context, fromRecipe);
    }

    public final ItemBasicAspectGetListener listener;

    ItemBasicAspectGetListeners(ItemBasicAspectGetListener listener) {
        this.listener = listener;
    }
}
