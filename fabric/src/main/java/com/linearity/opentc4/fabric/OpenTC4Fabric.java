package com.linearity.opentc4.fabric;

import com.linearity.opentc4.OpenTC4;
import net.fabricmc.api.ModInitializer;

public final class OpenTC4Fabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        OpenTC4.init();
    }
}
