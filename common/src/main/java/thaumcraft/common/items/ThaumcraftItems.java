package thaumcraft.common.items;

import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPattern;
import thaumcraft.common.Thaumcraft;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class ThaumcraftItems {

    public static class ItemTags {
        //TODO:Tag for forge and fabric,im lazy to write tag json :(
        // maybe i need even several issue/PR to realize where(recipe or st.) i didn't notice this part.
        public static final TagKey<Item> VOID_INGOT_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("thaumcraft:void_ingot"));
        public static final TagKey<Item> THAUMIUM_INGOT_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("thaumcraft:thaumium_ingot"));
        public static final TagKey<Item> PRIME_PEARL_TAG = TagKey.create(
                Registries.ITEM, new ResourceLocation("thaumcraft:prime_pearl"));

        public static final TagKey<Item> NUGGET = TagKey.create(
                Registries.ITEM, new ResourceLocation("minecraft:nuggets")
        );
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
        public static final TagKey<Item> CINNABAR_ORES = TagKey.create(Registries.ITEM,
                new ResourceLocation("c", "cinnabar_ores")
        );
        public static final TagKey<Item> UNNATURAL_HUNGER_NEEDED = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "unnatural_hunger_needed")
        );
        public static final TagKey<Item> WAND_CAP = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "wand_cap")
        );
        public static final TagKey<Item> WAND_ROD = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "wand_rod")
        );
        public static final TagKey<Item> STAFF_ROD = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "staff_rod")
        );
        public static final TagKey<Item> NODE_HARMFUL = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "node_harmful_component")
        );
        public static final TagKey<Item> RANGED_FOCUS_FOR_AI = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "ranged_focus_for_ai")
        );
        public static final TagKey<Item> SHARDS = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "shards")
        );
        public static final TagKey<Item> PRIMAL_SHARDS = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "primal_shards")
        );
        public static final TagKey<Item> CRYSTAL_CLUSTERS = TagKey.create(
                Registries.ITEM, new ResourceLocation(Thaumcraft.MOD_ID, "primal_crystals")
        );

    }

    public static class ItemDumps {
        public static class Nuggets {

            private static final Set<Item> NUGGET_SET = new IdentityHashMap<Item,Boolean>().keySet();
            public static final Set<Item> NUGGET_SET_VIEW = Collections.unmodifiableSet(NUGGET_SET);
            static {
                initNuggets();
            }
            public static void initNuggets() {
                NUGGET_SET.clear();
                NUGGET_SET.addAll(platformUtils.getItemsFromTag(ItemTags.NUGGET));
            }
        }
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


    public static void init() {
        ThaumcraftItemsRegistry.ITEMS.register();
        FuelRegistry.register(6400, ThaumcraftItemInstances.ALUMENTUM());
        FuelRegistry.register(400, ThaumcraftItemInstances.GREATWOOD_LOG(), ThaumcraftItemInstances.SILVERWOOD_LOG());//azanor's idea
        FuelRegistry.register(300, ThaumcraftItemInstances.GREATWOOD_PLANKS(), ThaumcraftItemInstances.SILVERWOOD_PLANKS());
        BannerPatternsRegistry.BANNER_PATTERNS.register();
    }
}
