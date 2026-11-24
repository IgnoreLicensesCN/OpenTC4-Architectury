package com.linearity.opentc4.mixinattachments;

import com.linearity.opentc4.mixin.accessors.ClimateSettingsAccessor;
import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

public interface BiomeMixinAccessor {

    ClimateSettingsAccessor opentc4$getClimateSettings();
}
