package thaumcraft.common.lib.world.biomes;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import thaumcraft.common.Thaumcraft;

import java.util.concurrent.atomic.AtomicReference;

public class BiomeRegistration {
    private BiomeRegistration() {}

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.BIOME);
    public static final RegistrySupplier<Biome> EERIE = BIOMES.register("eerie", () -> BiomeGenEerie.INSTANCE);
    public static final ResourceKey<Biome> EERIE_ID = EERIE.getKey();
    public static void init() {
        BIOMES.register();
    }
}
