package com.linearity.opentc4;

import net.minecraft.world.entity.player.Player;

public class OpenTC4CommonProxy {
    public static OpenTC4CommonProxy INSTANCE = new OpenTC4CommonProxy();
    public Player getLocalPlayer() {
        throw new RuntimeException("client only!");
    }
    public boolean isShiftKeyDown() {
        return false;
    }

    public int getLocalPlayerTicks(){
        throw new RuntimeException("client only!");
    }


}
