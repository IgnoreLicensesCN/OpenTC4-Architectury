package thaumcraft.common.items;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import thaumcraft.common.items.misc.*;

public class Items {

    public static final Item VOID_INGOT = Registry.SUPPLIER_VOID_INGOT.get();
    public static final Item ELDRITCH_EYE = Registry.SUPPLIER_ELDRITCH_EYE.get();
    public static final Item PRIME_PEARL = Registry.SUPPLIER_PRIME_PEARL.get();
    public static final Item CRIMSON_RITES = Registry.SUPPLIER_CRIMSON_RITES.get();
    public static final Item ELDRITCH_OBELISK_PLACER = Registry.SUPPLIER_ELDRITCH_OBELISK_PLACER.get();
    public static final Item RUNED_TABLET = Registry.SUPPLIER_RUNED_TABLET.get();

    public static class Registry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create("thaumcraft", Registries.ITEM);
        public static final RegistrySupplier<Item> SUPPLIER_VOID_INGOT = ITEMS.register("void_ingot", () -> new Item(new Item.Properties()));
        public static final RegistrySupplier<Item> SUPPLIER_ELDRITCH_EYE = ITEMS.register("eldritch_eye", EldritchEyeItem::new);
        public static final RegistrySupplier<Item> SUPPLIER_CRIMSON_RITES = ITEMS.register("crimson_rites", CrimsonRitesItem::new);
        public static final RegistrySupplier<Item> SUPPLIER_PRIME_PEARL = ITEMS.register("prime_pearl", PrimePearlItem::new);
        public static final RegistrySupplier<Item> SUPPLIER_RUNED_TABLET = ITEMS.register("runed_tablet", RunedTabletItem::new);
        public static final RegistrySupplier<Item> SUPPLIER_ELDRITCH_OBELISK_PLACER = ITEMS.register("eldritch_obelisk_placer", EldritchObeliskPlacerItem::new);

    }
    public static class ItemTags {
        public static final TagKey<Item> VOID_INGOT_TAG = TagKey.create(Registries.ITEM, ResourceLocation.tryParse("thaumcraft:tag_void_ingot"));
        public static final TagKey<Item> PRIME_PEARL_TAG = TagKey.create(Registries.ITEM, ResourceLocation.tryParse("thaumcraft:tag_prime_pearl"));
    }

    public static void init() {

    }
}
