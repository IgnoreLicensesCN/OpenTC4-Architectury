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
import thaumcraft.common.items.equipment.armor.cultist.CultistBootsItem;
import thaumcraft.common.items.equipment.armor.cultist.CultistLeaderPlateArmorItem;
import thaumcraft.common.items.equipment.armor.cultist.CultistPlateArmorItem;
import thaumcraft.common.items.equipment.armor.cultist.CultistRobeArmorItem;
import thaumcraft.common.items.equipment.armor.thaumaturge.*;
import thaumcraft.common.items.equipment.armor.voidarmor.VoidArmorItem;
import thaumcraft.common.items.equipment.armor.voidarmor.VoidRobeArmorItem;
import thaumcraft.common.items.equipment.armor.voidarmor.VoidRobeHelmetItem;
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
        public static AlumentumItem ALUMENTUM() {
            return Registry.SUPPLIER_ALUMENTUM.get();
        }//itemResource:0

        public static BlockItem NITOR() {
            return Registry.SUPPLIER_NITOR.get();
        }//itemResource:1

        public static Item THAUMIUM_INGOT() {
            return Registry.SUPPLIER_THAUMIUM_INGOT.get();
        }//itemResource:2 Thaumium Ingot

        public static Item QUICK_SILVER() {
            return Registry.SUPPLIER_QUICK_SILVER.get();
        }//itemResource:3

        public static Item MAGIC_TALLOW() {
            return Registry.SUPPLIER_MAGIC_TALLOW.get();
        }//itemResource:4

        public static Item AMBER_GEM() {
            return Registry.SUPPLIER_AMBER_GEM.get();
        }//itemResource:6

        public static Item ENCHANTED_FABRIC() {
            return Registry.SUPPLIER_ENCHANTED_FABRIC.get();
        }//itemResource:7

        public static Item VIS_FILTER() {
            return Registry.SUPPLIER_VIS_FILTER.get();
        }//itemResource:8

        public static KnowledgeFragmentItem KNOWLEDGE_FRAGMENT() {
            return Registry.SUPPLIER_KNOWLEDGE_FRAGMENT.get();
        }//itemResource:9

        public static Item MIRRORED_GLASS() {
            return Registry.SUPPLIER_MIRRORED_GLASS.get();
        }//itemResource:10

        public static Item TAINTED_GOO() {
            return Registry.SUPPLIER_TAINTED_GOO.get();
        }//itemResource:11

        public static Item TAINT_TENDRIL() {
            return Registry.SUPPLIER_TAINT_TENDRIL.get();
        }//itemResource:12 //TODO:new class

        public static Item JAR_LABEL() {
            return Registry.SUPPLIER_JAR_LABEL.get();
        }//itemResource:13

        public static Item SALIS_MUNDUS() {
            return Registry.SUPPLIER_SALIS_MUNDUS.get();
        }//itemResource:14

        public static PrimalCharmItem PRIMAL_CHARM() {
            return Registry.SUPPLIER_PRIMAL_CHARM.get();
        }//itemResource:15

        public static Item VOID_INGOT() {
            return Registry.SUPPLIER_VOID_INGOT.get();
        }//itemResource:16

        public static Item VOID_SEED() {
            return Registry.SUPPLIER_VOID_SEED.get();
        }//itemResource:17

        public static Item GOLD_COIN() {
            return Registry.SUPPLIER_GOLD_COIN.get();
        }//itemResource:18

        public static EldritchEyeItem ELDRITCH_EYE() {
            return Registry.SUPPLIER_ELDRITCH_EYE.get();
        }//itemEldritchObject:0

        public static CrimsonRitesItem CRIMSON_RITES() {
            return Registry.SUPPLIER_CRIMSON_RITES.get();
        }//itemEldritchObject:1

        public static RunedTabletItem RUNED_TABLET() {
            return Registry.SUPPLIER_RUNED_TABLET.get();
        }//itemEldritchObject:2

        public static PrimePearlItem PRIME_PEARL() {
            return Registry.SUPPLIER_PRIME_PEARL.get();
        }//itemEldritchObject:3

        public static EldritchObeliskPlacerItem ELDRITCH_OBELISK_PLACER() {
            return Registry.SUPPLIER_ELDRITCH_OBELISK_PLACER.get();
        }//itemEldritchObject:4

        public static ThaumometerItem THAUMOMETER() {
            return Registry.SUPPLIER_THAUMOMETER_ITEM.get();
        }

        // Wand Caps
        public static CopperWandCapItem COPPER_WAND_CAP() {
            return Registry.SUPPLIER_COPPER_WAND_CAP.get();
        }

        public static GoldWandCapItem GOLD_WAND_CAP() {
            return Registry.SUPPLIER_GOLD_WAND_CAP.get();
        }

        public static IronWandCapItem IRON_WAND_CAP() {
            return Registry.SUPPLIER_IRON_WAND_CAP.get();
        }

        public static SilverWandCapItem SILVER_WAND_CAP() {
            return Registry.SUPPLIER_SILVER_WAND_CAP.get();
        }

        public static ThaumiumWandCapItem THAUMIUM_WAND_CAP() {
            return Registry.SUPPLIER_THAUMIUM_WAND_CAP.get();
        }

        public static VoidWandCapItem VOID_WAND_CAP() {
            return Registry.SUPPLIER_VOID_WAND_CAP.get();
        }

        //wand rods
        public static BlazeWandRodItem BLAZE_WAND_ROD() {
            return Registry.SUPPLIER_BLAZE_WAND_ROD.get();
        }

        public static BoneWandRodItem BONE_WAND_ROD() {
            return Registry.SUPPLIER_BONE_WAND_ROD.get();
        }

        public static GreatWoodWandRodItem GREATWOOD_WAND_ROD() {
            return Registry.SUPPLIER_GREATWOOD_WAND_ROD.get();
        }

        public static IceWandRodItem ICE_WAND_ROD() {
            return Registry.SUPPLIER_ICE_WAND_ROD.get();
        }

        public static ObsidianWandRodItem OBSIDIAN_WAND_ROD() {
            return Registry.SUPPLIER_OBSIDIAN_WAND_ROD.get();
        }

        public static QuartzWandRodItem QUARTZ_WAND_ROD() {
            return Registry.SUPPLIER_QUARTZ_WAND_ROD.get();
        }

        public static ReedWandRodItem REED_WAND_ROD() {
            return Registry.SUPPLIER_REED_WAND_ROD.get();
        }

        public static SilverWoodWandRodItem SILVERWOOD_WAND_ROD() {
            return Registry.SUPPLIER_SILVERWOOD_WAND_ROD.get();
        }

        public static WoodWandRodItem WOOD_WAND_ROD() {
            return Registry.SUPPLIER_WOOD_WAND_ROD.get();
        }

        public static PrimalStaffRodItem PRIMAL_STAFF_ROD() {
            return Registry.SUPPLIER_PRIMAL_STAFF_ROD.get();
        }

        public static BlazeStaffRodItem BLAZE_STAFF_ROD() {
            return Registry.SUPPLIER_BLAZE_STAFF_ROD.get();
        }

        public static BoneStaffRodItem BONE_STAFF_ROD() {
            return Registry.SUPPLIER_BONE_STAFF_ROD.get();
        }

        public static GreatWoodStaffRodItem GREATWOOD_STAFF_ROD() {
            return Registry.SUPPLIER_GREATWOOD_STAFF_ROD.get();
        }

        public static IceStaffRodItem ICE_STAFF_ROD() {
            return Registry.SUPPLIER_ICE_STAFF_ROD.get();
        }

        public static ObsidianStaffRodItem OBSIDIAN_STAFF_ROD() {
            return Registry.SUPPLIER_OBSIDIAN_STAFF_ROD.get();
        }

        public static QuartzStaffRodItem QUARTZ_STAFF_ROD() {
            return Registry.SUPPLIER_QUARTZ_STAFF_ROD.get();
        }

        public static ReedStaffRodItem REED_STAFF_ROD() {
            return Registry.SUPPLIER_REED_STAFF_ROD.get();
        }

        public static SilverWoodStaffRodItem SILVERWOOD_STAFF_ROD() {
            return Registry.SUPPLIER_SILVERWOOD_STAFF_ROD.get();
        }

        public static Item SILVER_WAND_CAP_INERT() {
            return Registry.SUPPLIER_SILVER_WAND_CAP_INERT.get();
        }

        public static Item THAUMIUM_WAND_CAP_INERT() {
            return Registry.SUPPLIER_THAUMIUM_WAND_CAP_INERT.get();
        }

        public static Item VOID_WAND_CAP_INERT() {
            return Registry.SUPPLIER_VOID_WAND_CAP_INERT.get();
        }

        public static ZombieBrainItem ZOMBIE_BRAIN() {
            return Registry.SUPPLIER_ZOMBIE_BRAIN.get();
        }

        public static BlockItem GREATWOOD_LOG() {
            return Registry.SUPPLIER_GREATWOOD_LOG.get();
        }

        public static BlockItem SILVERWOOD_LOG() {
            return Registry.SUPPLIER_SILVERWOOD_LOG.get();
        }

        public static BlockItem GREATWOOD_PLANKS() {
            return Registry.SUPPLIER_GREATWOOD_PLANKS.get();
        }

        public static BlockItem SILVERWOOD_PLANKS() {
            return Registry.SUPPLIER_SILVERWOOD_PLANKS.get();
        }

        public static BlockItem GREATWOOD_LEAVES() {
            return Registry.SUPPLIER_GREATWOOD_LEAVES.get();
        }

        public static BlockItem SILVERWOOD_LEAVES() {
            return Registry.SUPPLIER_SILVERWOOD_LEAVES.get();
        }

        public static BlockItem GREATWOOD_SAPLING() {
            return Registry.SUPPLIER_GREATWOOD_SAPLING.get();
        }

        public static BlockItem SILVERWOOD_SAPLING() {
            return Registry.SUPPLIER_SILVERWOOD_SAPLING.get();
        }

        public static BlockItem OBSIDIAN_TOTEM() {
            return Registry.SUPPLIER_OBSIDIAN_TOTEM.get();
        }

        public static BlockItem OBSIDIAN_TIME() {
            return Registry.SUPPLIER_OBSIDIAN_TILE.get();
        }

        public static BlockItem PAVING_STONE_TRAVEL() {
            return Registry.SUPPLIER_PAVING_STONE_TRAVEL.get();
        }

        public static BlockItem PAVING_STONE_WARDING() {
            return Registry.SUPPLIER_PAVING_STONE_WARDING.get();
        }

        public static BlockItem THAUMIUM_BLOCK() {
            return Registry.SUPPLIER_THAUMIUM_BLOCK.get();
        }

        public static BlockItem TALLOW_BLOCK() {
            return Registry.SUPPLIER_TALLOW_BLOCK.get();
        }

        public static BlockItem ARCANE_STONE_BLOCK() {
            return Registry.SUPPLIER_ARCANE_STONE_BLOCK.get();
        }

        public static BlockItem ARCANE_STONE_BRICKS() {
            return Registry.SUPPLIER_ARCANE_STONE_BRICKS.get();
        }

        public static BlockItem GOLEM_FETTER() {
            return Registry.SUPPLIER_GOLEM_FETTER.get();
        }

        public static BlockItem ANCIENT_STONE() {
            return Registry.SUPPLIER_ANCIENT_STONE.get();
        }

        public static BlockItem ANCIENT_ROCK() {
            return Registry.SUPPLIER_ANCIENT_ROCK.get();
        }

        public static BlockItem CRUSTED_STONE() {
            return Registry.SUPPLIER_CRUSTED_STONE.get();
        }

        public static BlockItem ANCIENT_STONE_PEDESTAL() {
            return Registry.SUPPLIER_ANCIENT_STONE_PEDESTAL.get();
        }

        public static BlockItem ANCIENT_STONE_STAIRS() {
            return Registry.SUPPLIER_ANCIENT_STONE_STAIRS.get();
        }

        public static BlockItem ARCANE_STONE_BRICK_STAIRS() {
            return Registry.SUPPLIER_ARCANE_STONE_BRICK_STAIRS.get();
        }

        public static BlockItem GREATWOOD_PLANKS_STAIRS() {
            return Registry.SUPPLIER_GREATWOOD_PLANKS_STAIRS.get();
        }

        public static BlockItem SILVERWOOD_PLANKS_STAIRS() {
            return Registry.SUPPLIER_SILVERWOOD_PLANKS_STAIRS.get();
        }

        public static BlockItem ANCIENT_STONE_SLAB() {
            return Registry.SUPPLIER_ANCIENT_STONE_SLAB.get();
        }

        public static BlockItem ARCANE_STONE_BRICK_SLAB() {
            return Registry.SUPPLIER_ARCANE_STONE_BRICK_SLAB.get();
        }

        public static BlockItem GREATWOOD_PLANKS_SLAB() {
            return Registry.SUPPLIER_GREATWOOD_PLANKS_SLAB.get();
        }

        public static BlockItem SILVERWOOD_PLANKS_SLAB() {
            return Registry.SUPPLIER_SILVERWOOD_PLANKS_SLAB.get();
        }

        public static BlockItem AIR_CRYSTAL() {
            return Registry.SUPPLIER_AIR_CRYSTAL.get();
        }

        public static BlockItem FIRE_CRYSTAL() {
            return Registry.SUPPLIER_FIRE_CRYSTAL.get();
        }

        public static BlockItem WATER_CRYSTAL() {
            return Registry.SUPPLIER_WATER_CRYSTAL.get();
        }

        public static BlockItem EARTH_CRYSTAL() {
            return Registry.SUPPLIER_EARTH_CRYSTAL.get();
        }

        public static BlockItem ORDER_CRYSTAL() {
            return Registry.SUPPLIER_ORDER_CRYSTAL.get();
        }

        public static BlockItem ENTROPY_CRYSTAL() {
            return Registry.SUPPLIER_ENTROPY_CRYSTAL.get();
        }

        public static BlockItem MIXED_CRYSTAL() {
            return Registry.SUPPLIER_MIXED_CRYSTAL.get();
        }

        public static BlockItem STRANGE_CRYSTALS() {
            return Registry.SUPPLIER_STRANGE_CRYSTALS.get();
        }

        public static Item AIR_SHARD() {
            return Registry.SUPPLIER_AIR_SHARD.get();
        }

        public static Item FIRE_SHARD() {
            return Registry.SUPPLIER_FIRE_SHARD.get();
        }

        public static Item WATER_SHARD() {
            return Registry.SUPPLIER_WATER_SHARD.get();
        }

        public static Item EARTH_SHARD() {
            return Registry.SUPPLIER_EARTH_SHARD.get();
        }

        public static Item ORDER_SHARD() {
            return Registry.SUPPLIER_ORDER_SHARD.get();
        }

        public static Item ENTROPY_SHARD() {
            return Registry.SUPPLIER_ENTROPY_SHARD.get();
        }

        public static Item BALANCE_SHARD() {
            return Registry.SUPPLIER_BALANCE_SHARD.get();
        }

        public static Item CINNABAR_ORE() {
            return Registry.SUPPLIER_CINNABAR_ORE.get();
        }

        public static Item AMBER_ORE() {
            return Registry.SUPPLIER_AMBER_ORE.get();
        }

        public static Item AIR_INFUSED_STONE() {
            return Registry.SUPPLIER_AIR_INFUSED_STONE.get();
        }

        public static Item FIRE_INFUSED_STONE() {
            return Registry.SUPPLIER_FIRE_INFUSED_STONE.get();
        }

        public static Item WATER_INFUSED_STONE() {
            return Registry.SUPPLIER_WATER_INFUSED_STONE.get();
        }

        public static Item EARTH_INFUSED_STONE() {
            return Registry.SUPPLIER_EARTH_INFUSED_STONE.get();
        }

        public static Item ORDER_INFUSED_STONE() {
            return Registry.SUPPLIER_ORDER_INFUSED_STONE.get();
        }

        public static Item ENTROPY_INFUSED_STONE() {
            return Registry.SUPPLIER_ENTROPY_INFUSED_STONE.get();
        }

        public static Item AMBER_BLOCK() {
            return Registry.SUPPLIER_AMBER_BLOCK.get();
        }

        public static Item AMBER_BRICK() {
            return Registry.SUPPLIER_AMBER_BRICK.get();
        }

        public static BlockItem WHITE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_WHITE_TALLOW_CANDLE.get();
        }

        public static BlockItem ORANGE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_ORANGE_TALLOW_CANDLE.get();
        }

        public static BlockItem MAGENTA_TALLOW_CANDLE() {
            return Registry.SUPPLIER_MAGENTA_TALLOW_CANDLE.get();
        }

        public static BlockItem LIGHT_BLUE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE.get();
        }

        public static BlockItem YELLOW_TALLOW_CANDLE() {
            return Registry.SUPPLIER_YELLOW_TALLOW_CANDLE.get();
        }

        public static BlockItem LIME_TALLOW_CANDLE() {
            return Registry.SUPPLIER_LIME_TALLOW_CANDLE.get();
        }

        public static BlockItem PINK_TALLOW_CANDLE() {
            return Registry.SUPPLIER_PINK_TALLOW_CANDLE.get();
        }

        public static BlockItem GRAY_TALLOW_CANDLE() {
            return Registry.SUPPLIER_GRAY_TALLOW_CANDLE.get();
        }

        public static BlockItem LIGHT_GRAY_TALLOW_CANDLE() {
            return Registry.SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE.get();
        }

        public static BlockItem CYAN_TALLOW_CANDLE() {
            return Registry.SUPPLIER_CYAN_TALLOW_CANDLE.get();
        }

        public static BlockItem PURPLE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_PURPLE_TALLOW_CANDLE.get();
        }

        public static BlockItem BLUE_TALLOW_CANDLE() {
            return Registry.SUPPLIER_BLUE_TALLOW_CANDLE.get();
        }

        public static BlockItem BROWN_TALLOW_CANDLE() {
            return Registry.SUPPLIER_BROWN_TALLOW_CANDLE.get();
        }

        public static BlockItem GREEN_TALLOW_CANDLE() {
            return Registry.SUPPLIER_GREEN_TALLOW_CANDLE.get();
        }

        public static BlockItem RED_TALLOW_CANDLE() {
            return Registry.SUPPLIER_RED_TALLOW_CANDLE.get();
        }

        public static BlockItem BLACK_TALLOW_CANDLE() {
            return Registry.SUPPLIER_BLACK_TALLOW_CANDLE.get();
        }

        public static BlockItem SHIMMER_LEAF() {
            return Registry.SUPPLIER_SHIMMER_LEAF.get();
        }

        public static BlockItem CINDER_PEARL() {
            return Registry.SUPPLIER_CINDER_PEARL.get();
        }

        public static BlockItem MANA_SHROOM() {
            return Registry.SUPPLIER_MANA_SHROOM.get();
        }

        public static BlockItem ETHEREAL_BLOOM() {
            return Registry.SUPPLIER_ETHEREAL_BLOOM.get();
        }

        public static BlockItem ARCANE_BELLOW() {
            return Registry.SUPPLIER_ARCANE_BELLOW.get();
        }

        public static BlockItem ARCANE_DOOR() {
            return Registry.SUPPLIER_ARCANE_DOOR.get();
        }

        public static BannerPatternItem CULTIST_BANNER_PATTERN() {
            return Registry.SUPPLIER_CULTIST_BANNER_PATTERN.get();
        }

        public static BlockItem GLOWING_CRUSTED_STONE() {
            return Registry.SUPPLIER_GLOWING_CRUSTED_STONE.get();
        }

        public static BlockItem GLYPHED_STONE() {
            return Registry.SUPPLIER_GLYPHED_STONE.get();
        }

        public static BlockItem ANCIENT_GATEWAY() {
            return Registry.SUPPLIER_ANCIENT_GATEWAY.get();
        }

        public static BlockItem ANCIENT_LOCK_EMPTY() {
            return Registry.SUPPLIER_ANCIENT_LOCK_EMPTY.get();
        }

        public static BlockItem ANCIENT_LOCK_INSERTED() {
            return Registry.SUPPLIER_ANCIENT_LOCK_INSERTED.get();
        }

        public static BlockItem ELDRITCH_CRAB_SPAWNER() {
            return Registry.SUPPLIER_ELDRITCH_CRAB_SPAWNER.get();
        }

        public static BlockItem RUNED_STONE() {
            return Registry.SUPPLIER_RUNED_STONE.get();
        }

        public static BlockItem CRUSTED_TAINT() {
            return Registry.SUPPLIER_CRUSTED_TAINT.get();
        }

        public static BlockItem TAINTED_SOIL() {
            return Registry.SUPPLIER_TAINTED_SOIL.get();
        }

        public static BlockItem FIBROUS_TAINT() {
            return Registry.SUPPLIER_FIBROUS_TAINT.get();
        }

        public static BlockItem TAINTED_GRASS() {
            return Registry.SUPPLIER_TAINTED_GRASS.get();
        }

        public static BlockItem TAINTED_PLANT() {
            return Registry.SUPPLIER_TAINTED_PLANT.get();
        }

        public static BlockItem SPORE_STALK() {
            return Registry.SUPPLIER_SPORE_STALK.get();
        }

        public static BlockItem MATURE_SPORE_STALK() {
            return Registry.SUPPLIER_MATURE_SPORE_STALK.get();
        }

        public static BlockItem TABLE() {
            return Registry.SUPPLIER_TABLE.get();
        }

        public static InkWellItem INK_WELL() {
            return Registry.SUPPLIER_INK_WELL.get();
        }

        public static BlockItem ARCANE_WORKBENCH() {
            return Registry.SUPPLIER_ARCANE_WORKBENCH.get();
        }

        public static BlockItem DECONSTRUCTION_TABLE() {
            return Registry.SUPPLIER_DECONSTRUCTION_TABLE.get();
        }

        public static ResearchNoteItem RESEARCH_NOTE() {
            return Registry.SUPPLIER_RESEARCH_NOTE.get();
        }

        public static WandCastingItem WAND_CASTING() {
            return Registry.SUPPLIER_WAND_CASTING.get();
        }

        public static StaffCastingItem STAFF_CASTING() {
            return Registry.SUPPLIER_STAFF_CASTING.get();
        }

        public static SceptreCastingItem SCEPTRE_CASTING() {
            return Registry.SUPPLIER_SCEPTRE_CASTING.get();
        }

        public static BlockItem VIS_RELAY() {
            return Registry.SUPPLIER_VIS_RELAY.get();
        }

        public static BlockItem VIS_CHARGE_RELAY() {
            return Registry.SUPPLIER_VIS_CHARGE_RELAY.get();
        }

        public static BlockItem NODE_STABILIZER() {
            return Registry.SUPPLIER_NODE_STABILIZER.get();
        }

        public static BlockItem ADVANCED_NODE_STABILIZER() {
            return Registry.SUPPLIER_ADVANCED_NODE_STABILIZER.get();
        }

        public static BlockItem NODE_TRANSDUCER() {
            return Registry.SUPPLIER_NODE_TRANSDUCER.get();
        }

        public static BlockItem ALCHEMICAL_FURNACE() {
            return Registry.SUPPLIER_ALCHEMICAL_FURNACE.get();
        }

        public static BlockItem ADVANCED_ALCHEMICAL_CONSTRUCT() {
            return Registry.SUPPLIER_ADVANCED_ALCHEMICAL_CONSTRUCT.get();
        }

        public static BlockItem ALCHEMICAL_CONSTRUCT() {
            return Registry.SUPPLIER_ALCHEMICAL_CONSTRUCT.get();
        }

        public static BlockItem ARCANE_ALEMBIC() {
            return Registry.SUPPLIER_ARCANE_ALEMBIC.get();
        }

        public static EssentiaJarBlockItem ESSENTIA_JAR() {
            return Registry.SUPPLIER_ESSENTIA_JAR.get();
        }

        public static VoidJarBlockItem VOID_JAR() {
            return Registry.SUPPLIER_VOID_JAR.get();
        }

        public static BlockItem BRAIN_JAR() {
            return Registry.SUPPLIER_BRAIN_JAR.get();
        }

        public static NodeJarBlockItem NODE_JAR() {
            return Registry.SUPPLIER_NODE_JAR.get();
        }

        public static BlockItem ITEM_CRATE() {
            return Registry.SUPPLIER_ITEM_CRATE.get();
        }

        public static BlockItem CRUCIBLE() {
            return Registry.SUPPLIER_CRUCIBLE.get();
        }

        public static BlockItem ARCANE_LAMP() {
            return Registry.SUPPLIER_ARCANE_LAMP.get();
        }

        public static BlockItem GROWTH_ARCANE_LAMP() {
            return Registry.SUPPLIER_GROWTH_ARCANE_LAMP.get();
        }

        public static BlockItem FERTILITY_ARCANE_LAMP() {
            return Registry.SUPPLIER_FERTILITY_ARCANE_LAMP.get();
        }

        public static BlockItem MNEMONIC_MATRIX() {
            return Registry.SUPPLIER_MNEMONIC_MATRIX.get();
        }

        public static BlockItem ARCANE_LEVITATOR() {
            return Registry.SUPPLIER_ARCANE_LEVITATOR.get();
        }

        public static BlockItem ESSENTIA_RESERVOIR() {
            return Registry.SUPPLIER_ESSENTIA_RESERVOIR.get();
        }

        public static ManaBeanItem MANA_BEAN() {
            return Registry.SUPPLIER_MANA_BEAN.get();
        }

        public static BlockItem ESSENTIA_TUBE() {
            return Registry.SUPPLIER_ESSENTIA_TUBE.get();
        }

        public static BlockItem ESSENTIA_TUBE_VALVE() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_VALVE.get();
        }

        public static BlockItem ESSENTIA_TUBE_FILTER() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_FILTER.get();
        }

        public static BlockItem ESSENTIA_TUBE_RESTRICT() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_RESTRICT.get();
        }

        public static BlockItem ESSENTIA_TUBE_ONEWAY() {
            return Registry.SUPPLIER_ESSENTIA_TUBE_ONEWAY.get();
        }

        public static BlockItem ESSENTIA_BUFFER() {
            return Registry.SUPPLIER_ESSENTIA_BUFFER.get();
        }

        public static BlockItem ESSENTIA_CENTRIFUGE() {
            return Registry.SUPPLIER_ESSENTIA_CENTRIFUGE.get();
        }

        public static BlockItem ESSENTIA_CRYSTALLIZER() {
            return Registry.SUPPLIER_ESSENTIA_CRYSTALLIZER.get();
        }

        public static CrystalEssenceItem CRYSTAL_ESSENCE() {
            return Registry.SUPPLIER_CRYSTAL_ESSENCE.get();
        }

        public static BlockItem ARCANE_PRESSURE_PLATE() {
            return Registry.SUPPLIER_ARCANE_PRESSURE_PLATE.get();
        }

        public static BlockItem ARCANE_BORE() {
            return Registry.SUPPLIER_ARCANE_BORE_BASE.get();
        }

        public static BlockItem ARCANE_EAR() {
            return Registry.SUPPLIER_ARCANE_EAR.get();
        }

        public static BucketItem DEATH_FLUID_BUCKET() {
            return Registry.SUPPLIER_DEATH_FLUID_BUCKET.get();
        }

        public static BucketItem PURE_FLUID_BUCKET() {
            return Registry.SUPPLIER_PURE_FLUID_BUCKET.get();
        }

        public static ThaumiumArmorItem THAUMIUM_HELMET() {
            return Registry.SUPPLIER_THAUMIUM_HELMET.get();
        }

        public static ThaumiumArmorItem THAUMIUM_CHESTPLATE() {
            return Registry.SUPPLIER_THAUMIUM_CHESTPLATE.get();
        }

        public static ThaumiumArmorItem THAUMIUM_LEGGINGS() {
            return Registry.SUPPLIER_THAUMIUM_LEGGINGS.get();
        }

        public static ThaumiumArmorItem THAUMIUM_BOOTS() {
            return Registry.SUPPLIER_THAUMIUM_BOOTS.get();
        }

        public static VoidArmorItem VOID_HELMET() {
            return Registry.SUPPLIER_VOID_HELMET.get();
        }

        public static VoidArmorItem VOID_CHESTPLATE() {
            return Registry.SUPPLIER_VOID_CHESTPLATE.get();
        }

        public static VoidArmorItem VOID_LEGGINGS() {
            return Registry.SUPPLIER_VOID_LEGGINGS.get();
        }

        public static VoidArmorItem VOID_BOOTS() {
            return Registry.SUPPLIER_VOID_BOOTS.get();
        }

        public static MirrorBlockItem MIRROR() {
            return Registry.SUPPLIER_MIRROR.get();
        }

        public static MirrorBlockItem ESSENTIA_MIRROR() {
            return Registry.SUPPLIER_ESSENTIA_MIRROR.get();
        }

        public static BlockItem ARCANE_PEDESTAL() {
            return Registry.SUPPLIER_ARCANE_PEDESTAL.get();
        }

        public static BlockItem INFUSION_MATRIX() {
            return Registry.SUPPLIER_INFUSION_MATRIX.get();
        }

        public static BlockItem WAND_RECHARGE_PEDESTAL() {
            return Registry.SUPPLIER_WAND_RECHARGE_PEDESTAL.get();
        }

        public static BlockItem COMPOUND_RECHARGE_FOCUS() {
            return Registry.SUPPLIER_COMPOUND_RECHARGE_FOCUS.get();
        }

        public static BlockItem ARCANE_SPA() {
            return Registry.SUPPLIER_ARCANE_SPA.get();
        }

        public static BathSaltsItem BATH_SALTS() {
            return Registry.SUPPLIER_BATH_SALTS.get();
        }

        public static BlockItem FOCAL_MANIPULATOR() {
            return Registry.SUPPLIER_FOCAL_MANIPULATOR.get();
        }

        public static BlockItem FLUX_SCRUBBER() {
            return Registry.SUPPLIER_FLUX_SCRUBBER.get();
        }

        public static WispEssenceItem WISP_ESSENCE() {
            return Registry.SUPPLIER_WISP_ESSENCE.get();
        }

        public static VoidSwordItem VOID_SWORD() {
            return Registry.SUPPLIER_VOID_SWORD.get();
        }

        public static VoidShovelItem VOID_SHOVEL() {
            return Registry.SUPPLIER_VOID_SHOVEL.get();
        }

        public static VoidPickaxeItem VOID_PICKAXE() {
            return Registry.SUPPLIER_VOID_PICKAXE.get();
        }

        public static VoidHoeItem VOID_HOE() {
            return Registry.SUPPLIER_VOID_HOE.get();
        }

        public static VoidAxeItem VOID_AXE() {
            return Registry.SUPPLIER_VOID_AXE.get();
        }

        public static SwordItem THAUMIUM_SWORD() {
            return Registry.SUPPLIER_THAUMIUM_SWORD.get();
        }

        public static ShovelItem THAUMIUM_SHOVEL() {
            return Registry.SUPPLIER_THAUMIUM_SHOVEL.get();
        }

        public static PickaxeItem THAUMIUM_PICKAXE() {
            return Registry.SUPPLIER_THAUMIUM_PICKAXE.get();
        }

        public static AxeItem THAUMIUM_AXE() {
            return Registry.SUPPLIER_THAUMIUM_AXE.get();
        }

        public static HoeItem THAUMIUM_HOE() {
            return Registry.SUPPLIER_THAUMIUM_HOE.get();
        }

        public static PrimalCrusherItem PRIMAL_CRUSHER() {
            return Registry.SUPPLIER_PRIMAL_CRUSHER.get();
        }

        public static Item VOID_NUGGET() {
            return Registry.SUPPLIER_VOID_NUGGET.get();
        }

        public static Item THAUMIUM_NUGGET() {
            return Registry.SUPPLIER_THAUMIUM_NUGGET.get();
        }

        public static Item COPPER_NUGGET() {
            return Registry.SUPPLIER_COPPER_NUGGET.get();
        }

        public static Item QUICKSILVER_DROP() {
            return Registry.SUPPLIER_QUICKSILVER_DROP.get();
        }

        public static ElementalShovelItem ELEMENTAL_SHOVEL() {
            return Registry.SUPPLIER_ELEMENTAL_SHOVEL.get();
        }

        public static ElementalPickaxeItem ELEMENTAL_PICKAXE() {
            return Registry.SUPPLIER_ELEMENTAL_PICKAXE.get();
        }

        public static ElementalSwordItem ELEMENTAL_SWORD() {
            return Registry.SUPPLIER_ELEMENTAL_SWORD.get();
        }

        public static ElementalHoeItem ELEMENTAL_HOE() {
            return Registry.SUPPLIER_ELEMENTAL_HOE.get();
        }

        public static ElementalAxeItem ELEMENTAL_AXE() {
            return Registry.SUPPLIER_ELEMENTAL_AXE.get();
        }

        public static CrimsonSwordItem CRIMSON_SWORD() {
            return Registry.SUPPLIER_CRIMSON_SWORD.get();
        }

        public static BoneBowItem BONE_BOW() {
            return Registry.SUPPLIER_BONE_BOW.get();
        }

        public static AirArrowItem AIR_ARROW() {
            return Registry.SUPPLIER_AIR_ARROW.get();
        }

        public static FireArrowItem FIRE_ARROW() {
            return Registry.SUPPLIER_FIRE_ARROW.get();
        }

        public static EarthArrowItem EARTH_ARROW() {
            return Registry.SUPPLIER_EARTH_ARROW.get();
        }

        public static WaterArrowItem WATER_ARROW() {
            return Registry.SUPPLIER_WATER_ARROW.get();
        }

        public static OrderArrowItem ORDER_ARROW() {
            return Registry.SUPPLIER_ORDER_ARROW.get();
        }

        public static EntropyArrowItem ENTROPY_ARROW() {
            return Registry.SUPPLIER_ENTROPY_ARROW.get();
        }

        public static GogglesOfRevealingItem GOGGLES_OF_REVEALING() {
            return Registry.SUPPLIER_GOGGLES_OF_REVEALING.get();
        }

        //        public static final RobeArmorItem ROBE_HELMET = Registry.SUPPLIER_ROBE_HELMET.get();
        public static RobeArmorItem ROBE_CHESTPLATE() {
            return Registry.SUPPLIER_ROBE_CHESTPLATE.get();
        }

        public static RobeArmorItem ROBE_LEGGINGS() {
            return Registry.SUPPLIER_ROBE_LEGGINGS.get();
        }

        public static RobeArmorItem ROBE_BOOTS() {
            return Registry.SUPPLIER_ROBE_BOOTS.get();
        }

        public static ThaumostaticHarnessItem THAUMOSTATIC_HARNESS() {
            return Registry.SUPPLIER_THAUMOSTATIC_HARNESS.get();
        }

        public static TravellerBootsItem TRAVELLER_BOOTS() {
            return Registry.SUPPLIER_TRAVELLER_BOOTS.get();
        }

        public static CultistBootsItem CULTIST_BOOTS() {
            return Registry.SUPPLIER_CULTIST_BOOTS.get();
        }

        public static CultistPlateArmorItem CULTIST_PLATE_CHESTPLATE() {
            return Registry.SUPPLIER_CULTIST_PLATE_CHESTPLATE.get();
        }

        public static CultistPlateArmorItem CULTIST_PLATE_HELMET() {
            return Registry.SUPPLIER_CULTIST_PLATE_HELMET.get();
        }

        public static CultistPlateArmorItem CULTIST_PLATE_LEGGINGS() {
            return Registry.SUPPLIER_CULTIST_PLATE_LEGGINGS.get();
        }

        public static CultistRobeArmorItem CULTIST_ROBE_CHESTPLATE() {
            return Registry.SUPPLIER_CULTIST_ROBE_CHESTPLATE.get();
        }

        public static CultistRobeArmorItem CULTIST_ROBE_HELMET() {
            return Registry.SUPPLIER_CULTIST_ROBE_HELMET.get();
        }

        public static CultistRobeArmorItem CULTIST_ROBE_LEGGINGS() {
            return Registry.SUPPLIER_CULTIST_ROBE_LEGGINGS.get();
        }

        public static CultistLeaderPlateArmorItem CULTIST_LEADER_PLATE_CHESTPLATE() {
            return Registry.SUPPLIER_CULTIST_LEADER_PLATE_CHESTPLATE.get();
        }

        public static CultistLeaderPlateArmorItem CULTIST_LEADER_PLATE_HELMET() {
            return Registry.SUPPLIER_CULTIST_LEADER_PLATE_HELMET.get();
        }

        public static CultistLeaderPlateArmorItem CULTIST_LEADER_PLATE_LEGGINGS() {
            return Registry.SUPPLIER_CULTIST_LEADER_PLATE_LEGGINGS.get();
        }

        public static VoidRobeArmorItem VOID_ROBE_LEGGINGS() {
            return Registry.SUPPLIER_VOID_ROBE_LEGGINGS.get();
        }

        public static VoidRobeArmorItem VOID_ROBE_CHESTPLATE() {
            return Registry.SUPPLIER_VOID_ROBE_CHESTPLATE.get();
        }

        public static VoidRobeHelmetItem VOID_ROBE_HELMET() {
            return Registry.SUPPLIER_VOID_ROBE_HELMET.get();
        }
    }

    public static class Registry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.ITEM);

        public static final RegistrySupplier<AlumentumItem> SUPPLIER_ALUMENTUM = ITEMS.register(
                "alumentum", AlumentumItem::new);
        public static final RegistrySupplier<BlockItem> SUPPLIER_NITOR = ITEMS.register(
                "nitor", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.NITOR_BLOCK(), new Item.Properties()));
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
                "greatwood_log", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_LOG(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_LOG = ITEMS.register(
                "silverwood_log", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_LOG(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS = ITEMS.register(
                "greatwood_planks", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_PLANKS(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS = ITEMS.register(
                "silverwood_planks", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_PLANKS(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_LEAVES = ITEMS.register(
                "greatwood_leaves", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_LEAVES(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_LEAVES = ITEMS.register(
                "silverwood_leaves", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_LEAVES(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_SAPLING = ITEMS.register(
                "greatwood_sapling", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_SAPLING(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_SAPLING = ITEMS.register(
                "silverwood_sapling", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_SAPLING(), new Item.Properties()));


        public static final RegistrySupplier<BlockItem> SUPPLIER_OBSIDIAN_TOTEM = ITEMS.register(
                "obsidian_totem", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.OBSIDIAN_TOTEM(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_OBSIDIAN_TILE = ITEMS.register(
                "obsidian_tile", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.OBSIDIAN_TILE(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_PAVING_STONE_TRAVEL = ITEMS.register(
                "paving_stone_travel",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.PAVING_STONE_TRAVEL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PAVING_STONE_WARDING = ITEMS.register(
                "paving_stone_warding",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.PAVING_STONE_WARDING(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_THAUMIUM_BLOCK = ITEMS.register(
                "thaumium_block", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.THAUMIUM_BLOCK(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_TALLOW_BLOCK = ITEMS.register(
                "tallow_block", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TALLOW_BLOCK(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BLOCK = ITEMS.register(
                "arcane_stone_block", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_STONE_BLOCK(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICKS = ITEMS.register(
                "arcane_stone_bricks",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_STONE_BRICKS(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GOLEM_FETTER = ITEMS.register(
                "golem_fetter", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GOLEM_FETTER(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE = ITEMS.register(
                "ancient_stone", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_ROCK = ITEMS.register(
                "ancient_rock", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_ROCK(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_CRUSTED_STONE = ITEMS.register(
                "crusted_stone", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CRUSTED_STONE(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_PEDESTAL = ITEMS.register(
                "ancient_stone_pedestal",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_PEDESTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_STAIRS = ITEMS.register(
                "ancient_stone_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_STAIRS(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICK_STAIRS = ITEMS.register(
                "arcane_stone_brick_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_STONE_BRICK_STAIRS(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS_STAIRS = ITEMS.register(
                "greatwood_planks_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_PLANKS_STAIRS(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS_STAIRS = ITEMS.register(
                "silverwood_planks_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_PLANKS_STAIRS(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_SLAB = ITEMS.register(
                "ancient_stone_slab", () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_STONE_SLAB(), new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICK_SLAB = ITEMS.register(
                "arcane_stone_brick_slab",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_STONE_BRICK_SLAB(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS_SLAB = ITEMS.register(
                "greatwood_planks_slab",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREATWOOD_PLANKS_SLAB(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS_SLAB = ITEMS.register(
                "silverwood_planks_slab",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SILVERWOOD_PLANKS_SLAB(), new Item.Properties())
        );

        public static final RegistrySupplier<BlockItem> SUPPLIER_AIR_CRYSTAL = ITEMS.register(
                "air_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_CRYSTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FIRE_CRYSTAL = ITEMS.register(
                "fire_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_CRYSTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WATER_CRYSTAL = ITEMS.register(
                "water_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_CRYSTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_EARTH_CRYSTAL = ITEMS.register(
                "earth_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_CRYSTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORDER_CRYSTAL = ITEMS.register(
                "order_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_CRYSTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ENTROPY_CRYSTAL = ITEMS.register(
                "entropy_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_CRYSTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MIXED_CRYSTAL = ITEMS.register(
                "mixed_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MIXED_CRYSTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_STRANGE_CRYSTALS = ITEMS.register(
                "strange_crystals",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.STRANGE_CRYSTALS(), new Item.Properties())
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
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CINNABAR_ORE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_ORE = ITEMS.register(
                "amber_ore",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AMBER_ORE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AIR_INFUSED_STONE = ITEMS.register(
                "air_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AIR_INFUSED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FIRE_INFUSED_STONE = ITEMS.register(
                "fire_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FIRE_INFUSED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WATER_INFUSED_STONE = ITEMS.register(
                "water_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.WATER_INFUSED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_EARTH_INFUSED_STONE = ITEMS.register(
                "earth_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.EARTH_INFUSED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORDER_INFUSED_STONE = ITEMS.register(
                "order_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ORDER_INFUSED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ENTROPY_INFUSED_STONE = ITEMS.register(
                "entropy_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ENTROPY_INFUSED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_BLOCK = ITEMS.register(
                "amber_block",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AMBER_BLOCK(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_BRICK = ITEMS.register(
                "amber_brick",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.AMBER_BRICK(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WHITE_TALLOW_CANDLE = ITEMS.register(
                "white_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.WHITE_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORANGE_TALLOW_CANDLE = ITEMS.register(
                "orange_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ORANGE_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MAGENTA_TALLOW_CANDLE = ITEMS.register(
                "magenta_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MAGENTA_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE = ITEMS.register(
                "light_blue_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_BLUE_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_YELLOW_TALLOW_CANDLE = ITEMS.register(
                "yellow_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.YELLOW_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIME_TALLOW_CANDLE = ITEMS.register(
                "lime_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.LIME_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PINK_TALLOW_CANDLE = ITEMS.register(
                "pink_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.PINK_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GRAY_TALLOW_CANDLE = ITEMS.register(
                "gray_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GRAY_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE = ITEMS.register(
                "light_gray_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.LIGHT_GRAY_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CYAN_TALLOW_CANDLE = ITEMS.register(
                "cyan_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CYAN_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PURPLE_TALLOW_CANDLE = ITEMS.register(
                "purple_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.PURPLE_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BLUE_TALLOW_CANDLE = ITEMS.register(
                "blue_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.BLUE_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BROWN_TALLOW_CANDLE = ITEMS.register(
                "brown_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.BROWN_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREEN_TALLOW_CANDLE = ITEMS.register(
                "green_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GREEN_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_RED_TALLOW_CANDLE = ITEMS.register(
                "red_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.RED_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BLACK_TALLOW_CANDLE = ITEMS.register(
                "black_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.BLACK_TALLOW_CANDLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SHIMMER_LEAF = ITEMS.register(
                "shimmer_leaf",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SHIMMER_LEAF(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CINDER_PEARL = ITEMS.register(
                "cinder_pearl",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CINDER_PEARL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MANA_SHROOM = ITEMS.register(
                "mana_shroom",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MANA_SHROOM(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ETHEREAL_BLOOM = ITEMS.register(
                "ethereal_bloom",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ETHEREAL_BLOOM(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_BELLOW = ITEMS.register(
                "arcane_bellow",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_BELLOW(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_DOOR = ITEMS.register(
                "arcane_door",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_DOOR(), new Item.Properties())
        );
        public static final RegistrySupplier<BannerPatternItem> SUPPLIER_CULTIST_BANNER_PATTERN = ITEMS.register(
                "cultist_banner_pattern",
                () -> new BannerPatternItem(BannerPatternTags.CULTIST_TAG, new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GLOWING_CRUSTED_STONE = ITEMS.register(
                "glowing_crusted_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GLOWING_CRUSTED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GLYPHED_STONE = ITEMS.register(
                "glyphed_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GLYPHED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_GATEWAY = ITEMS.register(
                "ancient_gateway",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_GATEWAY(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_LOCK_EMPTY = ITEMS.register(
                "ancient_lock_empty",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_LOCK_EMPTY(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_LOCK_INSERTED = ITEMS.register(
                "ancient_lock_inserted",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ANCIENT_LOCK_INSERTED(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ELDRITCH_CRAB_SPAWNER = ITEMS.register(
                "eldritch_crab_spawner",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ELDRITCH_CRAB_SPAWNER(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_RUNED_STONE = ITEMS.register(
                "runed_stone",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.RUNED_STONE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CRUSTED_TAINT = ITEMS.register(
                "crusted_taint",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CRUSTED_TAINT(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_TAINTED_SOIL = ITEMS.register(
                "tainted_soil",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_SOIL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FIBROUS_TAINT = ITEMS.register(
                "fibrous_taint",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FIBROUS_TAINT(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_TAINTED_GRASS = ITEMS.register(
                "tainted_grass",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_GRASS(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_TAINTED_PLANT = ITEMS.register(
                "tainted_plant",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_PLANT(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SPORE_STALK = ITEMS.register(
                "spore_stalk",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.SPORE_STALK(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MATURE_SPORE_STALK = ITEMS.register(
                "mature_spore_stalk",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MATURE_SPORE_STALK(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_TABLE = ITEMS.register(
                "table",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.TABLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_WORKBENCH = ITEMS.register(
                "arcane_workbench",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_WORKBENCH(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_DECONSTRUCTION_TABLE = ITEMS.register(
                "deconstruction_table",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.DECONSTRUCTION_TABLE(), new Item.Properties())
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
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.VIS_RELAY(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_VIS_CHARGE_RELAY = ITEMS.register(
                "vis_charge_relay",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.VIS_CHARGE_RELAY(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_NODE_STABILIZER = ITEMS.register(
                "node_stabilizer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.NODE_STABILIZER(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ADVANCED_NODE_STABILIZER = ITEMS.register(
                "advanced_node_stabilizer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ADVANCED_NODE_STABILIZER(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_NODE_TRANSDUCER = ITEMS.register(
                "node_transducer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.NODE_TRANSDUCER(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ALCHEMICAL_FURNACE = ITEMS.register(
                "alchemical_furnace",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ALCHEMICAL_FURNACE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ADVANCED_ALCHEMICAL_CONSTRUCT = ITEMS.register(
                "advanced_alchemical_construct",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ADVANCED_ALCHEMICAL_CONSTRUCT(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ALCHEMICAL_CONSTRUCT = ITEMS.register(
                "alchemical_construct",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ALCHEMICAL_CONSTRUCT(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_ALEMBIC = ITEMS.register(
                "arcane_alembic",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_ALEMBIC(), new Item.Properties())
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
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.BRAIN_JAR(), new Item.Properties())
        );
        public static final RegistrySupplier<NodeJarBlockItem> SUPPLIER_NODE_JAR = ITEMS.register(
                "node_jar",
                NodeJarBlockItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ITEM_CRATE = ITEMS.register(
                "item_crate",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ITEM_CRATE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CRUCIBLE = ITEMS.register(
                "crucible",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.CRUCIBLE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_LAMP = ITEMS.register(
                "arcane_lamp",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_LAMP(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GROWTH_ARCANE_LAMP = ITEMS.register(
                "growth_arcane_lamp",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.GROWTH_ARCANE_LAMP(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FERTILITY_ARCANE_LAMP = ITEMS.register(
                "fertility_arcane_lamp",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FERTILITY_ARCANE_LAMP(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MNEMONIC_MATRIX = ITEMS.register(
                "mnemonic_matrix",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.MNEMONIC_MATRIX(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_LEVITATOR = ITEMS.register(
                "arcane_levitator",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_LEVITATOR(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_RESERVOIR = ITEMS.register(
                "essentia_reservoir",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_RESERVOIR(), new Item.Properties())
        );
        public static final RegistrySupplier<ManaBeanItem> SUPPLIER_MANA_BEAN = ITEMS.register(
                "mana_bean",
                ManaBeanItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE = ITEMS.register(
                "essentia_tube",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE_VALVE = ITEMS.register(
                "essentia_tube_valve",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_VALVE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE_FILTER = ITEMS.register(
                "essentia_tube_filter",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_FILTER(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE_RESTRICT = ITEMS.register(
                "essentia_tube_restrict",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_RESTRICT(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_TUBE_ONEWAY = ITEMS.register(
                "essentia_tube_oneway",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_TUBE_ONEWAY(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_BUFFER = ITEMS.register(
                "essentia_buffer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_BUFFER(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_CENTRIFUGE = ITEMS.register(
                "essentia_centrifuge",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_CENTRIFUGE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ESSENTIA_CRYSTALLIZER = ITEMS.register(
                "essentia_crystallizer",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_CRYSTALLIZER(), new Item.Properties())
        );
        public static final RegistrySupplier<CrystalEssenceItem> SUPPLIER_CRYSTAL_ESSENCE = ITEMS.register(
                "crystal_essentia", CrystalEssenceItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_PRESSURE_PLATE = ITEMS.register(
                "arcane_pressure_plate",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_PRESSURE_PLATE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_BORE_BASE = ITEMS.register(
                "arcane_bore",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_BORE_BASE(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_EAR = ITEMS.register(
                "arcane_ear",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_EAR(), new Item.Properties())
        );
        public static final RegistrySupplier<BucketItem> SUPPLIER_DEATH_FLUID_BUCKET = ITEMS.register(
                "death_fluid_bucket",
                () -> new BucketItem(
                        ThaumcraftFluids.ThaumcraftFluidInstances.DEATH_FLUID(), new Item.Properties().craftRemainder(
                        Items.BUCKET).stacksTo(1))
        );
        public static final RegistrySupplier<BucketItem> SUPPLIER_PURE_FLUID_BUCKET = ITEMS.register(
                "pure_fluid_bucket",
                () -> new BucketItem(
                        ThaumcraftFluids.ThaumcraftFluidInstances.PURE_FLUID_SOURCE(), new Item.Properties().craftRemainder(
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
                        ThaumcraftBlocks.ThaumcraftBlockInstances.MIRROR(),
                        new Item.Properties()
                )
        );
        public static final RegistrySupplier<MirrorBlockItem> SUPPLIER_ESSENTIA_MIRROR = ITEMS.register(
                "essentia_mirror",
                () -> new MirrorBlockItem(
                        ThaumcraftBlocks.ThaumcraftBlockInstances.ESSENTIA_MIRROR(),
                        new Item.Properties()
                )
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_PEDESTAL = ITEMS.register(
                "arcane_pedestal",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_PEDESTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_INFUSION_MATRIX = ITEMS.register(
                "infusion_matrix",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.INFUSION_MATRIX(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WAND_RECHARGE_PEDESTAL = ITEMS.register(
                "wand_recharge_pedestal",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.WAND_RECHARGE_PEDESTAL(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_COMPOUND_RECHARGE_FOCUS = ITEMS.register(
                "compound_recharge_focus",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.COMPOUND_RECHARGE_FOCUS(), new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_SPA = ITEMS.register(
                "arcane_spa",
                () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.ARCANE_SPA(), new Item.Properties())
        );
        public static final RegistrySupplier<BathSaltsItem> SUPPLIER_BATH_SALTS = ITEMS.register(
                "bath_salts",
                BathSaltsItem::new
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FOCAL_MANIPULATOR =
                ITEMS.register(
                        "focal_manipulator",
                        () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FOCAL_MANIPULATOR(), new Item.Properties())
                );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FLUX_SCRUBBER =
                ITEMS.register(
                        "flux_scrubber",
                        () -> new BlockItem(ThaumcraftBlocks.ThaumcraftBlockInstances.FLUX_SCRUBBER(), new Item.Properties())
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
        public static final RegistrySupplier<ThaumostaticHarnessItem> SUPPLIER_THAUMOSTATIC_HARNESS = ITEMS.register(
                "thaumostatic_harness",
                ThaumostaticHarnessItem::new
        );
        public static final RegistrySupplier<TravellerBootsItem> SUPPLIER_TRAVELLER_BOOTS = ITEMS.register(
                "traveller_boots",
                TravellerBootsItem::new
        );
        public static final RegistrySupplier<CultistBootsItem> SUPPLIER_CULTIST_BOOTS = ITEMS.register(
                "cultist_boots",
                CultistBootsItem::new
        );
        public static final RegistrySupplier<CultistPlateArmorItem> SUPPLIER_CULTIST_PLATE_CHESTPLATE = ITEMS.register(
                "cultist_plate_chestplate",
                () -> new  CultistPlateArmorItem(ArmorItem.Type.CHESTPLATE)
        );
        public static final RegistrySupplier<CultistPlateArmorItem> SUPPLIER_CULTIST_PLATE_HELMET = ITEMS.register(
                "cultist_plate_helmet",
                () -> new  CultistPlateArmorItem(ArmorItem.Type.HELMET)
        );
        public static final RegistrySupplier<CultistPlateArmorItem> SUPPLIER_CULTIST_PLATE_LEGGINGS = ITEMS.register(
                "cultist_plate_leggings",
                () -> new  CultistPlateArmorItem(ArmorItem.Type.LEGGINGS)
        );
        public static final RegistrySupplier<CultistRobeArmorItem> SUPPLIER_CULTIST_ROBE_CHESTPLATE = ITEMS.register(
                "cultist_robe_chestplate",
                () -> new  CultistRobeArmorItem(ArmorItem.Type.CHESTPLATE)
        );
        public static final RegistrySupplier<CultistRobeArmorItem> SUPPLIER_CULTIST_ROBE_HELMET = ITEMS.register(
                "cultist_robe_helmet",
                () -> new  CultistRobeArmorItem(ArmorItem.Type.HELMET)
        );
        public static final RegistrySupplier<CultistRobeArmorItem> SUPPLIER_CULTIST_ROBE_LEGGINGS = ITEMS.register(
                "cultist_robe_leggings",
                () -> new  CultistRobeArmorItem(ArmorItem.Type.LEGGINGS)
        );
        public static final RegistrySupplier<CultistLeaderPlateArmorItem> SUPPLIER_CULTIST_LEADER_PLATE_CHESTPLATE = ITEMS.register(
                "cultist_leader_plate_chestplate",
                () -> new  CultistLeaderPlateArmorItem(ArmorItem.Type.CHESTPLATE)
        );
        public static final RegistrySupplier<CultistLeaderPlateArmorItem> SUPPLIER_CULTIST_LEADER_PLATE_HELMET = ITEMS.register(
                "cultist_leader_plate_helmet",
                () -> new  CultistLeaderPlateArmorItem(ArmorItem.Type.HELMET)
        );
        public static final RegistrySupplier<CultistLeaderPlateArmorItem> SUPPLIER_CULTIST_LEADER_PLATE_LEGGINGS = ITEMS.register(
                "cultist_leader_plate_leggings",
                () -> new  CultistLeaderPlateArmorItem(ArmorItem.Type.LEGGINGS)
        );
        public static final RegistrySupplier<VoidRobeArmorItem> SUPPLIER_VOID_ROBE_LEGGINGS = ITEMS.register(
                "void_robe_leggings",
                () -> new VoidRobeArmorItem(ArmorItem.Type.LEGGINGS)
        );
        public static final RegistrySupplier<VoidRobeArmorItem> SUPPLIER_VOID_ROBE_CHESTPLATE = ITEMS.register(
                "void_robe_chestplate",
                () -> new VoidRobeArmorItem(ArmorItem.Type.CHESTPLATE)
        );
        public static final RegistrySupplier<VoidRobeHelmetItem> SUPPLIER_VOID_ROBE_HELMET = ITEMS.register(
                "void_robe_helmet",
                VoidRobeHelmetItem::new
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

            public static final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM());

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

            private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.ENCHANTED_FABRIC());
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

            private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM());

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

            private final Ingredient ingredient = Ingredient.of(ThaumcraftItemInstances.PRIMAL_CHARM());

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return ingredient;
            }
        };
    }


    public static void init() {

        Registry.ITEMS.register();
        FuelRegistry.register(6400, ThaumcraftItemInstances.ALUMENTUM());
        FuelRegistry.register(400, ThaumcraftItemInstances.GREATWOOD_LOG(), ThaumcraftItemInstances.SILVERWOOD_LOG());//azanor's idea
        FuelRegistry.register(300, ThaumcraftItemInstances.GREATWOOD_PLANKS(), ThaumcraftItemInstances.SILVERWOOD_PLANKS());
        BannerPatternsRegistry.BANNER_PATTERNS.register();
    }
}
