package thaumcraft.api.listeners.lamp.growth.apply;

import org.jetbrains.annotations.NotNull;

public abstract class GrowthLampGrowthAffectApplier implements Comparable<GrowthLampGrowthAffectApplier> {
    public final int priority;

    public GrowthLampGrowthAffectApplier(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull GrowthLampGrowthAffectApplier o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void apply(GrowthLampAffectContext context);
}
