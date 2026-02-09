package thaumcraft.api.listeners.warp.listeners;

import net.minecraft.world.entity.player.Player;

public abstract class GettingWarpDelayListener implements Comparable<GettingWarpDelayListener>{
    public final int priority;
    public GettingWarpDelayListener(int priority) {
        this.priority = priority;
    }

    public abstract int onGettingWarpEventDelayForPlayer(Player player);

    @Override
    public int compareTo(GettingWarpDelayListener o) {
        return Integer.compare(this.priority, o.priority);
    }
}
