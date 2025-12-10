package thaumcraft.common.lib.enchantment;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantment;


public class Enchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create("thaumcraft", Registries.ENCHANTMENT);
    public static final RegistrySupplier<Enchantment> FRUGAL = ENCHANTMENTS.register("frugal", EnchantmentFrugal::new);
    public static final RegistrySupplier<Enchantment> HASTE = ENCHANTMENTS.register("haste", EnchantmentHaste::new);
    public static final RegistrySupplier<Enchantment> POTENCY = ENCHANTMENTS.register("potency", EnchantmentPotency::new);
    public static final RegistrySupplier<Enchantment> REPAIR = ENCHANTMENTS.register("repair", EnchantmentRepair::new);
    public static final RegistrySupplier<Enchantment> WAND_FORTUNE = ENCHANTMENTS.register("repair", EnchantmentWandFortune::new);
    public static void init(){

    }
}
