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
//        MinecraftForge.EVENT_BUS.register(ForgeCapabilityEvents.class);

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
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        OpenTC4.onCommonSetup();
    }
    @SubscribeEvent
    public static void onSetupClient(FMLClientSetupEvent event) {
        OpenTC4.onInitializeClient();
    }
//    @Mod.EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//    public static class ForgeCapabilityEvents {
//        @SubscribeEvent
//        public static void attachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
//            if (event.getObject() instanceof SingleFluidContainerBlockEntity be) {
//                event.addCapability(
//                        ResourceLocation.tryBuild(Thaumcraft.MOD_ID, "simple_fluid_container_handler"),
//                        new ICapabilityProvider() {
//                            private final LazyOptional<IFluidHandler> handler =
//                                    LazyOptional.of(() -> new IFluidHandler() {
//
//                                    });
//
//                            @Override
//                            public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
//                                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, handler);
//                            }
//                        }
//                );
//            }
//        }
//    }


}
