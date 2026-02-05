package thaumcraft.api.expands.listeners.warp;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PickWarpEventContext {
    public PickWarpEventContext(int warp,
                                @NotNull Player player,
                                int actualWarp,
                                int warpCounter
    ) {
        this.warp = warp;
        this.player = player;
        this.actualWarp = actualWarp;
        this.warpCounter = warpCounter;
    }
    public PickWarpEventContext() {}

    public int warp = 0;
    public Player player = null;
    public int actualWarp = 0;
    public int warpCounter = 0;
    public int randWithWarp = Integer.MIN_VALUE;
}
