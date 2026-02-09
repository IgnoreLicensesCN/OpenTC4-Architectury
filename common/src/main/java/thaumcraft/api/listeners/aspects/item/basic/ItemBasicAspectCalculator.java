package thaumcraft.api.listeners.aspects.item.basic;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;

import java.util.HashMap;
import java.util.Map;

import static thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration.ITEMS_WITH_REGISTERED_BASIC_ASPECTS;
import static thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration.getCalculatedRegisteredBasicAspect;

public class ItemBasicAspectCalculator {
    private static final Map<Item, UnmodifiableAspectList<Aspect>> ITEM_BASIC_ASPECTS_CALCULATED = new HashMap<>();
    public static final ListenerManager<AbstractRecipeResolver> recipeAspectResolvers = new ListenerManager<>();

    static {
        for (var resolverWrapper: RecipeAspectResolvers.values()) {
            recipeAspectResolvers.registerListener(resolverWrapper.resolver);
        }
    }

    private static final RecipeResolveContext.ResolvedAspectAdder adder = (item, aspects) -> {
        var current = ITEM_BASIC_ASPECTS_CALCULATED.getOrDefault(item,UnmodifiableAspectList.EMPTY);
        if (current.isEmpty()) {
            ITEM_BASIC_ASPECTS_CALCULATED.put(item,aspects);
        }
        else if (!aspects.isEmpty()) {
            if (current.visSize() > aspects.visSize()) {
                ITEM_BASIC_ASPECTS_CALCULATED.put(item,aspects);
            }
        }
    };
    private static final RecipeResolveContext.ResolvedAspectGetter getter = item -> {
        var fromRegistered = getCalculatedRegisteredBasicAspect(item);
        if (!fromRegistered.isEmpty()){
            return fromRegistered;
        }
        return ITEM_BASIC_ASPECTS_CALCULATED.get(item);
    };

    public static void onDatapackReload(){
        ItemBasicAspectRegistration.onDatapackReload();
        var recipeResolveContext = new RecipeResolveContext(getter,adder,ITEMS_WITH_REGISTERED_BASIC_ASPECTS);
        recipeAspectResolvers.getListeners().forEach(AbstractRecipeResolver::reloadRecipes);

        recipeAspectResolvers.getListeners().forEach(l -> l.resolveItems(recipeResolveContext));
        while (recipeResolveContext.prepareForNextRun()){
            recipeAspectResolvers.getListeners().forEach(l -> l.resolveItems(recipeResolveContext));
        }

    }

    @NotNull("null -> empty")
    static UnmodifiableAspectList<Aspect> getBasicAspectsFromCalculated(@NotNull Item item) {
        return ITEM_BASIC_ASPECTS_CALCULATED.getOrDefault(item,UnmodifiableAspectList.EMPTY);
    }
    public static void init() {

    }
}
