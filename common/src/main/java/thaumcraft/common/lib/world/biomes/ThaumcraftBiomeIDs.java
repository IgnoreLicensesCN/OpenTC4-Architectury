package thaumcraft.common.lib.world.biomes;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import thaumcraft.common.Thaumcraft;

public class ThaumcraftBiomeIDs {

    public static class Registry{
        //chatgpt go fuck off,bootstrap couldn't be used!
        public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.BIOME);
        public static final RegistrySupplier<Biome> SUPPLIER_EERIE = BIOMES.register("eerie", BiomeGenEerie::createEerie);
        public static final RegistrySupplier<Biome> SUPPLIER_ELDRITCH = BIOMES.register("eldritch", BiomeGenEerie::createEerie);
    }
    public static final ResourceLocation EERIE_ID = Registry.SUPPLIER_EERIE.getId();
    public static final ResourceLocation ELDRITCH_ID = Registry.SUPPLIER_ELDRITCH.getId();

    public static void init() {
        Registry.BIOMES.register();
    }
}
