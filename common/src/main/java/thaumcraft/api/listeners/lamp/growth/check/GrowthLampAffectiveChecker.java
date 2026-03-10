package thaumcraft.api.listeners.lamp.growth.check;

import org.jetbrains.annotations.NotNull;

public abstract class GrowthLampAffectiveChecker implements Comparable<GrowthLampAffectiveChecker> {
    public final int priority;

    public GrowthLampAffectiveChecker(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull GrowthLampAffectiveChecker o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void test(GrowthLampAffectiveContext context);
}
