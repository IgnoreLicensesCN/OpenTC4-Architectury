package com.linearity.opentc4;

import com.linearity.opentc4.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientProxy extends CommonProxy {
    @Override
    public Player getLocalPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public boolean isShiftKeyDown() {
        var client = Minecraft.getInstance();
        return client.options.keyShift.isDown();
    }
}
