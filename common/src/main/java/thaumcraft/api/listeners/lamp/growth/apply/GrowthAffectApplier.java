package thaumcraft.api.listeners.lamp.growth.apply;

import org.jetbrains.annotations.NotNull;

public abstract class GrowthAffectApplier implements Comparable<GrowthAffectApplier> {
    public final int priority;

    public GrowthAffectApplier(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull GrowthAffectApplier o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void apply(GrowthLampAffectContext context);
}
