package thaumcraft.common.blocks;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import thaumcraft.common.blocks.crafted.GolemFetterBlock;
import thaumcraft.common.blocks.crafted.NitorBlock;
import thaumcraft.common.blocks.crafted.PavingStoneTravelBlock;
import thaumcraft.common.blocks.crafted.PavingStoneWardingBlock;
import thaumcraft.common.blocks.liquid.FluxGasBlock;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.blocks.technique.WardingAuraBlock;
import thaumcraft.common.blocks.worldgenerated.*;
import thaumcraft.common.lib.world.treegrower.GreatwoodTreeGrower;
import thaumcraft.common.lib.world.treegrower.SilverwoodTreeGrower;

public class ThaumcraftBlocks {
    public static final FluxGooBlock FLUX_GOO = Registry.SUPPLIER_FLUX_GOO.get();
    public static final FluxGasBlock FLUX_GAS = Registry.SUPPLIER_FLUX_GAS.get();
    public static final NitorBlock NITOR_BLOCK = Registry.SUPPLIER_NITOR_BLOCK.get();
    public static final AuraNodeBlock AURA_NODE = Registry.SUPPLIER_AURA_NODE.get();
    public static final RotatedPillarBlock GREATWOOD_LOG = Registry.SUPPLIER_GREATWOOD_LOG.get();
    public static final RotatedPillarBlock SILVERWOOD_LOG = Registry.SUPPLIER_SILVERWOOD_LOG.get();
    public static final SilverWoodKnotBlock SILVERWOOD_KNOT = Registry.SUPPLIER_SILVERWOOD_KNOT.get();
    public static final Block GREATWOOD_PLANKS = Registry.SUPPLIER_GREATWOOD_PLANKS.get();
    public static final Block SILVERWOOD_PLANKS = Registry.SUPPLIER_SILVERWOOD_PLANKS.get();
    public static final LeavesBlock GREATWOOD_LEAVES = Registry.SUPPLIER_GREATWOOD_LEAVES.get();
    public static final SilverwoodLeavesBlock SILVERWOOD_LEAVES = Registry.SUPPLIER_SILVERWOOD_LEAVES.get();
    public static final SaplingBlock GREATWOOD_SAPLING = Registry.SUPPLIER_GREATWOOD_SAPLING.get();
    public static final SaplingBlock SILVERWOOD_SAPLING = Registry.SUPPLIER_SILVERWOOD_SAPLING.get();
    public static final ObsidianTotemBlock OBSIDIAN_TOTEM = Registry.SUPPLIER_OBSIDIAN_TOTEM.get();
    public static final ObsidianTotemWithNodeBlock OBSIDIAN_TOTEM_WITH_NODE = Registry.SUPPLIER_OBSIDIAN_TOTEM_WITH_NODE.get();
    public static final Block OBSIDIAN_TILE = Registry.SUPPLIER_OBSIDIAN_TILE.get();
    public static final PavingStoneTravelBlock PAVING_STONE_TRAVEL = Registry.SUPPLIER_PAVING_STONE_TRAVEL.get();
    public static final PavingStoneWardingBlock PAVING_STONE_WARDING = Registry.SUPPLIER_PAVING_STONE_WARDING.get();
    public static final WardingAuraBlock WARDING_AURA = Registry.SUPPLIER_WARDING_AURA.get();
    public static final Block THAUMIUM_BLOCK = Registry.SUPPLIER_THAUMIUM_BLOCK.get();
    public static final Block TALLOW_BLOCK = Registry.SUPPLIER_TALLOW_BLOCK.get();
    public static final Block ARCANE_STONE_BLOCK = Registry.SUPPLIER_ARCANE_STONE_BLOCK.get();
    public static final Block ARCANE_STONE_BRICKS = Registry.SUPPLIER_ARCANE_STONE_BRICKS.get();
    public static final Block GOLEM_FETTER = Registry.SUPPLIER_GOLEM_FETTER.get();
    public static final AncientStoneBlock ANCIENT_STONE = Registry.SUPPLIER_ANCIENT_STONE.get();
    public static final AncientRockBlock ANCIENT_ROCK = Registry.SUPPLIER_ANCIENT_ROCK.get();
    public static final Block CRUSTED_STONE = Registry.SUPPLIER_CRUSTED_STONE.get();
    public static final Block ANCIENT_STONE_PEDESTAL = Registry.SUPPLIER_ANCIENT_STONE_PEDESTAL.get();
    public static class Registry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create("thaumcraft", Registries.BLOCK);
        public static final RegistrySupplier<FluxGooBlock> SUPPLIER_FLUX_GOO = BLOCKS.register("flux_goo", FluxGooBlock::new);
        public static final RegistrySupplier<FluxGasBlock> SUPPLIER_FLUX_GAS = BLOCKS.register("flux_gas", FluxGasBlock::new);
        public static final RegistrySupplier<NitorBlock> SUPPLIER_NITOR_BLOCK = BLOCKS.register("nitor_block", NitorBlock::new);
        public static final RegistrySupplier<AuraNodeBlock> SUPPLIER_AURA_NODE = BLOCKS.register("aura_node", AuraNodeBlock::new);
        public static final RegistrySupplier<RotatedPillarBlock> SUPPLIER_GREATWOOD_LOG = BLOCKS.register("greatwood_log",() -> log(MapColor.PODZOL,MapColor.TERRACOTTA_GRAY));
        public static final RegistrySupplier<RotatedPillarBlock> SUPPLIER_SILVERWOOD_LOG = BLOCKS.register("silverwood_log",() -> log(MapColor.COLOR_LIGHT_GRAY,MapColor.WOOL));
        public static final RegistrySupplier<SilverWoodKnotBlock> SUPPLIER_SILVERWOOD_KNOT = BLOCKS.register("silverwood_knot_log", SilverWoodKnotBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_GREATWOOD_PLANKS = BLOCKS.register("greatwood_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
        public static final RegistrySupplier<Block> SUPPLIER_SILVERWOOD_PLANKS = BLOCKS.register("silverwood_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
        public static final RegistrySupplier<LeavesBlock> SUPPLIER_GREATWOOD_LEAVES = BLOCKS.register("greatwood_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
        public static final RegistrySupplier<SilverwoodLeavesBlock> SUPPLIER_SILVERWOOD_LEAVES = BLOCKS.register("silverwood_leaves", SilverwoodLeavesBlock::new);
        public static final RegistrySupplier<SaplingBlock> SUPPLIER_GREATWOOD_SAPLING = BLOCKS.register("greatwood_sapling", () -> new SaplingBlock(new GreatwoodTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
        public static final RegistrySupplier<SaplingBlock> SUPPLIER_SILVERWOOD_SAPLING = BLOCKS.register("silverwood_sapling", () -> new SaplingBlock(new SilverwoodTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
        public static final RegistrySupplier<ObsidianTotemBlock> SUPPLIER_OBSIDIAN_TOTEM = BLOCKS.register("obsidian_totem", ObsidianTotemBlock::new);
        public static final RegistrySupplier<ObsidianTotemWithNodeBlock> SUPPLIER_OBSIDIAN_TOTEM_WITH_NODE = BLOCKS.register("obsidian_totem_with_node", ObsidianTotemWithNodeBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_OBSIDIAN_TILE = BLOCKS.register("obsidian_tile", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).explosionResistance(999)));
        public static final RegistrySupplier<PavingStoneTravelBlock> SUPPLIER_PAVING_STONE_TRAVEL = BLOCKS.register("paving_stone_travel", PavingStoneTravelBlock::new);
        public static final RegistrySupplier<PavingStoneWardingBlock> SUPPLIER_PAVING_STONE_WARDING = BLOCKS.register("paving_stone_warding", PavingStoneWardingBlock::new);
        public static final RegistrySupplier<WardingAuraBlock> SUPPLIER_WARDING_AURA = BLOCKS.register("warding_aura", WardingAuraBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_THAUMIUM_BLOCK = BLOCKS.register("thaumium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));//harness and resistance may changed,but i think it's also suitable
        public static final RegistrySupplier<Block> SUPPLIER_TALLOW_BLOCK = BLOCKS.register("tallow_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.F,10.F)));
        public static final RegistrySupplier<Block> SUPPLIER_ARCANE_STONE_BLOCK = BLOCKS.register("arcane_stone_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.F,10.F)));
        public static final RegistrySupplier<Block> SUPPLIER_ARCANE_STONE_BRICKS = BLOCKS.register("arcane_stone_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.F,10.F)));
        public static final RegistrySupplier<GolemFetterBlock> SUPPLIER_GOLEM_FETTER = BLOCKS.register("golem_fetter", GolemFetterBlock::new);
        public static final RegistrySupplier<AncientStoneBlock> SUPPLIER_ANCIENT_STONE = BLOCKS.register("ancient_stone", AncientStoneBlock::new);
        public static final RegistrySupplier<AncientRockBlock> SUPPLIER_ANCIENT_ROCK = BLOCKS.register("ancient_rock", AncientRockBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_CRUSTED_STONE = BLOCKS.register("crusted_stone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.F,10.F)
                .lightLevel(s -> 4)));
        public static final RegistrySupplier<Block> SUPPLIER_ANCIENT_STONE_PEDESTAL = BLOCKS.register("ancient_stone_pedestal", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.F,10.F)));

        static {
            BLOCKS.register();
        }
    }

    public static void init(){
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
