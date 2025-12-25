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
import thaumcraft.common.blocks.crafted.*;
import thaumcraft.common.blocks.liquid.FluxGasBlock;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.blocks.technique.WardingAuraBlock;
import thaumcraft.common.blocks.worldgenerated.*;
import thaumcraft.common.blocks.worldgenerated.ores.*;
import thaumcraft.common.lib.world.treegrower.GreatwoodTreeGrower;
import thaumcraft.common.lib.world.treegrower.SilverwoodTreeGrower;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

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

    public static class Registry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create("thaumcraft", Registries.BLOCK);
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
