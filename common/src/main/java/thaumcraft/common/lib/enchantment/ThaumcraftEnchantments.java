package thaumcraft.common.lib.enchantment;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;


public class ThaumcraftEnchantments {
    public static class Registry {
        public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create("thaumcraft", Registries.ENCHANTMENT);
        public static final RegistrySupplier<EnchantmentHaste> SUPPLIER_HASTE = ENCHANTMENTS.register("haste", EnchantmentHaste::new);
        public static final RegistrySupplier<EnchantmentRepair> SUPPLIER_REPAIR = ENCHANTMENTS.register("repair", EnchantmentRepair::new);
    }

    public static final ResourceKey<EnchantmentHaste> HASTE_KEY = Registry.SUPPLIER_HASTE.getKey();
    public static final ResourceKey<EnchantmentRepair> REPAIR_KEY = Registry.SUPPLIER_REPAIR.getKey();
    public static final EnchantmentHaste HASTE = Registry.SUPPLIER_HASTE.get();
    public static final EnchantmentRepair REPAIR = Registry.SUPPLIER_REPAIR.get();
    public static void init(){
        Registry.ENCHANTMENTS.register();
    }
}
