package thaumcraft.api.listeners.manabean.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.manabean.ManaBeanEatContext;
import thaumcraft.api.listeners.manabean.ManaBeanGrowContext;

public abstract class ManaBeanEatListener implements Comparable<ManaBeanEatListener> {
    public final int weight;
    public ManaBeanEatListener(int weight) {
        this.weight = weight;
    }
    @Override
    public int compareTo(@NotNull ManaBeanEatListener o) {
        return Integer.compare(this.weight, o.weight);
    }
    public abstract void onEatManaBean(ManaBeanEatContext context);
}
