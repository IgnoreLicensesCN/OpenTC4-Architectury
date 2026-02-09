package thaumcraft.api.listeners.researchtable.listeners;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.researchtable.WriteAspectContext;

public abstract class WriteAspectAfterListener implements Comparable<WriteAspectAfterListener> {
    public final int weight;

    protected WriteAspectAfterListener(int weight) {
        this.weight = weight;
    }

    public abstract void onEventTriggered(WriteAspectContext context);

    @Override
    public int compareTo(@NotNull WriteAspectAfterListener o) {
        return Integer.compare(weight, o.weight);
    }
}
