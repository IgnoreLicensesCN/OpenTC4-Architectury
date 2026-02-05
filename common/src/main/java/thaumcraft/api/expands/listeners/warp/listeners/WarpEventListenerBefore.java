package thaumcraft.api.expands.listeners.warp.listeners;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.expands.listeners.warp.PickWarpEventContext;

import org.jetbrains.annotations.NotNull;

public abstract class WarpEventListenerBefore implements Comparable<WarpEventListenerBefore> {
    public final int priority;

    /**
     * @param priority can be any integer.listeners will be {@link java.util.Collections#sort} by priority
     */
    protected WarpEventListenerBefore(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull WarpEventListenerBefore o) {
        return Integer.compare(o.priority, priority);
    }

    /**
     * trigger after the event
     * @param e event triggered
     * @param player victim
     */
    public abstract void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull Player player);
}
