package thaumcraft.api.expands.listeners.warp.listeners;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.expands.listeners.warp.PickWarpEventContext;


public abstract class PickWarpEventListenerAfter implements Comparable<PickWarpEventListenerAfter> {

    public final int priority;

    /**
     * @param priority can be any integer.listeners will be {@link java.util.Collections#sort} by priority
     */
    public PickWarpEventListenerAfter(int priority) {
        this.priority = priority;
    }

    /**
     *
     * @param e the original event
     * @param player victim
     * @return event to replace with.Do not set null.
     */
    public abstract @NotNull WarpEvent afterPickEvent(@NotNull PickWarpEventContext context, @NotNull WarpEvent e, @NotNull Player player);

    @Override
    public int compareTo(@NotNull PickWarpEventListenerAfter o) {
        return Integer.compare(priority, o.priority);
    }
}
