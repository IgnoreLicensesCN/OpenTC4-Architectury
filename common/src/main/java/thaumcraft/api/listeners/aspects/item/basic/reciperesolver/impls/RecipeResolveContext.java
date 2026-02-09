package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.UnmodifiableAspectList;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RecipeResolveContext {
    public final ResolvedAspectGetter resolvedAspect;
    public final ResolvedAspectAdder aspectAdder;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Set<Item> itemsResolvedLastTurn;

    public final Set<Item> itemsResolvedLastTurnView;
    private final Set<Item> itemsNewlyResolved;
    public final Set<Item> itemsNewlyResolvedView;

    public RecipeResolveContext(ResolvedAspectGetter resolvedAspect, ResolvedAspectAdder aspectAdder, Set<Item> itemsResolvedLastTurn) {
        this.resolvedAspect = resolvedAspect;
        this.aspectAdder = aspectAdder;
        this.itemsResolvedLastTurn = new HashSet<>(itemsResolvedLastTurn);
        this.itemsResolvedLastTurnView = Collections.unmodifiableSet(itemsResolvedLastTurn);
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
        return true;
    }
    public UnmodifiableAspectList<Aspect> getAlreadyCalculatedAspectForItem(Item item){
        return resolvedAspect.getAlreadyCalculatedAspectForItem(item);
    }
    public void addResolvedAspectForItem(Item item, UnmodifiableAspectList<Aspect> aspects){
        aspectAdder.addResolvedAspectForItem(item,aspects);
    };


    public interface ResolvedAspectGetter {
        @Nullable
        UnmodifiableAspectList<Aspect> getAlreadyCalculatedAspectForItem(Item item);
    }

    public interface ResolvedAspectAdder {
        void addResolvedAspectForItem(Item item, UnmodifiableAspectList<Aspect> aspects);
    }

}
