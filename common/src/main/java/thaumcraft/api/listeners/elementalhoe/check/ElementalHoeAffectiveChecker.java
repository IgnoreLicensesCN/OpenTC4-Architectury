package thaumcraft.api.listeners.elementalhoe.check;

import org.jetbrains.annotations.NotNull;

public abstract class ElementalHoeAffectiveChecker implements Comparable<ElementalHoeAffectiveChecker> {
    public final int priority;

    public ElementalHoeAffectiveChecker(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull ElementalHoeAffectiveChecker o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void test(ElementalHoeAffectiveContext context);
}
