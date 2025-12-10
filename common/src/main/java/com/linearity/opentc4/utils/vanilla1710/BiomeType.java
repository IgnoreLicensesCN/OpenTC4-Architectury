package com.linearity.opentc4.utils.vanilla1710;

import com.linearity.opentc4.mixinaccessors.BiomeAccessor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.*;

import static com.linearity.opentc4.utils.vanilla1710.BiomeTypeUtils.hasTreeType;

/**
 * BiomeType class replacement for BiomeDictionary.Type in 1.20.1
 */
public class BiomeType {

    private static final Map<String, BiomeType> REGISTRY = new LinkedHashMap<>();

    // ----------------------- Instance fields -----------------------
    private final String name;
    private final BiomeTypeMatcher predicate; // 判断一个 Biome 是否符合这个类型
    private final Set<BiomeType> children = new HashSet<>(); // 支持复合类型，如 WATER = OCEAN + RIVER

    // ----------------------- Constructor -----------------------
    private BiomeType(String name, BiomeTypeMatcher predicate,BiomeType... subtypes) {
        this.name = name;
        this.predicate = predicate;
        if (subtypes != null) {
            children.addAll(Arrays.asList(subtypes));
        }
        REGISTRY.put(name, this);
    }

    // ----------------------- Getter -----------------------
    public String getName() {
        return name;
    }

    public boolean includes(BiomeType other) {
        if (this == other) return true;
        return children.contains(other);
    }

    public boolean matches(Biome biome,ResourceKey<Biome> key) {
        if (predicate != null && predicate.matches(biome,key)) return true;
        for (BiomeType child : children) {
            if (child.matches(biome,key)) return true;
        }
        return false;
    }

    
    public interface BiomeTypeMatcher {
        boolean matches(Biome biome,ResourceKey<Biome> key);
    }
    
    // ----------------------- Static registration -----------------------

    public static BiomeType register(String name, BiomeTypeMatcher matcher,BiomeType... subTypes) {
        return new BiomeType(name, matcher,subTypes);
    }

    public static BiomeType get(String name) {
        return REGISTRY.get(name);
    }

    public static Collection<BiomeType> values() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }

    // ----------------------- 1.7.10 Types -----------------------
    public static final BiomeType HOT = register("HOT", (biome,key) -> biome.getBaseTemperature() > 1.0f);
    public static final BiomeType COLD = register("COLD", (biome,key) -> biome.getBaseTemperature() < 0.3f);

    public static final BiomeType SPARSE = register("SPARSE", (biome,key) -> biome.getGenerationSettings().features().isEmpty());
    public static final BiomeType DENSE = register("DENSE", (biome,key) -> !biome.getGenerationSettings().features().isEmpty());

    public static final BiomeType WET = register("WET", (biome,key) -> ((BiomeAccessor)(Object)biome).openTC4$getClimateSettings().opentc4$getDownfall() > 0.7f
    );
    public static final BiomeType DRY = register("DRY", (biome,key) -> ((BiomeAccessor)(Object)biome).openTC4$getClimateSettings().opentc4$getDownfall() < 0.3f);

    public static final BiomeType SAVANNA = register("SAVANNA", (biome,key) -> hasTreeType(biome.getGenerationSettings(),"acacia"));
    public static final BiomeType CONIFEROUS = register("CONIFEROUS", (biome,key) -> hasTreeType(biome.getGenerationSettings(),"spruce"));
    public static final BiomeType JUNGLE = register("JUNGLE", (biome,key) -> hasTreeType(biome.getGenerationSettings(),"jungle"));

    public static final BiomeType SPOOKY = register("SPOOKY", (biome,key) -> false); // 自定义判定
    public static final BiomeType DEAD = register("DEAD", (biome,key) -> false);
    public static final BiomeType LUSH = register("LUSH", (biome,key) ->
            {
                boolean temp = biome.getBaseTemperature() > 0.5f;
                boolean downfall = ((BiomeAccessor)(Object)biome).openTC4$getClimateSettings().opentc4$getDownfall() > 0.5f;
                return temp && downfall;
            }
    );
    public static final BiomeType NETHER = register("NETHER", (biome,key) ->
            Objects.equals(key,Biomes.NETHER_WASTES)
                    || Objects.equals(key,Biomes.BASALT_DELTAS)
                    || Objects.equals(key,Biomes.SOUL_SAND_VALLEY)
                    || Objects.equals(key,Biomes.CRIMSON_FOREST)
                    || Objects.equals(key,Biomes.WARPED_FOREST)
    );
    public static final BiomeType END = register("END", (biome,key) ->
            Objects.equals(key, Biomes.THE_END)
                    || Objects.equals(key,Biomes.END_HIGHLANDS)
                    || Objects.equals(key,Biomes.END_MIDLANDS)
                    || Objects.equals(key,Biomes.SMALL_END_ISLANDS)
                    || Objects.equals(key,Biomes.END_BARRENS)
    );
    public static final BiomeType MUSHROOM = register("MUSHROOM", (biome,key) -> false);
    public static final BiomeType MAGICAL = register("MAGICAL", (biome,key) -> false);

    public static final BiomeType OCEAN = register("OCEAN", (biome,key) -> ((BiomeAccessor)(Object)biome).openTC4$getClimateSettings().opentc4$getTemperature() < 0.8f && ((BiomeAccessor)(Object)biome).openTC4$getClimateSettings().opentc4$hasPrecipitation());
    public static final BiomeType RIVER = register("RIVER", (biome,key) -> false);

    public static final BiomeType WATER = register("WATER", null, OCEAN, RIVER);

    public static final BiomeType MESA = register("MESA", (biome,key) -> false);
    public static final BiomeType FOREST = register("FOREST", (biome,key) -> hasTreeType(biome.getGenerationSettings(),"oak"));
    public static final BiomeType PLAINS = register("PLAINS", (biome,key) -> false);
    public static final BiomeType MOUNTAIN = register("MOUNTAIN", (biome,key) -> biome.getBaseTemperature() < 0.7f && biome.getBaseTemperature() > 0.3f);
    public static final BiomeType HILLS = register("HILLS", (biome,key) -> false);
    public static final BiomeType SWAMP = register("SWAMP", (biome,key) -> hasTreeType(biome.getGenerationSettings(),"swamp"));
    public static final BiomeType SANDY = register("SANDY", (biome,key) -> hasTreeType(biome.getGenerationSettings(),"sand"));
    public static final BiomeType SNOWY = register("SNOWY", (biome, key) -> biome.getBaseTemperature() < 0.15F);
    public static final BiomeType WASTELAND = register("WASTELAND", (biome,key) -> false);
    public static final BiomeType BEACH = register("BEACH", (biome,key) -> hasTreeType(biome.getGenerationSettings(),"beach"));

    @Override
    public String toString() {
        return "BiomeType{" + name + "}";
    }
}