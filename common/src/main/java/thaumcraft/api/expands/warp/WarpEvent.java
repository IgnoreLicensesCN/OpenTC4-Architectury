package thaumcraft.api.expands.warp;

import net.minecraft.entity.player.Player;

import javax.annotation.Nonnull;

public abstract class WarpEvent implements Comparable<WarpEvent> {
    public final int warpRequired;
    public final int weight;
    public boolean retryAnotherFlag = false;
    public boolean enabledFlag = true;
    public boolean sendMiscPacket = true;

    public WarpEvent(int weight,int warpRequired) {
        this.warpRequired = warpRequired;
        this.weight = weight;
    }

    public void cancel() {
        this.enabledFlag = false;
    }

    public void enable() {
        this.enabledFlag = true;
    }

    @Override
    public int compareTo(@Nonnull WarpEvent o) {
        int warpRequiredCompared = Integer.compare(this.warpRequired, o.warpRequired);
        if (warpRequiredCompared == 0) {
            return Integer.compare(this.weight, o.weight);
        }
        return warpRequiredCompared;
    }
    public abstract void onEventTriggered(PickWarpEventContext warpContext,Player player);

    public static final WarpEvent EMPTY = new WarpEvent(0,0) {
        @Override
        public void onEventTriggered(PickWarpEventContext warpContext,Player player) {
        }
    };
}
