package thaumcraft.common.items;

import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.items.consumable.*;
import thaumcraft.common.items.consumable.aspectowning.CrystalEssenceItem;
import thaumcraft.common.items.consumable.aspectowning.ManaBeanItem;
import thaumcraft.common.items.consumable.aspectowning.WispEssenceItem;
import thaumcraft.common.items.eldritch.CrimsonRitesItem;
import thaumcraft.common.items.eldritch.EldritchEyeItem;
import thaumcraft.common.items.eldritch.EldritchObeliskPlacerItem;
import thaumcraft.common.items.eldritch.RunedTabletItem;
import thaumcraft.common.items.equipment.armor.GogglesOfRevealingItem;
import thaumcraft.common.items.equipment.armor.RobeArmorItem;
import thaumcraft.common.items.equipment.armor.ThaumiumArmorItem;
import thaumcraft.common.items.equipment.armor.VoidArmorItem;
import thaumcraft.common.items.equipment.elemental.*;
import thaumcraft.common.items.equipment.specialtool.*;
import thaumcraft.common.items.equipment.voidequip.*;
import thaumcraft.common.items.mateiral.PrimalCharmItem;
import thaumcraft.common.items.mateiral.PrimePearlItem;
import thaumcraft.common.items.research.ThaumometerItem;
import thaumcraft.common.items.jars.EssentiaJarBlockItem;
import thaumcraft.common.items.jars.NodeJarBlockItem;
import thaumcraft.common.items.jars.VoidJarBlockItem;
import thaumcraft.common.items.research.InkWellItem;
import thaumcraft.common.items.transport.MirrorBlockItem;
import thaumcraft.common.items.wands.rods.staffrods.*;
import thaumcraft.common.items.wands.rods.wandrods.*;
import thaumcraft.common.items.wands.wandcaps.*;
import thaumcraft.common.items.wands.wandtypes.SceptreCastingItem;
import thaumcraft.common.items.wands.wandtypes.StaffCastingItem;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;

import static net.minecraft.world.item.Items.GOLD_INGOT;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.THAUMIUM_INGOT_TAG;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.VOID_INGOT_TAG;

public class ThaumcraftItems {

    public static class ThaumcraftItemInstances {
        public static final AlumentumItem ALUMENTUM = Registry.SUPPLIER_ALUMENTUM.get();//itemResource:0
        public static final BlockItem NITOR = Registry.SUPPLIER_NITOR.get();//itemResource:1
        public static final Item THAUMIUM_INGOT = Registry.SUPPLIER_THAUMIUM_INGOT.get();//itemResource:2 Thaumium Ingot
        public static final Item QUICK_SILVER = Registry.SUPPLIER_QUICK_SILVER.get();//itemResource:3
        public static final Item MAGIC_TALLOW = Registry.SUPPLIER_MAGIC_TALLOW.get();//itemResource:4
        //seems azanor found that he has to register a special item for zombie brain.it's useless now.
    //    public static final Item ZOMBIE_BRAIN_REMOVED = Registry.SUPPLIER_ZOMBIE_BRAIN.get();//itemResource:5
        public static final Item AMBER_GEM = Registry.SUPPLIER_AMBER_GEM.get();//itemResource:6
        public static final Item ENCHANTED_FABRIC = Registry.SUPPLIER_ENCHANTED_FABRIC.get();//itemResource:7
        public static final Item VIS_FILTER = Registry.SUPPLIER_VIS_FILTER.get();//itemResource:8
        public static final KnowledgeFragmentItem KNOWLEDGE_FRAGMENT = Registry.SUPPLIER_KNOWLEDGE_FRAGMENT.get();//itemResource:9
        public static final Item MIRRORED_GLASS = Registry.SUPPLIER_MIRRORED_GLASS.get();//itemResource:10
        public static final Item TAINTED_GOO = Registry.SUPPLIER_TAINTED_GOO.get();//itemResource:11
        public static final Item TAINT_TENDRIL = Registry.SUPPLIER_TAINT_TENDRIL.get();//itemResource:12 //TODO:new class
        public static final Item JAR_LABEL = Registry.SUPPLIER_JAR_LABEL.get();//itemResource:13
        public static final Item SALIS_MUNDUS = Registry.SUPPLIER_SALIS_MUNDUS.get();//itemResource:14
        public static final PrimalCharmItem PRIMAL_CHARM = Registry.SUPPLIER_PRIMAL_CHARM.get();//itemResource:15
        public static final Item VOID_INGOT = Registry.SUPPLIER_VOID_INGOT.get();//itemResource:16
        public static final Item VOID_SEED = Registry.SUPPLIER_VOID_SEED.get();//itemResource:17
        public static final Item GOLD_COIN = Registry.SUPPLIER_GOLD_COIN.get();//itemResource:18
        public static final EldritchEyeItem ELDRITCH_EYE = Registry.SUPPLIER_ELDRITCH_EYE.get();//itemEldritchObject:0
        public static final CrimsonRitesItem CRIMSON_RITES = Registry.SUPPLIER_CRIMSON_RITES.get();//itemEldritchObject:1
        public static final RunedTabletItem RUNED_TABLET = Registry.SUPPLIER_RUNED_TABLET.get();//itemEldritchObject:2
        public static final PrimePearlItem PRIME_PEARL = Registry.SUPPLIER_PRIME_PEARL.get();//itemEldritchObject:3
        public static final EldritchObeliskPlacerItem ELDRITCH_OBELISK_PLACER = Registry.SUPPLIER_ELDRITCH_OBELISK_PLACER.get();//itemEldritchObject:4
        public static final ThaumometerItem THAUMOMETER = Registry.SUPPLIER_THAUMOMETER_ITEM.get();
        // Wand Caps
        public static final CopperWandCapItem COPPER_WAND_CAP = Registry.SUPPLIER_COPPER_WAND_CAP.get();
        public static final GoldWandCapItem GOLD_WAND_CAP = Registry.SUPPLIER_GOLD_WAND_CAP.get();
        public static final IronWandCapItem IRON_WAND_CAP = Registry.SUPPLIER_IRON_WAND_CAP.get();
        public static final SilverWandCapItem SILVER_WAND_CAP = Registry.SUPPLIER_SILVER_WAND_CAP.get();
        public static final ThaumiumWandCapItem THAUMIUM_WAND_CAP = Registry.SUPPLIER_THAUMIUM_WAND_CAP.get();
        public static final VoidWandCapItem VOID_WAND_CAP = Registry.SUPPLIER_VOID_WAND_CAP.get();
        //wand rods
        public static final BlazeWandRodItem BLAZE_WAND_ROD = Registry.SUPPLIER_BLAZE_WAND_ROD.get();
        public static final BoneWandRodItem BONE_WAND_ROD = Registry.SUPPLIER_BONE_WAND_ROD.get();
        public static final GreatWoodWandRodItem GREATWOOD_WAND_ROD = Registry.SUPPLIER_GREATWOOD_WAND_ROD.get();
        public static final IceWandRodItem ICE_WAND_ROD = Registry.SUPPLIER_ICE_WAND_ROD.get();
        public static final ObsidianWandRodItem OBSIDIAN_WAND_ROD = Registry.SUPPLIER_OBSIDIAN_WAND_ROD.get();
        public static final QuartzWandRodItem QUARTZ_WAND_ROD = Registry.SUPPLIER_QUARTZ_WAND_ROD.get();
        public static final ReedWandRodItem REED_WAND_ROD = Registry.SUPPLIER_REED_WAND_ROD.get();
        public static final SilverWoodWandRodItem SILVERWOOD_WAND_ROD = Registry.SUPPLIER_SILVERWOOD_WAND_ROD.get();
        public static final WoodWandRodItem WOOD_WAND_ROD = Registry.SUPPLIER_WOOD_WAND_ROD.get();
        public static final PrimalStaffRodItem PRIMAL_STAFF_ROD = Registry.SUPPLIER_PRIMAL_STAFF_ROD.get();
        public static final BlazeStaffRodItem BLAZE_STAFF_ROD = Registry.SUPPLIER_BLAZE_STAFF_ROD.get();
        public static final BoneStaffRodItem BONE_STAFF_ROD = Registry.SUPPLIER_BONE_STAFF_ROD.get();
        public static final GreatWoodStaffRodItem GREATWOOD_STAFF_ROD = Registry.SUPPLIER_GREATWOOD_STAFF_ROD.get();
        public static final IceStaffRodItem ICE_STAFF_ROD = Registry.SUPPLIER_ICE_STAFF_ROD.get();
        public static final ObsidianStaffRodItem OBSIDIAN_STAFF_ROD = Registry.SUPPLIER_OBSIDIAN_STAFF_ROD.get();
        public static final QuartzStaffRodItem QUARTZ_STAFF_ROD = Registry.SUPPLIER_QUARTZ_STAFF_ROD.get();
        public static final ReedStaffRodItem REED_STAFF_ROD = Registry.SUPPLIER_REED_STAFF_ROD.get();
        public static final SilverWoodStaffRodItem SILVERWOOD_STAFF_ROD = Registry.SUPPLIER_SILVERWOOD_STAFF_ROD.get();
        public static final Item SILVER_WAND_CAP_INERT = Registry.SUPPLIER_SILVER_WAND_CAP_INERT.get();
        public static final Item THAUMIUM_WAND_CAP_INERT = Registry.SUPPLIER_THAUMIUM_WAND_CAP_INERT.get();
        public static final Item VOID_WAND_CAP_INERT = Registry.SUPPLIER_VOID_WAND_CAP_INERT.get();
        public static final ZombieBrainItem ZOMBIE_BRAIN = Registry.SUPPLIER_ZOMBIE_BRAIN.get();
        public static final BlockItem GREATWOOD_LOG = Registry.SUPPLIER_GREATWOOD_LOG.get();
        public static final BlockItem SILVERWOOD_LOG = Registry.SUPPLIER_SILVERWOOD_LOG.get();
        public static final BlockItem GREATWOOD_PLANKS = Registry.SUPPLIER_GREATWOOD_PLANKS.get();
        public static final BlockItem SILVERWOOD_PLANKS = Registry.SUPPLIER_SILVERWOOD_PLANKS.get();
        public static final BlockItem GREATWOOD_LEAVES = Registry.SUPPLIER_GREATWOOD_LEAVES.get();
        public static final BlockItem SILVERWOOD_LEAVES = Registry.SUPPLIER_SILVERWOOD_LEAVES.get();
        public static final BlockItem GREATWOOD_SAPLING = Registry.SUPPLIER_GREATWOOD_SAPLING.get();
        public static final BlockItem SILVERWOOD_SAPLING = Registry.SUPPLIER_SILVERWOOD_SAPLING.get();
        public static final BlockItem OBSIDIAN_TOTEM = Registry.SUPPLIER_OBSIDIAN_TOTEM.get();
        public static final BlockItem OBSIDIAN_TIME = Registry.SUPPLIER_OBSIDIAN_TILE.get();
        public static final BlockItem PAVING_STONE_TRAVEL = Registry.SUPPLIER_PAVING_STONE_TRAVEL.get();
        public static final BlockItem PAVING_STONE_WARDING = Registry.SUPPLIER_PAVING_STONE_WARDING.get();
        public static final BlockItem THAUMIUM_BLOCK = Registry.SUPPLIER_THAUMIUM_BLOCK.get();
        public static final BlockItem TALLOW_BLOCK = Registry.SUPPLIER_TALLOW_BLOCK.get();
        public static final BlockItem ARCANE_STONE_BLOCK = Registry.SUPPLIER_ARCANE_STONE_BLOCK.get();
        public static final BlockItem ARCANE_STONE_BRICKS = Registry.SUPPLIER_ARCANE_STONE_BRICKS.get();
        public static final BlockItem GOLEM_FETTER = Registry.SUPPLIER_GOLEM_FETTER.get();
        public static final BlockItem ANCIENT_STONE = Registry.SUPPLIER_ANCIENT_STONE.get();
        public static final BlockItem ANCIENT_ROCK = Registry.SUPPLIER_ANCIENT_ROCK.get();
        public static final BlockItem CRUSTED_STONE = Registry.SUPPLIER_CRUSTED_STONE.get();
        public static final BlockItem ANCIENT_STONE_PEDESTAL = Registry.SUPPLIER_ANCIENT_STONE_PEDESTAL.get();
        public static final BlockItem ANCIENT_STONE_STAIRS = Registry.SUPPLIER_ANCIENT_STONE_STAIRS.get();
        public static final BlockItem ARCANE_STONE_BRICK_STAIRS = Registry.SUPPLIER_ARCANE_STONE_BRICK_STAIRS.get();
        public static final BlockItem GREATWOOD_PLANKS_STAIRS = Registry.SUPPLIER_GREATWOOD_PLANKS_STAIRS.get();
        public static final BlockItem SILVERWOOD_PLANKS_STAIRS = Registry.SUPPLIER_SILVERWOOD_PLANKS_STAIRS.get();
        public static final BlockItem ANCIENT_STONE_SLAB = Registry.SUPPLIER_ANCIENT_STONE_SLAB.get();
        public static final BlockItem ARCANE_STONE_BRICK_SLAB = Registry.SUPPLIER_ARCANE_STONE_BRICK_SLAB.get();
        public static final BlockItem GREATWOOD_PLANKS_SLAB = Registry.SUPPLIER_GREATWOOD_PLANKS_SLAB.get();
        public static final BlockItem SILVERWOOD_PLANKS_SLAB = Registry.SUPPLIER_SILVERWOOD_PLANKS_SLAB.get();
        public static final BlockItem AIR_CRYSTAL = Registry.SUPPLIER_AIR_CRYSTAL.get();
        public static final BlockItem FIRE_CRYSTAL = Registry.SUPPLIER_FIRE_CRYSTAL.get();
        public static final BlockItem WATER_CRYSTAL = Registry.SUPPLIER_WATER_CRYSTAL.get();
        public static final BlockItem EARTH_CRYSTAL = Registry.SUPPLIER_EARTH_CRYSTAL.get();
        public static final BlockItem ORDER_CRYSTAL = Registry.SUPPLIER_ORDER_CRYSTAL.get();
        public static final BlockItem ENTROPY_CRYSTAL = Registry.SUPPLIER_ENTROPY_CRYSTAL.get();
        public static final BlockItem MIXED_CRYSTAL = Registry.SUPPLIER_MIXED_CRYSTAL.get();
        public static final BlockItem STRANGE_CRYSTALS = Registry.SUPPLIER_STRANGE_CRYSTALS.get();
        public static final Item AIR_SHARD = Registry.SUPPLIER_AIR_SHARD.get();
        public static final Item FIRE_SHARD = Registry.SUPPLIER_FIRE_SHARD.get();
        public static final Item WATER_SHARD = Registry.SUPPLIER_WATER_SHARD.get();
        public static final Item EARTH_SHARD = Registry.SUPPLIER_EARTH_SHARD.get();
        public static final Item ORDER_SHARD = Registry.SUPPLIER_ORDER_SHARD.get();
        public static final Item ENTROPY_SHARD = Registry.SUPPLIER_ENTROPY_SHARD.get();
        public static final Item BALANCE_SHARD = Registry.SUPPLIER_BALANCE_SHARD.get();
        public static final Item CINNABAR_ORE = Registry.SUPPLIER_CINNABAR_ORE.get();
        public static final Item AMBER_ORE = Registry.SUPPLIER_AMBER_ORE.get();
        public static final Item AIR_INFUSED_STONE = Registry.SUPPLIER_AIR_INFUSED_STONE.get();
        public static final Item FIRE_INFUSED_STONE = Registry.SUPPLIER_FIRE_INFUSED_STONE.get();
        public static final Item WATER_INFUSED_STONE = Registry.SUPPLIER_WATER_INFUSED_STONE.get();
        public static final Item EARTH_INFUSED_STONE = Registry.SUPPLIER_EARTH_INFUSED_STONE.get();
        public static final Item ORDER_INFUSED_STONE = Registry.SUPPLIER_ORDER_INFUSED_STONE.get();
        public static final Item ENTROPY_INFUSED_STONE = Registry.SUPPLIER_ENTROPY_INFUSED_STONE.get();
        public static final Item AMBER_BLOCK = Registry.SUPPLIER_AMBER_BLOCK.get();
        public static final Item AMBER_BRICK = Registry.SUPPLIER_AMBER_BRICK.get();
        public static final BlockItem WHITE_TALLOW_CANDLE = Registry.SUPPLIER_WHITE_TALLOW_CANDLE.get();
        public static final BlockItem ORANGE_TALLOW_CANDLE = Registry.SUPPLIER_ORANGE_TALLOW_CANDLE.get();
        public static final BlockItem MAGENTA_TALLOW_CANDLE = Registry.SUPPLIER_MAGENTA_TALLOW_CANDLE.get();
        public static final BlockItem LIGHT_BLUE_TALLOW_CANDLE = Registry.SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE.get();
        public static final BlockItem YELLOW_TALLOW_CANDLE = Registry.SUPPLIER_YELLOW_TALLOW_CANDLE.get();
        public static final BlockItem LIME_TALLOW_CANDLE = Registry.SUPPLIER_LIME_TALLOW_CANDLE.get();
        public static final BlockItem PINK_TALLOW_CANDLE = Registry.SUPPLIER_PINK_TALLOW_CANDLE.get();
        public static final BlockItem GRAY_TALLOW_CANDLE = Registry.SUPPLIER_GRAY_TALLOW_CANDLE.get();
        public static final BlockItem LIGHT_GRAY_TALLOW_CANDLE = Registry.SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE.get();
        public static final BlockItem CYAN_TALLOW_CANDLE = Registry.SUPPLIER_CYAN_TALLOW_CANDLE.get();
        public static final BlockItem PURPLE_TALLOW_CANDLE = Registry.SUPPLIER_PURPLE_TALLOW_CANDLE.get();
        public static final BlockItem BLUE_TALLOW_CANDLE = Registry.SUPPLIER_BLUE_TALLOW_CANDLE.get();
        public static final BlockItem BROWN_TALLOW_CANDLE = Registry.SUPPLIER_BROWN_TALLOW_CANDLE.get();
        public static final BlockItem GREEN_TALLOW_CANDLE = Registry.SUPPLIER_GREEN_TALLOW_CANDLE.get();
        public static final BlockItem RED_TALLOW_CANDLE = Registry.SUPPLIER_RED_TALLOW_CANDLE.get();
        public static final BlockItem BLACK_TALLOW_CANDLE = Registry.SUPPLIER_BLACK_TALLOW_CANDLE.get();
        public static final BlockItem SHIMMER_LEAF = Registry.SUPPLIER_SHIMMER_LEAF.get();
        public static final BlockItem CINDER_PEARL = Registry.SUPPLIER_CINDER_PEARL.get();
        public static final BlockItem MANA_SHROOM = Registry.SUPPLIER_MANA_SHROOM.get();
        public static final BlockItem ETHEREAL_BLOOM = Registry.SUPPLIER_ETHEREAL_BLOOM.get();
        public static final BlockItem ARCANE_BELLOW = Registry.SUPPLIER_ARCANE_BELLOW.get();
        public static final BlockItem ARCANE_DOOR = Registry.SUPPLIER_ARCANE_DOOR.get();
        public static final BannerPatternItem CULTIST_BANNER_PATTERN = Registry.SUPPLIER_CULTIST_BANNER_PATTERN.get();
        public static final BlockItem GLOWING_CRUSTED_STONE = Registry.SUPPLIER_GLOWING_CRUSTED_STONE.get();
        public static final BlockItem GLYPHED_STONE = Registry.SUPPLIER_GLYPHED_STONE.get();
        public static final BlockItem ANCIENT_GATEWAY = Registry.SUPPLIER_ANCIENT_GATEWAY.get();
        public static final BlockItem ANCIENT_LOCK_EMPTY = Registry.SUPPLIER_ANCIENT_LOCK_EMPTY.get();
        public static final BlockItem ANCIENT_LOCK_INSERTED = Registry.SUPPLIER_ANCIENT_LOCK_INSERTED.get();
        public static final BlockItem ELDRITCH_CRAB_SPAWNER = Registry.SUPPLIER_ELDRITCH_CRAB_SPAWNER.get();
        public static final BlockItem RUNED_STONE = Registry.SUPPLIER_RUNED_STONE.get();
        public static final BlockItem CRUSTED_TAINT = Registry.SUPPLIER_CRUSTED_TAINT.get();
        public static final BlockItem TAINTED_SOIL = Registry.SUPPLIER_TAINTED_SOIL.get();
        public static final BlockItem FIBROUS_TAINT = Registry.SUPPLIER_FIBROUS_TAINT.get();
        public static final BlockItem TAINTED_GRASS = Registry.SUPPLIER_TAINTED_GRASS.get();
        public static final BlockItem TAINTED_PLANT = Registry.SUPPLIER_TAINTED_PLANT.get();
        public static final BlockItem SPORE_STALK = Registry.SUPPLIER_SPORE_STALK.get();
        public static final BlockItem MATURE_SPORE_STALK = Registry.SUPPLIER_MATURE_SPORE_STALK.get();
        public static final BlockItem TABLE = Registry.SUPPLIER_TABLE.get();
        public static final InkWellItem INK_WELL = Registry.SUPPLIER_INK_WELL.get();
        public static final BlockItem ARCANE_WORKBENCH = Registry.SUPPLIER_ARCANE_WORKBENCH.get();
        public static final BlockItem DECONSTRUCTION_TABLE = Registry.SUPPLIER_DECONSTRUCTION_TABLE.get();
        public static final ResearchNoteItem RESEARCH_NOTE = Registry.SUPPLIER_RESEARCH_NOTE.get();
        public static final WandCastingItem WAND_CASTING = Registry.SUPPLIER_WAND_CASTING.get();
        public static final StaffCastingItem STAFF_CASTING = Registry.SUPPLIER_STAFF_CASTING.get();
        public static final SceptreCastingItem SCEPTRE_CASTING = Registry.SUPPLIER_SCEPTRE_CASTING.get();
        public static final BlockItem VIS_RELAY = Registry.SUPPLIER_VIS_RELAY.get();
        public static final BlockItem VIS_CHARGE_RELAY = Registry.SUPPLIER_VIS_CHARGE_RELAY.get();
        public static final BlockItem NODE_STABILIZER = Registry.SUPPLIER_NODE_STABILIZER.get();
        public static final BlockItem ADVANCED_NODE_STABILIZER = Registry.SUPPLIER_ADVANCED_NODE_STABILIZER.get();
        public static final BlockItem NODE_TRANSDUCER = Registry.SUPPLIER_NODE_TRANSDUCER.get();
        public static final BlockItem ALCHEMICAL_FURNACE = Registry.SUPPLIER_ALCHEMICAL_FURNACE.get();
        public static final BlockItem ADVANCED_ALCHEMICAL_CONSTRUCT = Registry.SUPPLIER_ADVANCED_ALCHEMICAL_CONSTRUCT.get();
        public static final BlockItem ALCHEMICAL_CONSTRUCT = Registry.SUPPLIER_ALCHEMICAL_CONSTRUCT.get();
        public static final BlockItem ARCANE_ALEMBIC = Registry.SUPPLIER_ARCANE_ALEMBIC.get();
        public static final EssentiaJarBlockItem ESSENTIA_JAR = Registry.SUPPLIER_ESSENTIA_JAR.get();
        public static final VoidJarBlockItem VOID_JAR = Registry.SUPPLIER_VOID_JAR.get();
        public static final BlockItem BRAIN_JAR = Registry.SUPPLIER_BRAIN_JAR.get();
        public static final NodeJarBlockItem NODE_JAR = Registry.SUPPLIER_NODE_JAR.get();
        public static final BlockItem ITEM_CRATE = Registry.SUPPLIER_ITEM_CRATE.get();
        public static final BlockItem CRUCIBLE = Registry.SUPPLIER_CRUCIBLE.get();
        public static final BlockItem ARCANE_LAMP = Registry.SUPPLIER_ARCANE_LAMP.get();
        public static final BlockItem GROWTH_ARCANE_LAMP = Registry.SUPPLIER_GROWTH_ARCANE_LAMP.get();
        public static final BlockItem FERTILITY_ARCANE_LAMP = Registry.SUPPLIER_FERTILITY_ARCANE_LAMP.get();
        public static final BlockItem MNEMONIC_MATRIX = Registry.SUPPLIER_MNEMONIC_MATRIX.get();
        public static final BlockItem ARCANE_LEVITATOR = Registry.SUPPLIER_ARCANE_LEVITATOR.get();
        public static final BlockItem ESSENTIA_RESERVOIR = Registry.SUPPLIER_ESSENTIA_RESERVOIR.get();
        public static final ManaBeanItem MANA_BEAN = Registry.SUPPLIER_MANA_BEAN.get();
        public static final BlockItem ESSENTIA_TUBE = Registry.SUPPLIER_ESSENTIA_TUBE.get();
        public static final BlockItem ESSENTIA_TUBE_VALVE = Registry.SUPPLIER_ESSENTIA_TUBE_VALVE.get();
        public static final BlockItem ESSENTIA_TUBE_FILTER = Registry.SUPPLIER_ESSENTIA_TUBE_FILTER.get();
        public static final BlockItem ESSENTIA_TUBE_RESTRICT = Registry.SUPPLIER_ESSENTIA_TUBE_RESTRICT.get();
        public static final BlockItem ESSENTIA_TUBE_ONEWAY = Registry.SUPPLIER_ESSENTIA_TUBE_ONEWAY.get();
        public static final BlockItem ESSENTIA_BUFFER = Registry.SUPPLIER_ESSENTIA_BUFFER.get();
        public static final BlockItem ESSENTIA_CENTRIFUGE = Registry.SUPPLIER_ESSENTIA_CENTRIFUGE.get();
        public static final BlockItem ESSENTIA_CRYSTALLIZER = Registry.SUPPLIER_ESSENTIA_CRYSTALLIZER.get();
        public static final CrystalEssenceItem CRYSTAL_ESSENCE = Registry.SUPPLIER_CRYSTAL_ESSENCE.get();
        public static final BlockItem ARCANE_PRESSURE_PLATE = Registry.SUPPLIER_ARCANE_PRESSURE_PLATE.get();
        public static final BlockItem ARCANE_BORE = Registry.SUPPLIER_ARCANE_BORE_BASE.get();
        public static final BlockItem ARCANE_EAR = Registry.SUPPLIER_ARCANE_EAR.get();
        public static final BucketItem DEATH_FLUID_BUCKET = Registry.SUPPLIER_DEATH_FLUID_BUCKET.get();
        public static final BucketItem PURE_FLUID_BUCKET = Registry.SUPPLIER_PURE_FLUID_BUCKET.get();
        public static final ThaumiumArmorItem THAUMIUM_HELMET = Registry.SUPPLIER_THAUMIUM_HELMET.get();
        public static final ThaumiumArmorItem THAUMIUM_CHESTPLATE = Registry.SUPPLIER_THAUMIUM_CHESTPLATE.get();
        public static final ThaumiumArmorItem THAUMIUM_LEGGINGS = Registry.SUPPLIER_THAUMIUM_LEGGINGS.get();
        public static final ThaumiumArmorItem THAUMIUM_BOOTS = Registry.SUPPLIER_THAUMIUM_BOOTS.get();
        public static final VoidArmorItem VOID_HELMET = Registry.SUPPLIER_VOID_HELMET.get();
        public static final VoidArmorItem VOID_CHESTPLATE = Registry.SUPPLIER_VOID_CHESTPLATE.get();
        public static final VoidArmorItem VOID_LEGGINGS = Registry.SUPPLIER_VOID_LEGGINGS.get();
        public static final VoidArmorItem VOID_BOOTS = Registry.SUPPLIER_VOID_BOOTS.get();
        public static final MirrorBlockItem MIRROR = Registry.SUPPLIER_MIRROR.get();
        public static final MirrorBlockItem ESSENTIA_MIRROR = Registry.SUPPLIER_ESSENTIA_MIRROR.get();
        public static final BlockItem ARCANE_PEDESTAL = Registry.SUPPLIER_ARCANE_PEDESTAL.get();
        public static final BlockItem INFUSION_MATRIX = Registry.SUPPLIER_INFUSION_MATRIX.get();
        public static final BlockItem WAND_RECHARGE_PEDESTAL = Registry.SUPPLIER_WAND_RECHARGE_PEDESTAL.get();
        public static final BlockItem COMPOUND_RECHARGE_FOCUS = Registry.SUPPLIER_COMPOUND_RECHARGE_FOCUS.get();
        public static final BlockItem ARCANE_SPA = Registry.SUPPLIER_ARCANE_SPA.get();
        public static final BathSaltsItem BATH_SALTS = Registry.SUPPLIER_BATH_SALTS.get();
        public static final BlockItem FOCAL_MANIPULATOR = Registry.SUPPLIER_FOCAL_MANIPULATOR.get();
        public static final BlockItem FLUX_SCRUBBER = Registry.SUPPLIER_FLUX_SCRUBBER.get();
        public static final WispEssenceItem WISP_ESSENCE = Registry.SUPPLIER_WISP_ESSENCE.get();
        public static final VoidSwordItem VOID_SWORD = Registry.SUPPLIER_VOID_SWORD.get();
        public static final VoidShovelItem VOID_SHOVEL = Registry.SUPPLIER_VOID_SHOVEL.get();
        public static final VoidPickaxeItem VOID_PICKAXE = Registry.SUPPLIER_VOID_PICKAXE.get();
        public static final VoidHoeItem VOID_HOE = Registry.SUPPLIER_VOID_HOE.get();
        public static final VoidAxeItem VOID_AXE = Registry.SUPPLIER_VOID_AXE.get();
        public static final SwordItem THAUMIUM_SWORD = Registry.SUPPLIER_THAUMIUM_SWORD.get();
        public static final ShovelItem THAUMIUM_SHOVEL = Registry.SUPPLIER_THAUMIUM_SHOVEL.get();
        public static final PickaxeItem THAUMIUM_PICKAXE = Registry.SUPPLIER_THAUMIUM_PICKAXE.get();
        public static final AxeItem THAUMIUM_AXE = Registry.SUPPLIER_THAUMIUM_AXE.get();
        public static final HoeItem THAUMIUM_HOE = Registry.SUPPLIER_THAUMIUM_HOE.get();
        public static final PrimalCrusherItem PRIMAL_CRUSHER = Registry.SUPPLIER_PRIMAL_CRUSHER.get();
        public static final Item VOID_NUGGET = Registry.SUPPLIER_VOID_NUGGET.get();
        public static final Item THAUMIUM_NUGGET = Registry.SUPPLIER_THAUMIUM_NUGGET.get();
        public static final Item COPPER_NUGGET = Registry.SUPPLIER_COPPER_NUGGET.get();
        public static final Item QUICKSILVER_DROP = Registry.SUPPLIER_QUICKSILVER_DROP.get();
        public static final ElementalShovelItem ELEMENTAL_SHOVEL = Registry.SUPPLIER_ELEMENTAL_SHOVEL.get();
        public static final ElementalPickaxeItem ELEMENTAL_PICKAXE = Registry.SUPPLIER_ELEMENTAL_PICKAXE.get();
        public static final ElementalSwordItem ELEMENTAL_SWORD = Registry.SUPPLIER_ELEMENTAL_SWORD.get();
        public static final ElementalHoeItem ELEMENTAL_HOE = Registry.SUPPLIER_ELEMENTAL_HOE.get();
        public static final ElementalAxeItem ELEMENTAL_AXE = Registry.SUPPLIER_ELEMENTAL_AXE.get();
        public static final CrimsonSwordItem CRIMSON_SWORD = Registry.SUPPLIER_CRIMSON_SWORD.get();
        public static final BoneBowItem BONE_BOW = Registry.SUPPLIER_BONE_BOW.get();
        public static final AirArrowItem AIR_ARROW = Registry.SUPPLIER_AIR_ARROW.get();
        public static final FireArrowItem FIRE_ARROW = Registry.SUPPLIER_FIRE_ARROW.get();
        public static final EarthArrowItem EARTH_ARROW = Registry.SUPPLIER_EARTH_ARROW.get();
        public static final WaterArrowItem WATER_ARROW = Registry.SUPPLIER_WATER_ARROW.get();
        public static final OrderArrowItem ORDER_ARROW = Registry.SUPPLIER_ORDER_ARROW.get();
        public static final EntropyArrowItem ENTROPY_ARROW =  Registry.SUPPLIER_ENTROPY_ARROW.get();
        public static final GogglesOfRevealingItem GOGGLES_OF_REVEALING = Registry.SUPPLIER_GOGGLES_OF_REVEALING.get();
//        public static final RobeArmorItem ROBE_HELMET = Registry.SUPPLIER_ROBE_HELMET.get();
        public static final RobeArmorItem ROBE_CHESTPLATE = Registry.SUPPLIER_ROBE_CHESTPLATE.get();
        public static final RobeArmorItem ROBE_LEGGINGS = Registry.SUPPLIER_ROBE_LEGGINGS.get();
        public static final RobeArmorItem ROBE_BOOTS = Registry.SUPPLIER_ROBE_BOOTS.get();
    }

    public static class Registry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.ITEM);

        public static final RegistrySupplier<AlumentumItem> SUPPLIER_ALUMENTUM = ITEMS.register(
                "alumentum", AlumentumItem::new);
        public static final RegistrySupplier<BlockItem> SUPPLIER_NITOR = ITEMS.register(
                "nitor", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.NITOR_BLOCK, new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_THAUMIUM_INGOT = ITEMS.register(
                "thaumium_ingot", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_QUICK_SILVER = ITEMS.register(
                "quick_silver", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_MAGIC_TALLOW = ITEMS.register(
                "magic_tallow", () -> new Item(new Item.Properties()));
        //        public static final RegistrySupplier<Item> SUPPLIER_ZOMBIE_BRAIN_REMOVED = ITEMS.register("zombie_brain_removed", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_AMBER_GEM = ITEMS.register(
                "amber_gem", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_ENCHANTED_FABRIC = ITEMS.register(
                "enchanted_fabric", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_VIS_FILTER = ITEMS.register(
                "vis_filter", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<KnowledgeFragmentItem> SUPPLIER_KNOWLEDGE_FRAGMENT = ITEMS.register(
                "knowledge_fragment", KnowledgeFragmentItem::new);
        public static final RegistrySupplier<Item> SUPPLIER_MIRRORED_GLASS = ITEMS.register(
                "mirrored_glass", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_TAINTED_GOO = ITEMS.register(
                "tainted_goo", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_TAINT_TENDRIL = ITEMS.register(
                "taint_tendril", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<JarLabelItem> SUPPLIER_JAR_LABEL = ITEMS.register(
                "jar_label", JarLabelItem::new);
        public static final RegistrySupplier<Item> SUPPLIER_SALIS_MUNDUS = ITEMS.register(
                "salis_mundus", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<PrimalCharmItem> SUPPLIER_PRIMAL_CHARM = ITEMS.register(
                "primal_charm", PrimalCharmItem::new);
        public static final RegistrySupplier<Item> SUPPLIER_VOID_INGOT = ITEMS.register(
                "void_ingot", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_VOID_SEED = ITEMS.register(
                "void_seed", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_GOLD_COIN = ITEMS.register(
                "gold_coin", () -> new Item(new Item.Properties()));

        public static final RegistrySupplier<Item> SUPPLIER_VOID_NUGGET = ITEMS.register(
                "void_nugget", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_THAUMIUM_NUGGET = ITEMS.register(
                "thaumium_nugget", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_COPPER_NUGGET = ITEMS.register(
                "copper_nugget", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_QUICKSILVER_DROP = ITEMS.register(
                "quicksilver_drop", () -> new Item(new Item.Properties()));

        public static final RegistrySupplier<EldritchEyeItem> SUPPLIER_ELDRITCH_EYE = ITEMS.register(
                "eldritch_eye", EldritchEyeItem::new);
        public static final RegistrySupplier<CrimsonRitesItem> SUPPLIER_CRIMSON_RITES = ITEMS.register(
                "crimson_rites", CrimsonRitesItem::new);
        public static final RegistrySupplier<RunedTabletItem> SUPPLIER_RUNED_TABLET = ITEMS.register(
                "runed_tablet", RunedTabletItem::new);
        public static final RegistrySupplier<PrimePearlItem> SUPPLIER_PRIME_PEARL = ITEMS.register(
                "prime_pearl", PrimePearlItem::new);
        public static final RegistrySupplier<EldritchObeliskPlacerItem> SUPPLIER_ELDRITCH_OBELISK_PLACER = ITEMS.register(
                "ob_placer", EldritchObeliskPlacerItem::new);

        public static final RegistrySupplier<ThaumometerItem> SUPPLIER_THAUMOMETER_ITEM = ITEMS.register(
                "thaumometer", ThaumometerItem::new);

        //wand caps
        public static final RegistrySupplier<CopperWandCapItem> SUPPLIER_COPPER_WAND_CAP = ITEMS.register(
                "wand_cap_copper", CopperWandCapItem::new);//
        public static final RegistrySupplier<GoldWandCapItem> SUPPLIER_GOLD_WAND_CAP = ITEMS.register(
                "wand_cap_gold", GoldWandCapItem::new);
        public static final RegistrySupplier<IronWandCapItem> SUPPLIER_IRON_WAND_CAP = ITEMS.register(
                "wand_cap_iron", IronWandCapItem::new);
        public static final RegistrySupplier<SilverWandCapItem> SUPPLIER_SILVER_WAND_CAP = ITEMS.register(
                "wand_cap_silver", SilverWandCapItem::new);
        public static final RegistrySupplier<ThaumiumWandCapItem> SUPPLIER_THAUMIUM_WAND_CAP = ITEMS.register(
                "wand_cap_thaumium", ThaumiumWandCapItem::new);
        public static final RegistrySupplier<VoidWandCapItem> SUPPLIER_VOID_WAND_CAP = ITEMS.register(
                "wand_cap_void", VoidWandCapItem::new);


        //wand caps (inert)
        public static final RegistrySupplier<Item> SUPPLIER_SILVER_WAND_CAP_INERT = ITEMS.register(
                "wand_cap_silver_inert", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_THAUMIUM_WAND_CAP_INERT = ITEMS.register(
                "wand_cap_thaumium_inert", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_VOID_WAND_CAP_INERT = ITEMS.register(
                "wand_cap_void_inert", () -> new Item(new Item.Properties()));

        //wand rods
        public static final RegistrySupplier<BlazeWandRodItem> SUPPLIER_BLAZE_WAND_ROD = ITEMS.register(
                "wand_rod_blaze", BlazeWandRodItem::new);
        public static final RegistrySupplier<BoneWandRodItem> SUPPLIER_BONE_WAND_ROD = ITEMS.register(
                "wand_rod_bone", BoneWandRodItem::new);
        public static final RegistrySupplier<GreatWoodWandRodItem> SUPPLIER_GREATWOOD_WAND_ROD = ITEMS.register(
                "wand_rod_greatwood", GreatWoodWandRodItem::new);
        public static final RegistrySupplier<IceWandRodItem> SUPPLIER_ICE_WAND_ROD = ITEMS.register(
                "wand_rod_ice", IceWandRodItem::new);
        public static final RegistrySupplier<ObsidianWandRodItem> SUPPLIER_OBSIDIAN_WAND_ROD = ITEMS.register(
                "wand_rod_obsidian", ObsidianWandRodItem::new);
        public static final RegistrySupplier<QuartzWandRodItem> SUPPLIER_QUARTZ_WAND_ROD = ITEMS.register(
                "wand_rod_quartz", QuartzWandRodItem::new);
        public static final RegistrySupplier<ReedWandRodItem> SUPPLIER_REED_WAND_ROD = ITEMS.register(
                "wand_rod_reed", ReedWandRodItem::new);
        public static final RegistrySupplier<SilverWoodWandRodItem> SUPPLIER_SILVERWOOD_WAND_ROD = ITEMS.register(
                "wand_rod_silverwood", SilverWoodWandRodItem::new);
        public static final RegistrySupplier<WoodWandRodItem> SUPPLIER_WOOD_WAND_ROD = ITEMS.register(
                "wand_rod_wood", WoodWandRodItem::new);//fake in fact

        //staff rods
        public static final RegistrySupplier<PrimalStaffRodItem> SUPPLIER_PRIMAL_STAFF_ROD = ITEMS.register(
                "staff_rod_primal", PrimalStaffRodItem::new);
        public static final RegistrySupplier<BlazeStaffRodItem> SUPPLIER_BLAZE_STAFF_ROD = ITEMS.register(
                "staff_rod_blaze", BlazeStaffRodItem::new);
        public static final RegistrySupplier<BoneStaffRodItem> SUPPLIER_BONE_STAFF_ROD = ITEMS.register(
                "staff_rod_bone", BoneStaffRodItem::new);
        public static final RegistrySupplier<GreatWoodStaffRodItem> SUPPLIER_GREATWOOD_STAFF_ROD = ITEMS.register(
                "staff_rod_greatwood", GreatWoodStaffRodItem::new);
        public static final RegistrySupplier<IceStaffRodItem> SUPPLIER_ICE_STAFF_ROD = ITEMS.register(
                "staff_rod_ice", IceStaffRodItem::new);
        public static final RegistrySupplier<ObsidianStaffRodItem> SUPPLIER_OBSIDIAN_STAFF_ROD = ITEMS.register(
                "staff_rod_obsidian", ObsidianStaffRodItem::new);
        public static final RegistrySupplier<QuartzStaffRodItem> SUPPLIER_QUARTZ_STAFF_ROD = ITEMS.register(
                "staff_rod_quartz", QuartzStaffRodItem::new);
        public static final RegistrySupplier<ReedStaffRodItem> SUPPLIER_REED_STAFF_ROD = ITEMS.register(
                "staff_rod_reed", ReedStaffRodItem::new);
        public static final RegistrySupplier<SilverWoodStaffRodItem> SUPPLIER_SILVERWOOD_STAFF_ROD = ITEMS.register(
                "staff_rod_silverwood", SilverWoodStaffRodItem::new);

        public static final RegistrySupplier<ZombieBrainItem> SUPPLIER_ZOMBIE_BRAIN = ITEMS.register(
                "zombie_brain", ZombieBrainItem::new);

        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_LOG = ITEMS.register(
                "greatwood_log", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_LOG, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_LOG = ITEMS.register(
                "silverwood_log", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_LOG, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS = ITEMS.register(
                "greatwood_planks", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_PLANKS, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS = ITEMS.register(
                "silverwood_planks", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_PLANKS, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_LEAVES = ITEMS.register(
                "greatwood_leaves", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_LEAVES, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_LEAVES = ITEMS.register(
                "silverwood_leaves", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_LEAVES, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_SAPLING = ITEMS.register(
                "greatwood_sapling", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_SAPLING, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_SAPLING = ITEMS.register(
                "silverwood_sapling", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_SAPLING, new Item.Properties()));


        public static final RegistrySupplier<BlockItem> SUPPLIER_OBSIDIAN_TOTEM = ITEMS.register(
                "obsidian_totem", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.OBSIDIAN_TOTEM, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_OBSIDIAN_TILE = ITEMS.register(
                "obsidian_tile", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.OBSIDIAN_TILE, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_PAVING_STONE_TRAVEL = ITEMS.register(
                "paving_stone_travel",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.PAVING_STONE_TRAVEL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PAVING_STONE_WARDING = ITEMS.register(
                "paving_stone_warding",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.PAVING_STONE_WARDING, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_THAUMIUM_BLOCK = ITEMS.register(
                "thaumium_block", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.THAUMIUM_BLOCK, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_TALLOW_BLOCK = ITEMS.register(
                "tallow_block", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TALLOW_BLOCK, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BLOCK = ITEMS.register(
                "arcane_stone_block", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_STONE_BLOCK, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICKS = ITEMS.register(
                "arcane_stone_bricks",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_STONE_BRICKS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GOLEM_FETTER = ITEMS.register(
                "golem_fetter", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GOLEM_FETTER, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE = ITEMS.register(
                "ancient_stone", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_ROCK = ITEMS.register(
                "ancient_rock", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_ROCK, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_CRUSTED_STONE = ITEMS.register(
                "crusted_stone", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CRUSTED_STONE, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_PEDESTAL = ITEMS.register(
                "ancient_stone_pedestal",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_PEDESTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_STAIRS = ITEMS.register(
                "ancient_stone_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_STAIRS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICK_STAIRS = ITEMS.register(
                "arcane_stone_brick_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_STONE_BRICK_STAIRS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS_STAIRS = ITEMS.register(
                "greatwood_planks_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_PLANKS_STAIRS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS_STAIRS = ITEMS.register(
                "silverwood_planks_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_PLANKS_STAIRS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_SLAB = ITEMS.register(
                "ancient_stone_slab", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_SLAB, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICK_SLAB = ITEMS.register(
                "arcane_stone_brick_slab",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_STONE_BRICK_SLAB, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS_SLAB = ITEMS.register(
                "greatwood_planks_slab",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_PLANKS_SLAB, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS_SLAB = ITEMS.register(
                "silverwood_planks_slab",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_PLANKS_SLAB, new Item.Properties())
        );

        public static final RegistrySupplier<BlockItem> SUPPLIER_AIR_CRYSTAL = ITEMS.register(
                "air_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FIRE_CRYSTAL = ITEMS.register(
                "fire_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WATER_CRYSTAL = ITEMS.register(
                "water_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_EARTH_CRYSTAL = ITEMS.register(
                "earth_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORDER_CRYSTAL = ITEMS.register(
                "order_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ENTROPY_CRYSTAL = ITEMS.register(
                "entropy_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MIXED_CRYSTAL = ITEMS.register(
                "mixed_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MIXED_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_STRANGE_CRYSTALS = ITEMS.register(
                "strange_crystals",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.STRANGE_CRYSTALS, new Item.Properties())
        );

        public static final RegistrySupplier<Item> SUPPLIER_AIR_SHARD = ITEMS.register(
                "air_shard",
                () -> new Item(new Item.Properties())
        );

        public static final RegistrySupplier<Item> SUPPLIER_FIRE_SHARD = ITEMS.register(
                "fire_shard",
                () -> new Item(new Item.Properties())
        );

        public static final RegistrySupplier<Item> SUPPLIER_WATER_SHARD = ITEMS.register(
                "water_shard",
                () -> new Item(new Item.Properties())
        );

        public static final RegistrySupplier<Item> SUPPLIER_EARTH_SHARD = ITEMS.register(
                "earth_shard",
                () -> new Item(new Item.Properties())
        );

        public static final RegistrySupplier<Item> SUPPLIER_ORDER_SHARD = ITEMS.register(
                "order_shard",
                () -> new Item(new Item.Properties())
        );

        public static final RegistrySupplier<Item> SUPPLIER_ENTROPY_SHARD = ITEMS.register(
                "entropy_shard",
                () -> new Item(new Item.Properties())
        );

        public static final RegistrySupplier<Item> SUPPLIER_BALANCE_SHARD = ITEMS.register(
                "balance_shard",
                () -> new Item(new Item.Properties())
        );

        public static final RegistrySupplier<BlockItem> SUPPLIER_CINNABAR_ORE = ITEMS.register(
                "cinnabar_ore",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CINNABAR_ORE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_ORE = ITEMS.register(
                "amber_ore",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AMBER_ORE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AIR_INFUSED_STONE = ITEMS.register(
                "air_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FIRE_INFUSED_STONE = ITEMS.register(
                "fire_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WATER_INFUSED_STONE = ITEMS.register(
                "water_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_EARTH_INFUSED_STONE = ITEMS.register(
                "earth_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORDER_INFUSED_STONE = ITEMS.register(
                "order_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ENTROPY_INFUSED_STONE = ITEMS.register(
                "entropy_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_BLOCK = ITEMS.register(
                "amber_block",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AMBER_BLOCK, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_BRICK = ITEMS.register(
                "amber_brick",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AMBER_BRICK, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WHITE_TALLOW_CANDLE = ITEMS.register(
                "white_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.WHITE_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORANGE_TALLOW_CANDLE = ITEMS.register(
                "orange_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ORANGE_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MAGENTA_TALLOW_CANDLE = ITEMS.register(
                "magenta_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MAGENTA_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE = ITEMS.register(
                "light_blue_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_BLUE_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_YELLOW_TALLOW_CANDLE = ITEMS.register(
                "yellow_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.YELLOW_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIME_TALLOW_CANDLE = ITEMS.register(
                "lime_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.LIME_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PINK_TALLOW_CANDLE = ITEMS.register(
                "pink_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.PINK_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GRAY_TALLOW_CANDLE = ITEMS.register(
                "gray_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GRAY_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE = ITEMS.register(
                "light_gray_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_GRAY_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CYAN_TALLOW_CANDLE = ITEMS.register(
                "cyan_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CYAN_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PURPLE_TALLOW_CANDLE = ITEMS.register(
                "purple_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.PURPLE_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BLUE_TALLOW_CANDLE = ITEMS.register(
                "blue_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.BLUE_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BROWN_TALLOW_CANDLE = ITEMS.register(
                "brown_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.BROWN_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREEN_TALLOW_CANDLE = ITEMS.register(
                "green_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREEN_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_RED_TALLOW_CANDLE = ITEMS.register(
                "red_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.RED_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BLACK_TALLOW_CANDLE = ITEMS.register(
                "black_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.BLACK_TALLOW_CANDLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SHIMMER_LEAF = ITEMS.register(
                "shimmer_leaf",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SHIMMER_LEAF, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CINDER_PEARL = ITEMS.register(
                "cinder_pearl",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CINDER_PEARL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MANA_SHROOM = ITEMS.register(
                "mana_shroom",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MANA_SHROOM, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ETHEREAL_BLOOM = ITEMS.register(
                "ethereal_bloom",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ETHEREAL_BLOOM, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_BELLOW = ITEMS.register(
                "arcane_bellow",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_BELLOW, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_DOOR = ITEMS.register(
                "arcane_door",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_DOOR, new Item.Properties())
        );
        public static final RegistrySupplier<BannerPatternItem> SUPPLIER_CULTIST_BANNER_PATTERN = ITEMS.register(
                "cultist_banner_pattern",
                () -> new BannerPatternItem(BannerPatternTags.CULTIST_TAG, new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GLOWING_CRUSTED_STONE = ITEMS.register(
                "glowing_crusted_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GLOWING_CRUSTED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GLYPHED_STONE = ITEMS.register(
                "glyphed_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GLYPHED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_GATEWAY = ITEMS.register(
                "ancient_gateway",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_GATEWAY, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_LOCK_EMPTY = ITEMS.register(
                "ancient_lock_empty",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_LOCK_EMPTY, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_LOCK_INSERTED = ITEMS.register(
                "ancient_lock_inserted",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_LOCK_INSERTED, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ELDRITCH_CRAB_SPAWNER = ITEMS.register(
                "eldritch_crab_spawner",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_CRAB_SPAWNER, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_RUNED_STONE = ITEMS.register(
                "runed_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CRUSTED_TAINT = ITEMS.register(
                "crusted_taint",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CRUSTED_TAINT, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_TAINTED_SOIL = ITEMS.register(
                "tainted_soil",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_SOIL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FIBROUS_TAINT = ITEMS.register(
                "fibrous_taint",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FIBROUS_TAINT, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_TAINTED_GRASS = ITEMS.register(
                "tainted_grass",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_GRASS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_TAINTED_PLANT = ITEMS.register(
                "tainted_plant",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_PLANT, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SPORE_STALK = ITEMS.register(
                "spore_stalk",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SPORE_STALK, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MATURE_SPORE_STALK = ITEMS.register(
                "mature_spore_stalk",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MATURE_SPORE_STALK, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_TABLE = ITEMS.register(
                "table",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TABLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_WORKBENCH = ITEMS.register(
                "arcane_workbench",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_WORKBENCH, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_DECONSTRUCTION_TABLE = ITEMS.register(
                "deconstruction_table",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.DECONSTRUCTION_TABLE, new Item.Properties())
        );
        public static final RegistrySupplier<InkWellItem> SUPPLIER_INK_WELL = ITEMS.register(
                "ink_well",
                InkWellItem::new
        );
        public static final RegistrySupplier<ResearchNoteItem> SUPPLIER_RESEARCH_NOTE = ITEMS.register(
                "research_note",
                ResearchNoteItem::new
        );
        public static final RegistrySupplier<WandCastingItem> SUPPLIER_WAND_CASTING = ITEMS.register(
                "wand_casting",
                WandCastingItem::new
        );
        public static final RegistrySupplier<SceptreCastingItem> SUPPLIER_SCEPTRE_CASTING = ITEMS.register(
                "sceptre_casting",
                SceptreCastingItem::new
        );
        public static final RegistrySupplier<StaffCastingItem> SUPPLIER_STAFF_CASTING = ITEMS.register(
                "staff_casting",
                StaffCastingItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_VIS_RELAY = ITEMS.register(
                "vis_relay",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.VIS_RELAY, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_VIS_CHARGE_RELAY = ITEMS.register(
                "vis_charge_relay",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.VIS_CHARGE_RELAY, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_NODE_STABILIZER = ITEMS.register(
                "node_stabilizer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.NODE_STABILIZER, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ADVANCED_NODE_STABILIZER = ITEMS.register(
                "advanced_node_stabilizer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ADVANCED_NODE_STABILIZER, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_NODE_TRANSDUCER = ITEMS.register(
                "node_transducer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.NODE_TRANSDUCER, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ALCHEMICAL_FURNACE = ITEMS.register(
                "alchemical_furnace",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ALCHEMICAL_FURNACE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ADVANCED_ALCHEMICAL_CONSTRUCT = ITEMS.register(
                "advanced_alchemical_construct",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ADVANCED_ALCHEMICAL_CONSTRUCT, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ALCHEMICAL_CONSTRUCT = ITEMS.register(
                "alchemical_construct",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ALCHEMICAL_CONSTRUCT, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_ALEMBIC = ITEMS.register(
                "arcane_alembic",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_ALEMBIC, new Item.Properties())
        );
        public static final RegistrySupplier<EssentiaJarBlockItem> SUPPLIER_ESSENTIA_JAR = ITEMS.register(
                "essentia_jar",
                EssentiaJarBlockItem::new
        );
        public static final RegistrySupplier<VoidJarBlockItem> SUPPLIER_VOID_JAR = ITEMS.register(
                "void_jar",
                VoidJarBlockItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BRAIN_JAR = ITEMS.register(
                "brain_jar",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.BRAIN_JAR, new Item.Properties())
        );
        public static final RegistrySupplier<NodeJarBlockItem> SUPPLIER_NODE_JAR = ITEMS.register(
                "node_jar",
                NodeJarBlockItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ITEM_CRATE = ITEMS.register(
                "item_crate",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ITEM_CRATE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CRUCIBLE = ITEMS.register(
                "crucible",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CRUCIBLE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_LAMP = ITEMS.register(
                "arcane_lamp",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_LAMP, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GROWTH_ARCANE_LAMP = ITEMS.register(
                "growth_arcane_lamp",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GROWTH_ARCANE_LAMP, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FERTILITY_ARCANE_LAMP = ITEMS.register(
                "fertility_arcane_lamp",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FERTILITY_ARCANE_LAMP, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MNEMONIC_MATRIX = ITEMS.register(
                "mnemonic_matrix",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MNEMONIC_MATRIX, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_LEVITATOR = ITEMS.register(
                "arcane_levitator",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_LEVITATOR, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_RESERVOIR = ITEMS.register(
                "essentia_reservoir",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_RESERVOIR, new Item.Properties())
        );
        public static final RegistrySupplier<ManaBeanItem> SUPPLIER_MANA_BEAN = ITEMS.register(
                "mana_bean",
                ManaBeanItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE = ITEMS.register(
                "essentia_tube",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE_VALVE = ITEMS.register(
                "essentia_tube_valve",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_VALVE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE_FILTER = ITEMS.register(
                "essentia_tube_filter",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_FILTER, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE_RESTRICT = ITEMS.register(
                "essentia_tube_restrict",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_RESTRICT, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE_ONEWAY = ITEMS.register(
                "essentia_tube_oneway",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_ONEWAY, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_BUFFER = ITEMS.register(
                "essentia_buffer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_BUFFER, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_CENTRIFUGE = ITEMS.register(
                "essentia_centrifuge",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_CENTRIFUGE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_CRYSTALLIZER = ITEMS.register(
                "essentia_crystallizer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_CRYSTALLIZER, new Item.Properties())
        );
        public static final RegistrySupplier<CrystalEssenceItem> SUPPLIER_CRYSTAL_ESSENCE = ITEMS.register(
                "crystal_essentia", CrystalEssenceItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_PRESSURE_PLATE = ITEMS.register(
                "arcane_pressure_plate",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_PRESSURE_PLATE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_BORE_BASE = ITEMS.register(
                "arcane_bore",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_BORE_BASE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_EAR = ITEMS.register(
                "arcane_ear",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_EAR, new Item.Properties())
        );
        public static final RegistrySupplier<BucketItem> SUPPLIER_DEATH_FLUID_BUCKET = ITEMS.register(
                "death_fluid_bucket",
                () -> new BucketItem(
                        ThaumcraftFluids.ThaumcraftFluidInstances.DEATH_FLUID, new Item.Properties().craftRemainder(
                        Items.BUCKET).stacksTo(1))
        );
        public static final RegistrySupplier<BucketItem> SUPPLIER_PURE_FLUID_BUCKET = ITEMS.register(
                "pure_fluid_bucket",
                () -> new BucketItem(
                        ThaumcraftFluids.ThaumcraftFluidInstances.PURE_FLUID_SOURCE, new Item.Properties().craftRemainder(
                        Items.BUCKET).stacksTo(1))
        );
        public static final RegistrySupplier<ThaumiumArmorItem> SUPPLIER_THAUMIUM_HELMET = ITEMS.register(
                "thaumium_helmet",
                () -> new ThaumiumArmorItem(
                        ToolAndArmorMaterial.THAUMIUM,
                        ArmorItem.Type.HELMET,
                        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<ThaumiumArmorItem> SUPPLIER_THAUMIUM_CHESTPLATE = ITEMS.register(
                "thaumium_chestplate",
                () -> new ThaumiumArmorItem(
                        ToolAndArmorMaterial.THAUMIUM,
                        ArmorItem.Type.CHESTPLATE,
                        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<ThaumiumArmorItem> SUPPLIER_THAUMIUM_LEGGINGS = ITEMS.register(
                "thaumium_leggings",
                () -> new ThaumiumArmorItem(
                        ToolAndArmorMaterial.THAUMIUM,
                        ArmorItem.Type.LEGGINGS,
                        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<ThaumiumArmorItem> SUPPLIER_THAUMIUM_BOOTS = ITEMS.register(
                "thaumium_boots",
                () -> new ThaumiumArmorItem(
                        ToolAndArmorMaterial.THAUMIUM,
                        ArmorItem.Type.BOOTS,
                        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<VoidArmorItem> SUPPLIER_VOID_HELMET = ITEMS.register(
                "void_helmet",
                () -> new VoidArmorItem(
                        ToolAndArmorMaterial.ARMOR_VOID,
                        ArmorItem.Type.HELMET,
                        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<VoidArmorItem> SUPPLIER_VOID_CHESTPLATE = ITEMS.register(
                "void_chestplate",
                () -> new VoidArmorItem(
                        ToolAndArmorMaterial.ARMOR_VOID,
                        ArmorItem.Type.CHESTPLATE,
                        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<VoidArmorItem> SUPPLIER_VOID_LEGGINGS = ITEMS.register(
                "void_leggings",
                () -> new VoidArmorItem(
                        ToolAndArmorMaterial.ARMOR_VOID,
                        ArmorItem.Type.LEGGINGS,
                        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<VoidArmorItem> SUPPLIER_VOID_BOOTS = ITEMS.register(
                "void_boots",
                () -> new VoidArmorItem(
                        ToolAndArmorMaterial.ARMOR_VOID,
                        ArmorItem.Type.BOOTS,
                        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );

        public static final RegistrySupplier<MirrorBlockItem> SUPPLIER_MIRROR = ITEMS.register(
                "mirror",
                () -> new MirrorBlockItem(
                        ThaumcraftBlocks.ThaumcraftBlockInstances.MIRROR,
                        new Item.Properties()
                )
        );
        public static final RegistrySupplier<MirrorBlockItem> SUPPLIER_ESSENTIA_MIRROR = ITEMS.register(
                "essentia_mirror",
                () -> new MirrorBlockItem(
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_MIRROR,
                        new Item.Properties()
                )
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_PEDESTAL = ITEMS.register(
                "arcane_pedestal",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_PEDESTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_INFUSION_MATRIX = ITEMS.register(
                "infusion_matrix",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.INFUSION_MATRIX, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WAND_RECHARGE_PEDESTAL = ITEMS.register(
                "wand_recharge_pedestal",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.WAND_RECHARGE_PEDESTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_COMPOUND_RECHARGE_FOCUS = ITEMS.register(
                "compound_recharge_focus",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.COMPOUND_RECHARGE_FOCUS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_SPA = ITEMS.register(
                "arcane_spa",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_SPA, new Item.Properties())
        );
        public static final RegistrySupplier<BathSaltsItem> SUPPLIER_BATH_SALTS = ITEMS.register(
                "bath_salts",
                BathSaltsItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FOCAL_MANIPULATOR =
                ITEMS.register(
                        "focal_manipulator",
                        () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FOCAL_MANIPULATOR, new Item.Properties())
                );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FLUX_SCRUBBER =
                ITEMS.register(
                        "flux_scrubber",
                        () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FLUX_SCRUBBER, new Item.Properties())
                );
        public static final RegistrySupplier<WispEssenceItem> SUPPLIER_WISP_ESSENCE =
                ITEMS.register(
                        "wisp_essence",
                        WispEssenceItem::new
                );

        public static final RegistrySupplier<VoidSwordItem> SUPPLIER_VOID_SWORD = ITEMS.register(
                "void_sword",
                VoidSwordItem::new
        );
        public static final RegistrySupplier<VoidShovelItem> SUPPLIER_VOID_SHOVEL = ITEMS.register(
                "void_shovel",
                VoidShovelItem::new
        );
        public static final RegistrySupplier<VoidPickaxeItem> SUPPLIER_VOID_PICKAXE = ITEMS.register(
                "void_pickaxe",
                VoidPickaxeItem::new
        );
        public static final RegistrySupplier<VoidHoeItem> SUPPLIER_VOID_HOE = ITEMS.register(
                "void_hoe",
                VoidHoeItem::new
        );
        public static final RegistrySupplier<VoidAxeItem> SUPPLIER_VOID_AXE = ITEMS.register(
                "void_axed",
                VoidAxeItem::new
        );

        public static final RegistrySupplier<SwordItem> SUPPLIER_THAUMIUM_SWORD = ITEMS.register(
                "thaumium_sword",
                () -> new SwordItem(ToolAndArmorMaterial.TOOL_THAUMIUM, 3, -2.4F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<ShovelItem> SUPPLIER_THAUMIUM_SHOVEL = ITEMS.register(
                "thaumium_shovel",
                () -> new ShovelItem(ToolAndArmorMaterial.TOOL_THAUMIUM, 1.5F, -3.0F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<PickaxeItem> SUPPLIER_THAUMIUM_PICKAXE = ITEMS.register(
                "thaumium_pickaxe",
                () -> new PickaxeItem(ToolAndArmorMaterial.TOOL_THAUMIUM, 1, -2.8F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<AxeItem> SUPPLIER_THAUMIUM_AXE = ITEMS.register(
                "thaumium_axe",
                () -> new AxeItem(ToolAndArmorMaterial.TOOL_THAUMIUM, 6.0F, -3.1F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<HoeItem> SUPPLIER_THAUMIUM_HOE = ITEMS.register(
                "thaumium_hoe",
                () -> new HoeItem(ToolAndArmorMaterial.TOOL_THAUMIUM, -2, -1.0F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
                )
        );
        public static final RegistrySupplier<PrimalCrusherItem> SUPPLIER_PRIMAL_CRUSHER = ITEMS.register(
                "primal_crusher",
                PrimalCrusherItem::new
        );
        public static final RegistrySupplier<ElementalShovelItem> SUPPLIER_ELEMENTAL_SHOVEL = ITEMS.register(
                "elemental_shovel",
                ElementalShovelItem::new
        );
        public static final RegistrySupplier<ElementalPickaxeItem> SUPPLIER_ELEMENTAL_PICKAXE = ITEMS.register(
                "elemental_pickaxe",
                ElementalPickaxeItem::new
        );
        public static final RegistrySupplier<ElementalSwordItem> SUPPLIER_ELEMENTAL_SWORD = ITEMS.register(
                "elemental_sword",
                ElementalSwordItem::new
        );
        public static final RegistrySupplier<ElementalHoeItem> SUPPLIER_ELEMENTAL_HOE = ITEMS.register(
                "elemental_hoe",
                ElementalHoeItem::new
        );
        public static final RegistrySupplier<ElementalAxeItem> SUPPLIER_ELEMENTAL_AXE = ITEMS.register(
                "elemental_axe",
                ElementalAxeItem::new
        );
        public static final RegistrySupplier<CrimsonSwordItem> SUPPLIER_CRIMSON_SWORD = ITEMS.register(
                "crimson_sword",
                CrimsonSwordItem::new
        );
        public static final RegistrySupplier<BoneBowItem> SUPPLIER_BONE_BOW = ITEMS.register(
                "bone_bow",
                BoneBowItem::new
        );
        public static final RegistrySupplier<AirArrowItem> SUPPLIER_AIR_ARROW = ITEMS.register(
                "air_arrow",
                AirArrowItem::new
        );
        public static final RegistrySupplier<FireArrowItem> SUPPLIER_FIRE_ARROW = ITEMS.register(
                "fire_arrow",
                FireArrowItem::new
        );
        public static final RegistrySupplier<EarthArrowItem> SUPPLIER_EARTH_ARROW = ITEMS.register(
                "earth_arrow",
                EarthArrowItem::new
        );
        public static final RegistrySupplier<WaterArrowItem> SUPPLIER_WATER_ARROW = ITEMS.register(
                "water_arrow",
                WaterArrowItem::new
        );
        public static final RegistrySupplier<OrderArrowItem> SUPPLIER_ORDER_ARROW = ITEMS.register(
                "order_arrow",
                OrderArrowItem::new
        );
        public static final RegistrySupplier<EntropyArrowItem> SUPPLIER_ENTROPY_ARROW = ITEMS.register(
                "entropy_arrow",
                EntropyArrowItem::new
        );
        public static final RegistrySupplier<GogglesOfRevealingItem> SUPPLIER_GOGGLES_OF_REVEALING = ITEMS.register(
                "goggles_of_revealing",
                GogglesOfRevealingItem::new
        );
//        public static final RegistrySupplier<RobeArmorItem> SUPPLIER_ROBE_HELMET = ITEMS.register(
//                "robe_helmet",
//                () -> new RobeArmorItem(ArmorItem.Type.HELMET)
//        );
        public static final RegistrySupplier<RobeArmorItem> SUPPLIER_ROBE_CHESTPLATE = ITEMS.register(
                "robe_chestplate",
                () -> new RobeArmorItem(ArmorItem.Type.CHESTPLATE)
        );
        public static final RegistrySupplier<RobeArmorItem> SUPPLIER_ROBE_LEGGINGS = ITEMS.register(
                "robe_leggings",
                () -> new RobeArmorItem(ArmorItem.Type.LEGGINGS)
        );
        public static final RegistrySupplier<RobeArmorItem> SUPPLIER_ROBE_BOOTS = ITEMS.register(
                "robe_boots",
                () -> new RobeArmorItem(ArmorItem.Type.BOOTS)
        );
    }

    public static class ItemTags {
        //TODO:Tag for forge and fabric,im lazy to write tag json :(
        // maybe i need even several issue/PR to realize where(recipe or st.) i didn't notice this part.
        public static final TagKey<Item> VOID_INGOT_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("thaumcraft:void_ingot"));
        public static final TagKey<Item> THAUMIUM_INGOT_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("thaumcraft:thaumium_ingot"));
        public static final TagKey<Item> PRIME_PEARL_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("thaumcraft:prime_pearl"));

        public static final TagKey<Item> SILVER_NUGGET_FORGE_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("forge:nuggets/silver"));
        public static final TagKey<Item> SILVER_NUGGET_FABRIC_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("c:silver_nuggets"));
        public static final TagKey<Item> COPPER_NUGGET_FORGE_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("forge:nuggets/copper"));
        public static final TagKey<Item> COPPER_NUGGET_FABRIC_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("c:copper_nuggets"));
        public static final TagKey<Item> GOLD_NUGGET_FORGE_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("forge:nuggets/gold"));
        public static final TagKey<Item> GOLD_NUGGET_FABRIC_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("c:gold_nuggets"));
        public static final TagKey<Item> IRON_NUGGET_FORGE_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("forge:nuggets/iron"));
        public static final TagKey<Item> IRON_NUGGET_FABRIC_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("c:iron_nuggets"));
        public static final TagKey<Item> PLANKS_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("minecraft:planks"));
        public static final TagKey<Item> BLACK_DYE_FORGE_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("forge:dyes/black"));
        public static final TagKey<Item> BLACK_DYE_FABRIC_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("c:black_dyes"));
        public static final TagKey<Item> REPAIR_ENCHANTMENT_CAN_APPLY = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "repair_enchantment_can_apply")
        );
        public static final TagKey<Item> RECHARGE_PEDESTAL_CANNOT_APPLY = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "recharge_pedestal_cannot_apply")
        );
    }

    public static class BannerPatternTags {
        //TODO:(maybe)all aspect banner patterns,maybe we will need a map to store.Wont finished even first-time compile?
        public static final TagKey<BannerPattern> CULTIST_TAG = TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(Thaumcraft.MOD_ID, "cultist"));
    }

    public static class BannerPatternsRegistry {
        public static final DeferredRegister<BannerPattern> BANNER_PATTERNS =
                DeferredRegister.create(Thaumcraft.MOD_ID, Registries.BANNER_PATTERN);
        public static final RegistrySupplier<BannerPattern> SUPPLIER_CULTIST_PATTERN
                = BANNER_PATTERNS.register("cultist", () -> new BannerPattern("cultist"));

    }

    public static class ToolAndArmorMaterial {

        public static final Tier TOOL_THAUMIUM = new Tier() {
            @Override
            public int getUses() {
                return 400;
            }

            @Override
            public float getSpeed() {
                return 7F;
            }

            @Override
            public float getAttackDamageBonus() {
                return 2F;
            }

            @Override
            public int getLevel() {
                return 3;
            }

            @Override
            public int getEnchantmentValue() {
                return 22;
            }

            public static final Ingredient ingredient = Ingredient.of(THAUMIUM_INGOT_TAG);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }
        };
        public static final Tier TOOL_VOID = new Tier() {
            //"VOID", 4, 150, 8F, 3, 10
            @Override
            public int getUses() {
                return 150;
            }

            @Override
            public float getSpeed() {
                return 8F;
            }

            @Override
            public float getAttackDamageBonus() {
                return 3F;
            }

            @Override
            public int getLevel() {
                return 4;
            }

            @Override
            public int getEnchantmentValue() {
                return 10;
            }

            public static final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }
        };
        public static final Tier TOOL_THAUMIUM_ELEMENTAL = new Tier() {
            @Override
            public int getUses() {
                return 1500;
            }

            @Override
            public float getSpeed() {
                return 10F;
            }

            @Override
            public float getAttackDamageBonus() {
                return 3F;
            }

            @Override
            public int getLevel() {
                return 3;
            }

            @Override
            public int getEnchantmentValue() {
                return 18;
            }

            public static final Ingredient ingredient = Ingredient.of(THAUMIUM_INGOT_TAG);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }
        };
        public static final ArmorMaterial THAUMIUM = new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 13 * 25;
                    case CHESTPLATE -> 15 * 25;
                    case LEGGINGS -> 16 * 25;
                    case BOOTS -> 11 * 25;
                };
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 2;
                    case CHESTPLATE -> 6;
                    case LEGGINGS -> 5;
                    case BOOTS -> 2;
                };
            }

            @Override
            public int getEnchantmentValue() {
                return 25;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_IRON;
            }

            public static final Ingredient ingredient = Ingredient.of(THAUMIUM_INGOT_TAG);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }

            @Override
            public @NotNull String getName() {
                return "thaumium";
            }

            @Override
            public float getToughness() {
                return 0;
            }

            @Override
            public float getKnockbackResistance() {
                return 0;
            }
        };
        public static final ArmorMaterial ROBE = new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 11 * 25;
                    case CHESTPLATE -> 16 * 25;
                    case LEGGINGS -> 15 * 25;
                    case BOOTS -> 13 * 25;
                };
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 1;
                    case CHESTPLATE -> 3;
                    case LEGGINGS -> 2;
                    case BOOTS -> 1;
                };
            }

            @Override
            public int getEnchantmentValue() {
                return 25;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_IRON;
            }

            private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.ENCHANTED_FABRIC);
            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }

            @Override
            public @NotNull String getName() {
                return "special";
            }

            @Override
            public float getToughness() {
                return 0;
            }

            @Override
            public float getKnockbackResistance() {
                return 0;
            }
        };
        public static final ArmorMaterial SPECIAL = new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 11 * 25;
                    case CHESTPLATE -> 16 * 25;
                    case LEGGINGS -> 15 * 25;
                    case BOOTS -> 13 * 25;
                };
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 1;
                    case CHESTPLATE -> 3;
                    case LEGGINGS -> 2;
                    case BOOTS -> 1;
                };
            }

            @Override
            public int getEnchantmentValue() {
                return 25;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_IRON;
            }

            private final Ingredient ingredient = Ingredient.of(GOLD_INGOT);
            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }

            @Override
            public @NotNull String getName() {
                return "special";
            }

            @Override
            public float getToughness() {
                return 0;
            }

            @Override
            public float getKnockbackResistance() {
                return 0;
            }
        };
        public static final ArmorMaterial THAUMIUM_FORTRESS = new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 13 * 40;
                    case CHESTPLATE -> 15 * 40;
                    case LEGGINGS -> 16 * 40;
                    case BOOTS -> 11 * 40;
                };
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 3;
                    case CHESTPLATE -> 7;
                    case LEGGINGS -> 6;
                    case BOOTS -> 3;
                };
            }

            @Override
            public int getEnchantmentValue() {
                return 25;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_IRON;
            }

            public static final Ingredient ingredient = Ingredient.of(THAUMIUM_INGOT_TAG);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }

            @Override
            public @NotNull String getName() {
                return "fortress";
            }

            @Override
            public float getToughness() {
                return 0;
            }

            @Override
            public float getKnockbackResistance() {
                return 0;
            }
        };
        public static final ArmorMaterial ARMOR_VOID = new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 13 * 10;
                    case CHESTPLATE -> 15 * 10;
                    case LEGGINGS -> 16 * 10;
                    case BOOTS -> 11 * 10;
                };
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 3;
                    case CHESTPLATE -> 7;
                    case LEGGINGS -> 6;
                    case BOOTS -> 3;
                };
            }

            @Override
            public int getEnchantmentValue() {
                return 10;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_IRON;
            }

            public static final Ingredient ingredient = Ingredient.of(VOID_INGOT_TAG);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }

            @Override
            public @NotNull String getName() {
                return "void";
            }

            @Override
            public float getToughness() {
                return 0;
            }

            @Override
            public float getKnockbackResistance() {
                return 0;
            }
        };
        public static final ArmorMaterial VOID_FORTRESS = new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 13 * 18;
                    case LEGGINGS -> 15 * 18;
                    case CHESTPLATE -> 16 * 18;
                    case BOOTS -> 11 * 18;
                };
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return switch (type) {
                    case HELMET -> 4;
                    case CHESTPLATE -> 8;
                    case LEGGINGS -> 7;
                    case BOOTS -> 4;
                };
            }

            @Override
            public int getEnchantmentValue() {
                return 10;
            }

            @Override
            public @NotNull SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_IRON;
            }

            public static final Ingredient ingredient = Ingredient.of(VOID_INGOT_TAG);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }

            @Override
            public @NotNull String getName() {
                return "voidfortress";
            }

            @Override
            public float getToughness() {
                return 0;
            }

            @Override
            public float getKnockbackResistance() {
                return 0;
            }
        };
        public static final Tier PRIMAL_VOID = new Tier() {

            @Override
            public int getUses() {
                return 500;
            }

            @Override
            public float getSpeed() {
                return 8.0F;
            }

            @Override
            public float getAttackDamageBonus() {
                return 4.0F;
            }

            @Override
            public int getLevel() {
                return 5;
            }

            @Override
            public int getEnchantmentValue() {
                return 20;
            }

            private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }
        };
        public static final Tier CRIMSON_VOID = new Tier() {

            @Override
            public int getUses() {
                return 200;
            }

            @Override
            public float getSpeed() {
                return 8.0F;
            }

            @Override
            public float getAttackDamageBonus() {
                return 3.5F;
            }

            @Override
            public int getLevel() {
                return 5;
            }

            @Override
            public int getEnchantmentValue() {
                return 20;
            }

            private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM);

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }
        };
    }

    public static final BannerPattern CULTIST_PATTERN = BannerPatternsRegistry.SUPPLIER_CULTIST_PATTERN.get();

    public static void init() {

        Registry.ITEMS.register();
        FuelRegistry.register(6400, ThaumcraftItemInstances.ALUMENTUM);
        FuelRegistry.register(400, ThaumcraftItemInstances.GREATWOOD_LOG, ThaumcraftItemInstances.SILVERWOOD_LOG);//azanor's idea
        FuelRegistry.register(300, ThaumcraftItemInstances.GREATWOOD_PLANKS, ThaumcraftItemInstances.SILVERWOOD_PLANKS);
        BannerPatternsRegistry.BANNER_PATTERNS.register();
    }
}
