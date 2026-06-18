package thaumcraft.api.listeners.warp;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class PickWarpEventContext {
    public PickWarpEventContext(int warp,
                                @NotNull LivingEntity living,
                                int actualWarp,
                                int warpCounter
    ) {
        this.warp = warp;
        this.living = living;
        this.actualWarp = actualWarp;
        this.warpCounter = warpCounter;
    }
    public PickWarpEventContext() {}

    public int warp = 0;
    public LivingEntity living = null;
    public int actualWarp = 0;
    public int warpCounter = 0;
    public int randWithWarp = Integer.MIN_VALUE;
}
