package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;

import java.util.*;

public class RecipeResolveContext {
    public final ResolvedAspectGetter resolvedAspectGetter;
    public final ResolvedAspectAdder aspectAdder;

    private final Set<Item> itemsResolvedLastTurn;
    public final Set<Item> itemsResolvedLastTurnView;

    private final Set<Item> itemsNewlyResolved;
    public final Set<Item> itemsNewlyResolvedView;
    private final Map<Item, UnmodifiableAspectList<Aspect>> adderCache = new HashMap<>();

    public RecipeResolveContext(ResolvedAspectGetter resolvedAspectGetter, ResolvedAspectAdder aspectAdder, Set<Item> itemsResolvedLastTurn) {
        this.resolvedAspectGetter = resolvedAspectGetter;
        this.aspectAdder = aspectAdder;
        this.itemsResolvedLastTurn = new HashSet<>(itemsResolvedLastTurn);
        this.itemsResolvedLastTurnView = Collections.unmodifiableSet(this.itemsResolvedLastTurn);
        this.itemsNewlyResolved = new HashSet<>();
        this.itemsNewlyResolvedView = Collections.unmodifiableSet(this.itemsNewlyResolved);
    }

    public void addResolvedItem(Item item) {
        itemsNewlyResolved.add(item);
    }

    boolean prepareForNextRun(){
        if (itemsNewlyResolved.isEmpty()) {
            return false;
        }
        itemsResolvedLastTurn.clear();
        itemsResolvedLastTurn.addAll(itemsNewlyResolved);
        itemsNewlyResolved.clear();

        adderCache.forEach(aspectAdder::addResolvedAspectForItem);
        adderCache.clear();
        return true;
    }
    public @Nullable UnmodifiableAspectList<Aspect> getAlreadyCalculatedAspectForItem(Item item){
        return resolvedAspectGetter.getAlreadyCalculatedAspectForItem(item);
    }
    public void addResolvedAspectForItem(Item item, UnmodifiableAspectList<Aspect> aspects){
        var current = adderCache.getOrDefault(item,UnmodifiableAspectList.EMPTY);
        if (current.isEmpty()) {
            adderCache.put(item,aspects);
        }
        else if (!aspects.isEmpty()) {
            if (current.visSize() > aspects.visSize()) {
                adderCache.put(item,aspects);
            }
        }
    };


    public interface ResolvedAspectGetter {
        @Nullable
        UnmodifiableAspectList<Aspect> getAlreadyCalculatedAspectForItem(Item item);
    }

    public interface ResolvedAspectAdder {
        void addResolvedAspectForItem(Item item, UnmodifiableAspectList<Aspect> aspects);
    }

}
