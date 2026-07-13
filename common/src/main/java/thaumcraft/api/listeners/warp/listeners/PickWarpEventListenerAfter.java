package thaumcraft.api.listeners.warp.listeners;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.warp.PickWarpEventContext;


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
     * @param living victim
     * @return event to replace with.Do not set null.
     */
    public abstract @NotNull WarpEvent afterPickEvent(@NotNull PickWarpEventContext context, @NotNull WarpEvent e, @NotNull LivingEntity living);

    @Override
    public int compareTo(@NotNull PickWarpEventListenerAfter o) {
        return Integer.compare(priority, o.priority);
    }
}
