package thaumcraft.common.lib.world.biomes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import thaumcraft.common.Thaumcraft;

import java.util.Objects;

public class ThaumcraftBiomeIDs {

    public static class Registry{
        //chatgpt go fuck off,bootstrap couldn't be used!
//        public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Thaumcraft.MOD_ID, Registries.BIOME);
//        public static final RegistrySupplier<Biome> SUPPLIER_ELDRITCH = BIOMES.register("eldritch", BiomeGenEldritch::createEldritch);
//        public static final RegistrySupplier<Biome> SUPPLIER_TAINT = BIOMES.register("taint", BiomeGenTaint::createTaint);
//        public static final RegistrySupplier<Biome> SUPPLIER_MAGICAL_FOREST = BIOMES.register("magical_forest", BiomeGenMagicalForest::createMagicalForest);
//        static{
//            Registry.BIOMES.register();
//        }
    }
    public static final ResourceLocation EERIE_ID = Objects.requireNonNull(ResourceLocation.tryBuild(Thaumcraft.MOD_ID, "eerie"));
    public static final ResourceLocation ELDRITCH_ID = Objects.requireNonNull(ResourceLocation.tryBuild(Thaumcraft.MOD_ID, "eldritch"));
    public static final ResourceLocation TAINT_ID = Objects.requireNonNull(ResourceLocation.tryBuild(Thaumcraft.MOD_ID, "taint"));//TODO:Big tree and taint fibres feature
    public static final ResourceLocation MAGICAL_FOREST_ID = Objects.requireNonNull(ResourceLocation.tryBuild(Thaumcraft.MOD_ID, "magical_forest"));

    public static final ResourceKey<Biome> EERIE_KEY = ResourceKey.create(Registries.BIOME, EERIE_ID);
    public static final ResourceKey<Biome> ELDRITCH_KEY = ResourceKey.create(Registries.BIOME, ELDRITCH_ID);
    public static final ResourceKey<Biome> TAINT_KEY = ResourceKey.create(Registries.BIOME, TAINT_ID);
    public static final ResourceKey<Biome> MAGICAL_FOREST_KEY = ResourceKey.create(Registries.BIOME, MAGICAL_FOREST_ID);

    public static void init() {

    }
}
