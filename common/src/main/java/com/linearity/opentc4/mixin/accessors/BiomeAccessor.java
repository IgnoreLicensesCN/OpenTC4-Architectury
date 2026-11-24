package com.linearity.opentc4.mixin.accessors;

import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.class)
public interface BiomeAccessor {
    @Accessor("generationSettings")
    BiomeGenerationSettings getGenerationSettings();
    @Accessor("mobSettings")
    MobSpawnSettings getMobSpawnSettings();
    @Accessor("specialEffects")
    BiomeSpecialEffects getSpecialEffects();
    @Accessor("temperatureCache")
    ThreadLocal<Long2FloatLinkedOpenHashMap> getTemperatureCache();
}
