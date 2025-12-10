package com.linearity.opentc4.forge;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(OpenTC4.MOD_ID)
public final class OpenTC4Forge {
    public OpenTC4Forge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(OpenTC4.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        OpenTC4.init();
    }
}
