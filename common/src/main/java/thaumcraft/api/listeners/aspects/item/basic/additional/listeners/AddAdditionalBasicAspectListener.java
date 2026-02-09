package thaumcraft.api.listeners.aspects.item.basic.additional.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.aspects.item.basic.additional.AddAdditionalBasicAspectContext;

public abstract class AddAdditionalBasicAspectListener implements Comparable<AddAdditionalBasicAspectListener>{
    public final int weight;

    public AddAdditionalBasicAspectListener(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull AddAdditionalBasicAspectListener o) {
        return Integer.compare(this.weight, o.weight);
    }


    public abstract void considerAddAdditional(AddAdditionalBasicAspectContext context);
}
