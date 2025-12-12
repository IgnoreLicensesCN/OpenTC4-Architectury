package thaumcraft.common.items;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import thaumcraft.common.items.misc.*;
import thaumcraft.common.items.relics.ThaumometerItem;
import thaumcraft.common.items.wands.rods.staffrods.*;
import thaumcraft.common.items.wands.rods.wandrods.*;
import thaumcraft.common.items.wands.wandcaps.*;

public class ThaumcraftItems {

    public static final Item ALUMENTUM = Registry.SUPPLIER_ALUMENTUM.get();//itemResource:0
    public static final BlockItem NITOR = Registry.SUPPLIER_NITOR.get();//itemResource:1
    public static final Item THAUMIUM_INGOT = Registry.SUPPLIER_THAUMIUM_INGOT.get();//itemResource:2 Thaumium Ingot
    public static final Item QUICK_SILVER = Registry.SUPPLIER_QUICK_SILVER.get();//itemResource:3
    public static final Item MAGIC_TALLOW = Registry.SUPPLIER_MAGIC_TALLOW.get();//itemResource:4
    //seems anazor found that he has to register a special item for zombie brain.it's useless now.
//    public static final Item ZOMBIE_BRAIN_REMOVED = Registry.SUPPLIER_ZOMBIE_BRAIN.get();//itemResource:5
    public static final Item AMBER_GEM = Registry.SUPPLIER_AMBER_GEM.get();//itemResource:6
    public static final Item ENCHANTED_FABRIC = Registry.SUPPLIER_ENCHANTED_FABRIC.get();//itemResource:7
    public static final Item VIS_FILTER = Registry.SUPPLIER_VIS_FILTER.get();//itemResource:8
    public static final Item KNOWLEDGE_FRAGMENT = Registry.SUPPLIER_KNOWLEDGE_FRAGMENT.get();//itemResource:9
    public static final Item MIRRORED_GLASS = Registry.SUPPLIER_MIRRORED_GLASS.get();//itemResource:10
    public static final Item TAINTED_GOO = Registry.SUPPLIER_TAINTED_GOO.get();//itemResource:11
    public static final Item TAINT_TENDRIL = Registry.SUPPLIER_TAINT_TENDRIL.get();//itemResource:12
    public static final Item JAR_LABEL = Registry.SUPPLIER_JAR_LABEL.get();//itemResource:13
    public static final Item SALIS_MUNDUS = Registry.SUPPLIER_SALIS_MUNDUS.get();//itemResource:14
    public static final Item PRIMAL_CHARM = Registry.SUPPLIER_PRIMAL_CHARM.get();//itemResource:15
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



    public static class Registry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create("thaumcraft", Registries.ITEM);

        public static final RegistrySupplier<Item> SUPPLIER_ALUMENTUM = ITEMS.register("alumentum", () -> new Item(new Item.Properties()));//TODO:new item class(use->boom)
        public static final RegistrySupplier<BlockItem> SUPPLIER_NITOR = ITEMS.register("nitor", () -> new BlockItem(/*blockAiry:1*/,new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_THAUMIUM_INGOT = ITEMS.register("thaumium_ingot", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_QUICK_SILVER = ITEMS.register("quick_silver", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_MAGIC_TALLOW = ITEMS.register("magic_tallow", () -> new Item(new Item.Properties()));
        //        public static final RegistrySupplier<Item> SUPPLIER_ZOMBIE_BRAIN_REMOVED = ITEMS.register("zombie_brain_removed", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_AMBER_GEM = ITEMS.register("amber_gem", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_ENCHANTED_FABRIC = ITEMS.register("enchanted_fabric", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_VIS_FILTER = ITEMS.register("vis_filter", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_KNOWLEDGE_FRAGMENT = ITEMS.register("knowledge_fragment", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_MIRRORED_GLASS = ITEMS.register("mirrored_glass", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_TAINTED_GOO = ITEMS.register("tainted_goo", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_TAINT_TENDRIL = ITEMS.register("taint_tendril", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_JAR_LABEL = ITEMS.register("jar_label", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_SALIS_MUNDUS = ITEMS.register("salis_mundus", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_PRIMAL_CHARM = ITEMS.register("primal_charm", () -> new Item(new Item.Properties().stacksTo(1)));
        public static final RegistrySupplier<Item> SUPPLIER_VOID_INGOT = ITEMS.register("void_ingot", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_VOID_SEED = ITEMS.register("void_seed", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_GOLD_COIN = ITEMS.register("gold_coin", () -> new Item(new Item.Properties()));

        public static final RegistrySupplier<EldritchEyeItem> SUPPLIER_ELDRITCH_EYE = ITEMS.register("eldritch_eye", EldritchEyeItem::new);
        public static final RegistrySupplier<CrimsonRitesItem> SUPPLIER_CRIMSON_RITES = ITEMS.register("crimson_rites", CrimsonRitesItem::new);
        public static final RegistrySupplier<RunedTabletItem> SUPPLIER_RUNED_TABLET = ITEMS.register("runed_tablet", RunedTabletItem::new);
        public static final RegistrySupplier<PrimePearlItem> SUPPLIER_PRIME_PEARL = ITEMS.register("prime_pearl", PrimePearlItem::new);
        public static final RegistrySupplier<EldritchObeliskPlacerItem> SUPPLIER_ELDRITCH_OBELISK_PLACER = ITEMS.register("ob_placer", EldritchObeliskPlacerItem::new);

        public static final RegistrySupplier<ThaumometerItem> SUPPLIER_THAUMOMETER_ITEM = ITEMS.register("thaumometer", ThaumometerItem::new);

        //wand caps
        public static final RegistrySupplier<CopperWandCapItem> SUPPLIER_COPPER_WAND_CAP = ITEMS.register("wand_cap_copper", CopperWandCapItem::new);//
        public static final RegistrySupplier<GoldWandCapItem> SUPPLIER_GOLD_WAND_CAP = ITEMS.register("wand_cap_gold", GoldWandCapItem::new);
        public static final RegistrySupplier<IronWandCapItem> SUPPLIER_IRON_WAND_CAP = ITEMS.register("wand_cap_iron", IronWandCapItem::new);
        public static final RegistrySupplier<SilverWandCapItem> SUPPLIER_SILVER_WAND_CAP = ITEMS.register("wand_cap_silver", SilverWandCapItem::new);
        public static final RegistrySupplier<ThaumiumWandCapItem> SUPPLIER_THAUMIUM_WAND_CAP = ITEMS.register("wand_cap_thaumium", ThaumiumWandCapItem::new);
        public static final RegistrySupplier<VoidWandCapItem> SUPPLIER_VOID_WAND_CAP = ITEMS.register("wand_cap_void", VoidWandCapItem::new);
        
        //wand rods
        public static final RegistrySupplier<BlazeWandRodItem> SUPPLIER_BLAZE_WAND_ROD = ITEMS.register("wand_rod_blaze", BlazeWandRodItem::new);
        public static final RegistrySupplier<BoneWandRodItem> SUPPLIER_BONE_WAND_ROD = ITEMS.register("wand_rod_bone", BoneWandRodItem::new);
        public static final RegistrySupplier<GreatWoodWandRodItem> SUPPLIER_GREATWOOD_WAND_ROD = ITEMS.register("wand_rod_greatwood", GreatWoodWandRodItem::new);
        public static final RegistrySupplier<IceWandRodItem> SUPPLIER_ICE_WAND_ROD = ITEMS.register("wand_rod_ice", IceWandRodItem::new);
        public static final RegistrySupplier<ObsidianWandRodItem> SUPPLIER_OBSIDIAN_WAND_ROD = ITEMS.register("wand_rod_obsidian", ObsidianWandRodItem::new);
        public static final RegistrySupplier<QuartzWandRodItem> SUPPLIER_QUARTZ_WAND_ROD = ITEMS.register("wand_rod_quartz", QuartzWandRodItem::new);
        public static final RegistrySupplier<ReedWandRodItem> SUPPLIER_REED_WAND_ROD = ITEMS.register("wand_rod_reed", ReedWandRodItem::new);
        public static final RegistrySupplier<SilverWoodWandRodItem> SUPPLIER_SILVERWOOD_WAND_ROD = ITEMS.register("wand_rod_silverwood", SilverWoodWandRodItem::new);
        public static final RegistrySupplier<WoodWandRodItem> SUPPLIER_WOOD_WAND_ROD = ITEMS.register("wand_rod_wood", WoodWandRodItem::new);//fake in fact

        //staff rods
        public static final RegistrySupplier<PrimalStaffRodItem> SUPPLIER_PRIMAL_STAFF_ROD = ITEMS.register("staff_rod_primal",PrimalStaffRodItem::new);
        public static final RegistrySupplier<BlazeStaffRodItem> SUPPLIER_BLAZE_STAFF_ROD = ITEMS.register("staff_rod_blaze", BlazeStaffRodItem::new);
        public static final RegistrySupplier<BoneStaffRodItem> SUPPLIER_BONE_STAFF_ROD = ITEMS.register("staff_rod_bone", BoneStaffRodItem::new);
        public static final RegistrySupplier<GreatWoodStaffRodItem> SUPPLIER_GREATWOOD_STAFF_ROD = ITEMS.register("staff_rod_greatwood", GreatWoodStaffRodItem::new);
        public static final RegistrySupplier<IceStaffRodItem> SUPPLIER_ICE_STAFF_ROD = ITEMS.register("staff_rod_ice", IceStaffRodItem::new);
        public static final RegistrySupplier<ObsidianStaffRodItem> SUPPLIER_OBSIDIAN_STAFF_ROD = ITEMS.register("staff_rod_obsidian", ObsidianStaffRodItem::new);
        public static final RegistrySupplier<QuartzStaffRodItem> SUPPLIER_QUARTZ_STAFF_ROD = ITEMS.register("staff_rod_quartz", QuartzStaffRodItem::new);
        public static final RegistrySupplier<ReedStaffRodItem> SUPPLIER_REED_STAFF_ROD = ITEMS.register("staff_rod_reed", ReedStaffRodItem::new);
        public static final RegistrySupplier<SilverWoodStaffRodItem> SUPPLIER_SILVERWOOD_STAFF_ROD = ITEMS.register("staff_rod_silverwood", SilverWoodStaffRodItem::new);

        public static final RegistrySupplier<Item> SUPPLIER_SILVER_WAND_CAP_INERT = ITEMS.register("wand_cap_silver_inert",() -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_THAUMIUM_WAND_CAP_INERT = ITEMS.register("wand_cap_thaumium_inert",() -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_VOID_WAND_CAP_INERT = ITEMS.register("wand_cap_void_inert",() -> new Item(new Item.Properties()));
    }
    public static class ItemTags {
        //TODO:Tag for forge and fabric,im lazy to write tag json :(
        public static final TagKey<Item> VOID_INGOT_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("thaumcraft:tag_void_ingot"));
        public static final TagKey<Item> PRIME_PEARL_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("thaumcraft:tag_prime_pearl"));

        public static final TagKey<Item> SILVER_NUGGET_FORGE_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("forge:nuggets/silver"));
        public static final TagKey<Item> SILVER_NUGGET_FABRIC_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("c:silver_nuggets"));
        public static final TagKey<Item> COPPER_NUGGET_FORGE_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("forge:nuggets/copper"));
        public static final TagKey<Item> COPPER_NUGGET_FABRIC_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("c:copper_nuggets"));
        public static final TagKey<Item> GOLD_NUGGET_FORGE_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("forge:nuggets/gold"));
        public static final TagKey<Item> GOLD_NUGGET_FABRIC_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("c:gold_nuggets"));
        public static final TagKey<Item> IRON_NUGGET_FORGE_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("forge:nuggets/iron"));
        public static final TagKey<Item> IRON_NUGGET_FABRIC_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("c:iron_nuggets"));
    }

    public static void init() {
        Registry.ITEMS.register();
    }
}
