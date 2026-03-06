package com.linearity.opentc4.forge;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.crafted.CrucibleBlockEntity;

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
        MinecraftForge.EVENT_BUS.register(ForgeCapabilityEvents.class);

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
    @Mod.EventBusSubscriber(modid = Thaumcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeCapabilityEvents {
        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
            if (event.getObject() instanceof CrucibleBlockEntity be) {
                // 为所有的坩埚 BE 动态挂载流体处理器能力
                event.addCapability(
                        ResourceLocation.tryBuild(Thaumcraft.MOD_ID, "crucible_fluid_handler"),
                        new ICapabilityProvider() {
                            private final LazyOptional<IFluidHandler> handler =
                                    LazyOptional.of(() -> new IFluidHandler() {
                                        @Override
                                        public int getTanks() {
                                            return 1;
                                        }

                                        @Override
                                        public @NotNull FluidStack getFluidInTank(int i) {
                                            var beFluidStack = be.getFluidStack();
                                            return new FluidStack(
                                                    beFluidStack.getFluid(),
                                                    (int) beFluidStack.getAmount()
                                            );
                                        }

                                        @Override
                                        public int getTankCapacity(int i) {
                                            return 0;
                                        }

                                        @Override
                                        public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
                                            return be.canAcceptFluid(fluidStack.getFluid());
                                        }

                                        @Override
                                        public int fill(FluidStack fluidStack, FluidAction fluidAction) {
                                            return (int) be.insertFluid(
                                                    fluidStack.getFluid(),fluidStack.getAmount(),
                                                    fluidAction != FluidAction.SIMULATE);
                                        }

                                        @Override
                                        public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
                                            return FluidStack.EMPTY;
                                        }

                                        @Override
                                        public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
                                            return FluidStack.EMPTY;
                                        }
                                    });

                            @Override
                            public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
                                return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, handler);
                            }
                        }
                );
            }
        }
    }


}
