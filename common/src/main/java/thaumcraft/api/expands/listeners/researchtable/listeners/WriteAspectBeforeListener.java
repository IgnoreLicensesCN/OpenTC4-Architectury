package thaumcraft.api.expands.listeners.researchtable.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.expands.listeners.researchtable.WriteAspectContext;

public abstract class WriteAspectBeforeListener implements Comparable<WriteAspectBeforeListener> {
    public final int weight;

    protected WriteAspectBeforeListener(int weight) {
        this.weight = weight;
    }

    public abstract void onEventTriggered(WriteAspectContext context);

    @Override
    public int compareTo(@NotNull WriteAspectBeforeListener o) {
        return Integer.compare(weight, o.weight);
    }
}
