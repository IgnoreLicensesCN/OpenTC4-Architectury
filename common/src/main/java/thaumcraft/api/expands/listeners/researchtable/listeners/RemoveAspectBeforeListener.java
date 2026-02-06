package thaumcraft.api.expands.listeners.researchtable.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.expands.listeners.researchtable.RemoveAspectContext;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectContext;

public abstract class RemoveAspectBeforeListener implements Comparable<RemoveAspectBeforeListener> {
    public final int weight;

    protected RemoveAspectBeforeListener(int weight) {
        this.weight = weight;
    }

    public abstract void onEventTriggered(RemoveAspectContext context);

    @Override
    public int compareTo(@NotNull RemoveAspectBeforeListener o) {
        return Integer.compare(weight, o.weight);
    }
}
