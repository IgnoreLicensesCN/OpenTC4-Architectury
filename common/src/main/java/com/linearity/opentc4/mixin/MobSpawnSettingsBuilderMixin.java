package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.entities.ThaumcraftEntityEvents;

@Mixin(MobSpawnSettings.Builder.class)
public class MobSpawnSettingsBuilderMixin {
    @Inject(
            at = @At("HEAD"),
            method = "addSpawn"
    )
    public void opentc4$onAddSpawn(
            MobCategory mobCategory,
            MobSpawnSettings.SpawnerData spawnerData,
            CallbackInfoReturnable<MobSpawnSettings.Builder> cir
    ){
        ThaumcraftEntityEvents.EntitySpawnEvents.onAddSpawn((MobSpawnSettings.Builder)(Object) this,mobCategory,spawnerData);
    }
}
