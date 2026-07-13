package thaumcraft.common.lib.enchantment;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;
import thaumcraft.common.Thaumcraft;


public class ThaumcraftEnchantments {
    public static class Registry {
        public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.ENCHANTMENT);
        public static final RegistrySupplier<EnchantmentHaste> SUPPLIER_HASTE = ENCHANTMENTS.register("haste", EnchantmentHaste::new);
        public static final RegistrySupplier<EnchantmentRepair> SUPPLIER_REPAIR = ENCHANTMENTS.register("repair", EnchantmentRepair::new);
        public static final RegistrySupplier<EnchantmentDowsing> SUPPLIER_DOWSING = ENCHANTMENTS.register("dowsing", EnchantmentDowsing::new);
    }

    public static class ThaumcraftEnchantmentInstances {

        public static EnchantmentHaste HASTE() {
            return Registry.SUPPLIER_HASTE.get();
        }

        public static EnchantmentRepair REPAIR() {
            return Registry.SUPPLIER_REPAIR.get();
        }

        public static EnchantmentDowsing DOWSING() {
            return Registry.SUPPLIER_DOWSING.get();
        }
    }

    public static void init(){
        Registry.ENCHANTMENTS.register();
    }
}
