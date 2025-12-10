package com.linearity.opentc4.utils.vanilla1710;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class BiomeWithTypes {

    public static final Multimap<ResourceKey<Biome>, BiomeType> types = HashMultimap.create();

    public static void init() {
        cacheAllBiomes();
    }

    private static void cacheAllBiomes() {
        MinecraftServer server = platformUtils.getServer();
        var registry = server.registryAccess().registryOrThrow(Registries.BIOME);

        registry.entrySet().forEach(entry -> {
            Biome biome = entry.getValue();
            ResourceKey<Biome> key = entry.getKey();

            for (BiomeType type : BiomeType.values()) {
                if (type.matches(biome, key)) {
                    types.put(key, type);
                }
            }
        });
    }

    /** 安全 getter: 先查缓存，如果没找到就动态计算一次 */
    public static Collection<BiomeType> getBiomeTypes(ResourceKey<Biome> key) {

        Collection<BiomeType> result = types.get(key);
        if (result.isEmpty()) {
            Biome biome = getBiome(key);
            if (biome != null) {
                for (BiomeType type : BiomeType.values()) {
                    if (type.matches(biome, key)) {
                        types.put(key, type);
                    }
                }
            }
        }
        return types.get(key);
    }
    public static Biome getBiome(ResourceKey<Biome> key) {
        MinecraftServer server = platformUtils.getServer();
        var registry = server.registryAccess().registryOrThrow(Registries.BIOME);
        return registry.get(key);
    }

    @Nullable
    public static ResourceKey<Biome> getBiomeResKey(Biome key) {
        MinecraftServer server = platformUtils.getServer();
        var registry = server.registryAccess().registryOrThrow(Registries.BIOME);
        AtomicReference<ResourceKey<Biome>> result = new AtomicReference<>();
        registry.entrySet().forEach(entry -> {
            if (entry.getValue() == key) {
                result.set(entry.getKey());
            }
        });
        return result.get();
    }
}
