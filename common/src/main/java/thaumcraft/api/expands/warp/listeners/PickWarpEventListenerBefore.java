package thaumcraft.api.expands.warp.listeners;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.expands.warp.PickWarpEventContext;

import org.jetbrains.annotations.NotNull;

public abstract class PickWarpEventListenerBefore implements Comparable<PickWarpEventListenerBefore> {

    public final int priority;

    /**
     * @param priority can be any integer.listeners will be {@link java.util.Collections#sort} by priority
     */
    public PickWarpEventListenerBefore(int priority) {
        this.priority = priority;
    }

    /**
     *
     * @param e the context stores warp to calculate which event to pickup.
     * @param player victim
     */
    public abstract void beforePickEvent(PickWarpEventContext e, Player player);

    @Override
    public int compareTo(@NotNull PickWarpEventListenerBefore o) {
        return Integer.compare(priority, o.priority);
    }
}
