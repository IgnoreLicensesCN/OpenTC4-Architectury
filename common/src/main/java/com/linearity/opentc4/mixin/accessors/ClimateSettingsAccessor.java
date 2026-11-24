package com.linearity.opentc4.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net.minecraft.world.level.biome.Biome$ClimateSettings")
public interface ClimateSettingsAccessor {
    @Invoker("hasPrecipitation")
    boolean hasPrecipitation();
    @Invoker("temperature")
    float temperature();
    @Invoker("downfall")
    float downfall();
}
