package com.linearity.opentc4.fabric.client;

import com.linearity.opentc4.OpenTC4;
import net.fabricmc.api.ClientModInitializer;

public final class OpenTC4FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        OpenTC4.onInitializeClient();
    }
}
