package thaumcraft.api.listeners.researchtable.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.researchtable.RemoveAspectContext;

public abstract class RemoveAspectAfterListener implements Comparable<RemoveAspectAfterListener> {
    public final int weight;

    protected RemoveAspectAfterListener(int weight) {
        this.weight = weight;
    }

    public abstract void onEventTriggered(RemoveAspectContext context);

    @Override
    public int compareTo(@NotNull RemoveAspectAfterListener o) {
        return Integer.compare(weight, o.weight);
    }
}
