package thaumcraft.common.blocks;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import thaumcraft.common.blocks.liquid.FluxGasBlock;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;

public class ThaumcraftBlocks {
    public static final FluxGooBlock FLUX_GOO = Registry.SUPPLIER_FLUX_GOO.get();
    public static final FluxGasBlock FLUX_GAS = Registry.SUPPLIER_FLUX_GAS.get();
    public static final NitorBlock NITOR_BLOCK = Registry.SUPPLIER_NITOR_BLOCK.get();
    public static class Registry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create("thaumcraft", Registries.BLOCK);
        public static final RegistrySupplier<FluxGooBlock> SUPPLIER_FLUX_GOO = BLOCKS.register("flux_goo", FluxGooBlock::new);
        public static final RegistrySupplier<FluxGasBlock> SUPPLIER_FLUX_GAS = BLOCKS.register("flux_gas", FluxGasBlock::new);
        public static final RegistrySupplier<NitorBlock> SUPPLIER_NITOR_BLOCK = BLOCKS.register("nitor_block", NitorBlock::new);
    }

    public static void init(){
        Registry.BLOCKS.register();
        ThaumcraftFluids.init();
    }
}
