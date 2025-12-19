package thaumcraft.common.blocks;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import thaumcraft.common.blocks.crafted.NitorBlock;
import thaumcraft.common.blocks.liquid.FluxGasBlock;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.blocks.worldgenerated.AuraNodeBlock;
import thaumcraft.common.blocks.worldgenerated.SilverWoodKnotBlock;
import thaumcraft.common.blocks.worldgenerated.SilverwoodLeavesBlock;
import thaumcraft.common.lib.world.treegrower.SilverwoodTreeGrower;

public class ThaumcraftBlocks {
    public static final FluxGooBlock FLUX_GOO = Registry.SUPPLIER_FLUX_GOO.get();
    public static final FluxGasBlock FLUX_GAS = Registry.SUPPLIER_FLUX_GAS.get();
    public static final NitorBlock NITOR_BLOCK = Registry.SUPPLIER_NITOR_BLOCK.get();
    public static final AuraNodeBlock AURA_NODE = Registry.SUPPLIER_AURA_NODE.get();
    public static final RotatedPillarBlock GREATWOOD_LOG = Registry.SUPPLIER_GREATWWOOD_LOG.get();
    public static final RotatedPillarBlock SILVERWOOD_LOG = Registry.SUPPLIER_SILVERWOOD_LOG.get();
    public static final SilverWoodKnotBlock SILVERWOOD_KNOT = Registry.SUPPLIER_SILVERWOOD_KNOT.get();
    public static final Block GREATWOOD_PLANKS = Registry.SUPPLIER_GREATWOOD_PLANKS.get();
    public static final Block SILVERWOOD_PLANKS = Registry.SUPPLIER_SILVERWOOD_PLANKS.get();
    public static final LeavesBlock GREATWOOD_LEAVES = Registry.SUPPLIER_GREATWOOD_LEAVES.get();
    public static final SilverwoodLeavesBlock SILVERWOOD_LEAVES = Registry.SUPPLIER_SILVERWOOD_LEAVES.get();
    public static final SaplingBlock GREATWOOD_SAPLING = Registry.SUPPLIER_GREATWOOD_SAPLING.get();
    public static final SaplingBlock SILVERWOOD_SAPLING = Registry.SUPPLIER_SILVERWOOD_SAPLING.get();
    public static class Registry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create("thaumcraft", Registries.BLOCK);
        public static final RegistrySupplier<FluxGooBlock> SUPPLIER_FLUX_GOO = BLOCKS.register("flux_goo", FluxGooBlock::new);
        public static final RegistrySupplier<FluxGasBlock> SUPPLIER_FLUX_GAS = BLOCKS.register("flux_gas", FluxGasBlock::new);
        public static final RegistrySupplier<NitorBlock> SUPPLIER_NITOR_BLOCK = BLOCKS.register("nitor_block", NitorBlock::new);
        public static final RegistrySupplier<AuraNodeBlock> SUPPLIER_AURA_NODE = BLOCKS.register("aura_node", AuraNodeBlock::new);
        public static final RegistrySupplier<RotatedPillarBlock> SUPPLIER_GREATWWOOD_LOG = BLOCKS.register("greatwood_log",() -> log(MapColor.PODZOL,MapColor.TERRACOTTA_GRAY));
        public static final RegistrySupplier<RotatedPillarBlock> SUPPLIER_SILVERWOOD_LOG = BLOCKS.register("silverwood_log",() -> log(MapColor.COLOR_LIGHT_GRAY,MapColor.WOOL));
        public static final RegistrySupplier<SilverWoodKnotBlock> SUPPLIER_SILVERWOOD_KNOT = BLOCKS.register("silverwood_knot_log", SilverWoodKnotBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_GREATWOOD_PLANKS = BLOCKS.register("greatwood_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
        public static final RegistrySupplier<Block> SUPPLIER_SILVERWOOD_PLANKS = BLOCKS.register("silverwood_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
        public static final RegistrySupplier<LeavesBlock> SUPPLIER_GREATWOOD_LEAVES = BLOCKS.register("greatwood_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
        public static final RegistrySupplier<SilverwoodLeavesBlock> SUPPLIER_SILVERWOOD_LEAVES = BLOCKS.register("silverwood_leaves", SilverwoodLeavesBlock::new);
        public static final RegistrySupplier<SaplingBlock> SUPPLIER_GREATWOOD_SAPLING =
                BLOCKS.register("greatwood_sapling",
                        () -> new SaplingBlock(
                                new GreatwoodTreeGrower(),
                                BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)
                        )
                );
        public static final RegistrySupplier<SaplingBlock> SUPPLIER_SILVERWOOD_SAPLING =
                BLOCKS.register("silverwood_sapling",
                        () -> new SaplingBlock(
                                new SilverwoodTreeGrower(),
                                BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)
                        )
                );

    }

    public static void init(){
        Registry.BLOCKS.register();
        ThaumcraftFluids.init();
    }



    //stole from mc source
    private static RotatedPillarBlock log(MapColor top, MapColor side) {
        return new RotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()
        );
    }
}
