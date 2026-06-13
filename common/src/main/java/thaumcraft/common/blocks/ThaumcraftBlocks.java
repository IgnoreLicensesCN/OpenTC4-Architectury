package thaumcraft.common.blocks;

import com.linearity.colorannotation.annotation.RGBColor;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.abstracts.AbstractCrystalBlock;
import thaumcraft.common.blocks.crafted.*;
import thaumcraft.common.blocks.crafted.arcanebore.*;
import thaumcraft.common.blocks.crafted.essentia.*;
import thaumcraft.common.blocks.crafted.essentia.pipes.*;
import thaumcraft.common.blocks.crafted.fromtable.*;
import thaumcraft.common.blocks.crafted.essentia.jars.*;
import thaumcraft.common.blocks.crafted.infusion.ArcanePedestalBlock;
import thaumcraft.common.blocks.crafted.infusion.InfusionMatrixBlock;
import thaumcraft.common.blocks.crafted.infusion.InfusionPillarBlock;
import thaumcraft.common.blocks.crafted.jars.*;
import thaumcraft.common.blocks.crafted.lamps.*;
import thaumcraft.common.blocks.crafted.loot.*;
import thaumcraft.common.blocks.crafted.mirror.EssentiaMirrorBlock;
import thaumcraft.common.blocks.crafted.mirror.MirrorBlock;
import thaumcraft.common.blocks.crafted.noderelated.*;
import thaumcraft.common.blocks.crafted.ownedblock.*;
import thaumcraft.common.blocks.crafted.pavingstone.*;
import thaumcraft.common.blocks.crafted.noderelated.visnet.*;
import thaumcraft.common.blocks.crafted.essentia.thaumatorium.*;
import thaumcraft.common.blocks.crafted.visdevice.FocalManipulatorBlock;
import thaumcraft.common.blocks.crafted.visdevice.rechargepedestal.CompoundRechargeFocusBlock;
import thaumcraft.common.blocks.crafted.visdevice.rechargepedestal.WandRechargePedestalBlock;
import thaumcraft.common.blocks.liquid.*;
import thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.*;
import thaumcraft.common.blocks.multipartcomponent.infernalfurnace.*;
import thaumcraft.common.blocks.technique.*;
import thaumcraft.common.blocks.worldgenerated.*;
import thaumcraft.common.blocks.worldgenerated.decorations.*;
import thaumcraft.common.blocks.worldgenerated.eldritch.*;
import thaumcraft.common.blocks.worldgenerated.ores.*;
import thaumcraft.common.blocks.worldgenerated.taint.*;
import thaumcraft.common.lib.world.treegrower.GreatwoodTreeGrower;
import thaumcraft.common.lib.world.treegrower.SilverwoodTreeGrower;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

public class ThaumcraftBlocks {
    public static class ThaumcraftBlockInstances {

        public static FluxGooBlock FLUX_GOO() {
            return Registry.SUPPLIER_FLUX_GOO.get();
        }

        public static FluxGasBlock FLUX_GAS() {
            return Registry.SUPPLIER_FLUX_GAS.get();
        }

        public static NitorBlock NITOR_BLOCK() {
            return Registry.SUPPLIER_NITOR_BLOCK.get();
        }

        public static AuraNodeBlock AURA_NODE() {
            return Registry.SUPPLIER_AURA_NODE.get();
        }

        public static RotatedPillarBlock GREATWOOD_LOG() {
            return Registry.SUPPLIER_GREATWOOD_LOG.get();
        }

        public static RotatedPillarBlock SILVERWOOD_LOG() {
            return Registry.SUPPLIER_SILVERWOOD_LOG.get();
        }

        public static SilverWoodKnotBlock SILVERWOOD_KNOT() {
            return Registry.SUPPLIER_SILVERWOOD_KNOT.get();
        }

        public static Block GREATWOOD_PLANKS() {
            return Registry.SUPPLIER_GREATWOOD_PLANKS.get();
        }

        public static Block SILVERWOOD_PLANKS() {
            return Registry.SUPPLIER_SILVERWOOD_PLANKS.get();
        }

        public static LeavesBlock GREATWOOD_LEAVES() {
            return Registry.SUPPLIER_GREATWOOD_LEAVES.get();
        }

        public static SilverwoodLeavesBlock SILVERWOOD_LEAVES() {
            return Registry.SUPPLIER_SILVERWOOD_LEAVES.get();
        }

        public static SaplingBlock GREATWOOD_SAPLING() {
            return Registry.SUPPLIER_GREATWOOD_SAPLING.get();
        }

        public static SaplingBlock SILVERWOOD_SAPLING() {
            return Registry.SUPPLIER_SILVERWOOD_SAPLING.get();
        }

        public static ObsidianTotemBlock OBSIDIAN_TOTEM() {
            return Registry.SUPPLIER_OBSIDIAN_TOTEM.get();
        }

        public static ObsidianTotemWithNodeBlock OBSIDIAN_TOTEM_WITH_NODE() {
            return Registry.SUPPLIER_OBSIDIAN_TOTEM_WITH_NODE.get();
        }

        public static Block OBSIDIAN_TILE() {
            return Registry.SUPPLIER_OBSIDIAN_TILE.get();
        }

        public static PavingStoneTravelBlock PAVING_STONE_TRAVEL() {
            return Registry.SUPPLIER_PAVING_STONE_TRAVEL.get();
        }

        public static PavingStoneWardingBlock PAVING_STONE_WARDING() {
            return Registry.SUPPLIER_PAVING_STONE_WARDING.get();
        }

        public static WardingAuraBlock WARDING_AURA() {
            return Registry.SUPPLIER_WARDING_AURA.get();
        }

        public static Block THAUMIUM_BLOCK() {
            return Registry.SUPPLIER_THAUMIUM_BLOCK.get();
        }

        public static Block TALLOW_BLOCK() {
            return Registry.SUPPLIER_TALLOW_BLOCK.get();
        }

        public static Block ARCANE_STONE_BLOCK() {
            return Registry.SUPPLIER_ARCANE_STONE_BLOCK.get();
        }

        public static Block ARCANE_STONE_BRICKS() {
            return Registry.SUPPLIER_ARCANE_STONE_BRICKS.get();
        }

        public static Block GOLEM_FETTER() {
            return Registry.SUPPLIER_GOLEM_FETTER.get();
        }

        public static Block ANCIENT_STONE() {
            return Registry.SUPPLIER_ANCIENT_STONE.get();
        }

        public static Block ANCIENT_ROCK() {
            return Registry.SUPPLIER_ANCIENT_ROCK.get();
        }

        public static Block CRUSTED_STONE() {
            return Registry.SUPPLIER_CRUSTED_STONE.get();
        }

        public static Block ANCIENT_STONE_PEDESTAL() {
            return Registry.SUPPLIER_ANCIENT_STONE_PEDESTAL.get();
        }

        public static StairBlock ANCIENT_STONE_STAIRS() {
            return Registry.SUPPLIER_ANCIENT_STONE_STAIRS.get();
        }

        public static StairBlock ARCANE_STONE_BRICK_STAIRS() {
            return Registry.SUPPLIER_ARCANE_STONE_BRICK_STAIRS.get();
        }

        public static StairBlock GREATWOOD_PLANKS_STAIRS() {
            return Registry.SUPPLIER_GREATWOOD_PLANKS_STAIRS.get();
        }

        public static StairBlock SILVERWOOD_PLANKS_STAIRS() {
            return Registry.SUPPLIER_SILVERWOOD_PLANKS_STAIRS.get();
        }

        public static SlabBlock ANCIENT_STONE_SLAB() {
            return Registry.SUPPLIER_ANCIENT_STONE_SLAB.get();
        }

        public static SlabBlock ARCANE_STONE_BRICK_SLAB() {
            return Registry.SUPPLIER_ARCANE_STONE_BRICK_SLAB.get();
        }

        public static SlabBlock GREATWOOD_PLANKS_SLAB() {
            return Registry.SUPPLIER_GREATWOOD_PLANKS_SLAB.get();
        }

        public static SlabBlock SILVERWOOD_PLANKS_SLAB() {
            return Registry.SUPPLIER_SILVERWOOD_PLANKS_SLAB.get();
        }

        public static AbstractCrystalBlock AIR_CRYSTAL() {
            return Registry.SUPPLIER_AIR_CRYSTAL.get();
        }

        public static AbstractCrystalBlock FIRE_CRYSTAL() {
            return Registry.SUPPLIER_FIRE_CRYSTAL.get();
        }

        public static AbstractCrystalBlock WATER_CRYSTAL() {
            return Registry.SUPPLIER_WATER_CRYSTAL.get();
        }

        public static AbstractCrystalBlock EARTH_CRYSTAL() {
            return Registry.SUPPLIER_EARTH_CRYSTAL.get();
        }

        public static AbstractCrystalBlock ORDER_CRYSTAL() {
            return Registry.SUPPLIER_ORDER_CRYSTAL.get();
        }

        public static AbstractCrystalBlock ENTROPY_CRYSTAL() {
            return Registry.SUPPLIER_ENTROPY_CRYSTAL.get();
        }

        public static AbstractCrystalBlock MIXED_CRYSTAL() {
            return Registry.SUPPLIER_MIXED_CRYSTAL.get();
        }

        public static AbstractCrystalBlock STRANGE_CRYSTALS() {
            return Registry.SUPPLIER_STRANGE_CRYSTALS.get();
        }

        public static HungryChestBlock HUNGRY_CHEST() {
            return Registry.SUPPLIER_HUNGRY_CHEST.get();
        }

        public static EldritchVoidBlock ELDRITCH_VOID() {
            return Registry.SUPPLIER_ELDRITCH_VOID.get();
        }

        public static Block CINNABAR_ORE() {
            return Registry.SUPPLIER_CINNABAR_ORE.get();
        }

        public static Block AMBER_ORE() {
            return Registry.SUPPLIER_AMBER_ORE.get();
        }

        public static AirInfusedStoneBlock AIR_INFUSED_STONE() {
            return Registry.SUPPLIER_AIR_INFUSED_STONE.get();
        }

        public static FireInfusedStoneBlock FIRE_INFUSED_STONE() {
            return Registry.SUPPLIER_FIRE_INFUSED_STONE.get();
        }

        public static WaterInfusedStoneBlock WATER_INFUSED_STONE() {
            return Registry.SUPPLIER_WATER_INFUSED_STONE.get();
        }

        public static EarthInfusedStoneBlock EARTH_INFUSED_STONE() {
            return Registry.SUPPLIER_EARTH_INFUSED_STONE.get();
        }

        public static OrderInfusedStoneBlock ORDER_INFUSED_STONE() {
            return Registry.SUPPLIER_ORDER_INFUSED_STONE.get();
        }

        public static EntropyInfusedStoneBlock ENTROPY_INFUSED_STONE() {
            return Registry.SUPPLIER_ENTROPY_INFUSED_STONE.get();
        }

        public static Block AMBER_BLOCK() {
            return Registry.SUPPLIER_AMBER_BLOCK.get();
        }

        public static Block AMBER_BRICK() {
            return Registry.SUPPLIER_AMBER_BRICK.get();
        }

        public static WardedGlassBlock WARDED_GLASS() {
            return Registry.SUPPLIER_WARDED_GLASS.get();
        }

        public static TallowCandleBlock WHITE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_WHITE_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock ORANGE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_ORANGE_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock MAGENTA_TALLOW_CANDLE() {
            return Registry.SUPPLIER_MAGENTA_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock LIGHT_BLUE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock YELLOW_TALLOW_CANDLE() {
            return Registry.SUPPLIER_YELLOW_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock LIME_TALLOW_CANDLE() {
            return Registry.SUPPLIER_LIME_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock PINK_TALLOW_CANDLE() {
            return Registry.SUPPLIER_PINK_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock GRAY_TALLOW_CANDLE() {
            return Registry.SUPPLIER_GRAY_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock LIGHT_GRAY_TALLOW_CANDLE() {
            return Registry.SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock CYAN_TALLOW_CANDLE() {
            return Registry.SUPPLIER_CYAN_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock PURPLE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_PURPLE_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock BLUE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_BLUE_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock BROWN_TALLOW_CANDLE() {
            return Registry.SUPPLIER_BROWN_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock GREEN_TALLOW_CANDLE() {
            return Registry.SUPPLIER_GREEN_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock RED_TALLOW_CANDLE() {
            return Registry.SUPPLIER_RED_TALLOW_CANDLE.get();
        }

        public static TallowCandleBlock BLACK_TALLOW_CANDLE() {
            return Registry.SUPPLIER_BLACK_TALLOW_CANDLE.get();
        }

        public static ShimmerLeafBlock SHIMMER_LEAF() {
            return Registry.SUPPLIER_SHIMMER_LEAF.get();
        }

        public static CinderPearlBlock CINDER_PEARL() {
            return Registry.SUPPLIER_CINDER_PEARL.get();
        }

        public static ManaShroomBlock MANA_SHROOM() {
            return Registry.SUPPLIER_MANA_SHROOM.get();
        }

        public static EtherealBloomBlock ETHEREAL_BLOOM() {
            return Registry.SUPPLIER_ETHEREAL_BLOOM.get();
        }

        public static InfernalFurnaceBarBlock INFERNAL_FURNACE_BAR() {
            return Registry.SUPPLIER_INFERNAL_FURNACE_BAR.get();
        }

        public static InfernalFurnaceSideBlock INFERNAL_FURNACE_SIDE() {
            return Registry.SUPPLIER_INFERNAL_FURNACE_SIDE.get();
        }

        public static InfernalFurnaceCornerBlock INFERNAL_FURNACE_CORNER() {
            return Registry.SUPPLIER_INFERNAL_FURNACE_CORNER.get();
        }

        public static InfernalFurnaceEdgeXAxisBlock INFERNAL_FURNACE_X_AXIS() {
            return Registry.SUPPLIER_INFERNAL_FURNACE_X_AXIS.get();
        }

        public static InfernalFurnaceEdgeYAxisBlock INFERNAL_FURNACE_Y_AXIS() {
            return Registry.SUPPLIER_INFERNAL_FURNACE_Y_AXIS.get();
        }

        public static InfernalFurnaceEdgeZAxisBlock INFERNAL_FURNACE_Z_AXIS() {
            return Registry.SUPPLIER_INFERNAL_FURNACE_Z_AXIS.get();
        }

        public static InfernalFurnaceLavaBlock INFERNAL_FURNACE_LAVA() {
            return Registry.SUPPLIER_INFERNAL_FURNACE_LAVA.get();
        }

        public static ArcaneBellowBlock ARCANE_BELLOW() {
            return Registry.SUPPLIER_ARCANE_BELLOW.get();
        }

        public static ArcaneDoorBlock ARCANE_DOOR() {
            return Registry.SUPPLIER_ARCANE_DOOR.get();
        }

        public static EldritchAltarBlock ELDRITCH_ALTAR() {
            return Registry.SUPPLIER_ELDRITCH_ALTAR.get();
        }

        public static EldritchObeliskBlock ELDRITCH_OBELISK() {
            return Registry.SUPPLIER_ELDRITCH_OBELISK.get();
        }

        public static EldritchObeliskWithTickerBlock ELDRITCH_OBELISK_WITH_TICKER() {
            return Registry.SUPPLIER_ELDRITCH_OBELISK_WITH_TICKER.get();
        }

        public static EldritchCapstoneBlock ELDRITCH_CAPSTONE() {
            return Registry.SUPPLIER_ELDRITCH_CAPSTONE.get();
        }

        public static GlowingClustedStoneBlock GLOWING_CRUSTED_STONE() {
            return Registry.SUPPLIER_GLOWING_CRUSTED_STONE.get();
        }

        public static GlyphedStoneBlock GLYPHED_STONE() {
            return Registry.SUPPLIER_GLYPHED_STONE.get();
        }

        public static Block ANCIENT_GATEWAY() {
            return Registry.SUPPLIER_ANCIENT_GATEWAY.get();
        }

        public static AncientLockEmptyBlock ANCIENT_LOCK_EMPTY() {
            return Registry.SUPPLIER_ANCIENT_LOCK_EMPTY.get();
        }

        public static AncientLockInsertedBlock ANCIENT_LOCK_INSERTED() {
            return Registry.SUPPLIER_ANCIENT_LOCK_INSERTED.get();
        }

        public static EldritchCrabSpawnerBlock ELDRITCH_CRAB_SPAWNER() {
            return Registry.SUPPLIER_ELDRITCH_CRAB_SPAWNER.get();
        }

        public static RunedStoneBlock RUNED_STONE() {
            return Registry.SUPPLIER_RUNED_STONE.get();
        }

        public static CrustedTaintBlock CRUSTED_TAINT() {
            return Registry.SUPPLIER_CRUSTED_TAINT.get();
        }

        public static TaintedSoilBlock TAINTED_SOIL() {
            return Registry.SUPPLIER_TAINTED_SOIL.get();
        }

        public static FibrousTaintBlock FIBROUS_TAINT() {
            return Registry.SUPPLIER_FIBROUS_TAINT.get();
        }

        public static TaintedGrassBlock TAINTED_GRASS() {
            return Registry.SUPPLIER_TAINTED_GRASS.get();
        }

        public static TaintedPlantBlock TAINTED_PLANT() {
            return Registry.SUPPLIER_TAINTED_PLANT.get();
        }

        public static SporeStalkBlock SPORE_STALK() {
            return Registry.SUPPLIER_SPORE_STALK.get();
        }

        public static MatureSporeStalkBlock MATURE_SPORE_STALK() {
            return Registry.SUPPLIER_MATURE_SPORE_STALK.get();
        }

        public static TableBlock TABLE() {
            return Registry.SUPPLIER_TABLE.get();
        }

        public static ArcaneWorkbenchBlock ARCANE_WORKBENCH() {
            return Registry.SUPPLIER_ARCANE_WORKBENCH.get();
        }

        public static DeconstructionTableBlock DECONSTRUCTION_TABLE() {
            return Registry.SUPPLIER_DECONSTRUCTION_TABLE.get();
        }

        public static ResearchTableLeftPartBlock RESEARCH_TABLE_LEFT_PART() {
            return Registry.SUPPLIER_RESEARCH_TABLE_LEFT_PART.get();
        }

        public static ResearchTableRightPartBlock RESEARCH_TABLE_RIGHT_PART() {
            return Registry.SUPPLIER_RESEARCH_TABLE_RIGHT_PART.get();
        }

        public static GlimmerOfLightBlock GLIMMER_OF_LIGHT() {
            return Registry.SUPPLIER_GLIMMER_OF_LIGHT.get();
        }

        public static VisNetRelayBlock VIS_RELAY() {
            return Registry.SUPPLIER_VIS_RELAY.get();
        }

        public static VisNetChargeRelayBlock VIS_CHARGE_RELAY() {
            return Registry.SUPPLIER_VIS_CHARGE_RELAY.get();
        }

        public static EnergizedAuraNodeBlock ENERGIZED_NODE() {
            return Registry.SUPPLIER_ENERGIZED_NODE.get();
        }

        public static NodeStabilizerBlock NODE_STABILIZER() {
            return Registry.SUPPLIER_NODE_STABILIZER.get();
        }

        public static AdvancedNodeStabilizerBlock ADVANCED_NODE_STABILIZER() {
            return Registry.SUPPLIER_ADVANCED_NODE_STABILIZER.get();
        }

        public static NodeTransducerBlock NODE_TRANSDUCER() {
            return Registry.SUPPLIER_NODE_TRANSDUCER.get();
        }

        public static ImpassableBlock IMPASSABLE() {
            return Registry.SUPPLIER_IMPASSABLE_BLOCK.get();
        }

        public static SappingFieldBlock SAPPING_FIELD() {
            return Registry.SUPPLIER_SAPPING_FIELD.get();
        }

        public static StaticFieldBlock STATIC_FIELD() {
            return Registry.SUPPLIER_STATIC_FIELD.get();
        }

        public static AlchemicalFurnaceBlock ALCHEMICAL_FURNACE() {
            return Registry.SUPPLIER_ALCHEMICAL_FURNACE.get();
        }

        public static Block ADVANCED_ALCHEMICAL_CONSTRUCT() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_CONSTRUCT.get();
        }

        public static Block ALCHEMICAL_CONSTRUCT() {
            return Registry.SUPPLIER_ALCHEMICAL_CONSTRUCT.get();
        }

        public static ArcaneAlembicBlock ARCANE_ALEMBIC() {
            return Registry.SUPPLIER_ARCANE_ALEMBIC.get();
        }

        public static AdvancedAlchemicalFurnaceUpperFenceBlock ADVANCED_ALCHEMICAL_FURNACE_UPPER_FENCE() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_UPPER_FENCE.get();
        }

        public static AdvancedAlchemicalFurnaceBaseCornerBlock ADVANCED_ALCHEMICAL_FURNACE_BASE_CORNER() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_BASE_CORNER.get();
        }

        public static AdvancedAlchemicalFurnaceNozzleBlock ADVANCED_ALCHEMICAL_FURNACE_NOZZLE() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_NOZZLE.get();
        }

        public static AdvancedAlchemicalFurnaceAlembicBlock ADVANCED_ALCHEMICAL_FURNACE_ALEMBIC() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_ALEMBIC.get();
        }

        public static AdvancedAlchemicalFurnaceBaseBlock ADVANCED_ALCHEMICAL_FURNACE_BASE() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_BASE.get();
        }

        public static EssentiaJarBlock ESSENTIA_JAR() {
            return Registry.SUPPLIER_ESSENTIA_JAR.get();
        }

        public static VoidJarBlock VOID_JAR() {
            return Registry.SUPPLIER_VOID_JAR.get();
        }

        public static BrainJarBlock BRAIN_JAR() {
            return Registry.SUPPLIER_BRAIN_JAR.get();
        }

        public static NodeJarBlock NODE_JAR() {
            return Registry.SUPPLIER_NODE_JAR.get();
        }

        public static ItemCrateBlock ITEM_CRATE() {
            return Registry.SUPPLIER_ITEM_CRATE.get();
        }

        public static CrucibleBlock CRUCIBLE() {
            return Registry.SUPPLIER_CRUCIBLE.get();
        }

        public static ArcaneLampBlock ARCANE_LAMP() {
            return Registry.SUPPLIER_ARCANE_LAMP.get();
        }

        public static GrowthArcaneLampBlock GROWTH_ARCANE_LAMP() {
            return Registry.SUPPLIER_GROWTH_ARCANE_LAMP.get();
        }

        public static FertilityArcaneLampBlock FERTILITY_ARCANE_LAMP() {
            return Registry.SUPPLIER_FERTILITY_ARCANE_LAMP.get();
        }

        public static ThaumatoriumBottomBlock THAUMATORIUM_BOTTOM() {
            return Registry.SUPPLIER_THAUMATORIUM_BOTTOM.get();
        }

        public static ThaumatoriumTopBlock THAUMATORIUM_TOP() {
            return Registry.SUPPLIER_THAUMATORIUM_TOP.get();
        }

        public static MnemonicMatrixBlock MNEMONIC_MATRIX() {
            return Registry.SUPPLIER_MNEMONIC_MATRIX.get();
        }

        public static ArcaneLevitatorBubbleBlock ARCANE_LEVITATOR_BUBBLE() {
            return Registry.SUPPLIER_ARCANE_LEVITATOR_BUBBLE.get();
        }

        public static ArcaneLevitatorBlock ARCANE_LEVITATOR() {
            return Registry.SUPPLIER_ARCANE_LEVITATOR.get();
        }

        public static EssentiaReservoirBlock ESSENTIA_RESERVOIR() {
            return Registry.SUPPLIER_ESSENTIA_RESERVOIR.get();
        }

        public static ManaBeanBlock MANA_BEAN() {
            return Registry.SUPPLIER_MANA_BEAN.get();
        }

        public static EssentiaTubeBlock ESSENTIA_TUBE() {
            return Registry.SUPPLIER_ESSENTIA_TUBE.get();
        }

        public static EssentiaTubeValveBlock ESSENTIA_TUBE_VALVE() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_VALVE.get();
        }

        public static EssentiaTubeFilterBlock ESSENTIA_TUBE_FILTER() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_FILTER.get();
        }

        public static EssentiaTubeRestrictBlock ESSENTIA_TUBE_RESTRICT() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_RESTRICT.get();
        }

        public static EssentiaTubeOnewayBlock ESSENTIA_TUBE_ONEWAY() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_ONEWAY.get();
        }

        public static EssentiaBufferBlock ESSENTIA_BUFFER() {
            return Registry.SUPPLIER_ESSENTIA_BUFFER.get();
        }

        public static EssentiaCentrifugeBlock ESSENTIA_CENTRIFUGE() {
            return Registry.SUPPLIER_ESSENTIA_CENTRIFUGE.get();
        }

        public static EssentiaCrystallizerBlock ESSENTIA_CRYSTALLIZER() {
            return Registry.SUPPLIER_ESSENTIA_CRYSTALLIZER.get();
        }

        public static ArcanePressurePlateBlock ARCANE_PRESSURE_PLATE() {
            return Registry.SUPPLIER_ARCANE_PRESSURE_PLATE.get();
        }

        public static ArcaneBoreBaseBlock ARCANE_BORE_BASE() {
            return Registry.SUPPLIER_ARCANE_BORE_BASE.get();
        }

        public static ArcaneBoreDrillBlock ARCANE_BORE_DRILL() {
            return Registry.SUPPLIER_ARCANE_BORE_DRILL.get();
        }

        public static ArcaneEarBlock ARCANE_EAR() {
            return Registry.SUPPLIER_ARCANE_EAR.get();
        }

        public static DeathFluidBlock DEATH_FLUID() {
            return Registry.SUPPLIER_DEATH_FLUID.get();
        }

        public static PureFluidBlock PURE_FLUID() {
            return Registry.SUPPLIER_PURE_FLUID.get();
        }

        public static WardedBlock WARDED_BLOCK() {
            return Registry.SUPPLIER_WARDED_BLOCK.get();
        }

        public static HoleBlock HOLE() {
            return Registry.SUPPLIER_HOLE_BLOCK.get();
        }

        public static UrnLootBlock URN_LOOT() {
            return Registry.SUPPLIER_URN_LOOT.get();
        }

        public static CrateLootBlock CRATE_LOOT() {
            return Registry.SUPPLIER_CRATE_LOOT.get();
        }

        public static EldritchPortalBlock ELDRITCH_PORTAL() {
            return Registry.SUPPLIER_ELDRITCH_PORTAL_BLOCK.get();
        }

        public static MirrorBlock MIRROR() {
            return Registry.SUPPLIER_MIRROR.get();
        }

        public static EssentiaMirrorBlock ESSENTIA_MIRROR() {
            return Registry.SUPPLIER_ESSENTIA_MIRROR.get();
        }

        public static ArcanePedestalBlock ARCANE_PEDESTAL() {
            return Registry.SUPPLIER_ARCANE_PEDESTAL.get();
        }

        public static InfusionPillarBlock INFUSION_PILLAR() {
            return Registry.SUPPLIER_INFUSION_PILLAR.get();
        }

        public static InfusionMatrixBlock INFUSION_MATRIX() {
            return Registry.SUPPLIER_INFUSION_MATRIX.get();
        }

        public static WandRechargePedestalBlock WAND_RECHARGE_PEDESTAL() {
            return Registry.SUPPLIER_WAND_RECHARGE_PEDESTAL.get();
        }

        public static CompoundRechargeFocusBlock COMPOUND_RECHARGE_FOCUS() {
            return Registry.SUPPLIER_COMPOUND_RECHARGE_FOCUS.get();
        }

        public static ArcaneSpaBlock ARCANE_SPA() {
            return Registry.SUPPLIER_ARCANE_SPA.get();
        }

        public static FocalManipulatorBlock FOCAL_MANIPULATOR() {
            return Registry.SUPPLIER_FOCAL_MANIPULATOR.get();
        }

        public static FluxScrubberBlock FLUX_SCRUBBER() {
            return Registry.SUPPLIER_FLUX_SCRUBBER.get();
        }
    }

    public static class Registry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.BLOCK);
        public static final RegistrySupplier<FluxGooBlock> SUPPLIER_FLUX_GOO = BLOCKS.register(
                "flux_goo", FluxGooBlock::new);
        public static final RegistrySupplier<FluxGasBlock> SUPPLIER_FLUX_GAS = BLOCKS.register(
                "flux_gas", FluxGasBlock::new);
        public static final RegistrySupplier<NitorBlock> SUPPLIER_NITOR_BLOCK = BLOCKS.register(
                "nitor", NitorBlock::new);
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
        public static final RegistrySupplier<Block> SUPPLIER_ANCIENT_STONE = BLOCKS.register(
                "ancient_stone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.F, 10.F)));
        public static final RegistrySupplier<Block> SUPPLIER_ANCIENT_ROCK = BLOCKS.register(
                "ancient_rock", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE).strength(2.F,10.F))
        );
        public static final RegistrySupplier<Block> SUPPLIER_CRUSTED_STONE = BLOCKS.register(
                "crusted_stone", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(2.F, 10.F)
                        .lightLevel(s -> 4))
        );
        public static final RegistrySupplier<Block> SUPPLIER_ANCIENT_STONE_PEDESTAL = BLOCKS.register(
                "ancient_stone_pedestal", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(2.F, 10.F))
        );
        public static final RegistrySupplier<StairBlock> SUPPLIER_ANCIENT_STONE_STAIRS = BLOCKS.register(
                "ancient_stone_stairs", () -> new StairBlock(ThaumcraftBlockInstances.ANCIENT_STONE().defaultBlockState(),
                        BlockBehaviour.Properties.copy(ThaumcraftBlockInstances.ANCIENT_STONE()))
        );
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
        public static final RegistrySupplier<SlabBlock> SUPPLIER_ANCIENT_STONE_SLAB = BLOCKS.register(
                "ancient_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(ThaumcraftBlockInstances.ANCIENT_STONE())));
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
                () -> new AbstractCrystalBlock(new int[]{Aspects.AIR.color}) {
                }
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_FIRE_CRYSTAL = BLOCKS.register(
                "fire_crystal_cluster",
                () -> new AbstractCrystalBlock(new int[]{0xff3c01}) {
                }
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_WATER_CRYSTAL = BLOCKS.register(
                "water_crystal_cluster",
                () -> new AbstractCrystalBlock(new int[]{0x0090ff}) {
                }
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_EARTH_CRYSTAL = BLOCKS.register(
                "earth_crystal_cluster",
                () -> new AbstractCrystalBlock(new int[]{Aspects.EARTH.color}) {
                }
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_ORDER_CRYSTAL = BLOCKS.register(
                "order_crystal_cluster",
                () -> new AbstractCrystalBlock(new int[]{0xeeccff}) {
                }
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_ENTROPY_CRYSTAL = BLOCKS.register(
                "entropy_crystal_cluster",
                () -> new AbstractCrystalBlock(new int[]{0x555577}) {
                }
        );
        private static final @RGBColor int[] MIXED_PARTICLE_COLORS = new @RGBColor int[]{
                0xffff7e,
                0xff3c01,
                0x0090ff,
                0x00a000,
                0xeeccff,
                0x555577
        };
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_MIXED_CRYSTAL = BLOCKS.register(
                "mixed_crystal_cluster",
                () -> new AbstractCrystalBlock(MIXED_PARTICLE_COLORS) {
                }
        );
        public static final RegistrySupplier<AbstractCrystalBlock> SUPPLIER_STRANGE_CRYSTALS = BLOCKS.register(
                "strange_crystals",
                () -> new AbstractCrystalBlock(new int[]{0xFFFFFF}) {
                }
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
                                .strength(1.5f, 5.f)
                )
        );
        public static final RegistrySupplier<DropExperienceBlock> SUPPLIER_AMBER_ORE = BLOCKS.register(
                "amber_ore",
                () -> new DropExperienceBlock(
                        BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)
                                .sound(SoundType.STONE)
                                .strength(1.5f, 5.f), UniformInt.of(1, 4)
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
                        .strength(1.5f, 5.f)
                )
        );
        public static final RegistrySupplier<Block> SUPPLIER_AMBER_BRICK = BLOCKS.register(
                "amber_brick",
                () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)
                        .strength(1.5f, 5.f)
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
                        .strength(-1, Float.MAX_VALUE)
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
                "arcane_workbench",
                ArcaneWorkbenchBlock::new
        );
        public static final RegistrySupplier<DeconstructionTableBlock> SUPPLIER_DECONSTRUCTION_TABLE = BLOCKS.register(
                "deconstruction_table",
                DeconstructionTableBlock::new
        );
        public static final RegistrySupplier<ResearchTableLeftPartBlock> SUPPLIER_RESEARCH_TABLE_LEFT_PART = BLOCKS.register(
                "research_table_left_part",
                ResearchTableLeftPartBlock::new
        );
        public static final RegistrySupplier<ResearchTableRightPartBlock> SUPPLIER_RESEARCH_TABLE_RIGHT_PART = BLOCKS.register(
                "research_table_right_part",
                ResearchTableRightPartBlock::new
        );
        public static final RegistrySupplier<GlimmerOfLightBlock> SUPPLIER_GLIMMER_OF_LIGHT = BLOCKS.register(
                "glimmer_of_light",
                GlimmerOfLightBlock::new
        );
        public static final RegistrySupplier<VisNetRelayBlock> SUPPLIER_VIS_RELAY = BLOCKS.register(
                "vis_relay",
                VisNetRelayBlock::new
        );
        public static final RegistrySupplier<VisNetChargeRelayBlock> SUPPLIER_VIS_CHARGE_RELAY = BLOCKS.register(
                "vis_charge_relay",
                VisNetChargeRelayBlock::new
        );
        public static final RegistrySupplier<EnergizedAuraNodeBlock> SUPPLIER_ENERGIZED_NODE = BLOCKS.register(
                "energized_node",
                EnergizedAuraNodeBlock::new
        );
        public static final RegistrySupplier<NodeStabilizerBlock> SUPPLIER_NODE_STABILIZER =
                BLOCKS.register(
                        "node_stabilizer",
                        NodeStabilizerBlock::new
                );
        public static final RegistrySupplier<AdvancedNodeStabilizerBlock> SUPPLIER_ADVANCED_NODE_STABILIZER =
                BLOCKS.register(
                        "advanced_node_stabilizer",
                        AdvancedNodeStabilizerBlock::new
                );
        public static final RegistrySupplier<NodeTransducerBlock> SUPPLIER_NODE_TRANSDUCER =
                BLOCKS.register(
                        "node_transducer",
                        NodeTransducerBlock::new
                );
        public static final RegistrySupplier<ImpassableBlock> SUPPLIER_IMPASSABLE_BLOCK =
                BLOCKS.register(
                        "impassable",
                        ImpassableBlock::new
                );
        public static final RegistrySupplier<SappingFieldBlock> SUPPLIER_SAPPING_FIELD =
                BLOCKS.register(
                        "sapping_field",
                        SappingFieldBlock::new
                );
        public static final RegistrySupplier<StaticFieldBlock> SUPPLIER_STATIC_FIELD =
                BLOCKS.register(
                        "static_field",
                        StaticFieldBlock::new
                );
        public static final RegistrySupplier<AlchemicalFurnaceBlock> SUPPLIER_ALCHEMICAL_FURNACE =
                BLOCKS.register(
                        "alchemical_furnace",
                        AlchemicalFurnaceBlock::new
                );
        public static final RegistrySupplier<Block> SUPPLIER_ADVANCED_ALCHEMICAL_CONSTRUCT =
                BLOCKS.register(
                        "advanced_alchemical_construct",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                .lightLevel(s -> 11)
                                .strength(3, 17)
                        )
                );
        public static final RegistrySupplier<Block> SUPPLIER_ALCHEMICAL_CONSTRUCT =
                BLOCKS.register(
                        "alchemical_construct",
                        () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                                .strength(3, 17)
                        )
                );
        public static final RegistrySupplier<ArcaneAlembicBlock> SUPPLIER_ARCANE_ALEMBIC = BLOCKS.register(
                "arcane_alembic",
                ArcaneAlembicBlock::new
        );
        public static final RegistrySupplier<AdvancedAlchemicalFurnaceAlembicBlock> SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_ALEMBIC =
                BLOCKS.register(
                        "advanced_alchemical_furnace_alembic",
                        AdvancedAlchemicalFurnaceAlembicBlock::new
                );
        public static final RegistrySupplier<AdvancedAlchemicalFurnaceUpperFenceBlock> SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_UPPER_FENCE =
                BLOCKS.register(
                        "advanced_alchemical_furnace_upper_fence",
                        AdvancedAlchemicalFurnaceUpperFenceBlock::new
                );
        public static final RegistrySupplier<AdvancedAlchemicalFurnaceNozzleBlock> SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_NOZZLE =
                BLOCKS.register(
                        "advanced_alchemical_furnace_nozzle",
                        AdvancedAlchemicalFurnaceNozzleBlock::new
                );
        public static final RegistrySupplier<AdvancedAlchemicalFurnaceBaseCornerBlock> SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_BASE_CORNER =
                BLOCKS.register(
                        "advanced_alchemical_furnace_base_corner",
                        AdvancedAlchemicalFurnaceBaseCornerBlock::new
                );
        public static final RegistrySupplier<AdvancedAlchemicalFurnaceBaseBlock> SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_BASE =
                BLOCKS.register(
                        "advanced_alchemical_furnace_base",
                        AdvancedAlchemicalFurnaceBaseBlock::new
                );
        public static final RegistrySupplier<EssentiaJarBlock> SUPPLIER_ESSENTIA_JAR =
                BLOCKS.register(
                        "essentia_jar",
                        EssentiaJarBlock::new
                );
        public static final RegistrySupplier<VoidJarBlock> SUPPLIER_VOID_JAR =
                BLOCKS.register(
                        "void_jar",
                        VoidJarBlock::new
                );
        public static final RegistrySupplier<BrainJarBlock> SUPPLIER_BRAIN_JAR =
                BLOCKS.register(
                        "brain_jar",
                        BrainJarBlock::new
                );
        public static final RegistrySupplier<NodeJarBlock> SUPPLIER_NODE_JAR =
                BLOCKS.register(
                        "node_jar",
                        NodeJarBlock::new
                );
        public static final RegistrySupplier<ItemCrateBlock> SUPPLIER_ITEM_CRATE =
                BLOCKS.register(
                        "item_crate",
                        ItemCrateBlock::new
                );
        public static final RegistrySupplier<CrucibleBlock> SUPPLIER_CRUCIBLE =
                BLOCKS.register(
                        "crucible",
                        CrucibleBlock::new
                );
        public static final RegistrySupplier<ArcaneLampBlock> SUPPLIER_ARCANE_LAMP =
                BLOCKS.register(
                        "arcane_lamp",
                        ArcaneLampBlock::new
                );
        public static final RegistrySupplier<GrowthArcaneLampBlock> SUPPLIER_GROWTH_ARCANE_LAMP =
                BLOCKS.register(
                        "growth_arcane_lamp",
                        GrowthArcaneLampBlock::new
                );
        public static final RegistrySupplier<FertilityArcaneLampBlock> SUPPLIER_FERTILITY_ARCANE_LAMP =
                BLOCKS.register(
                        "fertility_arcane_lamp",
                        FertilityArcaneLampBlock::new
                );
        public static final RegistrySupplier<ThaumatoriumBottomBlock> SUPPLIER_THAUMATORIUM_BOTTOM =
                BLOCKS.register(
                        "thaumatorium_bottom",
                        ThaumatoriumBottomBlock::new
                );
        public static final RegistrySupplier<ThaumatoriumTopBlock> SUPPLIER_THAUMATORIUM_TOP =
                BLOCKS.register(
                        "thaumatorium_top",
                        ThaumatoriumTopBlock::new
                );
        public static final RegistrySupplier<MnemonicMatrixBlock> SUPPLIER_MNEMONIC_MATRIX =
                BLOCKS.register(
                        "mnemonic_matrix",
                        MnemonicMatrixBlock::new
                );
        public static final RegistrySupplier<ArcaneLevitatorBubbleBlock> SUPPLIER_ARCANE_LEVITATOR_BUBBLE =
                BLOCKS.register(
                        "arcane_levitator_bubble",
                        ArcaneLevitatorBubbleBlock::new
                );
        public static final RegistrySupplier<ArcaneLevitatorBlock> SUPPLIER_ARCANE_LEVITATOR =
                BLOCKS.register(
                        "arcane_levitator",
                        ArcaneLevitatorBlock::new
                );
        public static final RegistrySupplier<EssentiaReservoirBlock> SUPPLIER_ESSENTIA_RESERVOIR =
                BLOCKS.register(
                        "essentia_reservoir",
                        EssentiaReservoirBlock::new
                );
        public static final RegistrySupplier<ManaBeanBlock> SUPPLIER_MANA_BEAN =
                BLOCKS.register(
                        "mana_bean",
                        ManaBeanBlock::new
                );

        public static final RegistrySupplier<EssentiaTubeBlock> SUPPLIER_ESSENTIA_TUBE =
                BLOCKS.register(
                        "essentia_tube",
                        EssentiaTubeBlock::new
                );
        public static final RegistrySupplier<EssentiaTubeValveBlock> SUPPLIER_ESSENTIA_TUBE_VALVE =
                BLOCKS.register(
                        "essentia_tube_valve",
                        EssentiaTubeValveBlock::new
                );
        public static final RegistrySupplier<EssentiaTubeFilterBlock> SUPPLIER_ESSENTIA_TUBE_FILTER =
                BLOCKS.register(
                        "essentia_tube_filter",
                        EssentiaTubeFilterBlock::new
                );
        public static final RegistrySupplier<EssentiaTubeRestrictBlock> SUPPLIER_ESSENTIA_TUBE_RESTRICT =
                BLOCKS.register(
                        "essentia_tube_restrict",
                        EssentiaTubeRestrictBlock::new
                );
        public static final RegistrySupplier<EssentiaTubeOnewayBlock> SUPPLIER_ESSENTIA_TUBE_ONEWAY =
                BLOCKS.register(
                        "essentia_tube_oneway",
                        EssentiaTubeOnewayBlock::new
                );
        public static final RegistrySupplier<EssentiaBufferBlock> SUPPLIER_ESSENTIA_BUFFER =
                BLOCKS.register(
                        "essentia_buffer",
                        EssentiaBufferBlock::new
                );
        public static final RegistrySupplier<EssentiaCentrifugeBlock> SUPPLIER_ESSENTIA_CENTRIFUGE =
                BLOCKS.register(
                        "essentia_centrifuge",
                        EssentiaCentrifugeBlock::new
                );
        public static final RegistrySupplier<EssentiaCrystallizerBlock> SUPPLIER_ESSENTIA_CRYSTALLIZER =
                BLOCKS.register(
                        "essentia_crystallizer",
                        EssentiaCrystallizerBlock::new
                );
        public static final RegistrySupplier<ArcanePressurePlateBlock> SUPPLIER_ARCANE_PRESSURE_PLATE =
                BLOCKS.register(
                        "arcane_pressure_plate",
                        ArcanePressurePlateBlock::new
                );
        public static final RegistrySupplier<ArcaneBoreBaseBlock> SUPPLIER_ARCANE_BORE_BASE =
                BLOCKS.register(
                        "arcane_bore_base",
                        ArcaneBoreBaseBlock::new
                );
        public static final RegistrySupplier<ArcaneBoreDrillBlock> SUPPLIER_ARCANE_BORE_DRILL =
                BLOCKS.register(
                        "arcane_bore_drill",
                        ArcaneBoreDrillBlock::new
                );
        public static final RegistrySupplier<ArcaneEarBlock> SUPPLIER_ARCANE_EAR =
                BLOCKS.register(
                        "arcane_ear",
                        ArcaneEarBlock::new
                );
        public static final RegistrySupplier<DeathFluidBlock> SUPPLIER_DEATH_FLUID =
                BLOCKS.register(
                        "death_fluid",
                        DeathFluidBlock::new
                );
        public static final RegistrySupplier<PureFluidBlock> SUPPLIER_PURE_FLUID =
                BLOCKS.register(
                        "pure_fluid",
                        PureFluidBlock::new
                );
        public static final RegistrySupplier<WardedBlock> SUPPLIER_WARDED_BLOCK =
                BLOCKS.register(
                        "warded_block",
                        WardedBlock::new
                );
        public static final RegistrySupplier<HoleBlock> SUPPLIER_HOLE_BLOCK =
                BLOCKS.register(
                        "hole",
                        HoleBlock::new
                );
        public static final RegistrySupplier<UrnLootBlock> SUPPLIER_URN_LOOT =
                BLOCKS.register(
                        "urn_loot",
                        UrnLootBlock::new
                );
        public static final RegistrySupplier<CrateLootBlock> SUPPLIER_CRATE_LOOT =
                BLOCKS.register(
                        "crate_loot",
                        CrateLootBlock::new
                );
        public static final RegistrySupplier<EldritchPortalBlock> SUPPLIER_ELDRITCH_PORTAL_BLOCK =
                BLOCKS.register(
                        "eldritch_portal",
                        EldritchPortalBlock::new
                );
        public static final RegistrySupplier<MirrorBlock> SUPPLIER_MIRROR =
                BLOCKS.register(
                        "mirror",
                        MirrorBlock::new
                );
        public static final RegistrySupplier<EssentiaMirrorBlock> SUPPLIER_ESSENTIA_MIRROR =
                BLOCKS.register(
                        "essentia_mirror",
                        EssentiaMirrorBlock::new
                );
        public static final RegistrySupplier<ArcanePedestalBlock> SUPPLIER_ARCANE_PEDESTAL =
                BLOCKS.register(
                        "arcane_pedestal",
                        ArcanePedestalBlock::new
                );
        public static final RegistrySupplier<InfusionPillarBlock> SUPPLIER_INFUSION_PILLAR =
                BLOCKS.register(
                        "infusion_pillar",
                        InfusionPillarBlock::new
                );
        public static final RegistrySupplier<InfusionMatrixBlock> SUPPLIER_INFUSION_MATRIX =
                BLOCKS.register(
                        "infusion_matrix",
                        InfusionMatrixBlock::new
                );
        public static final RegistrySupplier<WandRechargePedestalBlock> SUPPLIER_WAND_RECHARGE_PEDESTAL =
                BLOCKS.register(
                        "wand_recharge_pedestal",
                        WandRechargePedestalBlock::new
                );
        public static final RegistrySupplier<CompoundRechargeFocusBlock> SUPPLIER_COMPOUND_RECHARGE_FOCUS =
                BLOCKS.register(
                        "compound_recharge_focus",
                        CompoundRechargeFocusBlock::new
                );
        public static final RegistrySupplier<ArcaneSpaBlock> SUPPLIER_ARCANE_SPA =
                BLOCKS.register(
                        "arcane_spa",
                        ArcaneSpaBlock::new
                );
        public static final RegistrySupplier<FocalManipulatorBlock> SUPPLIER_FOCAL_MANIPULATOR =
                BLOCKS.register(
                        "focal_manipulator",
                        FocalManipulatorBlock::new
                );
        public static final RegistrySupplier<FluxScrubberBlock> SUPPLIER_FLUX_SCRUBBER =
                BLOCKS.register(
                        "flux_scrubber",
                        FluxScrubberBlock::new
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

    public static class Tags {
        public static final TagKey<Block> JAR_BLOCK = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "jar_block")
        );//TODO:Jars put in
        public static final TagKey<Block> CRUCIBLE_HEATER = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "crucible_heater")
        );
        public static final TagKey<Block> REDSTONE_CONTROLLABLE_CRUCIBLE_HEATER = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "redstone_controllable_crucible_heater")
        );
        public static final TagKey<Block> GROWTH_LAMP_NOT_AFFECTIVE = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "growth_lamp_not_affective")
        );
        public static final TagKey<Block> GROWTH_LAMP_AFFECTIVE = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "growth_lamp_affective")
        );
        public static final TagKey<Block> GROWTH_LAMP_AFFECTIVE_RANDOM_TICK = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "growth_lamp_affective_random_tick")
        );
        public static final TagKey<Block> GROWTH_LAMP_AFFECTIVE_TICK = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "growth_lamp_affective_tick")
        );
        public static final TagKey<Block> GLIMMER_OF_LIGHT_WONT_OVERRIDE = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "glimmer_of_light_wont_override")
        );
        public static final TagKey<Block> MANA_BEAN_SURVIVES = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "mana_bean_survives")
        );
        public static final TagKey<Block> REFLECTS_PLAYER = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "reflects_player")
        );
        public static final TagKey<Block> PRIMAL_CRUSHER_MINEABLE =
                TagKey.create(
                        Registries.BLOCK,
                        new ResourceLocation(Thaumcraft.MOD_ID, "primal_crusher_mineable")
                );


        public static final TagKey<Block> ELEMENTAL_HOE_NOT_AFFECTIVE = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "elemental_hoe_not_affective")
        );
        public static final TagKey<Block> ELEMENTAL_HOE_AFFECTIVE = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "elemental_hoe_affective")
        );
        public static final TagKey<Block> ELEMENTAL_HOE_AFFECTIVE_RANDOM_TICK = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "elemental_hoe_affective_random_tick")
        );
        public static final TagKey<Block> ELEMENTAL_HOE_AFFECTIVE_TICK = TagKey.create(
                Registries.BLOCK, new ResourceLocation(Thaumcraft.MOD_ID, "elemental_hoe_affective_tick")
        );
    }
}
