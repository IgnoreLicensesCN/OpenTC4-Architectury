package thaumcraft.common.lib.enchantment;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;


public class ThaumcraftEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create("thaumcraft", Registries.ENCHANTMENT);
    public static final RegistrySupplier<Enchantment> HASTE = ENCHANTMENTS.register("haste", EnchantmentHaste::new);
    public static final RegistrySupplier<Enchantment> REPAIR = ENCHANTMENTS.register("repair", EnchantmentRepair::new);

    public static final ResourceKey<Enchantment> HASTE_KEY = HASTE.getKey();
    public static final ResourceKey<Enchantment> REPAIR_KEY = REPAIR.getKey();
    public static void init(){
        ENCHANTMENTS.register();

    }
}
