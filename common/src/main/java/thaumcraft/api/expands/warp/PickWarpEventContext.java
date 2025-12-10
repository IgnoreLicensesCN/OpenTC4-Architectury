package thaumcraft.api.expands.warp;

import net.minecraft.entity.player.Player;

import javax.annotation.Nullable;

public class PickWarpEventContext {
    public PickWarpEventContext(int warp,
                                @Nullable WarpEvent e,
                                @Nullable Player player,
                                int actualWarp,
                                int warpCounter
    ) {
        this.warp = warp;
        this.e = e;
        this.player = player;
        this.actualWarp = actualWarp;
        this.warpCounter = warpCounter;
    }
    public PickWarpEventContext() {}

    public int warp = 0;
    public WarpEvent e = null;
    public Player player = null;
    public int actualWarp = 0;
    public int warpCounter = 0;
    public int randWithWarp = Integer.MIN_VALUE;
}
