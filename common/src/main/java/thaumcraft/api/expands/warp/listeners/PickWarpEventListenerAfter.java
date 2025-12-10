package thaumcraft.api.expands.warp.listeners;

import net.minecraft.entity.player.Player;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpEvent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
    public abstract @Nonnull WarpEvent afterPickEvent(PickWarpEventContext context, WarpEvent e, Player player);

    @Override
    public int compareTo(@Nonnull PickWarpEventListenerAfter o) {
        return Integer.compare(priority, o.priority);
    }
}
