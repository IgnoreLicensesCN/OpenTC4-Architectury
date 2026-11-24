package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinattachments.BiomeMixinAccessor;
import com.linearity.opentc4.mixin.accessors.ClimateSettingsAccessor;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeMixinAccessor {
    @Shadow @Final private ClimateSettingsAccessor climateSettings;

    @Override
    public ClimateSettingsAccessor opentc4$getClimateSettings() {
        return climateSettings;
    }
}
