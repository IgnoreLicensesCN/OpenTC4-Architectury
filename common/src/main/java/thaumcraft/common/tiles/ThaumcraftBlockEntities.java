package thaumcraft.common.tiles;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.abstracts.owned.KeyableOwnedBlockEntity;
import thaumcraft.common.tiles.abstracts.owned.OwnedBlockEntity;
import thaumcraft.common.tiles.crafted.*;
import thaumcraft.common.tiles.crafted.essentiabe.*;
import thaumcraft.common.tiles.crafted.essentiabe.advancedalchemicalfurnace.*;
import thaumcraft.common.tiles.crafted.essentiabe.jars.EssentiaJarBlockEntity;
import thaumcraft.common.tiles.crafted.essentiabe.jars.VoidJarBlockEntity;
import thaumcraft.common.tiles.crafted.essentiabe.pipes.*;
import thaumcraft.common.tiles.crafted.infernalfurnace.InfernalFurnaceBlockEntity;
import thaumcraft.common.tiles.crafted.infernalfurnace.InfernalFurnaceNozzleBlockEntity;
import thaumcraft.common.tiles.crafted.infusion.ArcanePedestalBlockEntity;
import thaumcraft.common.tiles.crafted.infusion.InfusionMatrixBlockEntity;
import thaumcraft.common.tiles.crafted.jars.*;
import thaumcraft.common.tiles.crafted.lamp.*;
import thaumcraft.common.tiles.crafted.mirror.EssentiaMirrorBlockEntity;
import thaumcraft.common.tiles.crafted.mirror.MirrorBlockEntity;
import thaumcraft.common.tiles.crafted.vis.FocalManipulatorBlockEntity;
import thaumcraft.common.tiles.crafted.vis.visnet.EnergizedAuraNodeBlockEntity;
import thaumcraft.common.tiles.crafted.vis.visnet.NodeTransducerBlockEntity;
import thaumcraft.common.tiles.crafted.vis.visnet.VisNetChargeRelayBlockEntity;
import thaumcraft.common.tiles.crafted.vis.visnet.VisNetRelayBlockEntity;
import thaumcraft.common.tiles.eldritch.*;
import thaumcraft.common.tiles.generated.*;
import thaumcraft.common.tiles.node.*;
import thaumcraft.common.tiles.technique.HoleBlockEntity;
import thaumcraft.common.tiles.technique.WardedBlockEntity;

public class ThaumcraftBlockEntities {

    public static class BlockEntityTypeInstances {

        public static BlockEntityType<NodeBlockEntity> AURA_NODE() {
            return Registry.SUPPLIER_AURA_NODE.get();
        }

        public static BlockEntityType<SilverWoodKnotNodeBlockEntity> SILVERWOOD_KNOT_NODE() {
            return Registry.SUPPLIER_SILVERWOOD_KNOT_NODE.get();
        }

        public static BlockEntityType<ObsidianTotemNodeBlockEntity> OBSIDIAN_TOTEM_NODE() {
            return Registry.SUPPLIER_OBSIDIAN_TOTEM_NODE.get();
        }

        public static BlockEntityType<WardingStoneBlockEntity> WARDING_STONE() {
            return Registry.SUPPLIER_WARDING_STONE.get();
        }

        public static BlockEntityType<HungryChestBlockEntity> HUNGRY_CHEST() {
            return Registry.SUPPLIER_HUNGRY_CHEST.get();
        }

        public static BlockEntityType<EldritchVoidBlockEntity> ELDRITCH_VOID() {
            return Registry.SUPPLIER_ELDRITCH_VOID.get();
        }

        public static BlockEntityType<OwnedBlockEntity> OWNED() {
            return Registry.SUPPLIER_OWNED.get();
        }

        public static BlockEntityType<EtherealBloomBlockEntity> ETHEREAL_BLOOM() {
            return Registry.SUPPLIER_ETHEREAL_BLOOM.get();
        }

        public static BlockEntityType<InfernalFurnaceBlockEntity> INFERNAL_FURNACE() {
            return Registry.SUPPLIER_INFERNAL_FURNACE.get();
        }

        public static BlockEntityType<EldritchAltarBlockEntity> ELDRITCH_ALTAR() {
            return Registry.SUPPLIER_ELDRITCH_ALTAR.get();
        }

        public static BlockEntityType<EldritchObeliskBlockEntity> ELDRITCH_OBELISK() {
            return Registry.SUPPLIER_ELDRITCH_OBELISK.get();
        }

        public static BlockEntityType<EldritchObeliskWithTickerBlockEntity> ELDRITCH_OBELISK_WITH_TICKER() {
            return Registry.SUPPLIER_ELDRITCH_OBELISK_WITH_TICKER.get();
        }

        public static BlockEntityType<EldritchCapstoneBlockEntity> ELDRITCH_CAPSTONE() {
            return Registry.SUPPLIER_ELDRITCH_CAPSTONE.get();
        }

        public static BlockEntityType<AncientLockInsertedBlockEntity> ANCIENT_LOCK_INSERTED() {
            return Registry.SUPPLIER_ANCIENT_LOCK_INSERTED.get();
        }

        public static BlockEntityType<EldritchCrabSpawnerBlockEntity> ELDRITCH_CRAB_SPAWNER() {
            return Registry.SUPPLIER_ELDRITCH_CRAB_SPAWNER.get();
        }

        public static BlockEntityType<RunedStoneBlockEntity> RUNED_STONE() {
            return Registry.SUPPLIER_RUNED_STONE.get();
        }

        public static BlockEntityType<ArcaneWorkbenchBlockEntity> ARCANE_WORKBENCH() {
            return Registry.SUPPLIER_ARCANE_WORKBENCH.get();
        }

        public static BlockEntityType<DeconstructionTableBlockEntity> DECONSTRUCTION_TABLE() {
            return Registry.SUPPLIER_DECONSTRUCTION_TABLE.get();
        }

        public static BlockEntityType<ResearchTableBlockEntity> RESEARCH_TABLE() {
            return Registry.SUPPLIER_RESEARCH_TABLE.get();
        }

        public static BlockEntityType<VisNetRelayBlockEntity> VIS_RELAY() {
            return Registry.SUPPLIER_VIS_RELAY.get();
        }

        public static BlockEntityType<VisNetChargeRelayBlockEntity> VIS_CHARGE_RELAY() {
            return Registry.SUPPLIER_VIS_CHARGE_RELAY.get();
        }

        public static BlockEntityType<EnergizedAuraNodeBlockEntity> ENERGIZED_NODE() {
            return Registry.SUPPLIER_ENERGIZED_NODE.get();
        }

        public static BlockEntityType<NodeTransducerBlockEntity> NODE_TRANSDUCER() {
            return Registry.SUPPLIER_NODE_TRANSDUCER.get();
        }

        public static BlockEntityType<AlchemicalFurnaceBlockEntity> ALCHEMICAL_FURNACE() {
            return Registry.SUPPLIER_ALCHEMICAL_FURNACE.get();
        }

        public static BlockEntityType<ArcaneAlembicBlockEntity> ARCANE_ALEMBIC() {
            return Registry.SUPPLIER_ARCANE_ALEMBIC.get();
        }

        public static BlockEntityType<AdvancedAlchemicalFurnaceBlockEntity> ADVANCED_ALCHEMICAL_FURNACE() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE.get();
        }

        public static BlockEntityType<AdvancedAlchemicalFurnaceNozzleBlockEntity> ADVANCED_ALCHEMICAL_FURNACE_NOZZLE() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_NOZZLE.get();
        }

        public static BlockEntityType<EssentiaJarBlockEntity> ESSENTIA_JAR() {
            return Registry.SUPPLIER_ESSENTIA_JAR.get();
        }

        public static BlockEntityType<VoidJarBlockEntity> VOID_JAR() {
            return Registry.SUPPLIER_VOID_JAR.get();
        }

        public static BlockEntityType<BrainJarBlockEntity> BRAIN_JAR() {
            return Registry.SUPPLIER_BRAIN_JAR.get();
        }

        public static BlockEntityType<NodeJarBlockEntity> NODE_JAR() {
            return Registry.SUPPLIER_NODE_JAR.get();
        }

        public static BlockEntityType<CrucibleBlockEntity> CRUCIBLE() {
            return Registry.SUPPLIER_CRUCIBLE.get();
        }

        public static BlockEntityType<GrowthArcaneLampBlockEntity> GROWTH_ARCANE_LAMP() {
            return Registry.SUPPLIER_GROWTH_ARCANE_LAMP.get();
        }

        public static BlockEntityType<FertilityArcaneLampBlockEntity> FERTILITY_ARCANE_LAMP() {
            return Registry.SUPPLIER_FERTILITY_ARCANE_LAMP.get();
        }

        public static BlockEntityType<ThaumatoriumBlockEntity> THAUMATORIUM() {
            return Registry.SUPPLIER_THAUMATORIUM.get();
        }

        public static BlockEntityType<EssentiaReservoirBlockEntity> ESSENTIA_RESERVOIR() {
            return Registry.SUPPLIER_ESSENTIA_RESERVOIR.get();
        }

        public static BlockEntityType<ManaBeanBlockEntity> MANA_BEAN() {
            return Registry.SUPPLIER_MANA_BEAN.get();
        }

        public static BlockEntityType<InfernalFurnaceNozzleBlockEntity> INFERNAL_FURNACE_NOZZLE() {
            return Registry.SUPPLIER_INFERNAL_FURNACE_NOZZLE.get();
        }

        public static BlockEntityType<EssentiaTubeBlockEntity> ESSENTIA_TUBE() {
            return Registry.SUPPLIER_ESSENTIA_TUBE.get();
        }

        public static BlockEntityType<EssentiaTubeValveBlockEntity> ESSENTIA_TUBE_VALVE() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_VALVE.get();
        }

        public static BlockEntityType<EssentiaTubeFilterBlockEntity> ESSENTIA_TUBE_FILTER() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_FILTER.get();
        }

        public static BlockEntityType<EssentiaTubeRestrictBlockEntity> ESSENTIA_TUBE_RESTRICT() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_RESTRICT.get();
        }

        public static BlockEntityType<EssentiaTubeOnewayBlockEntity> ESSENTIA_TUBE_ONEWAY() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_ONEWAY.get();
        }

        public static BlockEntityType<EssentiaBufferBlockEntity> ESSENTIA_BUFFER() {
            return Registry.SUPPLIER_ESSENTIA_BUFFER.get();
        }

        public static BlockEntityType<EssentiaCentrifugeBlockEntity> ESSENTIA_CENTRIFUGE() {
            return Registry.SUPPLIER_ESSENTIA_CENTRIFUGE.get();
        }

        public static BlockEntityType<EssentiaCrystallizerBlockEntity> ESSENTIA_CRYSTALLIZER() {
            return Registry.SUPPLIER_ESSENTIA_CRYSTALLIZER.get();
        }

        public static BlockEntityType<ArcaneBoreBlockEntity> ARCANE_BORE() {
            return Registry.SUPPLIER_ARCANE_BORE.get();
        }

        public static BlockEntityType<ArcaneEarBlockEntity> ARCANE_EAR() {
            return Registry.SUPPLIER_ARCANE_EAR.get();
        }

        public static BlockEntityType<WardedBlockEntity> WARDED_BLOCK() {
            return Registry.SUPPLIER_WARDED_BLOCK.get();
        }

        public static BlockEntityType<HoleBlockEntity> HOLE() {
            return Registry.SUPPLIER_HOLE.get();
        }

        public static BlockEntityType<EldritchPortalBlockEntity> ELDRITCH_PORTAL() {
            return Registry.SUPPLIER_ELDRITCH_PORTAL.get();
        }

        public static BlockEntityType<MirrorBlockEntity> MIRROR() {
            return Registry.SUPPLIER_MIRROR.get();
        }

        public static BlockEntityType<EssentiaMirrorBlockEntity> ESSENTIA_MIRROR() {
            return Registry.SUPPLIER_ESSENTIA_MIRROR.get();
        }

        public static BlockEntityType<ArcanePedestalBlockEntity> ARCANE_PEDESTAL() {
            return Registry.SUPPLIER_ARCANE_PEDESTAL.get();
        }

        public static BlockEntityType<InfusionMatrixBlockEntity> INFUSION_MATRIX() {
            return Registry.SUPPLIER_INFUSION_MATRIX.get();
        }

        public static BlockEntityType<WandRechargePedestalBlockBlockEntity> WAND_RECHARGE_PEDESTAL() {
            return Registry.SUPPLIER_WAND_RECHARGE_PEDESTAL.get();
        }

        public static BlockEntityType<ArcaneSpaBlockEntity> ARCANE_SPA() {
            return Registry.SUPPLIER_ARCANE_SPA.get();
        }

        public static BlockEntityType<FocalManipulatorBlockEntity> FOCAL_MANIPULATOR() {
            return Registry.SUPPLIER_FOCAL_MANIPULATOR.get();
        }

        public static BlockEntityType<FluxScrubberBlockEntity> FLUX_SCRUBBER() {
            return Registry.SUPPLIER_FLUX_SCRUBBER.get();
        }
        public static BlockEntityType<KeyableOwnedBlockEntity> KEYABLE_OWNED() {
            return Registry.SUPPLIER_KEYABLE_OWNED.get();
        }
    }

    public static class Registry{
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Thaumcraft.MOD_ID,
                Registries.BLOCK_ENTITY_TYPE
        );

        public static final RegistrySupplier<BlockEntityType<NodeBlockEntity>> SUPPLIER_AURA_NODE = BLOCK_ENTITIES.register(
                "node",
                () -> BlockEntityType.Builder.of(NodeBlockEntity::new, ThaumcraftBlocks.ThaumcraftBlockInstances.AURA_NODE()).build(null)//seems "type" not used,mekanism uses "null"
        );
        public static final RegistrySupplier<BlockEntityType<SilverWoodKnotNodeBlockEntity>> SUPPLIER_SILVERWOOD_KNOT_NODE = BLOCK_ENTITIES.register(
                "silverwood_knot_node",
                () -> BlockEntityType.Builder.of(SilverWoodKnotNodeBlockEntity::new, ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_KNOT()).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<ObsidianTotemNodeBlockEntity>> SUPPLIER_OBSIDIAN_TOTEM_NODE = BLOCK_ENTITIES.register(
                "obsidian_totem_node",
                () -> BlockEntityType.Builder.of(ObsidianTotemNodeBlockEntity::new, ThaumcraftBlocks.ThaumcraftBlockInstances.OBSIDIAN_TOTEM_WITH_NODE()).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<WardingStoneBlockEntity>> SUPPLIER_WARDING_STONE = BLOCK_ENTITIES.register(
                "warding_stone",
                () -> BlockEntityType.Builder.of(WardingStoneBlockEntity::new, ThaumcraftBlocks.ThaumcraftBlockInstances.PAVING_STONE_WARDING()).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<HungryChestBlockEntity>> SUPPLIER_HUNGRY_CHEST = BLOCK_ENTITIES.register(
                "hungry_chest",
                () -> BlockEntityType.Builder.of(HungryChestBlockEntity::new, ThaumcraftBlocks.ThaumcraftBlockInstances.HUNGRY_CHEST()).build(null)//seems "type" not used
        );
        public static final RegistrySupplier<BlockEntityType<EldritchVoidBlockEntity>> SUPPLIER_ELDRITCH_VOID = BLOCK_ENTITIES.register(
                "eldritch_void",
                () -> BlockEntityType.Builder.of(EldritchVoidBlockEntity::new, ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_VOID()).build(null)//seems "type" not used
        );

        public static final RegistrySupplier<BlockEntityType<OwnedBlockEntity>> SUPPLIER_OWNED = BLOCK_ENTITIES.register(
                "owned",
                () -> BlockEntityType.Builder.of(
                        OwnedBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.WARDED_GLASS()
                ).build(null)
        );

        public static final RegistrySupplier<BlockEntityType<EtherealBloomBlockEntity>> SUPPLIER_ETHEREAL_BLOOM = BLOCK_ENTITIES.register(
                "ethereal_bloom",
                () -> BlockEntityType.Builder.of(
                        EtherealBloomBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ETHEREAL_BLOOM()
                ).build(null)
        );

        public static final RegistrySupplier<BlockEntityType<InfernalFurnaceBlockEntity>> SUPPLIER_INFERNAL_FURNACE = BLOCK_ENTITIES.register(
                "infernal_furnace",
                () -> BlockEntityType.Builder.of(
                        InfernalFurnaceBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.INFERNAL_FURNACE_LAVA()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<EldritchAltarBlockEntity>> SUPPLIER_ELDRITCH_ALTAR = BLOCK_ENTITIES.register(
                "eldritch_altar",
                () -> BlockEntityType.Builder.of(
                        EldritchAltarBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_ALTAR()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<EldritchObeliskBlockEntity>> SUPPLIER_ELDRITCH_OBELISK = BLOCK_ENTITIES.register(
                "eldritch_obelisk",
                () -> BlockEntityType.Builder.of(
                        EldritchObeliskBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_OBELISK()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<EldritchObeliskWithTickerBlockEntity>> SUPPLIER_ELDRITCH_OBELISK_WITH_TICKER = BLOCK_ENTITIES.register(
                "eldritch_obelisk_with_ticker",
                () -> BlockEntityType.Builder.of(
                        EldritchObeliskWithTickerBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_OBELISK_WITH_TICKER()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<EldritchCapstoneBlockEntity>> SUPPLIER_ELDRITCH_CAPSTONE = BLOCK_ENTITIES.register(
                "eldritch_capstone",
                () -> BlockEntityType.Builder.of(
                        EldritchCapstoneBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_CAPSTONE()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<AncientLockInsertedBlockEntity>> SUPPLIER_ANCIENT_LOCK_INSERTED = BLOCK_ENTITIES.register(
                "ancient_lock_inserted",
                () -> BlockEntityType.Builder.of(
                        AncientLockInsertedBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_LOCK_INSERTED()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<EldritchCrabSpawnerBlockEntity>> SUPPLIER_ELDRITCH_CRAB_SPAWNER = BLOCK_ENTITIES.register(
                "eldritch_crab_spawner",
                () -> BlockEntityType.Builder.of(
                        EldritchCrabSpawnerBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_CRAB_SPAWNER()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<RunedStoneBlockEntity>> SUPPLIER_RUNED_STONE = BLOCK_ENTITIES.register(
                "runed_stone",
                () -> BlockEntityType.Builder.of(
                        RunedStoneBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<ArcaneWorkbenchBlockEntity>> SUPPLIER_ARCANE_WORKBENCH = BLOCK_ENTITIES.register(
                "arcane_workbench",
                () -> BlockEntityType.Builder.of(
                        ArcaneWorkbenchBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_WORKBENCH()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<DeconstructionTableBlockEntity>> SUPPLIER_DECONSTRUCTION_TABLE = BLOCK_ENTITIES.register(
                "deconstruction_table",
                () -> BlockEntityType.Builder.of(
                        DeconstructionTableBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.DECONSTRUCTION_TABLE()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<ResearchTableBlockEntity>> SUPPLIER_RESEARCH_TABLE = BLOCK_ENTITIES.register(
                "research_table",
                () -> BlockEntityType.Builder.of(
                        ResearchTableBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.RESEARCH_TABLE_LEFT_PART()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<VisNetRelayBlockEntity>> SUPPLIER_VIS_RELAY = BLOCK_ENTITIES.register(
                "vis_relay",
                () -> BlockEntityType.Builder.of(
                        VisNetRelayBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.VIS_RELAY()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<VisNetChargeRelayBlockEntity>> SUPPLIER_VIS_CHARGE_RELAY = BLOCK_ENTITIES.register(
                "vis_charge_relay",
                () -> BlockEntityType.Builder.of(
                        VisNetChargeRelayBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.VIS_CHARGE_RELAY()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<EnergizedAuraNodeBlockEntity>> SUPPLIER_ENERGIZED_NODE = BLOCK_ENTITIES.register(
                "energized_node",
                () -> BlockEntityType.Builder.of(
                        EnergizedAuraNodeBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ENERGIZED_NODE()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<NodeTransducerBlockEntity>> SUPPLIER_NODE_TRANSDUCER = BLOCK_ENTITIES.register(
                "node_transducer",
                () -> BlockEntityType.Builder.of(
                        NodeTransducerBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.NODE_TRANSDUCER()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<AlchemicalFurnaceBlockEntity>> SUPPLIER_ALCHEMICAL_FURNACE = BLOCK_ENTITIES.register(
                "alchemical_furnace",
                () -> BlockEntityType.Builder.of(
                        AlchemicalFurnaceBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ALCHEMICAL_FURNACE()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<ArcaneAlembicBlockEntity>> SUPPLIER_ARCANE_ALEMBIC = BLOCK_ENTITIES.register(
                "arcane_alembic",
                () -> BlockEntityType.Builder.of(
                        ArcaneAlembicBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_ALEMBIC()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<AdvancedAlchemicalFurnaceBlockEntity>> SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE = BLOCK_ENTITIES.register(
                "advanced_alchemical_furnace",
                () -> BlockEntityType.Builder.of(
                        AdvancedAlchemicalFurnaceBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ADVANCED_ALCHEMICAL_FURNACE_BASE()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<AdvancedAlchemicalFurnaceNozzleBlockEntity>> SUPPLIER_ADVANCED_ALCHEMICAL_FURNACE_NOZZLE = BLOCK_ENTITIES.register(
                "advanced_alchemical_furnace_nozzle",
                () -> BlockEntityType.Builder.of(
                        AdvancedAlchemicalFurnaceNozzleBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ADVANCED_ALCHEMICAL_FURNACE_NOZZLE()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<EssentiaJarBlockEntity>> SUPPLIER_ESSENTIA_JAR = BLOCK_ENTITIES.register(
                "essentia_jar",
                () -> BlockEntityType.Builder.of(
                        EssentiaJarBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_JAR()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<VoidJarBlockEntity>> SUPPLIER_VOID_JAR = BLOCK_ENTITIES.register(
                "void_jar",
                () -> BlockEntityType.Builder.of(
                        VoidJarBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.VOID_JAR()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<BrainJarBlockEntity>> SUPPLIER_BRAIN_JAR = BLOCK_ENTITIES.register(
                "brain_jar",
                () -> BlockEntityType.Builder.of(
                        BrainJarBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.BRAIN_JAR()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<NodeJarBlockEntity>> SUPPLIER_NODE_JAR = BLOCK_ENTITIES.register(
                "node_jar",
                () -> BlockEntityType.Builder.of(
                        NodeJarBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.NODE_JAR()
                ).build(null)
        );
        public static final RegistrySupplier<BlockEntityType<CrucibleBlockEntity>> SUPPLIER_CRUCIBLE = BLOCK_ENTITIES.register(
                "crucible",
                () -> BlockEntityType.Builder.of(
                        CrucibleBlockEntity::new,
                        ThaumcraftBlocks.ThaumcraftBlockInstances.CRUCIBLE()
                ).build(null)
        );

        public static final RegistrySupplier<BlockEntityType<GrowthArcaneLampBlockEntity>> SUPPLIER_GROWTH_ARCANE_LAMP =
                BLOCK_ENTITIES.register(
                        "growth_arcane_lamp",
                        () -> BlockEntityType.Builder.of(
                                GrowthArcaneLampBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.GROWTH_ARCANE_LAMP()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<FertilityArcaneLampBlockEntity>> SUPPLIER_FERTILITY_ARCANE_LAMP =
                BLOCK_ENTITIES.register(
                        "fertility_arcane_lamp",
                        () -> BlockEntityType.Builder.of(
                                FertilityArcaneLampBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.FERTILITY_ARCANE_LAMP()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<ThaumatoriumBlockEntity>> SUPPLIER_THAUMATORIUM =
                BLOCK_ENTITIES.register(
                        "thaumatorium",

                        () -> BlockEntityType.Builder.of(
                                ThaumatoriumBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.THAUMATORIUM_BOTTOM()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaReservoirBlockEntity>> SUPPLIER_ESSENTIA_RESERVOIR =
                BLOCK_ENTITIES.register(
                        "essentia_reservoir",

                        () -> BlockEntityType.Builder.of(
                                EssentiaReservoirBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_RESERVOIR()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<ManaBeanBlockEntity>> SUPPLIER_MANA_BEAN =
                BLOCK_ENTITIES.register(
                        "mana_bean",
                        () -> BlockEntityType.Builder.of(
                                ManaBeanBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.MANA_BEAN()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<InfernalFurnaceNozzleBlockEntity>> SUPPLIER_INFERNAL_FURNACE_NOZZLE =
                BLOCK_ENTITIES.register(
                        "infernal_furnace_nozzle",
                        () -> BlockEntityType.Builder.of(
                                InfernalFurnaceNozzleBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.INFERNAL_FURNACE_SIDE()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaTubeBlockEntity>> SUPPLIER_ESSENTIA_TUBE =
                BLOCK_ENTITIES.register(
                        "essentia_tube",
                        () -> BlockEntityType.Builder.of(
                                EssentiaTubeBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaTubeValveBlockEntity>> SUPPLIER_ESSENTIA_TUBE_VALVE =
                BLOCK_ENTITIES.register(
                        "essentia_tube_valve",
                        () -> BlockEntityType.Builder.of(
                                EssentiaTubeValveBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_VALVE()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaTubeFilterBlockEntity>> SUPPLIER_ESSENTIA_TUBE_FILTER =
                BLOCK_ENTITIES.register(
                        "essentia_tube_filter",
                        () -> BlockEntityType.Builder.of(
                                EssentiaTubeFilterBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_FILTER()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaTubeRestrictBlockEntity>> SUPPLIER_ESSENTIA_TUBE_RESTRICT =
                BLOCK_ENTITIES.register(
                        "essentia_tube_restrict",
                        () -> BlockEntityType.Builder.of(
                                EssentiaTubeRestrictBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_RESTRICT()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaTubeOnewayBlockEntity>> SUPPLIER_ESSENTIA_TUBE_ONEWAY =
                BLOCK_ENTITIES.register(
                        "essentia_tube_oneway",
                        () -> BlockEntityType.Builder.of(
                                EssentiaTubeOnewayBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_ONEWAY()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaBufferBlockEntity>> SUPPLIER_ESSENTIA_BUFFER =
                BLOCK_ENTITIES.register(
                        "essentia_buffer",
                        () -> BlockEntityType.Builder.of(
                                EssentiaBufferBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_BUFFER()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaCentrifugeBlockEntity>> SUPPLIER_ESSENTIA_CENTRIFUGE =
                BLOCK_ENTITIES.register(
                        "essentia_centrifuge",
                        () -> BlockEntityType.Builder.of(
                                EssentiaCentrifugeBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_CENTRIFUGE()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaCrystallizerBlockEntity>> SUPPLIER_ESSENTIA_CRYSTALLIZER =
                BLOCK_ENTITIES.register(
                        "essentia_crystallizer",
                        () -> BlockEntityType.Builder.of(
                                EssentiaCrystallizerBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_CRYSTALLIZER()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<ArcaneBoreBlockEntity>> SUPPLIER_ARCANE_BORE =
                BLOCK_ENTITIES.register(
                        "arcane_bore",
                        () -> BlockEntityType.Builder.of(
                                ArcaneBoreBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_BORE_BASE()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<ArcaneEarBlockEntity>> SUPPLIER_ARCANE_EAR =
                BLOCK_ENTITIES.register(
                        "arcane_ear",
                        () -> BlockEntityType.Builder.of(
                                ArcaneEarBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_EAR()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<WardedBlockEntity>> SUPPLIER_WARDED_BLOCK =
                BLOCK_ENTITIES.register(
                        "warded_block",
                        () -> BlockEntityType.Builder.of(
                                WardedBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.WARDED_BLOCK()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<HoleBlockEntity>> SUPPLIER_HOLE =
                BLOCK_ENTITIES.register(
                        "hole",
                        () -> BlockEntityType.Builder.of(
                                HoleBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.HOLE()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EldritchPortalBlockEntity>> SUPPLIER_ELDRITCH_PORTAL =
                BLOCK_ENTITIES.register(
                        "eldritch_portal",
                        () -> BlockEntityType.Builder.of(
                                EldritchPortalBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_PORTAL()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<MirrorBlockEntity>> SUPPLIER_MIRROR =
                BLOCK_ENTITIES.register(
                        "mirror",
                        () -> BlockEntityType.Builder.of(
                                MirrorBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.MIRROR()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<EssentiaMirrorBlockEntity>> SUPPLIER_ESSENTIA_MIRROR =
                BLOCK_ENTITIES.register(
                        "essentia_mirror",
                        () -> BlockEntityType.Builder.of(
                                EssentiaMirrorBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_MIRROR()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<ArcanePedestalBlockEntity>> SUPPLIER_ARCANE_PEDESTAL =
                BLOCK_ENTITIES.register(
                        "arcane_pedestal",
                        () -> BlockEntityType.Builder.of(
                                ArcanePedestalBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_PEDESTAL()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<InfusionMatrixBlockEntity>> SUPPLIER_INFUSION_MATRIX =
                BLOCK_ENTITIES.register(
                        "infusion_matrix",
                        () -> BlockEntityType.Builder.of(
                                InfusionMatrixBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.INFUSION_MATRIX()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<WandRechargePedestalBlockBlockEntity>> SUPPLIER_WAND_RECHARGE_PEDESTAL =
                BLOCK_ENTITIES.register(
                        "wand_recharge_pedestal",
                        () -> BlockEntityType.Builder.of(
                                WandRechargePedestalBlockBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.WAND_RECHARGE_PEDESTAL()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<ArcaneSpaBlockEntity>> SUPPLIER_ARCANE_SPA =
                BLOCK_ENTITIES.register(
                        "arcane_spa",
                        () -> BlockEntityType.Builder.of(
                                ArcaneSpaBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_SPA()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<FocalManipulatorBlockEntity>> SUPPLIER_FOCAL_MANIPULATOR =
                BLOCK_ENTITIES.register(
                        "focal_manipulator",
                        () -> BlockEntityType.Builder.of(
                                FocalManipulatorBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.FOCAL_MANIPULATOR()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<FluxScrubberBlockEntity>> SUPPLIER_FLUX_SCRUBBER =
                BLOCK_ENTITIES.register(
                        "flux_scrubber",
                        () -> BlockEntityType.Builder.of(
                                FluxScrubberBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.FLUX_SCRUBBER()
                        ).build(null)
                );
        public static final RegistrySupplier<BlockEntityType<KeyableOwnedBlockEntity>> SUPPLIER_KEYABLE_OWNED =
                BLOCK_ENTITIES.register(
                        "keyable_owned",
                        () -> BlockEntityType.Builder.of(
                                KeyableOwnedBlockEntity::new,
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_DOOR(),
                                ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_PRESSURE_PLATE()
                        ).build(null)
                );


    }

    public static void init(){
        Registry.BLOCK_ENTITIES.register();
    }
}
