package thaumcraft.api.listeners.manabean.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.manabean.ManaBeanGrowContext;

public abstract class ManaBeanGrowListener implements Comparable<ManaBeanGrowListener> {
    public final int weight;
    public ManaBeanGrowListener(int weight) {
        this.weight = weight;
    }
    @Override
    public int compareTo(@NotNull ManaBeanGrowListener o) {
        return Integer.compare(this.weight, o.weight);
    }
    public abstract void onGrowStageChanged(ManaBeanGrowContext context);
}
