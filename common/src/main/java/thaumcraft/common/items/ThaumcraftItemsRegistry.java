package thaumcraft.common.items;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.liquid.ThaumcraftFluids;
import thaumcraft.common.items.baubles.belt.HoverGirdleItem;
import thaumcraft.common.items.baubles.amulet.EmergencyRunicAmuletItem;
import thaumcraft.common.items.baubles.amulet.RunicAmuletItem;
import thaumcraft.common.items.baubles.amulet.visamulet.ReinforcedVisAmuletItem;
import thaumcraft.common.items.baubles.amulet.visamulet.VisAmuletItem;
import thaumcraft.common.items.baubles.belt.KineticRunicGirdleItem;
import thaumcraft.common.items.baubles.belt.RunicGirdleItem;
import thaumcraft.common.items.baubles.mundane.*;
import thaumcraft.common.items.baubles.ring.AbstractApprenticesRingItem;
import thaumcraft.common.items.baubles.ring.runicring.*;
import thaumcraft.common.items.clusters.ClusterItem;
import thaumcraft.common.items.consumable.*;
import thaumcraft.common.items.consumable.aspectowning.*;
import thaumcraft.common.items.consumable.lootbag.*;
import thaumcraft.common.items.consumable.throwable.AlumentumItem;
import thaumcraft.common.items.consumable.throwable.TaintBottleItem;
import thaumcraft.common.items.eldritch.*;
import thaumcraft.common.items.eldritch.RunedTabletItem;
import thaumcraft.common.items.equipment.armor.cultist.*;
import thaumcraft.common.items.equipment.armor.thaumaturge.*;
import thaumcraft.common.items.equipment.armor.voidarmor.*;
import thaumcraft.common.items.equipment.elemental.*;
import thaumcraft.common.items.equipment.masks.*;
import thaumcraft.common.items.equipment.specialtool.*;
import thaumcraft.common.items.equipment.voidequip.*;
import thaumcraft.common.items.jars.*;
import thaumcraft.common.items.mateiral.*;
import thaumcraft.common.items.misc.*;
import thaumcraft.common.items.research.*;
import thaumcraft.common.items.transport.*;
import thaumcraft.common.items.wands.FocusPouchItem;
import thaumcraft.common.items.wands.foci.ExcavationFocusItem;
import thaumcraft.common.items.wands.foci.FireFocusItem;
import thaumcraft.common.items.wands.foci.ShockFocusItem;
import thaumcraft.common.items.wands.rods.staffrods.*;
import thaumcraft.common.items.wands.rods.wandrods.*;
import thaumcraft.common.items.wands.wandcaps.*;
import thaumcraft.common.items.wands.wandtypes.*;

import java.util.*;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static com.linearity.opentc4.utils.RecipeManagerModifier.addRecipesServer;
import static thaumcraft.common.items.ThaumcraftItems.ItemTags.CINNABAR_ORES;

public class ThaumcraftItemsRegistry {
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
    public static final RegistrySupplier<DegradableTaintedMaterialItem> SUPPLIER_TAINTED_GOO = ITEMS.register(
            "tainted_goo", DegradableTaintedMaterialItem::new);
    public static final RegistrySupplier<DegradableTaintedMaterialItem> SUPPLIER_TAINT_TENDRIL = ITEMS.register(
            "taint_tendril", DegradableTaintedMaterialItem::new);
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
            "copper_wand_cap", CopperWandCapItem::new);//
    public static final RegistrySupplier<GoldWandCapItem> SUPPLIER_GOLD_WAND_CAP = ITEMS.register(
            "gold_wand_cap", GoldWandCapItem::new);
    public static final RegistrySupplier<IronWandCapItem> SUPPLIER_IRON_WAND_CAP = ITEMS.register(
            "iron_wand_cap", IronWandCapItem::new);
    public static final RegistrySupplier<SilverWandCapItem> SUPPLIER_SILVER_WAND_CAP = ITEMS.register(
            "silver_wand_cap", SilverWandCapItem::new);
    public static final RegistrySupplier<ThaumiumWandCapItem> SUPPLIER_THAUMIUM_WAND_CAP = ITEMS.register(
            "thaumium_wand_cap", ThaumiumWandCapItem::new);
    public static final RegistrySupplier<VoidWandCapItem> SUPPLIER_VOID_WAND_CAP = ITEMS.register(
            "void_wand_cap", VoidWandCapItem::new);


    //wand caps (inert)
    public static final RegistrySupplier<Item> SUPPLIER_SILVER_INERT_WAND_CAP = ITEMS.register(
            "silver_inert_wand_cap", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> SUPPLIER_THAUMIUM_INERT_WAND_CAP = ITEMS.register(
            "thaumium_inert_wand_cap", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> SUPPLIER_VOID_INERT_WAND_CAP = ITEMS.register(
            "void_inert_wand_cap", () -> new Item(new Item.Properties()));

    //wand rods
    public static final RegistrySupplier<BlazeWandRodItem> SUPPLIER_BLAZE_WAND_ROD = ITEMS.register(
            "blaze_wand_rod", BlazeWandRodItem::new);
    public static final RegistrySupplier<BoneWandRodItem> SUPPLIER_BONE_WAND_ROD = ITEMS.register(
            "bone_wand_rod", BoneWandRodItem::new);
    public static final RegistrySupplier<GreatWoodWandRodItem> SUPPLIER_GREATWOOD_WAND_ROD = ITEMS.register(
            "greatwood_wand_rod", GreatWoodWandRodItem::new);
    public static final RegistrySupplier<IceWandRodItem> SUPPLIER_ICE_WAND_ROD = ITEMS.register(
            "ice_wand_rod", IceWandRodItem::new);
    public static final RegistrySupplier<ObsidianWandRodItem> SUPPLIER_OBSIDIAN_WAND_ROD = ITEMS.register(
            "obsidian_wand_rod", ObsidianWandRodItem::new);
    public static final RegistrySupplier<QuartzWandRodItem> SUPPLIER_QUARTZ_WAND_ROD = ITEMS.register(
            "quartz_wand_rod", QuartzWandRodItem::new);
    public static final RegistrySupplier<ReedWandRodItem> SUPPLIER_REED_WAND_ROD = ITEMS.register(
            "reed_wand_rod", ReedWandRodItem::new);
    public static final RegistrySupplier<SilverWoodWandRodItem> SUPPLIER_SILVERWOOD_WAND_ROD = ITEMS.register(
            "silverwood_wand_rod", SilverWoodWandRodItem::new);
    public static final RegistrySupplier<WoodWandRodItem> SUPPLIER_WOOD_WAND_ROD = ITEMS.register(
            "wood_wand_rod", WoodWandRodItem::new);

    //staff rods
    public static final RegistrySupplier<PrimalStaffRodItem> SUPPLIER_PRIMAL_STAFF_ROD = ITEMS.register(
            "primal_staff_rod", PrimalStaffRodItem::new);
    public static final RegistrySupplier<BlazeStaffRodItem> SUPPLIER_BLAZE_STAFF_ROD = ITEMS.register(
            "blaze_staff_rod", BlazeStaffRodItem::new);
    public static final RegistrySupplier<BoneStaffRodItem> SUPPLIER_BONE_STAFF_ROD = ITEMS.register(
            "bone_staff_rod", BoneStaffRodItem::new);
    public static final RegistrySupplier<GreatWoodStaffRodItem> SUPPLIER_GREATWOOD_STAFF_ROD = ITEMS.register(
            "greatwood_staff_rod", GreatWoodStaffRodItem::new);
    public static final RegistrySupplier<IceStaffRodItem> SUPPLIER_ICE_STAFF_ROD = ITEMS.register(
            "ice_staff_rod", IceStaffRodItem::new);
    public static final RegistrySupplier<ObsidianStaffRodItem> SUPPLIER_OBSIDIAN_STAFF_ROD = ITEMS.register(
            "obsidian_staff_rod", ObsidianStaffRodItem::new);
    public static final RegistrySupplier<QuartzStaffRodItem> SUPPLIER_QUARTZ_STAFF_ROD = ITEMS.register(
            "quartz_staff_rod", QuartzStaffRodItem::new);
    public static final RegistrySupplier<ReedStaffRodItem> SUPPLIER_REED_STAFF_ROD = ITEMS.register(
            "reed_staff_rod", ReedStaffRodItem::new);
    public static final RegistrySupplier<SilverWoodStaffRodItem> SUPPLIER_SILVERWOOD_STAFF_ROD = ITEMS.register(
            "silverwood_staff_rod", SilverWoodStaffRodItem::new);

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
            () -> new BannerPatternItem(ThaumcraftItems.BannerPatternTags.CULTIST_TAG, new Item.Properties().stacksTo(1).rarity(Rarity.RARE))
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
                    ThaumcraftToolAndArmorMaterial.THAUMIUM,
                    ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<ThaumiumArmorItem> SUPPLIER_THAUMIUM_CHESTPLATE = ITEMS.register(
            "thaumium_chestplate",
            () -> new ThaumiumArmorItem(
                    ThaumcraftToolAndArmorMaterial.THAUMIUM,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<ThaumiumArmorItem> SUPPLIER_THAUMIUM_LEGGINGS = ITEMS.register(
            "thaumium_leggings",
            () -> new ThaumiumArmorItem(
                    ThaumcraftToolAndArmorMaterial.THAUMIUM,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<ThaumiumArmorItem> SUPPLIER_THAUMIUM_BOOTS = ITEMS.register(
            "thaumium_boots",
            () -> new ThaumiumArmorItem(
                    ThaumcraftToolAndArmorMaterial.THAUMIUM,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<VoidArmorItem> SUPPLIER_VOID_HELMET = ITEMS.register(
            "void_helmet",
            () -> new VoidArmorItem(
                    ThaumcraftToolAndArmorMaterial.ARMOR_VOID,
                    ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<VoidArmorItem> SUPPLIER_VOID_CHESTPLATE = ITEMS.register(
            "void_chestplate",
            () -> new VoidArmorItem(
                    ThaumcraftToolAndArmorMaterial.ARMOR_VOID,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<VoidArmorItem> SUPPLIER_VOID_LEGGINGS = ITEMS.register(
            "void_leggings",
            () -> new VoidArmorItem(
                    ThaumcraftToolAndArmorMaterial.ARMOR_VOID,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<VoidArmorItem> SUPPLIER_VOID_BOOTS = ITEMS.register(
            "void_boots",
            () -> new VoidArmorItem(
                    ThaumcraftToolAndArmorMaterial.ARMOR_VOID,
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
            () -> new SwordItem(ThaumcraftToolAndArmorMaterial.TOOL_THAUMIUM, 3, -2.4F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<ShovelItem> SUPPLIER_THAUMIUM_SHOVEL = ITEMS.register(
            "thaumium_shovel",
            () -> new ShovelItem(ThaumcraftToolAndArmorMaterial.TOOL_THAUMIUM, 1.5F, -3.0F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<PickaxeItem> SUPPLIER_THAUMIUM_PICKAXE = ITEMS.register(
            "thaumium_pickaxe",
            () -> new PickaxeItem(ThaumcraftToolAndArmorMaterial.TOOL_THAUMIUM, 1, -2.8F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<AxeItem> SUPPLIER_THAUMIUM_AXE = ITEMS.register(
            "thaumium_axe",
            () -> new AxeItem(ThaumcraftToolAndArmorMaterial.TOOL_THAUMIUM, 6.0F, -3.1F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
            )
    );
    public static final RegistrySupplier<HoeItem> SUPPLIER_THAUMIUM_HOE = ITEMS.register(
            "thaumium_hoe",
            () -> new HoeItem(ThaumcraftToolAndArmorMaterial.TOOL_THAUMIUM, -2, -1.0F, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)
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
            () -> new CultistPlateArmorItem(ArmorItem.Type.CHESTPLATE)
    );
    public static final RegistrySupplier<CultistPlateArmorItem> SUPPLIER_CULTIST_PLATE_HELMET = ITEMS.register(
            "cultist_plate_helmet",
            () -> new CultistPlateArmorItem(ArmorItem.Type.HELMET)
    );
    public static final RegistrySupplier<CultistPlateArmorItem> SUPPLIER_CULTIST_PLATE_LEGGINGS = ITEMS.register(
            "cultist_plate_leggings",
            () -> new CultistPlateArmorItem(ArmorItem.Type.LEGGINGS)
    );
    public static final RegistrySupplier<CultistRobeArmorItem> SUPPLIER_CULTIST_ROBE_CHESTPLATE = ITEMS.register(
            "cultist_robe_chestplate",
            () -> new CultistRobeArmorItem(ArmorItem.Type.CHESTPLATE)
    );
    public static final RegistrySupplier<CultistRobeArmorItem> SUPPLIER_CULTIST_ROBE_HELMET = ITEMS.register(
            "cultist_robe_helmet",
            () -> new CultistRobeArmorItem(ArmorItem.Type.HELMET)
    );
    public static final RegistrySupplier<CultistRobeArmorItem> SUPPLIER_CULTIST_ROBE_LEGGINGS = ITEMS.register(
            "cultist_robe_leggings",
            () -> new CultistRobeArmorItem(ArmorItem.Type.LEGGINGS)
    );
    public static final RegistrySupplier<CultistLeaderPlateArmorItem> SUPPLIER_CULTIST_LEADER_PLATE_CHESTPLATE = ITEMS.register(
            "cultist_leader_plate_chestplate",
            () -> new CultistLeaderPlateArmorItem(ArmorItem.Type.CHESTPLATE)
    );
    public static final RegistrySupplier<CultistLeaderPlateArmorItem> SUPPLIER_CULTIST_LEADER_PLATE_HELMET = ITEMS.register(
            "cultist_leader_plate_helmet",
            () -> new CultistLeaderPlateArmorItem(ArmorItem.Type.HELMET)
    );
    public static final RegistrySupplier<CultistLeaderPlateArmorItem> SUPPLIER_CULTIST_LEADER_PLATE_LEGGINGS = ITEMS.register(
            "cultist_leader_plate_leggings",
            () -> new CultistLeaderPlateArmorItem(ArmorItem.Type.LEGGINGS)
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

    public static final RegistrySupplier<ThaumiumFortressHelmetItem> SUPPLIER_THAUMIUM_FORTRESS_HELMET = ITEMS.register(
            "thaumium_fortress_helmet",
            ThaumiumFortressHelmetItem::new
    );
    public static final RegistrySupplier<ThaumiumFortressArmorItem> SUPPLIER_THAUMIUM_FORTRESS_CHESTPLATE = ITEMS.register(
            "thaumium_fortress_chestplate",
            () -> new ThaumiumFortressArmorItem(ArmorItem.Type.CHESTPLATE)
    );
    public static final RegistrySupplier<ThaumiumFortressArmorItem> SUPPLIER_THAUMIUM_FORTRESS_LEGGINGS = ITEMS.register(
            "thaumium_fortress_leggings",
            () -> new ThaumiumFortressArmorItem(ArmorItem.Type.LEGGINGS)
    );
    public static final RegistrySupplier<AngryGhostMaskItem> SUPPLIER_ANGRY_GHOST_MASK = ITEMS.register(
            "angry_ghost_mask",
            AngryGhostMaskItem::new
    );
    public static final RegistrySupplier<GrinningDevilMaskItem> SUPPLIER_GRINNING_DEVIL_MASK = ITEMS.register(
            "grinning_devil_mask",
            GrinningDevilMaskItem::new
    );
    public static final RegistrySupplier<SippingFiendMaskItem> SUPPLIER_SIPPING_FIEND_MASK = ITEMS.register(
            "sipping_fiend_mask",
            SippingFiendMaskItem::new
    );
    public static final RegistrySupplier<EssentiaPhialItem> SUPPLIER_ESSENTIA_PHIAL = ITEMS.register(
            "essentia_phial",
            EssentiaPhialItem::new
    );
    public static final RegistrySupplier<CompassStoneItem> SUPPLIER_COMPASS_STONE = ITEMS.register(
            "compass_stone",
            CompassStoneItem::new
    );
    public static final RegistrySupplier<Item> SUPPLIER_CHICKEN_NUGGET = ITEMS.register(
            "chicken_nugget",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).fast().build()))
    );
    public static final RegistrySupplier<Item> SUPPLIER_BEEF_NUGGET = ITEMS.register(
            "beef_nugget",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).fast().build()))
    );
    public static final RegistrySupplier<Item> SUPPLIER_PORK_NUGGET = ITEMS.register(
            "pork_nugget",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).fast().build()))
    );
    public static final RegistrySupplier<Item> SUPPLIER_COD_NUGGET = ITEMS.register(
            "cod_nugget",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).fast().build()))
    );
    public static final RegistrySupplier<Item> SUPPLIER_TRIPLE_MEAT = ITEMS.register(
            "triple_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8F).effect(new MobEffectInstance(MobEffects.REGENERATION,5,0),0.66F).build()))
    );
    public static final RegistrySupplier<HandMirrorItem> SUPPLIER_HAND_MIRROR = ITEMS.register(
            "hand_mirror",
            HandMirrorItem::new
    );
    public static final RegistrySupplier<SanityCheckerItem> SUPPLIER_SANITY_CHECKER = ITEMS.register(
            "sanity_checker",
            SanityCheckerItem::new
    );
    public static final RegistrySupplier<SanitySoapItem> SUPPLIER_SANITY_SOAP = ITEMS.register(
            "sanity_soap",
            SanitySoapItem::new
    );
    public static final RegistrySupplier<IronKeyItem> SUPPLIER_IRON_KEY = ITEMS.register(
            "iron_key",
            IronKeyItem::new
    );
    public static final RegistrySupplier<GoldKeyItem> SUPPLIER_GOLD_KEY = ITEMS.register(
            "gold_key",
            GoldKeyItem::new
    );
    public static final RegistrySupplier<VisAmuletItem> SUPPLIER_VIS_AMULET = ITEMS.register(
            "vis_amulet",
            VisAmuletItem::new
    );
    public static final RegistrySupplier<ReinforcedVisAmuletItem> SUPPLIER_REINFORCED_VIS_AMULET = ITEMS.register(
            "reinforced_vis_amulet",
            ReinforcedVisAmuletItem::new
    );
    public static final RegistrySupplier<MundaneBeltItem> SUPPLIER_MUNDANE_BELT = ITEMS.register(
            "mundane_belt",
            MundaneBeltItem::new
    );
    public static final RegistrySupplier<MundaneAmuletItem> SUPPLIER_MUNDANE_AMULET = ITEMS.register(
            "mundane_amulet",
            MundaneAmuletItem::new
    );
    public static final RegistrySupplier<MundaneRingItem> SUPPLIER_MUNDANE_RING = ITEMS.register(
            "mundane_ring",
            MundaneRingItem::new
    );
    public static final RegistrySupplier<AbstractApprenticesRingItem> SUPPLIER_AIR_APPRENTICES_RING = ITEMS.register(
            "air_apprentices_ring",
            () -> new AbstractApprenticesRingItem(Aspects.AIR)
    );
    public static final RegistrySupplier<AbstractApprenticesRingItem> SUPPLIER_WATER_APPRENTICES_RING = ITEMS.register(
            "water_apprentices_ring",
            () -> new AbstractApprenticesRingItem(Aspects.WATER)
    );
    public static final RegistrySupplier<AbstractApprenticesRingItem> SUPPLIER_FIRE_APPRENTICES_RING = ITEMS.register(
            "fire_apprentices_ring",
            () -> new AbstractApprenticesRingItem(Aspects.FIRE)
    );
    public static final RegistrySupplier<AbstractApprenticesRingItem> SUPPLIER_EARTH_APPRENTICES_RING = ITEMS.register(
            "earth_apprentices_ring",
            () -> new AbstractApprenticesRingItem(Aspects.EARTH)
    );
    public static final RegistrySupplier<AbstractApprenticesRingItem> SUPPLIER_ORDER_APPRENTICES_RING = ITEMS.register(
            "order_apprentices_ring",
            () -> new AbstractApprenticesRingItem(Aspects.ORDER)
    );
    public static final RegistrySupplier<AbstractApprenticesRingItem> SUPPLIER_ENTROPY_APPRENTICES_RING = ITEMS.register(
            "entropy_apprentices_ring",
            () -> new AbstractApprenticesRingItem(Aspects.ENTROPY)
    );
    public static final RegistrySupplier<ProtectionRingItem> SUPPLIER_PROTECTION_RING = ITEMS.register(
            "protection_ring",
            ProtectionRingItem::new
    );
    public static final RegistrySupplier<RunicShieldRingItem> SUPPLIER_RUNIC_SHIELD_RING = ITEMS.register(
            "runic_shield_ring",
            RunicShieldRingItem::new
    );
    public static final RegistrySupplier<ChargedRunicShieldRingItem> SUPPLIER_CHARGED_RUNIC_SHIELD_RING = ITEMS.register(
            "charged_runic_shield_ring",
            ChargedRunicShieldRingItem::new
    );
    public static final RegistrySupplier<RevitalizingRunicShieldRingItem> SUPPLIER_REVITALIZING_RUNIC_SHIELD_RING = ITEMS.register(
            "revitalizing_runic_shield_ring",
            RevitalizingRunicShieldRingItem::new
    );
    public static final RegistrySupplier<CommonLootBagItem> SUPPLIER_COMMON_LOOT_BAG = ITEMS.register(
            "common_loot_bag",
            CommonLootBagItem::new
    );
    public static final RegistrySupplier<UncommonLootBagItem> SUPPLIER_UNCOMMON_LOOT_BAG = ITEMS.register(
            "uncommon_loot_bag",
            UncommonLootBagItem::new
    );
    public static final RegistrySupplier<RareLootBagItem> SUPPLIER_RARE_LOOT_BAG = ITEMS.register(
            "rare_loot_bag",
            RareLootBagItem::new
    );
    public static final RegistrySupplier<HoverGirdleItem> SUPPLIER_HOVER_GIRDLE = ITEMS.register(
            "hover_girdle",
            HoverGirdleItem::new
    );
    public static final RegistrySupplier<RunicAmuletItem> SUPPLIER_RUNIC_AMULET = ITEMS.register(
            "runic_amulet",
            RunicAmuletItem::new
    );
    public static final RegistrySupplier<EmergencyRunicAmuletItem> SUPPLIER_EMERGENCY_RUNIC_AMULET = ITEMS.register(
            "emergency_runic_amulet",
            EmergencyRunicAmuletItem::new
    );
    public static final RegistrySupplier<RunicGirdleItem> SUPPLIER_RUNIC_GIRDLE = ITEMS.register(
            "runic_girdle",
            RunicGirdleItem::new
    );
    public static final RegistrySupplier<KineticRunicGirdleItem> SUPPLIER_KINETIC_RUNIC_GIRDLE = ITEMS.register(
            "kinetic_runic_girdle",
            KineticRunicGirdleItem::new
    );
    public static final RegistrySupplier<FocusPouchItem> SUPPLIER_FOCUS_POUCH = ITEMS.register(
            "focus_pouch",
            FocusPouchItem::new
    );
    public static final RegistrySupplier<EssentiaResonatorItem> SUPPLIER_ESSENTIA_RESONATOR = ITEMS.register(
            "essentia_resonator",
            EssentiaResonatorItem::new
    );
    public static final RegistrySupplier<ThaumonomiconItem> SUPPLIER_THAUMONOMICON = ITEMS.register(
            "thaumonomicon",
            ThaumonomiconItem::new
    );
    public static final RegistrySupplier<TaintBottleItem> SUPPLIER_TAINT_BOTTLE = ITEMS.register(
            "taint_bottle",
            TaintBottleItem::new
    );
    public static final RegistrySupplier<ExcavationFocusItem> SUPPLIER_EXCAVATION_FOCUS = ITEMS.register(
            "excavation_focus",
            ExcavationFocusItem::new
    );

    public static final RegistrySupplier<FireFocusItem> SUPPLIER_FIRE_FOCUS = ITEMS.register(
            "fire_focus",
            FireFocusItem::new
    );
    public static final RegistrySupplier<ShockFocusItem> SUPPLIER_SHOCK_FOCUS = ITEMS.register(
            "shock_focus",
            ShockFocusItem::new
    );
    public static final Map<TagKey<Item>,RegistrySupplier<ClusterItem>> CLUSTER_ITEMS = new HashMap<>();
    public record ClusterRegistrationArgs(String orePrefix,Set<TagKey<Item>> tagsToRegister,TagKey<Item> burnIntoTag){
        public void registerToClusterItems(DeferredRegister<Item> items){
            var supplier = items.register(
                    orePrefix + "_cluster",
                    () -> new ClusterItem(tagsToRegister, burnIntoTag)
            );
            for (var tag : tagsToRegister){
                CLUSTER_ITEMS.put(tag,supplier);
            }
        }
    }
    static {
        for (String prefix : new String[]{//TODO:[maybe wont finished]More ore tags
                "iron","gold","copper","silver","osmium","tin","uranium","lead","nickel"
        }) {
            new ClusterRegistrationArgs(
                    prefix,
                    Set.of(
                            TagKey.create(Registries.ITEM, new ResourceLocation("c", "raw_"+prefix+"_ores")),
                            TagKey.create(Registries.ITEM, new ResourceLocation("forge", "raw_materials/"+prefix))
                    ),
                    TagKey.create(Registries.ITEM, new ResourceLocation("c", prefix+"_ingots"))
            ).registerToClusterItems(ITEMS);
        }
        var cinnabarRegistrySupplier = ITEMS.register(

                "cinnabar_cluster",
                () -> new ClusterItem(Set.of(
                        CINNABAR_ORES
                ), () -> new ItemStack(SUPPLIER_CINNABAR_ORE.get()))
        );
        CLUSTER_ITEMS.put(CINNABAR_ORES,cinnabarRegistrySupplier);
    }
    public static void onDatapackReloaded(){
        CLUSTER_ITEMS.values().forEach(supplier -> supplier.get().registerDowsing());
        Collection<Recipe<?>> recipesToAdd = new ArrayList<>();
        final Item[] emptyItemArr = new Item[0];
        for (var clusterSupplier : CLUSTER_ITEMS.values()){
            var clusterItem = clusterSupplier.get();
            var clusterResult = clusterItem.willBurnInto.get();
            if (!clusterResult.isEmpty()){
                Set<Item> itemsForTags = new HashSet<>();
                clusterItem.forTags.forEach(
                        tag -> itemsForTags.addAll(platformUtils.getItemsFromTag(tag))
                );

                recipesToAdd.add(new SmeltingRecipe(
                        new ResourceLocation(Thaumcraft.MOD_ID,clusterItem.arch$registryName().getPath() + "_smelting"),
                        "thaumcraft_ore_clusters",
                        CookingBookCategory.BLOCKS,
                        Ingredient.of(itemsForTags.toArray(emptyItemArr)),
                        clusterResult,
                        1.4F,200
                        ));
                recipesToAdd.add(new BlastingRecipe(
                        new ResourceLocation(Thaumcraft.MOD_ID,clusterItem.arch$registryName().getPath() + "_smelting_blast"),
                        "thaumcraft_ore_clusters_blast",
                        CookingBookCategory.BLOCKS,
                        Ingredient.of(itemsForTags.toArray(emptyItemArr)),
                        clusterResult,
                        1.4F,100
                ));
            }
        }
        addRecipesServer(recipesToAdd);
    }
}
