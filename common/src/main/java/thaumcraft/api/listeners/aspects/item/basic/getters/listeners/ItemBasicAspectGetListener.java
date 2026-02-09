package thaumcraft.api.listeners.aspects.item.basic.getters.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetContext;

public abstract class ItemBasicAspectGetListener implements Comparable<ItemBasicAspectGetListener> {
    public final int weight;

    public ItemBasicAspectGetListener(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull ItemBasicAspectGetListener o) {
        return Integer.compare(this.weight, o.weight);
    }
    public abstract void getBasicAspects(ItemBasicAspectGetContext context);
}
