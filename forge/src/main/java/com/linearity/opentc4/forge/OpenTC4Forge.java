package com.linearity.opentc4.forge;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.concurrent.CompletableFuture;

@Mod(OpenTC4.MOD_ID)
public final class OpenTC4Forge {

    static PlatformUniqueUtilsForge platformUniqueUtilsForge = new PlatformUniqueUtilsForge();

    @SuppressWarnings("for removal")//fk u forge
    public OpenTC4Forge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(
                OpenTC4.MOD_ID,
                FMLJavaModLoadingContext.get().getModEventBus()
        );

        // Run our common setup.
        OpenTC4.init(platformUniqueUtilsForge);
        OpenTC4.onInitialize();
    }

    @SubscribeEvent
    public static void onDataPackReload(AddReloadListenerEvent event){
        event.addListener(
                (
                        barrier,
                        resourceManager,
                        prepareProfiler,
                        applyProfiler,
                        prepareExecutor,
                        applyExecutor
                ) -> CompletableFuture.runAsync(
                        OpenTC4::onDatapackReload, applyExecutor
                )
        );
        ;
    }
    @SubscribeEvent
    public static void onServerStarting(FMLCommonSetupEvent event) {
        OpenTC4.onServerStarting();
    }
    @SubscribeEvent
    public static void onClientStarting(FMLClientSetupEvent event) {
        OpenTC4.onClientStarting();
    }


}
