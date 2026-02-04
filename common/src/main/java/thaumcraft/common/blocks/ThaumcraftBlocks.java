package thaumcraft.common.blocks;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.abstracts.AbstractCrystalBlock;
import thaumcraft.common.blocks.crafted.*;
import thaumcraft.common.blocks.liquid.FluxGasBlock;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.blocks.multipartcomponent.infernalfurnace.*;
import thaumcraft.common.blocks.technique.WardingAuraBlock;
import thaumcraft.common.blocks.worldgenerated.*;
import thaumcraft.common.blocks.worldgenerated.decorations.CinderPearlBlock;
import thaumcraft.common.blocks.worldgenerated.decorations.ManaShroomBlock;
import thaumcraft.common.blocks.worldgenerated.decorations.ShimmerLeafBlock;
import thaumcraft.common.blocks.worldgenerated.eldritch.*;
import thaumcraft.common.blocks.worldgenerated.ores.*;
import thaumcraft.common.blocks.worldgenerated.taint.*;
import thaumcraft.common.lib.world.treegrower.GreatwoodTreeGrower;
import thaumcraft.common.lib.world.treegrower.SilverwoodTreeGrower;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.eldritch.RunedStoneBlock;

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
    public static final AncientStoneStairBlock ANCIENT_STONE_STAIRS = Registry.SUPPLIER_ANCIENT_STONE_STAIRS.get();
    public static final StairBlock ARCANE_STONE_BRICK_STAIRS = Registry.SUPPLIER_ARCANE_STONE_BRICK_STAIRS.get();
    public static final StairBlock GREATWOOD_PLANKS_STAIRS = Registry.SUPPLIER_GREATWOOD_PLANKS_STAIRS.get();
    public static final StairBlock SILVERWOOD_PLANKS_STAIRS = Registry.SUPPLIER_SILVERWOOD_PLANKS_STAIRS.get();
    public static final AncientStoneSlabBlock ANCIENT_STONE_SLAB = Registry.SUPPLIER_ANCIENT_STONE_SLAB.get();
    public static final SlabBlock ARCANE_STONE_BRICK_SLAB = Registry.SUPPLIER_ARCANE_STONE_BRICK_SLAB.get();
    public static final SlabBlock GREATWOOD_PLANKS_SLAB = Registry.SUPPLIER_GREATWOOD_PLANKS_SLAB.get();
    public static final SlabBlock SILVERWOOD_PLANKS_SLAB = Registry.SUPPLIER_SILVERWOOD_PLANKS_SLAB.get();

    public static final AbstractCrystalBlock AIR_CRYSTAL = Registry.SUPPLIER_AIR_CRYSTAL.get();
    public static final AbstractCrystalBlock FIRE_CRYSTAL = Registry.SUPPLIER_FIRE_CRYSTAL.get();
    public static final AbstractCrystalBlock WATER_CRYSTAL = Registry.SUPPLIER_WATER_CRYSTAL.get();
    public static final AbstractCrystalBlock EARTH_CRYSTAL = Registry.SUPPLIER_EARTH_CRYSTAL.get();
    public static final AbstractCrystalBlock ORDER_CRYSTAL = Registry.SUPPLIER_ORDER_CRYSTAL.get();
    public static final AbstractCrystalBlock ENTROPY_CRYSTAL = Registry.SUPPLIER_ENTROPY_CRYSTAL.get();
    public static final AbstractCrystalBlock MIXED_CRYSTAL = Registry.SUPPLIER_MIXED_CRYSTAL.get();
    public static final AbstractCrystalBlock STRANGE_CRYSTALS = Registry.SUPPLIER_STRANGE_CRYSTALS.get();

    public static final HungryChestBlock HUNGRY_CHEST = Registry.SUPPLIER_HUNGRY_CHEST.get();
    public static final EldritchVoidBlock ELDRITCH_VOID = Registry.SUPPLIER_ELDRITCH_VOID.get();

    public static final Block CINNABAR_ORE = Registry.SUPPLIER_CINNABAR_ORE.get();
    public static final Block AMBER_ORE = Registry.SUPPLIER_AMBER_ORE.get();
    public static final AirInfusedStoneBlock AIR_INFUSED_STONE = Registry.SUPPLIER_AIR_INFUSED_STONE.get();
    public static final FireInfusedStoneBlock FIRE_INFUSED_STONE = Registry.SUPPLIER_FIRE_INFUSED_STONE.get();
    public static final WaterInfusedStoneBlock WATER_INFUSED_STONE = Registry.SUPPLIER_WATER_INFUSED_STONE.get();
    public static final EarthInfusedStoneBlock EARTH_INFUSED_STONE = Registry.SUPPLIER_EARTH_INFUSED_STONE.get();
    public static final OrderInfusedStoneBlock ORDER_INFUSED_STONE = Registry.SUPPLIER_ORDER_INFUSED_STONE.get();
    public static final EntropyInfusedStoneBlock ENTROPY_INFUSED_STONE = Registry.SUPPLIER_ENTROPY_INFUSED_STONE.get();

    public static final Block AMBER_BLOCK = Registry.SUPPLIER_AMBER_BLOCK.get();
    public static final Block AMBER_BRICK = Registry.SUPPLIER_AMBER_BRICK.get();

    public static final WardedGlassBlock WARDED_GLASS = Registry.SUPPLIER_WARDED_GLASS.get();

    public static final TallowCandleBlock WHITE_TALLOW_CANDLE = Registry.SUPPLIER_WHITE_TALLOW_CANDLE.get();
    public static final TallowCandleBlock ORANGE_TALLOW_CANDLE = Registry.SUPPLIER_ORANGE_TALLOW_CANDLE.get();
    public static final TallowCandleBlock MAGENTA_TALLOW_CANDLE = Registry.SUPPLIER_MAGENTA_TALLOW_CANDLE.get();
    public static final TallowCandleBlock LIGHT_BLUE_TALLOW_CANDLE = Registry.SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE.get();
    public static final TallowCandleBlock YELLOW_TALLOW_CANDLE = Registry.SUPPLIER_YELLOW_TALLOW_CANDLE.get();
    public static final TallowCandleBlock LIME_TALLOW_CANDLE = Registry.SUPPLIER_LIME_TALLOW_CANDLE.get();
    public static final TallowCandleBlock PINK_TALLOW_CANDLE = Registry.SUPPLIER_PINK_TALLOW_CANDLE.get();
    public static final TallowCandleBlock GRAY_TALLOW_CANDLE = Registry.SUPPLIER_GRAY_TALLOW_CANDLE.get();
    public static final TallowCandleBlock LIGHT_GRAY_TALLOW_CANDLE = Registry.SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE.get();
    public static final TallowCandleBlock CYAN_TALLOW_CANDLE = Registry.SUPPLIER_CYAN_TALLOW_CANDLE.get();
    public static final TallowCandleBlock PURPLE_TALLOW_CANDLE = Registry.SUPPLIER_PURPLE_TALLOW_CANDLE.get();
    public static final TallowCandleBlock BLUE_TALLOW_CANDLE = Registry.SUPPLIER_BLUE_TALLOW_CANDLE.get();
    public static final TallowCandleBlock BROWN_TALLOW_CANDLE = Registry.SUPPLIER_BROWN_TALLOW_CANDLE.get();
    public static final TallowCandleBlock GREEN_TALLOW_CANDLE = Registry.SUPPLIER_GREEN_TALLOW_CANDLE.get();
    public static final TallowCandleBlock RED_TALLOW_CANDLE = Registry.SUPPLIER_RED_TALLOW_CANDLE.get();
    public static final TallowCandleBlock BLACK_TALLOW_CANDLE = Registry.SUPPLIER_BLACK_TALLOW_CANDLE.get();

    public static final ShimmerLeafBlock SHIMMER_LEAF = Registry.SUPPLIER_SHIMMER_LEAF.get();
    public static final CinderPearlBlock CINDER_PEARL = Registry.SUPPLIER_CINDER_PEARL.get();
    public static final ManaShroomBlock MANA_SHROOM = Registry.SUPPLIER_MANA_SHROOM.get();

    public static final EtherealBloomBlock ETHEREAL_BLOOM = Registry.SUPPLIER_ETHEREAL_BLOOM.get();

    public static final InfernalFurnaceBarBlock INFERNAL_FURNACE_BAR = Registry.SUPPLIER_INFERNAL_FURNACE_BAR.get();
    public static final InfernalFurnaceSideBlock INFERNAL_FURNACE_SIDE = Registry.SUPPLIER_INFERNAL_FURNACE_SIDE.get();
    public static final InfernalFurnaceBottomBlock INFERNAL_FURNACE_BOTTOM = Registry.SUPPLIER_INFERNAL_FURNACE_BOTTOM.get();
    public static final InfernalFurnaceCornerBlock INFERNAL_FURNACE_CORNER = Registry.SUPPLIER_INFERNAL_FURNACE_CORNER.get();
    public static final InfernalFurnaceEdgeXAxisBlock INFERNAL_FURNACE_X_AXIS = Registry.SUPPLIER_INFERNAL_FURNACE_X_AXIS.get();
    public static final InfernalFurnaceEdgeYAxisBlock INFERNAL_FURNACE_Y_AXIS = Registry.SUPPLIER_INFERNAL_FURNACE_Y_AXIS.get();
    public static final InfernalFurnaceEdgeZAxisBlock INFERNAL_FURNACE_Z_AXIS = Registry.SUPPLIER_INFERNAL_FURNACE_Z_AXIS.get();
    public static final InfernalFurnaceLavaBlock INFERNAL_FURNACE_LAVA = Registry.SUPPLIER_INFERNAL_FURNACE_LAVA.get();

    public static final ArcaneBellowBlock ARCANE_BELLOW = Registry.SUPPLIER_ARCANE_BELLOW.get();
    public static final ArcaneDoorBlock ARCANE_DOOR = Registry.SUPPLIER_ARCANE_DOOR.get();

    public static final EldritchAltarBlock ELDRITCH_ALTAR = Registry.SUPPLIER_ELDRITCH_ALTAR.get();

    public static final EldritchObeliskBlock ELDRITCH_OBELISK = Registry.SUPPLIER_ELDRITCH_OBELISK.get();
    public static final EldritchObeliskWithTickerBlock ELDRITCH_OBELISK_WITH_TICKER = Registry.SUPPLIER_ELDRITCH_OBELISK_WITH_TICKER.get();
    public static final EldritchCapstoneBlock ELDRITCH_CAPSTONE = Registry.SUPPLIER_ELDRITCH_CAPSTONE.get();
    public static final GlowingClustedStoneBlock GLOWING_CRUSTED_STONE = Registry.SUPPLIER_GLOWING_CRUSTED_STONE.get();
    public static final GlyphedStoneBlock GLYPHED_STONE = Registry.SUPPLIER_GLYPHED_STONE.get();
    public static final Block ANCIENT_GATEWAY = Registry.SUPPLIER_ANCIENT_GATEWAY.get();
    public static final AncientLockEmptyBlock ANCIENT_LOCK_EMPTY = Registry.SUPPLIER_ANCIENT_LOCK_EMPTY.get();
    public static final AncientLockInsertedBlock ANCIENT_LOCK_INSERTED = Registry.SUPPLIER_ANCIENT_LOCK_INSERTED.get();
    public static final EldritchCrabSpawnerBlock ELDRITCH_CRAB_SPAWNER = Registry.SUPPLIER_ELDRITCH_CRAB_SPAWNER.get();
    public static final RunedStoneBlock RUNED_STONE = Registry.SUPPLIER_RUNED_STONE.get();
    public static final CrustedTaintBlock CRUSTED_TAINT = Registry.SUPPLIER_CRUSTED_TAINT.get();
    public static final TaintedSoilBlock TAINTED_SOIL = Registry.SUPPLIER_TAINTED_SOIL.get();
//    public static final FleshBlock BLOCK_OF_FLESH = Registry.SUPPLIER_FLESH.get();
    public static final FibrousTaintBlock FIBROUS_TAINT = Registry.SUPPLIER_FIBROUS_TAINT.get();
    public static final TaintedGrassBlock TAINTED_GRASS = Registry.SUPPLIER_TAINTED_GRASS.get();
    public static final TaintedPlantBlock TAINTED_PLANT = Registry.SUPPLIER_TAINTED_PLANT.get();
    public static final SporeStalkBlock SPORE_STALK = Registry.SUPPLIER_SPORE_STALK.get();
    public static final MatureSporeStalkBlock MATURE_SPORE_STALK = Registry.SUPPLIER_MATURE_SPORE_STALK.get();
    public static final TableBlock TABLE = Registry.SUPPLIER_TABLE.get();
    public static final ArcaneWorkbenchBlock ARCANE_WORKBENCH = Registry.SUPPLIER_ARCANE_WORKBENCH.get();

    public static class Registry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.BLOCK);
        public static final RegistrySupplier<FluxGooBlock> SUPPLIER_FLUX_GOO = BLOCKS.register(
                "flux_goo", FluxGooBlock::new);
        public static final RegistrySupplier<FluxGasBlock> SUPPLIER_FLUX_GAS = BLOCKS.register(
                "flux_gas", FluxGasBlock::new);
        public static final RegistrySupplier<NitorBlock> SUPPLIER_NITOR_BLOCK = BLOCKS.register(
                "nitor_block", NitorBlock::new);
        public static final RegistrySupplier<AuraNodeBlock> SUPPLIER_AURA_NODE = BLOCKS.register(
                "aura_node", AuraNodeBlock::new);
        public static final RegistrySupplier<RotatedPillarBlock> SUPPLIER_GREATWOOD_LOG = BLOCKS.register(
                "greatwood_log", () -> log(MapColor.PODZOL, MapColor.TERRACOTTA_GRAY));
        public static final RegistrySupplier<RotatedPillarBlock> SUPPLIER_SILVERWOOD_LOG = BLOCKS.register(
                "silverwood_log", () -> log(MapColor.COLOR_LIGHT_GRAY, MapColor.WOOL));
        public static final RegistrySupplier<SilverWoodKnotBlock> SUPPLIER_SILVERWOOD_KNOT = BLOCKS.register(
                "silverwood_knot_log", SilverWoodKnotBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_GREATWOOD_PLANKS = BLOCKS.register(
                "greatwood_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
        public static final RegistrySupplier<Block> SUPPLIER_SILVERWOOD_PLANKS = BLOCKS.register(
                "silverwood_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
        public static final RegistrySupplier<LeavesBlock> SUPPLIER_GREATWOOD_LEAVES = BLOCKS.register(
                "greatwood_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
        public static final RegistrySupplier<SilverwoodLeavesBlock> SUPPLIER_SILVERWOOD_LEAVES = BLOCKS.register(
                "silverwood_leaves", SilverwoodLeavesBlock::new);
        public static final RegistrySupplier<SaplingBlock> SUPPLIER_GREATWOOD_SAPLING = BLOCKS.register(
                "greatwood_sapling",
                () -> new SaplingBlock(new GreatwoodTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING))
        );
        public static final RegistrySupplier<SaplingBlock> SUPPLIER_SILVERWOOD_SAPLING = BLOCKS.register(
                "silverwood_sapling",
                () -> new SaplingBlock(new SilverwoodTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING))
        );
        public static final RegistrySupplier<ObsidianTotemBlock> SUPPLIER_OBSIDIAN_TOTEM = BLOCKS.register(
                "obsidian_totem", ObsidianTotemBlock::new);
        public static final RegistrySupplier<ObsidianTotemWithNodeBlock> SUPPLIER_OBSIDIAN_TOTEM_WITH_NODE = BLOCKS.register(
                "obsidian_totem_with_node", ObsidianTotemWithNodeBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_OBSIDIAN_TILE = BLOCKS.register(
                "obsidian_tile", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN)
                        .explosionResistance(999))
        );
        public static final RegistrySupplier<PavingStoneTravelBlock> SUPPLIER_PAVING_STONE_TRAVEL = BLOCKS.register(
                "paving_stone_travel", PavingStoneTravelBlock::new);
        public static final RegistrySupplier<PavingStoneWardingBlock> SUPPLIER_PAVING_STONE_WARDING = BLOCKS.register(
                "paving_stone_warding", PavingStoneWardingBlock::new);
        public static final RegistrySupplier<WardingAuraBlock> SUPPLIER_WARDING_AURA = BLOCKS.register(
                "warding_aura", WardingAuraBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_THAUMIUM_BLOCK = BLOCKS.register(
                "thaumium_block",
                () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))
        );//harness and resistance may changed,but i think it's also suitable
        public static final RegistrySupplier<Block> SUPPLIER_TALLOW_BLOCK = BLOCKS.register(
                "tallow_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(2.F, 10.F))
        );
        public static final RegistrySupplier<Block> SUPPLIER_ARCANE_STONE_BLOCK = BLOCKS.register(
                "arcane_stone_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(2.F, 10.F))
        );
        public static final RegistrySupplier<Block> SUPPLIER_ARCANE_STONE_BRICKS = BLOCKS.register(
                "arcane_stone_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(2.F, 10.F))
        );
        public static final RegistrySupplier<GolemFetterBlock> SUPPLIER_GOLEM_FETTER = BLOCKS.register(
                "golem_fetter", GolemFetterBlock::new);
        public static final RegistrySupplier<AncientStoneBlock> SUPPLIER_ANCIENT_STONE = BLOCKS.register(
                "ancient_stone", AncientStoneBlock::new);
        public static final RegistrySupplier<AncientRockBlock> SUPPLIER_ANCIENT_ROCK = BLOCKS.register(
                "ancient_rock", AncientRockBlock::new);
        public static final RegistrySupplier<Block> SUPPLIER_CRUSTED_STONE = BLOCKS.register(
                "crusted_stone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(2.F, 10.F)
                        .lightLevel(s -> 4))
        );
        public static final RegistrySupplier<Block> SUPPLIER_ANCIENT_STONE_PEDESTAL = BLOCKS.register(
                "ancient_stone_pedestal", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(2.F, 10.F))
        );
        public static final RegistrySupplier<AncientStoneStairBlock> SUPPLIER_ANCIENT_STONE_STAIRS = BLOCKS.register(
                "ancient_stone_stairs", AncientStoneStairBlock::new);
        public static final RegistrySupplier<StairBlock> SUPPLIER_ARCANE_STONE_BRICK_STAIRS = BLOCKS.register(
                "arcane_stone_brick_stairs", () -> new StairBlock(
                        SUPPLIER_ARCANE_STONE_BRICKS.get()
                                .defaultBlockState(), BlockBehaviour.Properties.copy(SUPPLIER_ARCANE_STONE_BRICKS.get())
                )
        );
        public static final RegistrySupplier<StairBlock> SUPPLIER_GREATWOOD_PLANKS_STAIRS = BLOCKS.register(
                "greatwood_planks_stairs", () -> new StairBlock(
                        SUPPLIER_GREATWOOD_PLANKS.get()
                                .defaultBlockState(), BlockBehaviour.Properties.copy(SUPPLIER_GREATWOOD_PLANKS.get())
                )
        );
        public static final RegistrySupplier<StairBlock> SUPPLIER_SILVERWOOD_PLANKS_STAIRS = BLOCKS.register(
                "silverwood_planks_stairs", () -> new StairBlock(
                        SUPPLIER_SILVERWOOD_PLANKS.get()
                                .defaultBlockState(), BlockBehaviour.Properties.copy(SUPPLIER_SILVERWOOD_PLANKS.get())
                )
        );
        public static final RegistrySupplier<AncientStoneSlabBlock> SUPPLIER_ANCIENT_STONE_SLAB = BLOCKS.register(
                "ancient_stone_slab", AncientStoneSlabBlock::new);
        public static final RegistrySupplier<SlabBlock> SUPPLIER_ARCANE_STONE_BRICK_SLAB = BLOCKS.register(
                "arcane_stone_brick_slab",
                () -> new SlabBlock(BlockBehaviour.Properties.copy(SUPPLIER_ARCANE_STONE_BRICKS.get())
                )
        );
        public static final RegistrySupplier<SlabBlock> SUPPLIER_GREATWOOD_PLANKS_SLAB = BLOCKS.register(
                "greatwood_planks_slab",
                () -> new SlabBlock(BlockBehaviour.Properties.copy(SUPPLIER_GREATWOOD_PLANKS.get())
                )
        );
        public static final RegistrySupplier<SlabBlock> SUPPLIER_SILVERWOOD_PLANKS_SLAB = BLOCKS.register(
                "silverwood_planks_slab",
                () -> new SlabBlock(BlockBehaviour.Properties.copy(SUPPLIER_SILVERWOOD_PLANKS.get()))
        );

        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_AIR_CRYSTAL = BLOCKS.register(
                "air_crystal_cluster",
                ()-> new AbstractCrystalBlock(new int[]{0xffff7e}){}
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_FIRE_CRYSTAL = BLOCKS.register(
                "fire_crystal_cluster",
                ()-> new AbstractCrystalBlock(new int[]{0xff3c01}){}
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_WATER_CRYSTAL = BLOCKS.register(
                "water_crystal_cluster",
                ()-> new AbstractCrystalBlock(new int[]{0x0090ff}){}
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_EARTH_CRYSTAL = BLOCKS.register(
                "earth_crystal_cluster",
                ()-> new AbstractCrystalBlock(new int[]{0x00a000}){}
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_ORDER_CRYSTAL = BLOCKS.register(
                "order_crystal_cluster",
                ()-> new AbstractCrystalBlock(new int[]{0xeeccff}){}
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_ENTROPY_CRYSTAL = BLOCKS.register(
                "entropy_crystal_cluster",
                ()-> new AbstractCrystalBlock(new int[]{0x555577}){}
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_MIXED_CRYSTAL = BLOCKS.register(
                "mixed_crystal_cluster",
                ()-> new AbstractCrystalBlock(new int[]{0xffff7e, 0xff3c01, 0x90ff, 0xa000, 0xeeccff, 0x555577}){}
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_STRANGE_CRYSTALS = BLOCKS.register(
                "strange_crystals",
                ()-> new AbstractCrystalBlock(new int[]{0xFFFFFF}){}
        );
        public static final RegistrySupplier<HungryChestBlock> SUPPLIER_HUNGRY_CHEST = BLOCKS.register(
                "hungry_chest",
                () -> new HungryChestBlock(ThaumcraftBlockEntities.Registry.SUPPLIER_HUNGRY_CHEST::get)
        );
        public static final RegistrySupplier<EldritchVoidBlock> SUPPLIER_ELDRITCH_VOID = BLOCKS.register(
                "eldritch_void",
                EldritchVoidBlock::new
        );
        public static final RegistrySupplier<Block> SUPPLIER_CINNABAR_ORE = BLOCKS.register(
                "cinnabar_ore",
                () -> new Block(
                        BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                                .sound(SoundType.STONE)
                                .strength(1.5f,5.f)
                )
        );
        public static final RegistrySupplier<DropExperienceBlock> SUPPLIER_AMBER_ORE = BLOCKS.register(
                "amber_ore",
                () -> new DropExperienceBlock(
                        BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)
                                .sound(SoundType.STONE)
                                .strength(1.5f,5.f), UniformInt.of(1,4)
                )
        );
        public static final RegistrySupplier<AirInfusedStoneBlock> SUPPLIER_AIR_INFUSED_STONE = BLOCKS.register(
                "air_infused_stone",
                AirInfusedStoneBlock::new
        );
        public static final RegistrySupplier<FireInfusedStoneBlock> SUPPLIER_FIRE_INFUSED_STONE = BLOCKS.register(
                "fire_infused_stone",
                FireInfusedStoneBlock::new
        );
        public static final RegistrySupplier<WaterInfusedStoneBlock> SUPPLIER_WATER_INFUSED_STONE = BLOCKS.register(
                "water_infused_stone",
                WaterInfusedStoneBlock::new
        );
        public static final RegistrySupplier<EarthInfusedStoneBlock> SUPPLIER_EARTH_INFUSED_STONE = BLOCKS.register(
                "earth_infused_stone",
                EarthInfusedStoneBlock::new
        );
        public static final RegistrySupplier<OrderInfusedStoneBlock> SUPPLIER_ORDER_INFUSED_STONE = BLOCKS.register(
                "order_infused_stone",
                OrderInfusedStoneBlock::new
        );
        public static final RegistrySupplier<EntropyInfusedStoneBlock> SUPPLIER_ENTROPY_INFUSED_STONE = BLOCKS.register(
                "entropy_infused_stone",
                EntropyInfusedStoneBlock::new
        );
        public static final RegistrySupplier<Block> SUPPLIER_AMBER_BLOCK = BLOCKS.register(
                "amber_block",
                () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(1.5f,5.f)
                )
        );
        public static final RegistrySupplier<Block> SUPPLIER_AMBER_BRICK = BLOCKS.register(
                "amber_brick",
                () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(1.5f,5.f)
                )
        );
        public static final RegistrySupplier<WardedGlassBlock> SUPPLIER_WARDED_GLASS = BLOCKS.register(
                "warded_glass",
                WardedGlassBlock::new
        );

        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_WHITE_TALLOW_CANDLE = BLOCKS.register(
                "white_tallow_candle",
                () -> new TallowCandleBlock(0xf0f0f0)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_ORANGE_TALLOW_CANDLE = BLOCKS.register(
                "orange_tallow_candle",
                () -> new TallowCandleBlock(0xeb8844)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_MAGENTA_TALLOW_CANDLE = BLOCKS.register(
                "magenta_tallow_candle",
                () -> new TallowCandleBlock(0xc354cd)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE = BLOCKS.register(
                "light_blue_tallow_candle",
                () -> new TallowCandleBlock(0x6689d3)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_YELLOW_TALLOW_CANDLE = BLOCKS.register(
                "yellow_tallow_candle",
                () -> new TallowCandleBlock(0xdecf2a)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_LIME_TALLOW_CANDLE = BLOCKS.register(
                "lime_tallow_candle",
                () -> new TallowCandleBlock(0x41cd34)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_PINK_TALLOW_CANDLE = BLOCKS.register(
                "pink_tallow_candle",
                () -> new TallowCandleBlock(0xd88198)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_GRAY_TALLOW_CANDLE = BLOCKS.register(
                "gray_tallow_candle",
                () -> new TallowCandleBlock(0x434343)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE = BLOCKS.register(
                "light_gray_tallow_candle",
                () -> new TallowCandleBlock(0xa0a0a0)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_CYAN_TALLOW_CANDLE = BLOCKS.register(
                "cyan_tallow_candle",
                () -> new TallowCandleBlock(0x287697)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_PURPLE_TALLOW_CANDLE = BLOCKS.register(
                "purple_tallow_candle",
                () -> new TallowCandleBlock(0x7b2fbe)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_BLUE_TALLOW_CANDLE = BLOCKS.register(
                "blue_tallow_candle",
                () -> new TallowCandleBlock(0x253192)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_BROWN_TALLOW_CANDLE = BLOCKS.register(
                "brown_tallow_candle",
                () -> new TallowCandleBlock(0x51301a)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_GREEN_TALLOW_CANDLE = BLOCKS.register(
                "green_tallow_candle",
                () -> new TallowCandleBlock(0x3b511a)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_RED_TALLOW_CANDLE = BLOCKS.register(
                "red_tallow_candle",
                () -> new TallowCandleBlock(0xb3312c)
        );
        public static final RegistrySupplier<TallowCandleBlock> SUPPLIER_BLACK_TALLOW_CANDLE = BLOCKS.register(
                "black_tallow_candle",
                () -> new TallowCandleBlock(0x1e1b1b)
        );
        public static final RegistrySupplier<ShimmerLeafBlock> SUPPLIER_SHIMMER_LEAF = BLOCKS.register(
                "shimmer_leaf",
                ShimmerLeafBlock::new
        );
        public static final RegistrySupplier<CinderPearlBlock> SUPPLIER_CINDER_PEARL = BLOCKS.register(
                "cinder_pearl",
                CinderPearlBlock::new
        );
        public static final RegistrySupplier<ManaShroomBlock> SUPPLIER_MANA_SHROOM = BLOCKS.register(
                "mana_shroom",
                ManaShroomBlock::new
        );
        public static final RegistrySupplier<EtherealBloomBlock> SUPPLIER_ETHEREAL_BLOOM = BLOCKS.register(
                "ethereal_bloom",
                EtherealBloomBlock::new
        );
        public static final RegistrySupplier<InfernalFurnaceBarBlock> SUPPLIER_INFERNAL_FURNACE_BAR = BLOCKS.register(
                "infernal_furnace_bar",
                InfernalFurnaceBarBlock::new
        );
        public static final RegistrySupplier<InfernalFurnaceSideBlock> SUPPLIER_INFERNAL_FURNACE_SIDE = BLOCKS.register(
                "infernal_furnace_side",
                InfernalFurnaceSideBlock::new
        );
        public static final RegistrySupplier<InfernalFurnaceBottomBlock> SUPPLIER_INFERNAL_FURNACE_BOTTOM = BLOCKS.register(
                "infernal_furnace_bottom",
                InfernalFurnaceBottomBlock::new
        );
        public static final RegistrySupplier<InfernalFurnaceCornerBlock> SUPPLIER_INFERNAL_FURNACE_CORNER = BLOCKS.register(
                "infernal_furnace_corner",
                InfernalFurnaceCornerBlock::new
        );
        public static final RegistrySupplier<InfernalFurnaceEdgeXAxisBlock> SUPPLIER_INFERNAL_FURNACE_X_AXIS = BLOCKS.register(
                "infernal_furnace_x_axis",
                InfernalFurnaceEdgeXAxisBlock::new
        );
        public static final RegistrySupplier<InfernalFurnaceEdgeYAxisBlock> SUPPLIER_INFERNAL_FURNACE_Y_AXIS = BLOCKS.register(
                "infernal_furnace_y_axis",
                InfernalFurnaceEdgeYAxisBlock::new
        );
        public static final RegistrySupplier<InfernalFurnaceEdgeZAxisBlock> SUPPLIER_INFERNAL_FURNACE_Z_AXIS = BLOCKS.register(
                "infernal_furnace_z_axis",
                InfernalFurnaceEdgeZAxisBlock::new
        );
        public static final RegistrySupplier<InfernalFurnaceLavaBlock> SUPPLIER_INFERNAL_FURNACE_LAVA = BLOCKS.register(
                "infernal_furnace_lava",
                InfernalFurnaceLavaBlock::new
        );
        public static final RegistrySupplier<ArcaneBellowBlock> SUPPLIER_ARCANE_BELLOW = BLOCKS.register(
                "arcane_bellow",
                ArcaneBellowBlock::new
        );
        public static final RegistrySupplier<ArcaneDoorBlock> SUPPLIER_ARCANE_DOOR = BLOCKS.register(
                "arcane_door",
                ArcaneDoorBlock::new
        );
        public static final RegistrySupplier<EldritchAltarBlock> SUPPLIER_ELDRITCH_ALTAR = BLOCKS.register(
                "eldritch_altar",
                EldritchAltarBlock::new
        );
        public static final RegistrySupplier<EldritchObeliskBlock> SUPPLIER_ELDRITCH_OBELISK = BLOCKS.register(
                "eldritch_obelisk",
                EldritchObeliskBlock::new
        );
        public static final RegistrySupplier<EldritchObeliskWithTickerBlock> SUPPLIER_ELDRITCH_OBELISK_WITH_TICKER = BLOCKS.register(
                "eldritch_obelisk_with_ticker",
                EldritchObeliskWithTickerBlock::new
        );
        public static final RegistrySupplier<EldritchCapstoneBlock> SUPPLIER_ELDRITCH_CAPSTONE = BLOCKS.register(
                "eldritch_capstone",
                EldritchCapstoneBlock::new
        );
        public static final RegistrySupplier<GlowingClustedStoneBlock> SUPPLIER_GLOWING_CRUSTED_STONE = BLOCKS.register(
                "glowing_crusted_stone",
                GlowingClustedStoneBlock::new
        );
        public static final RegistrySupplier<GlyphedStoneBlock> SUPPLIER_GLYPHED_STONE = BLOCKS.register(
                "glyphed_stone",
                GlyphedStoneBlock::new
        );
        public static final RegistrySupplier<Block> SUPPLIER_ANCIENT_GATEWAY = BLOCKS.register(
                "ancient_gateway",
                () -> new Block(BlockBehaviour.Properties.of()
                        .strength(-1,Float.MAX_VALUE)
                        .sound(SoundType.STONE)
                        .mapColor(MapColor.COLOR_BLACK)
                        .lightLevel(s -> 12))
        );
        public static final RegistrySupplier<AncientLockEmptyBlock> SUPPLIER_ANCIENT_LOCK_EMPTY = BLOCKS.register(
                "ancient_lock_empty",
                AncientLockEmptyBlock::new
        );
        public static final RegistrySupplier<AncientLockInsertedBlock> SUPPLIER_ANCIENT_LOCK_INSERTED = BLOCKS.register(
                "ancient_lock_inserted",
                AncientLockInsertedBlock::new
        );
        public static final RegistrySupplier<EldritchCrabSpawnerBlock> SUPPLIER_ELDRITCH_CRAB_SPAWNER = BLOCKS.register(
                "eldritch_crab_spawner",
                EldritchCrabSpawnerBlock::new
        );
        public static final RegistrySupplier<RunedStoneBlock> SUPPLIER_RUNED_STONE = BLOCKS.register(
                "runed_stone",
                RunedStoneBlock::new
        );

        //        public static final RegistrySupplier<FleshBlock> SUPPLIER_FLESH = BLOCKS.register(
//                "block_of_flesh",
//                FleshBlock::new
//        );//shouldn't be this.This is crafted and cant spread taint in latest 1.7.10 TC4
        public static final RegistrySupplier<CrustedTaintBlock> SUPPLIER_CRUSTED_TAINT = BLOCKS.register(
                "crusted_taint",
                CrustedTaintBlock::new
        );
        public static final RegistrySupplier<TaintedSoilBlock> SUPPLIER_TAINTED_SOIL = BLOCKS.register(
                "tainted_soil",
                TaintedSoilBlock::new
        );
        public static final RegistrySupplier<FibrousTaintBlock> SUPPLIER_FIBROUS_TAINT = BLOCKS.register(
                "fibrous_taint",
                FibrousTaintBlock::new
        );
        public static final RegistrySupplier<TaintedGrassBlock> SUPPLIER_TAINTED_GRASS = BLOCKS.register(
                "tainted_grass",
                TaintedGrassBlock::new
        );
        public static final RegistrySupplier<TaintedPlantBlock> SUPPLIER_TAINTED_PLANT = BLOCKS.register(
                "tainted_plant",
                TaintedPlantBlock::new
        );
        public static final RegistrySupplier<SporeStalkBlock> SUPPLIER_SPORE_STALK = BLOCKS.register(
                "spore_stalk",
                SporeStalkBlock::new
        );
        public static final RegistrySupplier<MatureSporeStalkBlock> SUPPLIER_MATURE_SPORE_STALK = BLOCKS.register(
                "mature_spore_stalk",
                MatureSporeStalkBlock::new
        );
        public static final RegistrySupplier<TableBlock> SUPPLIER_TABLE = BLOCKS.register(
                "table",
                TableBlock::new
        );
        public static final RegistrySupplier<ArcaneWorkbenchBlock> SUPPLIER_ARCANE_WORKBENCH = BLOCKS.register(
                "table",
                ArcaneWorkbenchBlock::new
        );

        static {
            BLOCKS.register();
        }
    }

    public static void init() {

        ThaumcraftFluids.init();
    }

    //stole from mc source
    private static RotatedPillarBlock log(MapColor top, MapColor side) {
        return new RotatedPillarBlock(
                BlockBehaviour.Properties.of()
                        .mapColor(blockState -> blockState.getValue(
                                RotatedPillarBlock.AXIS) == Direction.Axis.Y ? top : side)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.0F)
                        .sound(SoundType.WOOD)
                        .ignitedByLava()
        );
    }
}
