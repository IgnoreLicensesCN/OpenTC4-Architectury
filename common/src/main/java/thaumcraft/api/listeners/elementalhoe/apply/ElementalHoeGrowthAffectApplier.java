package thaumcraft.api.listeners.elementalhoe.apply;

import org.jetbrains.annotations.NotNull;

public abstract class ElementalHoeGrowthAffectApplier implements Comparable<ElementalHoeGrowthAffectApplier> {
    public final int priority;

    public ElementalHoeGrowthAffectApplier(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull ElementalHoeGrowthAffectApplier o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void apply(ElementalHoeAffectContext context);
}
