package thaumcraft.api.listeners.lamp.fertility.check;

import org.jetbrains.annotations.NotNull;

public abstract class FertilityLampAffectiveChecker implements Comparable<FertilityLampAffectiveChecker> {
    public final int priority;

    public FertilityLampAffectiveChecker(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull FertilityLampAffectiveChecker o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void test(FertilityLampAffectiveContext context);
}
