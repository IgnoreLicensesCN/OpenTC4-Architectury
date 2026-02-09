package com.linearity.opentc4.fabric;

import com.linearity.opentc4.OpenTC4;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class OpenTC4Fabric implements ModInitializer {
    @Override
    public void onInitialize() {
        OpenTC4.onInitialize();
        PlatformUniqueUtilsFabric platformUniqueUtilsFabric = new PlatformUniqueUtilsFabric();
        ServerLifecycleEvents.SERVER_STARTING.register(server -> platformUniqueUtilsFabric.server = server);
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((minecraftServer, closeableResourceManager, b) -> {
            OpenTC4.onDatapackReload();
        });
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        OpenTC4.init(platformUniqueUtilsFabric);
    }
}
