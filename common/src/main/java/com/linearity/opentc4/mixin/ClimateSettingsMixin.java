package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.ClimateSettingsAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.level.biome.Biome$ClimateSettings")
public abstract class ClimateSettingsMixin implements ClimateSettingsAccessor {

    @Shadow public abstract boolean hasPrecipitation();
    @Shadow public abstract float temperature();
    @Shadow
    public abstract float downfall();

    @Override
    public boolean opentc4$hasPrecipitation() { return hasPrecipitation(); }

    @Override
    public float opentc4$getTemperature() { return temperature(); }

    @Override
    public float opentc4$getDownfall() { return downfall(); }
}

