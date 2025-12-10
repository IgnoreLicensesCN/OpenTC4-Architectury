package com.linearity.opentc4;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class OpenTC4ClientProxy extends OpenTC4CommonProxy {
    @Override
    public Player getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public boolean isShiftKeyDown() {
        var client = Minecraft.getInstance();
        return client.options.keyShift.isDown();
    }

    @Override
    public int getLocalPlayerTicks() {
        var player = getLocalPlayer();
        if (player != null) {
            return player.tickCount;
        }
        return 0;
    }
}
