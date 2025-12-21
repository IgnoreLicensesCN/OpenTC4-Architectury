package thaumcraft.common.blocks.liquid;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class ThaumcraftFluids {

    public static final FluxGooFluid FLUX_GOO_FLOWING = Registry.SUPPLIER_FLUX_GOO_FLOWING.get();
    public static final FluxGasFluid FLUX_GAS_FLOWING = Registry.SUPPLIER_FLUX_GAS_FLOWING.get();
    public static class Registry {
        public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create("thhaumcraft", Registries.FLUID);
        public static final RegistrySupplier<FluxGooFluid> SUPPLIER_FLUX_GOO_FLOWING = FLUIDS.register("flux_goo_flowing",FluxGooFluid::new);
        public static final RegistrySupplier<FluxGasFluid> SUPPLIER_FLUX_GAS_FLOWING = FLUIDS.register("flux_gas_flowing",FluxGasFluid::new);


        static {
            FLUIDS.register();
        }
    }
    public static void init(){
    }
}
