package thaumcraft.api.expands.warp.listeners;

import net.minecraft.entity.player.Player;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpEvent;

import javax.annotation.Nonnull;

public abstract class WarpEventListenerBefore implements Comparable<WarpEventListenerBefore> {
    public final int priority;

    /**
     * @param priority can be any integer.listeners will be {@link java.util.Collections#sort} by priority
     */
    protected WarpEventListenerBefore(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@Nonnull WarpEventListenerBefore o) {
        return Integer.compare(o.priority, priority);
    }

    /**
     * trigger after the event
     * @param e event triggered
     * @param player victim
     */
    public abstract void onWarpEvent(@Nonnull PickWarpEventContext warpContext, @Nonnull WarpEvent e, @Nonnull Player player);
}
