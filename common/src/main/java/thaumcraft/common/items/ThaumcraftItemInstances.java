package thaumcraft.common.items;
import net.minecraft.world.item.*;
import thaumcraft.common.items.baubles.belt.HoverGirdleItem;
import thaumcraft.common.items.baubles.amulet.EmergencyRunicAmuletItem;
import thaumcraft.common.items.baubles.amulet.RunicAmuletItem;
import thaumcraft.common.items.baubles.amulet.visamulet.ReinforcedVisAmuletItem;
import thaumcraft.common.items.baubles.amulet.visamulet.VisAmuletItem;
import thaumcraft.common.items.baubles.belt.KineticRunicGirdleItem;
import thaumcraft.common.items.baubles.belt.RunicGirdleItem;
import thaumcraft.common.items.baubles.mundane.MundaneAmuletItem;
import thaumcraft.common.items.baubles.mundane.MundaneBeltItem;
import thaumcraft.common.items.baubles.mundane.MundaneRingItem;
import thaumcraft.common.items.baubles.ring.AbstractApprenticesRingItem;
import thaumcraft.common.items.baubles.ring.runicring.ChargedRunicShieldRingItem;
import thaumcraft.common.items.baubles.ring.runicring.ProtectionRingItem;
import thaumcraft.common.items.baubles.ring.runicring.RevitalizingRunicShieldRingItem;
import thaumcraft.common.items.baubles.ring.runicring.RunicShieldRingItem;
import thaumcraft.common.items.consumable.*;
import thaumcraft.common.items.consumable.aspectowning.CrystalEssenceItem;
import thaumcraft.common.items.consumable.aspectowning.ManaBeanItem;
import thaumcraft.common.items.consumable.aspectowning.WispEssenceItem;
import thaumcraft.common.items.consumable.lootbag.CommonLootBagItem;
import thaumcraft.common.items.consumable.lootbag.RareLootBagItem;
import thaumcraft.common.items.consumable.lootbag.UncommonLootBagItem;
import thaumcraft.common.items.consumable.throwable.AlumentumItem;
import thaumcraft.common.items.consumable.throwable.TaintBottleItem;
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
import thaumcraft.common.items.equipment.masks.AngryGhostMaskItem;
import thaumcraft.common.items.equipment.masks.GrinningDevilMaskItem;
import thaumcraft.common.items.equipment.masks.SippingFiendMaskItem;
import thaumcraft.common.items.equipment.specialtool.*;
import thaumcraft.common.items.equipment.voidequip.*;
import thaumcraft.common.items.jars.EssentiaJarBlockItem;
import thaumcraft.common.items.jars.NodeJarBlockItem;
import thaumcraft.common.items.jars.VoidJarBlockItem;
import thaumcraft.common.items.mateiral.DegradableTaintedMaterialItem;
import thaumcraft.common.items.mateiral.PrimalCharmItem;
import thaumcraft.common.items.mateiral.PrimePearlItem;
import thaumcraft.common.items.misc.*;
import thaumcraft.common.items.research.InkWellItem;
import thaumcraft.common.items.research.ThaumometerItem;
import thaumcraft.common.items.transport.HandMirrorItem;
import thaumcraft.common.items.transport.MirrorBlockItem;
import thaumcraft.common.items.wands.FocusPouchItem;
import thaumcraft.common.items.wands.foci.*;
import thaumcraft.common.items.wands.rods.staffrods.*;
import thaumcraft.common.items.wands.rods.wandrods.*;
import thaumcraft.common.items.wands.wandcaps.*;
import thaumcraft.common.items.wands.wandtypes.SceptreCastingItem;
import thaumcraft.common.items.wands.wandtypes.StaffCastingItem;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
public class ThaumcraftItemInstances {
    public static AlumentumItem ALUMENTUM() {
        return ThaumcraftItemsRegistry.SUPPLIER_ALUMENTUM.get();
    }//itemResource:0
    public static BlockItem NITOR() {
        return ThaumcraftItemsRegistry.SUPPLIER_NITOR.get();
    }//itemResource:1
    public static Item THAUMIUM_INGOT() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_INGOT.get();
    }//itemResource:2 Thaumium Ingot
    public static Item QUICK_SILVER() {
        return ThaumcraftItemsRegistry.SUPPLIER_QUICK_SILVER.get();
    }//itemResource:3
    public static Item MAGIC_TALLOW() {
        return ThaumcraftItemsRegistry.SUPPLIER_MAGIC_TALLOW.get();
    }//itemResource:4
    public static Item AMBER_GEM() {
        return ThaumcraftItemsRegistry.SUPPLIER_AMBER_GEM.get();
    }//itemResource:6
    public static Item ENCHANTED_FABRIC() {
        return ThaumcraftItemsRegistry.SUPPLIER_ENCHANTED_FABRIC.get();
    }//itemResource:7
    public static Item VIS_FILTER() {
        return ThaumcraftItemsRegistry.SUPPLIER_VIS_FILTER.get();
    }//itemResource:8
    public static KnowledgeFragmentItem KNOWLEDGE_FRAGMENT() {
        return ThaumcraftItemsRegistry.SUPPLIER_KNOWLEDGE_FRAGMENT.get();
    }//itemResource:9
    public static Item MIRRORED_GLASS() {
        return ThaumcraftItemsRegistry.SUPPLIER_MIRRORED_GLASS.get();
    }//itemResource:10
    public static DegradableTaintedMaterialItem TAINTED_GOO() {
        return ThaumcraftItemsRegistry.SUPPLIER_TAINTED_GOO.get();
    }//itemResource:11
    public static DegradableTaintedMaterialItem TAINT_TENDRIL() {
        return ThaumcraftItemsRegistry.SUPPLIER_TAINT_TENDRIL.get();
    }//itemResource:12
    public static Item JAR_LABEL() {
        return ThaumcraftItemsRegistry.SUPPLIER_JAR_LABEL.get();
    }//itemResource:13
    public static Item SALIS_MUNDUS() {
        return ThaumcraftItemsRegistry.SUPPLIER_SALIS_MUNDUS.get();
    }//itemResource:14
    public static PrimalCharmItem PRIMAL_CHARM() {
        return ThaumcraftItemsRegistry.SUPPLIER_PRIMAL_CHARM.get();
    }//itemResource:15
    public static Item VOID_INGOT() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_INGOT.get();
    }//itemResource:16
    public static Item VOID_SEED() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_SEED.get();
    }//itemResource:17
    public static Item GOLD_COIN() {
        return ThaumcraftItemsRegistry.SUPPLIER_GOLD_COIN.get();
    }//itemResource:18
    public static EldritchEyeItem ELDRITCH_EYE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ELDRITCH_EYE.get();
    }//itemEldritchObject:0
    public static CrimsonRitesItem CRIMSON_RITES() {
        return ThaumcraftItemsRegistry.SUPPLIER_CRIMSON_RITES.get();
    }//itemEldritchObject:1
    public static RunedTabletItem RUNED_TABLET() {
        return ThaumcraftItemsRegistry.SUPPLIER_RUNED_TABLET.get();
    }//itemEldritchObject:2
    public static PrimePearlItem PRIME_PEARL() {
        return ThaumcraftItemsRegistry.SUPPLIER_PRIME_PEARL.get();
    }//itemEldritchObject:3
    public static EldritchObeliskPlacerItem ELDRITCH_OBELISK_PLACER() {
        return ThaumcraftItemsRegistry.SUPPLIER_ELDRITCH_OBELISK_PLACER.get();
    }//itemEldritchObject:4
    public static ThaumometerItem THAUMOMETER() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMOMETER_ITEM.get();
    }
    // Wand Caps
    public static CopperWandCapItem COPPER_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_COPPER_WAND_CAP.get();
    }
    public static GoldWandCapItem GOLD_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_GOLD_WAND_CAP.get();
    }
    public static IronWandCapItem IRON_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_IRON_WAND_CAP.get();
    }
    public static SilverWandCapItem SILVER_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVER_WAND_CAP.get();
    }
    public static ThaumiumWandCapItem THAUMIUM_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_WAND_CAP.get();
    }
    public static VoidWandCapItem VOID_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_WAND_CAP.get();
    }
    //wand rods
    public static BlazeWandRodItem BLAZE_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_BLAZE_WAND_ROD.get();
    }
    public static BoneWandRodItem BONE_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_BONE_WAND_ROD.get();
    }
    public static GreatWoodWandRodItem GREATWOOD_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_WAND_ROD.get();
    }
    public static IceWandRodItem ICE_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_ICE_WAND_ROD.get();
    }
    public static ObsidianWandRodItem OBSIDIAN_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_OBSIDIAN_WAND_ROD.get();
    }
    public static QuartzWandRodItem QUARTZ_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_QUARTZ_WAND_ROD.get();
    }
    public static ReedWandRodItem REED_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_REED_WAND_ROD.get();
    }
    public static SilverWoodWandRodItem SILVERWOOD_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_WAND_ROD.get();
    }
    public static WoodWandRodItem WOOD_WAND_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_WOOD_WAND_ROD.get();
    }
    public static PrimalStaffRodItem PRIMAL_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_PRIMAL_STAFF_ROD.get();
    }
    public static BlazeStaffRodItem BLAZE_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_BLAZE_STAFF_ROD.get();
    }
    public static BoneStaffRodItem BONE_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_BONE_STAFF_ROD.get();
    }
    public static GreatWoodStaffRodItem GREATWOOD_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_STAFF_ROD.get();
    }
    public static IceStaffRodItem ICE_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_ICE_STAFF_ROD.get();
    }
    public static ObsidianStaffRodItem OBSIDIAN_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_OBSIDIAN_STAFF_ROD.get();
    }
    public static QuartzStaffRodItem QUARTZ_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_QUARTZ_STAFF_ROD.get();
    }
    public static ReedStaffRodItem REED_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_REED_STAFF_ROD.get();
    }
    public static SilverWoodStaffRodItem SILVERWOOD_STAFF_ROD() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_STAFF_ROD.get();
    }
    public static Item SILVER_INERT_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVER_INERT_WAND_CAP.get();
    }
    public static Item THAUMIUM_INERT_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_INERT_WAND_CAP.get();
    }
    public static Item VOID_INERT_WAND_CAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_INERT_WAND_CAP.get();
    }
    public static ZombieBrainItem ZOMBIE_BRAIN() {
        return ThaumcraftItemsRegistry.SUPPLIER_ZOMBIE_BRAIN.get();
    }
    public static BlockItem GREATWOOD_LOG() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_LOG.get();
    }
    public static BlockItem SILVERWOOD_LOG() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_LOG.get();
    }
    public static BlockItem GREATWOOD_PLANKS() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_PLANKS.get();
    }
    public static BlockItem SILVERWOOD_PLANKS() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_PLANKS.get();
    }
    public static BlockItem GREATWOOD_LEAVES() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_LEAVES.get();
    }
    public static BlockItem SILVERWOOD_LEAVES() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_LEAVES.get();
    }
    public static BlockItem GREATWOOD_SAPLING() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_SAPLING.get();
    }
    public static BlockItem SILVERWOOD_SAPLING() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_SAPLING.get();
    }
    public static BlockItem OBSIDIAN_TOTEM() {
        return ThaumcraftItemsRegistry.SUPPLIER_OBSIDIAN_TOTEM.get();
    }
    public static BlockItem OBSIDIAN_TIME() {
        return ThaumcraftItemsRegistry.SUPPLIER_OBSIDIAN_TILE.get();
    }
    public static BlockItem PAVING_STONE_TRAVEL() {
        return ThaumcraftItemsRegistry.SUPPLIER_PAVING_STONE_TRAVEL.get();
    }
    public static BlockItem PAVING_STONE_WARDING() {
        return ThaumcraftItemsRegistry.SUPPLIER_PAVING_STONE_WARDING.get();
    }
    public static BlockItem THAUMIUM_BLOCK() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_BLOCK.get();
    }
    public static BlockItem TALLOW_BLOCK() {
        return ThaumcraftItemsRegistry.SUPPLIER_TALLOW_BLOCK.get();
    }
    public static BlockItem ARCANE_STONE_BLOCK() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_STONE_BLOCK.get();
    }
    public static BlockItem ARCANE_STONE_BRICKS() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_STONE_BRICKS.get();
    }
    public static BlockItem GOLEM_FETTER() {
        return ThaumcraftItemsRegistry.SUPPLIER_GOLEM_FETTER.get();
    }
    public static BlockItem ANCIENT_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANCIENT_STONE.get();
    }
    public static BlockItem ANCIENT_ROCK() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANCIENT_ROCK.get();
    }
    public static BlockItem CRUSTED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_CRUSTED_STONE.get();
    }
    public static BlockItem ANCIENT_STONE_PEDESTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANCIENT_STONE_PEDESTAL.get();
    }
    public static BlockItem ANCIENT_STONE_STAIRS() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANCIENT_STONE_STAIRS.get();
    }
    public static BlockItem ARCANE_STONE_BRICK_STAIRS() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_STONE_BRICK_STAIRS.get();
    }
    public static BlockItem GREATWOOD_PLANKS_STAIRS() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_PLANKS_STAIRS.get();
    }
    public static BlockItem SILVERWOOD_PLANKS_STAIRS() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_PLANKS_STAIRS.get();
    }
    public static BlockItem ANCIENT_STONE_SLAB() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANCIENT_STONE_SLAB.get();
    }
    public static BlockItem ARCANE_STONE_BRICK_SLAB() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_STONE_BRICK_SLAB.get();
    }
    public static BlockItem GREATWOOD_PLANKS_SLAB() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_PLANKS_SLAB.get();
    }
    public static BlockItem SILVERWOOD_PLANKS_SLAB() {
        return ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_PLANKS_SLAB.get();
    }
    public static BlockItem AIR_CRYSTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_AIR_CRYSTAL.get();
    }
    public static BlockItem FIRE_CRYSTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_FIRE_CRYSTAL.get();
    }
    public static BlockItem WATER_CRYSTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_WATER_CRYSTAL.get();
    }
    public static BlockItem EARTH_CRYSTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_EARTH_CRYSTAL.get();
    }
    public static BlockItem ORDER_CRYSTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_ORDER_CRYSTAL.get();
    }
    public static BlockItem ENTROPY_CRYSTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_ENTROPY_CRYSTAL.get();
    }
    public static BlockItem MIXED_CRYSTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_MIXED_CRYSTAL.get();
    }
    public static BlockItem STRANGE_CRYSTALS() {
        return ThaumcraftItemsRegistry.SUPPLIER_STRANGE_CRYSTALS.get();
    }
    public static Item AIR_SHARD() {
        return ThaumcraftItemsRegistry.SUPPLIER_AIR_SHARD.get();
    }
    public static Item FIRE_SHARD() {
        return ThaumcraftItemsRegistry.SUPPLIER_FIRE_SHARD.get();
    }
    public static Item WATER_SHARD() {
        return ThaumcraftItemsRegistry.SUPPLIER_WATER_SHARD.get();
    }
    public static Item EARTH_SHARD() {
        return ThaumcraftItemsRegistry.SUPPLIER_EARTH_SHARD.get();
    }
    public static Item ORDER_SHARD() {
        return ThaumcraftItemsRegistry.SUPPLIER_ORDER_SHARD.get();
    }
    public static Item ENTROPY_SHARD() {
        return ThaumcraftItemsRegistry.SUPPLIER_ENTROPY_SHARD.get();
    }
    public static Item BALANCE_SHARD() {
        return ThaumcraftItemsRegistry.SUPPLIER_BALANCE_SHARD.get();
    }
    public static Item CINNABAR_ORE() {
        return ThaumcraftItemsRegistry.SUPPLIER_CINNABAR_ORE.get();
    }
    public static Item AMBER_ORE() {
        return ThaumcraftItemsRegistry.SUPPLIER_AMBER_ORE.get();
    }
    public static Item AIR_INFUSED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_AIR_INFUSED_STONE.get();
    }
    public static Item FIRE_INFUSED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_FIRE_INFUSED_STONE.get();
    }
    public static Item WATER_INFUSED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_WATER_INFUSED_STONE.get();
    }
    public static Item EARTH_INFUSED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_EARTH_INFUSED_STONE.get();
    }
    public static Item ORDER_INFUSED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ORDER_INFUSED_STONE.get();
    }
    public static Item ENTROPY_INFUSED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ENTROPY_INFUSED_STONE.get();
    }
    public static Item AMBER_BLOCK() {
        return ThaumcraftItemsRegistry.SUPPLIER_AMBER_BLOCK.get();
    }
    public static Item AMBER_BRICK() {
        return ThaumcraftItemsRegistry.SUPPLIER_AMBER_BRICK.get();
    }
    public static BlockItem WHITE_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_WHITE_TALLOW_CANDLE.get();
    }
    public static BlockItem ORANGE_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ORANGE_TALLOW_CANDLE.get();
    }
    public static BlockItem MAGENTA_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_MAGENTA_TALLOW_CANDLE.get();
    }
    public static BlockItem LIGHT_BLUE_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE.get();
    }
    public static BlockItem YELLOW_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_YELLOW_TALLOW_CANDLE.get();
    }
    public static BlockItem LIME_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_LIME_TALLOW_CANDLE.get();
    }
    public static BlockItem PINK_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_PINK_TALLOW_CANDLE.get();
    }
    public static BlockItem GRAY_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_GRAY_TALLOW_CANDLE.get();
    }
    public static BlockItem LIGHT_GRAY_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE.get();
    }
    public static BlockItem CYAN_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_CYAN_TALLOW_CANDLE.get();
    }
    public static BlockItem PURPLE_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_PURPLE_TALLOW_CANDLE.get();
    }
    public static BlockItem BLUE_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_BLUE_TALLOW_CANDLE.get();
    }
    public static BlockItem BROWN_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_BROWN_TALLOW_CANDLE.get();
    }
    public static BlockItem GREEN_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_GREEN_TALLOW_CANDLE.get();
    }
    public static BlockItem RED_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_RED_TALLOW_CANDLE.get();
    }
    public static BlockItem BLACK_TALLOW_CANDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_BLACK_TALLOW_CANDLE.get();
    }
    public static BlockItem SHIMMER_LEAF() {
        return ThaumcraftItemsRegistry.SUPPLIER_SHIMMER_LEAF.get();
    }
    public static BlockItem CINDER_PEARL() {
        return ThaumcraftItemsRegistry.SUPPLIER_CINDER_PEARL.get();
    }
    public static BlockItem MANA_SHROOM() {
        return ThaumcraftItemsRegistry.SUPPLIER_MANA_SHROOM.get();
    }
    public static BlockItem ETHEREAL_BLOOM() {
        return ThaumcraftItemsRegistry.SUPPLIER_ETHEREAL_BLOOM.get();
    }
    public static BlockItem ARCANE_BELLOW() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_BELLOW.get();
    }
    public static BlockItem ARCANE_DOOR() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_DOOR.get();
    }
    public static BannerPatternItem CULTIST_BANNER_PATTERN() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_BANNER_PATTERN.get();
    }
    public static BlockItem GLOWING_CRUSTED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_GLOWING_CRUSTED_STONE.get();
    }
    public static BlockItem GLYPHED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_GLYPHED_STONE.get();
    }
    public static BlockItem ANCIENT_GATEWAY() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANCIENT_GATEWAY.get();
    }
    public static BlockItem ANCIENT_LOCK_EMPTY() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANCIENT_LOCK_EMPTY.get();
    }
    public static BlockItem ANCIENT_LOCK_INSERTED() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANCIENT_LOCK_INSERTED.get();
    }
    public static BlockItem ELDRITCH_CRAB_SPAWNER() {
        return ThaumcraftItemsRegistry.SUPPLIER_ELDRITCH_CRAB_SPAWNER.get();
    }
    public static BlockItem RUNED_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_RUNED_STONE.get();
    }
    public static BlockItem CRUSTED_TAINT() {
        return ThaumcraftItemsRegistry.SUPPLIER_CRUSTED_TAINT.get();
    }
    public static BlockItem TAINTED_SOIL() {
        return ThaumcraftItemsRegistry.SUPPLIER_TAINTED_SOIL.get();
    }
    public static BlockItem FIBROUS_TAINT() {
        return ThaumcraftItemsRegistry.SUPPLIER_FIBROUS_TAINT.get();
    }
    public static BlockItem TAINTED_GRASS() {
        return ThaumcraftItemsRegistry.SUPPLIER_TAINTED_GRASS.get();
    }
    public static BlockItem TAINTED_PLANT() {
        return ThaumcraftItemsRegistry.SUPPLIER_TAINTED_PLANT.get();
    }
    public static BlockItem SPORE_STALK() {
        return ThaumcraftItemsRegistry.SUPPLIER_SPORE_STALK.get();
    }
    public static BlockItem MATURE_SPORE_STALK() {
        return ThaumcraftItemsRegistry.SUPPLIER_MATURE_SPORE_STALK.get();
    }
    public static BlockItem TABLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_TABLE.get();
    }
    public static InkWellItem INK_WELL() {
        return ThaumcraftItemsRegistry.SUPPLIER_INK_WELL.get();
    }
    public static BlockItem ARCANE_WORKBENCH() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_WORKBENCH.get();
    }
    public static BlockItem DECONSTRUCTION_TABLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_DECONSTRUCTION_TABLE.get();
    }
    public static ResearchNoteItem RESEARCH_NOTE() {
        return ThaumcraftItemsRegistry.SUPPLIER_RESEARCH_NOTE.get();
    }
    public static WandCastingItem WAND_CASTING() {
        return ThaumcraftItemsRegistry.SUPPLIER_WAND_CASTING.get();
    }
    public static StaffCastingItem STAFF_CASTING() {
        return ThaumcraftItemsRegistry.SUPPLIER_STAFF_CASTING.get();
    }
    public static SceptreCastingItem SCEPTRE_CASTING() {
        return ThaumcraftItemsRegistry.SUPPLIER_SCEPTRE_CASTING.get();
    }
    public static BlockItem VIS_RELAY() {
        return ThaumcraftItemsRegistry.SUPPLIER_VIS_RELAY.get();
    }
    public static BlockItem VIS_CHARGE_RELAY() {
        return ThaumcraftItemsRegistry.SUPPLIER_VIS_CHARGE_RELAY.get();
    }
    public static BlockItem NODE_STABILIZER() {
        return ThaumcraftItemsRegistry.SUPPLIER_NODE_STABILIZER.get();
    }
    public static BlockItem ADVANCED_NODE_STABILIZER() {
        return ThaumcraftItemsRegistry.SUPPLIER_ADVANCED_NODE_STABILIZER.get();
    }
    public static BlockItem NODE_TRANSDUCER() {
        return ThaumcraftItemsRegistry.SUPPLIER_NODE_TRANSDUCER.get();
    }
    public static BlockItem ALCHEMICAL_FURNACE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ALCHEMICAL_FURNACE.get();
    }
    public static BlockItem ADVANCED_ALCHEMICAL_CONSTRUCT() {
        return ThaumcraftItemsRegistry.SUPPLIER_ADVANCED_ALCHEMICAL_CONSTRUCT.get();
    }
    public static BlockItem ALCHEMICAL_CONSTRUCT() {
        return ThaumcraftItemsRegistry.SUPPLIER_ALCHEMICAL_CONSTRUCT.get();
    }
    public static BlockItem ARCANE_ALEMBIC() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_ALEMBIC.get();
    }
    public static EssentiaJarBlockItem ESSENTIA_JAR() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_JAR.get();
    }
    public static VoidJarBlockItem VOID_JAR() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_JAR.get();
    }
    public static BlockItem BRAIN_JAR() {
        return ThaumcraftItemsRegistry.SUPPLIER_BRAIN_JAR.get();
    }
    public static NodeJarBlockItem NODE_JAR() {
        return ThaumcraftItemsRegistry.SUPPLIER_NODE_JAR.get();
    }
    public static BlockItem ITEM_CRATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ITEM_CRATE.get();
    }
    public static BlockItem CRUCIBLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_CRUCIBLE.get();
    }
    public static BlockItem ARCANE_LAMP() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_LAMP.get();
    }
    public static BlockItem GROWTH_ARCANE_LAMP() {
        return ThaumcraftItemsRegistry.SUPPLIER_GROWTH_ARCANE_LAMP.get();
    }
    public static BlockItem FERTILITY_ARCANE_LAMP() {
        return ThaumcraftItemsRegistry.SUPPLIER_FERTILITY_ARCANE_LAMP.get();
    }
    public static BlockItem MNEMONIC_MATRIX() {
        return ThaumcraftItemsRegistry.SUPPLIER_MNEMONIC_MATRIX.get();
    }
    public static BlockItem ARCANE_LEVITATOR() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_LEVITATOR.get();
    }
    public static BlockItem ESSENTIA_RESERVOIR() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_RESERVOIR.get();
    }
    public static ManaBeanItem MANA_BEAN() {
        return ThaumcraftItemsRegistry.SUPPLIER_MANA_BEAN.get();
    }
    public static BlockItem ESSENTIA_TUBE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_TUBE.get();
    }
    public static BlockItem ESSENTIA_TUBE_VALVE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_TUBE_VALVE.get();
    }
    public static BlockItem ESSENTIA_TUBE_FILTER() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_TUBE_FILTER.get();
    }
    public static BlockItem ESSENTIA_TUBE_RESTRICT() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_TUBE_RESTRICT.get();
    }
    public static BlockItem ESSENTIA_TUBE_ONEWAY() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_TUBE_ONEWAY.get();
    }
    public static BlockItem ESSENTIA_BUFFER() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_BUFFER.get();
    }
    public static BlockItem ESSENTIA_CENTRIFUGE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_CENTRIFUGE.get();
    }
    public static BlockItem ESSENTIA_CRYSTALLIZER() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_CRYSTALLIZER.get();
    }
    public static CrystalEssenceItem CRYSTAL_ESSENCE() {
        return ThaumcraftItemsRegistry.SUPPLIER_CRYSTAL_ESSENCE.get();
    }
    public static BlockItem ARCANE_PRESSURE_PLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_PRESSURE_PLATE.get();
    }
    public static BlockItem ARCANE_BORE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_BORE_BASE.get();
    }
    public static BlockItem ARCANE_EAR() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_EAR.get();
    }
    public static BucketItem DEATH_FLUID_BUCKET() {
        return ThaumcraftItemsRegistry.SUPPLIER_DEATH_FLUID_BUCKET.get();
    }
    public static BucketItem PURE_FLUID_BUCKET() {
        return ThaumcraftItemsRegistry.SUPPLIER_PURE_FLUID_BUCKET.get();
    }
    public static ThaumiumArmorItem THAUMIUM_HELMET() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_HELMET.get();
    }
    public static ThaumiumArmorItem THAUMIUM_CHESTPLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_CHESTPLATE.get();
    }
    public static ThaumiumArmorItem THAUMIUM_LEGGINGS() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_LEGGINGS.get();
    }
    public static ThaumiumArmorItem THAUMIUM_BOOTS() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_BOOTS.get();
    }
    public static VoidArmorItem VOID_HELMET() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_HELMET.get();
    }
    public static VoidArmorItem VOID_CHESTPLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_CHESTPLATE.get();
    }
    public static VoidArmorItem VOID_LEGGINGS() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_LEGGINGS.get();
    }
    public static VoidArmorItem VOID_BOOTS() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_BOOTS.get();
    }
    public static MirrorBlockItem MIRROR() {
        return ThaumcraftItemsRegistry.SUPPLIER_MIRROR.get();
    }
    public static MirrorBlockItem ESSENTIA_MIRROR() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_MIRROR.get();
    }
    public static BlockItem ARCANE_PEDESTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_PEDESTAL.get();
    }
    public static BlockItem INFUSION_MATRIX() {
        return ThaumcraftItemsRegistry.SUPPLIER_INFUSION_MATRIX.get();
    }
    public static BlockItem WAND_RECHARGE_PEDESTAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_WAND_RECHARGE_PEDESTAL.get();
    }
    public static BlockItem COMPOUND_RECHARGE_FOCUS() {
        return ThaumcraftItemsRegistry.SUPPLIER_COMPOUND_RECHARGE_FOCUS.get();
    }
    public static BlockItem ARCANE_SPA() {
        return ThaumcraftItemsRegistry.SUPPLIER_ARCANE_SPA.get();
    }
    public static BathSaltsItem BATH_SALTS() {
        return ThaumcraftItemsRegistry.SUPPLIER_BATH_SALTS.get();
    }
    public static BlockItem FOCAL_MANIPULATOR() {
        return ThaumcraftItemsRegistry.SUPPLIER_FOCAL_MANIPULATOR.get();
    }
    public static BlockItem FLUX_SCRUBBER() {
        return ThaumcraftItemsRegistry.SUPPLIER_FLUX_SCRUBBER.get();
    }
    public static WispEssenceItem WISP_ESSENCE() {
        return ThaumcraftItemsRegistry.SUPPLIER_WISP_ESSENCE.get();
    }
    public static VoidSwordItem VOID_SWORD() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_SWORD.get();
    }
    public static VoidShovelItem VOID_SHOVEL() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_SHOVEL.get();
    }
    public static VoidPickaxeItem VOID_PICKAXE() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_PICKAXE.get();
    }
    public static VoidHoeItem VOID_HOE() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_HOE.get();
    }
    public static VoidAxeItem VOID_AXE() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_AXE.get();
    }
    public static SwordItem THAUMIUM_SWORD() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_SWORD.get();
    }
    public static ShovelItem THAUMIUM_SHOVEL() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_SHOVEL.get();
    }
    public static PickaxeItem THAUMIUM_PICKAXE() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_PICKAXE.get();
    }
    public static AxeItem THAUMIUM_AXE() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_AXE.get();
    }
    public static HoeItem THAUMIUM_HOE() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_HOE.get();
    }
    public static PrimalCrusherItem PRIMAL_CRUSHER() {
        return ThaumcraftItemsRegistry.SUPPLIER_PRIMAL_CRUSHER.get();
    }
    public static Item VOID_NUGGET() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_NUGGET.get();
    }
    public static Item THAUMIUM_NUGGET() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_NUGGET.get();
    }
    public static Item COPPER_NUGGET() {
        return ThaumcraftItemsRegistry.SUPPLIER_COPPER_NUGGET.get();
    }
    public static Item QUICKSILVER_DROP() {
        return ThaumcraftItemsRegistry.SUPPLIER_QUICKSILVER_DROP.get();
    }
    public static ElementalShovelItem ELEMENTAL_SHOVEL() {
        return ThaumcraftItemsRegistry.SUPPLIER_ELEMENTAL_SHOVEL.get();
    }
    public static ElementalPickaxeItem ELEMENTAL_PICKAXE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ELEMENTAL_PICKAXE.get();
    }
    public static ElementalSwordItem ELEMENTAL_SWORD() {
        return ThaumcraftItemsRegistry.SUPPLIER_ELEMENTAL_SWORD.get();
    }
    public static ElementalHoeItem ELEMENTAL_HOE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ELEMENTAL_HOE.get();
    }
    public static ElementalAxeItem ELEMENTAL_AXE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ELEMENTAL_AXE.get();
    }
    public static CrimsonSwordItem CRIMSON_SWORD() {
        return ThaumcraftItemsRegistry.SUPPLIER_CRIMSON_SWORD.get();
    }
    public static BoneBowItem BONE_BOW() {
        return ThaumcraftItemsRegistry.SUPPLIER_BONE_BOW.get();
    }
    public static AirArrowItem AIR_ARROW() {
        return ThaumcraftItemsRegistry.SUPPLIER_AIR_ARROW.get();
    }
    public static FireArrowItem FIRE_ARROW() {
        return ThaumcraftItemsRegistry.SUPPLIER_FIRE_ARROW.get();
    }
    public static EarthArrowItem EARTH_ARROW() {
        return ThaumcraftItemsRegistry.SUPPLIER_EARTH_ARROW.get();
    }
    public static WaterArrowItem WATER_ARROW() {
        return ThaumcraftItemsRegistry.SUPPLIER_WATER_ARROW.get();
    }
    public static OrderArrowItem ORDER_ARROW() {
        return ThaumcraftItemsRegistry.SUPPLIER_ORDER_ARROW.get();
    }
    public static EntropyArrowItem ENTROPY_ARROW() {
        return ThaumcraftItemsRegistry.SUPPLIER_ENTROPY_ARROW.get();
    }
    public static GogglesOfRevealingItem GOGGLES_OF_REVEALING() {
        return ThaumcraftItemsRegistry.SUPPLIER_GOGGLES_OF_REVEALING.get();
    }
    //        public static final RobeArmorItem ROBE_HELMET = Registry.SUPPLIER_ROBE_HELMET.get();
    public static RobeArmorItem ROBE_CHESTPLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_ROBE_CHESTPLATE.get();
    }
    public static RobeArmorItem ROBE_LEGGINGS() {
        return ThaumcraftItemsRegistry.SUPPLIER_ROBE_LEGGINGS.get();
    }
    public static RobeArmorItem ROBE_BOOTS() {
        return ThaumcraftItemsRegistry.SUPPLIER_ROBE_BOOTS.get();
    }
    public static ThaumostaticHarnessItem THAUMOSTATIC_HARNESS() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMOSTATIC_HARNESS.get();
    }
    public static TravellerBootsItem TRAVELLER_BOOTS() {
        return ThaumcraftItemsRegistry.SUPPLIER_TRAVELLER_BOOTS.get();
    }
    public static CultistBootsItem CULTIST_BOOTS() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_BOOTS.get();
    }
    public static CultistPlateArmorItem CULTIST_PLATE_CHESTPLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_PLATE_CHESTPLATE.get();
    }
    public static CultistPlateArmorItem CULTIST_PLATE_HELMET() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_PLATE_HELMET.get();
    }
    public static CultistPlateArmorItem CULTIST_PLATE_LEGGINGS() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_PLATE_LEGGINGS.get();
    }
    public static CultistRobeArmorItem CULTIST_ROBE_CHESTPLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_ROBE_CHESTPLATE.get();
    }
    public static CultistRobeArmorItem CULTIST_ROBE_HELMET() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_ROBE_HELMET.get();
    }
    public static CultistRobeArmorItem CULTIST_ROBE_LEGGINGS() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_ROBE_LEGGINGS.get();
    }
    public static CultistLeaderPlateArmorItem CULTIST_LEADER_PLATE_CHESTPLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_LEADER_PLATE_CHESTPLATE.get();
    }
    public static CultistLeaderPlateArmorItem CULTIST_LEADER_PLATE_HELMET() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_LEADER_PLATE_HELMET.get();
    }
    public static CultistLeaderPlateArmorItem CULTIST_LEADER_PLATE_LEGGINGS() {
        return ThaumcraftItemsRegistry.SUPPLIER_CULTIST_LEADER_PLATE_LEGGINGS.get();
    }
    public static VoidRobeArmorItem VOID_ROBE_LEGGINGS() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_ROBE_LEGGINGS.get();
    }
    public static VoidRobeArmorItem VOID_ROBE_CHESTPLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_ROBE_CHESTPLATE.get();
    }
    public static VoidRobeHelmetItem VOID_ROBE_HELMET() {
        return ThaumcraftItemsRegistry.SUPPLIER_VOID_ROBE_HELMET.get();
    }
    public static ThaumiumFortressHelmetItem THAUMIUM_FORTRESS_HELMET() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_FORTRESS_HELMET.get();
    }
    public static ThaumiumFortressArmorItem THAUMIUM_FORTRESS_CHESTPLATE() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_FORTRESS_CHESTPLATE.get();
    }
    public static ThaumiumFortressArmorItem THAUMIUM_FORTRESS_LEGGINGS() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_FORTRESS_LEGGINGS.get();
    }
    public static AngryGhostMaskItem ANGRY_GHOST_MASK() {
        return ThaumcraftItemsRegistry.SUPPLIER_ANGRY_GHOST_MASK.get();
    }
    public static GrinningDevilMaskItem GRINNING_DEVILS_MASK() {
        return ThaumcraftItemsRegistry.SUPPLIER_GRINNING_DEVIL_MASK.get();
    }
    public static SippingFiendMaskItem SIPPING_FIEND_MASK() {
        return ThaumcraftItemsRegistry.SUPPLIER_SIPPING_FIEND_MASK.get();
    }
    public static EssentiaPhialItem ESSENTIA_PHIAL() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_PHIAL.get();
    }
    public static CompassStoneItem COMPASS_STONE() {
        return ThaumcraftItemsRegistry.SUPPLIER_COMPASS_STONE.get();
    }
    public static Item CHICKEN_NUGGET(){
        return ThaumcraftItemsRegistry.SUPPLIER_CHICKEN_NUGGET.get();
    }
    public static Item BEEF_NUGGET(){
        return ThaumcraftItemsRegistry.SUPPLIER_BEEF_NUGGET.get();
    }
    public static Item PORK_NUGGET(){
        return ThaumcraftItemsRegistry.SUPPLIER_PORK_NUGGET.get();
    }
    public static Item COD_NUGGET(){
        return ThaumcraftItemsRegistry.SUPPLIER_COD_NUGGET.get();
    }
    public static Item TRIPLE_MEAT() {
        return ThaumcraftItemsRegistry.SUPPLIER_TRIPLE_MEAT.get();
    }
    public static HandMirrorItem HAND_MIRROR() {
        return ThaumcraftItemsRegistry.SUPPLIER_HAND_MIRROR.get();
    }
    public static SanityCheckerItem SANITY_CHECKER() {
        return ThaumcraftItemsRegistry.SUPPLIER_SANITY_CHECKER.get();
    }
    public static IronKeyItem IRON_KEY() {
        return ThaumcraftItemsRegistry.SUPPLIER_IRON_KEY.get();
    }
    public static GoldKeyItem GOLD_KEY() {
        return ThaumcraftItemsRegistry.SUPPLIER_GOLD_KEY.get();
    }
    public static SanitySoapItem SANITY_SOAP() {
        return ThaumcraftItemsRegistry.SUPPLIER_SANITY_SOAP.get();
    }
    public static VisAmuletItem VIS_AMULET() {
        return ThaumcraftItemsRegistry.SUPPLIER_VIS_AMULET.get();
    }
    public static ReinforcedVisAmuletItem REINFORCED_VIS_AMULET() {
        return ThaumcraftItemsRegistry.SUPPLIER_REINFORCED_VIS_AMULET.get();
    }
    public static MundaneAmuletItem MUNDANE_AMULET() {
        return ThaumcraftItemsRegistry.SUPPLIER_MUNDANE_AMULET.get();
    }
    public static MundaneBeltItem MUNDANE_BELT() {
        return ThaumcraftItemsRegistry.SUPPLIER_MUNDANE_BELT.get();
    }
    public static MundaneRingItem MUNDANE_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_MUNDANE_RING.get();
    }
    public static AbstractApprenticesRingItem AIR_APPRENTICES_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_AIR_APPRENTICES_RING.get();
    }
    public static AbstractApprenticesRingItem WATER_APPRENTICES_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_WATER_APPRENTICES_RING.get();
    }
    public static AbstractApprenticesRingItem FIRE_APPRENTICES_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_FIRE_APPRENTICES_RING.get();
    }
    public static AbstractApprenticesRingItem EARTH_APPRENTICES_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_EARTH_APPRENTICES_RING.get();
    }
    public static AbstractApprenticesRingItem ORDER_APPRENTICES_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_ORDER_APPRENTICES_RING.get();
    }
    public static AbstractApprenticesRingItem ENTROPY_APPRENTICES_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_ENTROPY_APPRENTICES_RING.get();
    }
    public static ProtectionRingItem PROTECTION_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_PROTECTION_RING.get();
    }
    public static RunicShieldRingItem RUNE_SHIELD_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_RUNIC_SHIELD_RING.get();
    }
    public static ChargedRunicShieldRingItem CHARGED_RUNE_SHIELD_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_CHARGED_RUNIC_SHIELD_RING.get();
    }
    public static RevitalizingRunicShieldRingItem REVITALIZING_RUNE_SHIELD_RING() {
        return ThaumcraftItemsRegistry.SUPPLIER_REVITALIZING_RUNIC_SHIELD_RING.get();
    }
    public static CommonLootBagItem COMMON_LOOT_BAG() {
        return ThaumcraftItemsRegistry.SUPPLIER_COMMON_LOOT_BAG.get();
    }
    public static UncommonLootBagItem UNCOMMON_LOOT_BAG() {
        return ThaumcraftItemsRegistry.SUPPLIER_UNCOMMON_LOOT_BAG.get();
    }
    public static RareLootBagItem RARE_LOOT_BAG() {
        return ThaumcraftItemsRegistry.SUPPLIER_RARE_LOOT_BAG.get();
    }
    public static HoverGirdleItem HOVER_GIRDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_HOVER_GIRDLE.get();
    }
    public static RunicAmuletItem RUNIC_AMULET() {
        return ThaumcraftItemsRegistry.SUPPLIER_RUNIC_AMULET.get();
    }
    public static EmergencyRunicAmuletItem EMERGENCY_RUNIC_AMULET() {
        return ThaumcraftItemsRegistry.SUPPLIER_EMERGENCY_RUNIC_AMULET.get();
    }
    public static RunicGirdleItem RUNIC_GIRDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_RUNIC_GIRDLE.get();
    }
    public static KineticRunicGirdleItem KINETIC_RUNIC_GIRDLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_KINETIC_RUNIC_GIRDLE.get();
    }
    public static FocusPouchItem FOCUS_POUCH() {
        return ThaumcraftItemsRegistry.SUPPLIER_FOCUS_POUCH.get();
    }
    public static EssentiaResonatorItem ESSENTIA_RESONATOR() {
        return ThaumcraftItemsRegistry.SUPPLIER_ESSENTIA_RESONATOR.get();
    }
    public static ThaumonomiconItem THAUMONOMICON() {
        return ThaumcraftItemsRegistry.SUPPLIER_THAUMONOMICON.get();
    }
    public static TaintBottleItem TAINT_BOTTLE() {
        return ThaumcraftItemsRegistry.SUPPLIER_TAINT_BOTTLE.get();
    }
    public static ExcavationFocusItem EXCAVATION_FOCUS() {
        return ThaumcraftItemsRegistry.SUPPLIER_EXCAVATION_FOCUS.get();
    }
    public static FireFocusItem FIRE_FOCUS() {
        return ThaumcraftItemsRegistry.SUPPLIER_FIRE_FOCUS.get();
    }
    public static ShockFocusItem SHOCK_FOCUS() {
        return ThaumcraftItemsRegistry.SUPPLIER_SHOCK_FOCUS.get();
    }
    public static FrostFocusItem FROST_FOCUS() {
        return ThaumcraftItemsRegistry.SUPPLIER_FROST_FOCUS.get();
    }
}
