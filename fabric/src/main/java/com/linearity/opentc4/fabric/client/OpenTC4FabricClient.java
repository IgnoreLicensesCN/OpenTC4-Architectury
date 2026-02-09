package com.linearity.opentc4.fabric.client;

import com.linearity.opentc4.OpenTC4;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public final class OpenTC4FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        OpenTC4.onInitializeClient();
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ClientLifecycleEvents.CLIENT_STARTED.register(client->{
            OpenTC4.onClientStarting();
        });
    }
}
