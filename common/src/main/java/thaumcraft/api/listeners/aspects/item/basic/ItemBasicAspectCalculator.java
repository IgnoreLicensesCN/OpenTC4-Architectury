package thaumcraft.api.listeners.aspects.item.basic;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.RecipeCalculateStage;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage.RecipeResolveContext;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage.SimpleCalculateStageImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration.ITEMS_WITH_REGISTERED_BASIC_ASPECTS;
import static thaumcraft.api.listeners.aspects.item.basic.ItemBasicAspectRegistration.getCalculatedRegisteredBasicAspect;

public class ItemBasicAspectCalculator {
    private static final Map<Item, UnmodifiableAspectList<Aspect>> ITEM_BASIC_ASPECTS_CALCULATED = new HashMap<>();

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

    public static final ListenerManager<AbstractRecipeResolver> STONE_CUTTER_RESOLVERS = new ListenerManager<>();
    public static final ListenerManager<AbstractRecipeResolver> VANILLA_CRAFTING_RESOLVER = new ListenerManager<>();
    public static final ListenerManager<AbstractRecipeResolver> VANILLA_SMITHING_RESOLVER = new ListenerManager<>();
    public static final ListenerManager<AbstractRecipeResolver> ALL_RESOLVERS = new ListenerManager<>();
    static {
        STONE_CUTTER_RESOLVERS.registerListener(RecipeAspectResolvers.VANILLA_STONE_CUTTER.resolver);
        VANILLA_SMITHING_RESOLVER.registerListener(RecipeAspectResolvers.VANILLA_SMITHING.resolver);
        VANILLA_CRAFTING_RESOLVER.registerListener(RecipeAspectResolvers.VANILLA_CRAFTING.resolver);
        //TODO:TC4's own
        for (var vanillaResolver:RecipeAspectResolvers.values()) {
            ALL_RESOLVERS.registerListener(vanillaResolver.resolver);
        }
    }
    public static final ListenerManager<RecipeCalculateStage> calculateStages = new ListenerManager<>();

    public static final RecipeCalculateStage STAGE_STONE_CUTTER = new SimpleCalculateStageImpl(
            100, STONE_CUTTER_RESOLVERS
    );
    public static final RecipeCalculateStage STAGE_VANILLA_SMITHING = new SimpleCalculateStageImpl(
            200, VANILLA_SMITHING_RESOLVER
    );
    public static final RecipeCalculateStage STAGE_VANILLA_CRAFTING = new SimpleCalculateStageImpl(
            300, VANILLA_CRAFTING_RESOLVER
    );
    public static final RecipeCalculateStage STAGE_ALL_RESOLVERS = new SimpleCalculateStageImpl(
            1000, ALL_RESOLVERS
    );

    static {
        calculateStages.registerListener(STAGE_STONE_CUTTER);
        calculateStages.registerListener(STAGE_VANILLA_SMITHING);
        calculateStages.registerListener(STAGE_VANILLA_CRAFTING);

        calculateStages.registerListener(STAGE_ALL_RESOLVERS);
    }


    public static void onDatapackReload(){
        ItemBasicAspectRegistration.onDatapackReload();
        var context = new RecipeResolveContext(getter,adder,ITEMS_WITH_REGISTERED_BASIC_ASPECTS);
        for (RecipeCalculateStage listener : calculateStages.getListeners()) {
            listener.startStage(context);
            Set<Item> calculatedNow = new HashSet<>(ITEM_BASIC_ASPECTS_CALCULATED.size());
            ITEM_BASIC_ASPECTS_CALCULATED.forEach(
                    (item,aspects) -> {
                        if (!aspects.isEmpty()) {
                            calculatedNow.add(item);
                        }
                    }
            );
            context = new RecipeResolveContext(getter,adder,calculatedNow);
        }
    }

    @NotNull("null -> empty")
    static UnmodifiableAspectList<Aspect> getBasicAspectsFromCalculated(@NotNull Item item) {
        return ITEM_BASIC_ASPECTS_CALCULATED.getOrDefault(item,UnmodifiableAspectList.EMPTY);
    }
    public static void init() {

    }
}
