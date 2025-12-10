package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.BiomeAccessor;
import com.linearity.opentc4.mixinaccessors.ClimateSettingsAccessor;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeAccessor {
    @Shadow @Final private ClimateSettingsAccessor climateSettings;

    @Override
    public ClimateSettingsAccessor openTC4$getClimateSettings() {
        return climateSettings;
    }
    // generationSettings
    @Shadow @Final private BiomeGenerationSettings generationSettings;

    @Override
    public BiomeGenerationSettings openTC4$getGenerationSettings() {
        return generationSettings;
    }
    // mobSettings
    @Shadow @Final private MobSpawnSettings mobSettings;

    @Override
    public MobSpawnSettings openTC4$getMobSettings() {
        return mobSettings;
    }

    // specialEffects
    @Shadow @Final private BiomeSpecialEffects specialEffects;

    @Override
    public BiomeSpecialEffects openTC4$getSpecialEffects() {
        return specialEffects;
    }

    // temperatureCache
    @Shadow @Final private ThreadLocal<Long2FloatLinkedOpenHashMap> temperatureCache;

    @Override
    public ThreadLocal<Long2FloatLinkedOpenHashMap> openTC4$getTemperatureCache() {
        return temperatureCache;
    }
}
