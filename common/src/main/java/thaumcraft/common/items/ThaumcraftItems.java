package thaumcraft.common.items;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.items.consumable.AlumentumItem;
import thaumcraft.common.items.consumable.KnowledgeFragmentItem;
import thaumcraft.common.items.consumable.ZombieBrainItem;
import thaumcraft.common.items.mateiral.PrimalCharmItem;
import thaumcraft.common.items.misc.*;
import thaumcraft.common.items.relics.ThaumometerItem;
import thaumcraft.common.items.wands.rods.staffrods.*;
import thaumcraft.common.items.wands.rods.wandrods.*;
import thaumcraft.common.items.wands.wandcaps.*;

public class ThaumcraftItems {

    public static final AlumentumItem ALUMENTUM = Registry.SUPPLIER_ALUMENTUM.get();//itemResource:0
    public static final BlockItem NITOR = Registry.SUPPLIER_NITOR.get();//itemResource:1
    public static final Item THAUMIUM_INGOT = Registry.SUPPLIER_THAUMIUM_INGOT.get();//itemResource:2 Thaumium Ingot
    public static final Item QUICK_SILVER = Registry.SUPPLIER_QUICK_SILVER.get();//itemResource:3
    public static final Item MAGIC_TALLOW = Registry.SUPPLIER_MAGIC_TALLOW.get();//itemResource:4
    //seems anazor found that he has to register a special item for zombie brain.it's useless now.
//    public static final Item ZOMBIE_BRAIN_REMOVED = Registry.SUPPLIER_ZOMBIE_BRAIN.get();//itemResource:5
    public static final Item AMBER_GEM = Registry.SUPPLIER_AMBER_GEM.get();//itemResource:6
    public static final Item ENCHANTED_FABRIC = Registry.SUPPLIER_ENCHANTED_FABRIC.get();//itemResource:7
    public static final Item VIS_FILTER = Registry.SUPPLIER_VIS_FILTER.get();//itemResource:8
    public static final KnowledgeFragmentItem KNOWLEDGE_FRAGMENT = Registry.SUPPLIER_KNOWLEDGE_FRAGMENT.get();//itemResource:9
    public static final Item MIRRORED_GLASS = Registry.SUPPLIER_MIRRORED_GLASS.get();//itemResource:10
    public static final Item TAINTED_GOO = Registry.SUPPLIER_TAINTED_GOO.get();//itemResource:11
    public static final Item TAINT_TENDRIL = Registry.SUPPLIER_TAINT_TENDRIL.get();//itemResource:12
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

    public static final Item AIR_SHARD      = Registry.SUPPLIER_AIR_SHARD.get();
    public static final Item FIRE_SHARD     = Registry.SUPPLIER_FIRE_SHARD.get();
    public static final Item WATER_SHARD    = Registry.SUPPLIER_WATER_SHARD.get();
    public static final Item EARTH_SHARD    = Registry.SUPPLIER_EARTH_SHARD.get();
    public static final Item ORDER_SHARD    = Registry.SUPPLIER_ORDER_SHARD.get();
    public static final Item ENTROPY_SHARD  = Registry.SUPPLIER_ENTROPY_SHARD.get();
    public static final Item BALANCE_SHARD  = Registry.SUPPLIER_BALANCE_SHARD.get();

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

    //===========================================================================================

    public static class Registry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create("thaumcraft", Registries.ITEM);

        public static final RegistrySupplier<AlumentumItem> SUPPLIER_ALUMENTUM = ITEMS.register(
                "alumentum", AlumentumItem::new);
        public static final RegistrySupplier<BlockItem> SUPPLIER_NITOR = ITEMS.register(
                "nitor", () -> new BlockItem(ThaumcraftBlocks.NITOR_BLOCK, new Item.Properties()));
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
        public static final RegistrySupplier<Item> SUPPLIER_JAR_LABEL = ITEMS.register(
                "jar_label", () -> new Item(new Item.Properties()));
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
                "greatwood_log", () -> new BlockItem(ThaumcraftBlocks.GREATWOOD_LOG, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_LOG = ITEMS.register(
                "silverwood_log", () -> new BlockItem(ThaumcraftBlocks.SILVERWOOD_LOG, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS = ITEMS.register(
                "greatwood_planks", () -> new BlockItem(ThaumcraftBlocks.GREATWOOD_PLANKS, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS = ITEMS.register(
                "silverwood_planks", () -> new BlockItem(ThaumcraftBlocks.SILVERWOOD_PLANKS, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_LEAVES = ITEMS.register(
                "greatwood_leaves", () -> new BlockItem(ThaumcraftBlocks.GREATWOOD_LEAVES, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_LEAVES = ITEMS.register(
                "silverwood_leaves", () -> new BlockItem(ThaumcraftBlocks.SILVERWOOD_LEAVES, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_SAPLING = ITEMS.register(
                "greatwood_sapling", () -> new BlockItem(ThaumcraftBlocks.GREATWOOD_SAPLING, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_SAPLING = ITEMS.register(
                "silverwood_sapling", () -> new BlockItem(ThaumcraftBlocks.SILVERWOOD_SAPLING, new Item.Properties()));


        public static final RegistrySupplier<BlockItem> SUPPLIER_OBSIDIAN_TOTEM = ITEMS.register(
                "obsidian_totem", () -> new BlockItem(ThaumcraftBlocks.OBSIDIAN_TOTEM, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_OBSIDIAN_TILE = ITEMS.register(
                "obsidian_tile", () -> new BlockItem(ThaumcraftBlocks.OBSIDIAN_TILE, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_PAVING_STONE_TRAVEL = ITEMS.register(
                "paving_stone_travel",
                () -> new BlockItem(ThaumcraftBlocks.PAVING_STONE_TRAVEL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PAVING_STONE_WARDING = ITEMS.register(
                "paving_stone_warding",
                () -> new BlockItem(ThaumcraftBlocks.PAVING_STONE_WARDING, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_THAUMIUM_BLOCK = ITEMS.register(
                "thaumium_block", () -> new BlockItem(ThaumcraftBlocks.THAUMIUM_BLOCK, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_TALLOW_BLOCK = ITEMS.register(
                "tallow_block", () -> new BlockItem(ThaumcraftBlocks.TALLOW_BLOCK, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BLOCK = ITEMS.register(
                "arcane_stone_block", () -> new BlockItem(ThaumcraftBlocks.ARCANE_STONE_BLOCK, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICKS = ITEMS.register(
                "arcane_stone_bricks",
                () -> new BlockItem(ThaumcraftBlocks.ARCANE_STONE_BRICKS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GOLEM_FETTER = ITEMS.register(
                "golem_fetter", () -> new BlockItem(ThaumcraftBlocks.GOLEM_FETTER, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE = ITEMS.register(
                "ancient_stone", () -> new BlockItem(ThaumcraftBlocks.ANCIENT_STONE, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_ROCK = ITEMS.register(
                "ancient_rock", () -> new BlockItem(ThaumcraftBlocks.ANCIENT_ROCK, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_CRUSTED_STONE = ITEMS.register(
                "crusted_stone", () -> new BlockItem(ThaumcraftBlocks.CRUSTED_STONE, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_PEDESTAL = ITEMS.register(
                "ancient_stone_pedestal",
                () -> new BlockItem(ThaumcraftBlocks.ANCIENT_STONE_PEDESTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_STAIRS = ITEMS.register(
                "ancient_stone_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ANCIENT_STONE_STAIRS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICK_STAIRS = ITEMS.register(
                "arcane_stone_brick_stairs",
                () -> new BlockItem(ThaumcraftBlocks.ARCANE_STONE_BRICK_STAIRS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS_STAIRS = ITEMS.register(
                "greatwood_planks_stairs",
                () -> new BlockItem(ThaumcraftBlocks.GREATWOOD_PLANKS_STAIRS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS_STAIRS = ITEMS.register(
                "silverwood_planks_stairs",
                () -> new BlockItem(ThaumcraftBlocks.SILVERWOOD_PLANKS_STAIRS, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ANCIENT_STONE_SLAB = ITEMS.register(
                "ancient_stone_slab", () -> new BlockItem(ThaumcraftBlocks.ANCIENT_STONE_SLAB, new Item.Properties()));
        public static final RegistrySupplier<BlockItem> SUPPLIER_ARCANE_STONE_BRICK_SLAB = ITEMS.register(
                "arcane_stone_brick_slab",
                () -> new BlockItem(ThaumcraftBlocks.ARCANE_STONE_BRICK_SLAB, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREATWOOD_PLANKS_SLAB = ITEMS.register(
                "greatwood_planks_slab",
                () -> new BlockItem(ThaumcraftBlocks.GREATWOOD_PLANKS_SLAB, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SILVERWOOD_PLANKS_SLAB = ITEMS.register(
                "silverwood_planks_slab",
                () -> new BlockItem(ThaumcraftBlocks.SILVERWOOD_PLANKS_SLAB, new Item.Properties())
        );

        public static final RegistrySupplier<BlockItem> SUPPLIER_AIR_CRYSTAL = ITEMS.register(
                "air_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.AIR_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FIRE_CRYSTAL = ITEMS.register(
                "fire_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.FIRE_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WATER_CRYSTAL = ITEMS.register(
                "water_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.WATER_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_EARTH_CRYSTAL = ITEMS.register(
                "earth_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.EARTH_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORDER_CRYSTAL = ITEMS.register(
                "order_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ORDER_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ENTROPY_CRYSTAL = ITEMS.register(
                "entropy_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.ENTROPY_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MIXED_CRYSTAL = ITEMS.register(
                "mixed_crystal_cluster",
                () -> new BlockItem(ThaumcraftBlocks.MIXED_CRYSTAL, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_STRANGE_CRYSTALS = ITEMS.register(
                "strange_crystals",
                () -> new BlockItem(ThaumcraftBlocks.STRANGE_CRYSTALS, new Item.Properties())
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
                () -> new BlockItem(ThaumcraftBlocks.CINNABAR_ORE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_ORE = ITEMS.register(
                "amber_ore",
                () -> new BlockItem(ThaumcraftBlocks.AMBER_ORE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AIR_INFUSED_STONE = ITEMS.register(
                "air_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.AIR_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_FIRE_INFUSED_STONE = ITEMS.register(
                "fire_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.FIRE_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WATER_INFUSED_STONE = ITEMS.register(
                "water_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.WATER_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_EARTH_INFUSED_STONE = ITEMS.register(
                "earth_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.EARTH_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORDER_INFUSED_STONE = ITEMS.register(
                "order_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ORDER_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ENTROPY_INFUSED_STONE = ITEMS.register(
                "entropy_infused_stone",
                () -> new BlockItem(ThaumcraftBlocks.ENTROPY_INFUSED_STONE, new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_BLOCK = ITEMS.register(
                "amber_block",
                () -> new BlockItem(ThaumcraftBlocks.AMBER_BLOCK,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_AMBER_BRICK = ITEMS.register(
                "amber_brick",
                () -> new BlockItem(ThaumcraftBlocks.AMBER_BRICK,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_WHITE_TALLOW_CANDLE = ITEMS.register(
                "white_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.WHITE_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_ORANGE_TALLOW_CANDLE = ITEMS.register(
                "orange_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.ORANGE_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MAGENTA_TALLOW_CANDLE = ITEMS.register(
                "magenta_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.MAGENTA_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIGHT_BLUE_TALLOW_CANDLE = ITEMS.register(
                "light_blue_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.LIGHT_BLUE_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_YELLOW_TALLOW_CANDLE = ITEMS.register(
                "yellow_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.YELLOW_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIME_TALLOW_CANDLE = ITEMS.register(
                "lime_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.LIME_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PINK_TALLOW_CANDLE = ITEMS.register(
                "pink_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.PINK_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GRAY_TALLOW_CANDLE = ITEMS.register(
                "gray_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.GRAY_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_LIGHT_GRAY_TALLOW_CANDLE = ITEMS.register(
                "light_gray_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.LIGHT_GRAY_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CYAN_TALLOW_CANDLE = ITEMS.register(
                "cyan_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.CYAN_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_PURPLE_TALLOW_CANDLE = ITEMS.register(
                "purple_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.PURPLE_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BLUE_TALLOW_CANDLE = ITEMS.register(
                "blue_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.BLUE_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BROWN_TALLOW_CANDLE = ITEMS.register(
                "brown_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.BROWN_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_GREEN_TALLOW_CANDLE = ITEMS.register(
                "green_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.GREEN_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_RED_TALLOW_CANDLE = ITEMS.register(
                "red_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.RED_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_BLACK_TALLOW_CANDLE = ITEMS.register(
                "black_tallow_candle",
                () -> new BlockItem(ThaumcraftBlocks.BLACK_TALLOW_CANDLE,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_SHIMMER_LEAF = ITEMS.register(
                "shimmer_leaf",
                () -> new BlockItem(ThaumcraftBlocks.SHIMMER_LEAF,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_CINDER_PEARL = ITEMS.register(
                "cinder_pearl",
                () -> new BlockItem(ThaumcraftBlocks.CINDER_PEARL,new Item.Properties())
        );
        public static final RegistrySupplier<BlockItem> SUPPLIER_MANA_SHROOM = ITEMS.register(
                "mana_shroom",
                () -> new BlockItem(ThaumcraftBlocks.MANA_SHROOM,new Item.Properties())
        );


        static {
            Registry.ITEMS.register();
        }
    }

    public static class ItemTags {
        //TODO:Tag for forge and fabric,im lazy to write tag json :(
        public static final TagKey<Item> VOID_INGOT_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("thaumcraft:tag_void_ingot"));
        public static final TagKey<Item> PRIME_PEARL_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("thaumcraft:tag_prime_pearl"));

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
    }

    public static void init() {

    }
}
