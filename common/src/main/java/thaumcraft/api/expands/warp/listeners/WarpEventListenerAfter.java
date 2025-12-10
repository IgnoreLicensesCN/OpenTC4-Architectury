package thaumcraft.api.expands.warp.listeners;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.expands.warp.PickWarpEventContext;
import thaumcraft.api.expands.warp.WarpEvent;



public abstract class WarpEventListenerAfter implements Comparable<WarpEventListenerAfter> {
    public final int priority;
    /**
     * @param priority can be any integer.listeners will be {@link java.util.Collections#sort} by priority
     */
    public WarpEventListenerAfter(int priority) {
        this.priority = priority;
    }
    @Override
    public int compareTo(@NotNull WarpEventListenerAfter o) {
        return Integer.compare(o.priority, priority);
    }

    /**
     * trigger after the event
     * @param e event triggered
     * @param player victim
     */
    public abstract void onWarpEvent(@NotNull PickWarpEventContext warpContext, @NotNull WarpEvent e, @NotNull Player player);
}
