package thaumcraft.common.blocks.liquid;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import thaumcraft.common.Thaumcraft;

public class ThaumcraftFluids {

    public static class ThaumcraftFluidInstances {

        public static FluxGooFluid FLUX_GOO_FLUID() {
            return Registry.SUPPLIER_FLUX_GOO_FLUID.get();
        }

        public static FluxGasFluid FLUX_GAS_FLUID() {
            return Registry.SUPPLIER_FLUX_GAS_FLUID.get();
        }

        public static DeathFluid DEATH_FLUID() {
            return Registry.SUPPLIER_DEATH_FLUID.get();
        }

        public static PureFluid.PureFluidSource PURE_FLUID_SOURCE() {
            return Registry.SUPPLIER_PURE_FLUID_SOURCE.get();
        }

        public static PureFluid.PureFluidFlowing PURE_FLUID_FLOWING() {
            return Registry.SUPPLIER_PURE_FLUID_FLOWING.get();
        }
    }

    public static class Registry {
        public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.FLUID);
        public static final RegistrySupplier<FluxGooFluid> SUPPLIER_FLUX_GOO_FLUID = FLUIDS.register("flux_goo",FluxGooFluid::new);
        public static final RegistrySupplier<FluxGasFluid> SUPPLIER_FLUX_GAS_FLUID = FLUIDS.register("flux_gas",FluxGasFluid::new);
        public static final RegistrySupplier<DeathFluid> SUPPLIER_DEATH_FLUID = FLUIDS.register("death_fluid",DeathFluid::new);
        public static final RegistrySupplier<PureFluid.PureFluidSource> SUPPLIER_PURE_FLUID_SOURCE = FLUIDS.register("pure_fluid_source", PureFluid.PureFluidSource::new);
        public static final RegistrySupplier<PureFluid.PureFluidFlowing> SUPPLIER_PURE_FLUID_FLOWING = FLUIDS.register("pure_fluid_flowing", PureFluid.PureFluidFlowing::new);

        static {
            FLUIDS.register();
        }
    }
    public static void init(){
    }

    public static class Tags{
        public static final TagKey<Fluid> CRUCIBLE_HEATER = TagKey.create(
                Registries.FLUID, new ResourceLocation(Thaumcraft.MOD_ID,"crucuble_heater"));
        public static final TagKey<Fluid> FLUX = TagKey.create(
                Registries.FLUID, new ResourceLocation(Thaumcraft.MOD_ID,"flux"));
    }
}
