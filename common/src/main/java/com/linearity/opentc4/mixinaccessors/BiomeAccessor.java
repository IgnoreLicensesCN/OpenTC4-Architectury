package com.linearity.opentc4.mixinaccessors;

import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public interface BiomeAccessor {

    ClimateSettingsAccessor openTC4$getClimateSettings();
    BiomeGenerationSettings openTC4$getGenerationSettings();
    MobSpawnSettings openTC4$getMobSettings();
    BiomeSpecialEffects openTC4$getSpecialEffects();
    ThreadLocal<Long2FloatLinkedOpenHashMap> openTC4$getTemperatureCache();
}
