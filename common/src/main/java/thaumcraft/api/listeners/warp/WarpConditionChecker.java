package thaumcraft.api.listeners.warp;

import net.minecraft.world.entity.player.Player;

public abstract class WarpConditionChecker implements Comparable<WarpConditionChecker> {
    public WarpConditionChecker(int priority) {
        this.priority = priority;
    }

    public final int priority;

    /**
     *
     * @param context
     * @param player victim
     * @return true if can trigger wrap event
     */
    public abstract boolean check(PickWarpEventContext context, Player player);

    @Override
    public int compareTo(WarpConditionChecker o) {
        return Integer.compare(priority, o.priority);
    }
}
