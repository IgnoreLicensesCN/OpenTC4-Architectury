package thaumcraft.api.listeners.researchtable.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.researchtable.RemoveAspectContext;

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
