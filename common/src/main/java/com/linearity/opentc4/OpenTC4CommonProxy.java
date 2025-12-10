package com.linearity.opentc4;

import net.minecraft.world.entity.player.Player;

public class CommonProxy {
    public static CommonProxy INSTANCE = new CommonProxy();
    public Player getLocalPlayer() {
        throw new RuntimeException("client only!");
    }
    public boolean isShiftKeyDown() {
        return false;
    }
}
