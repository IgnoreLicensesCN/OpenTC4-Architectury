package com.linearity.opentc4.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static thaumcraft.common.lib.utils.EntityUtils.makeChampionOnSpawn;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "finalizeSpawn",at=@At("TAIL"))
    public void opentc4$finalizeSpawn(ServerLevelAccessor serverLevelAccessor,
                                      DifficultyInstance difficultyInstance,
                                      MobSpawnType mobSpawnType,
                                      SpawnGroupData spawnGroupData,
                                      CompoundTag compoundTag,
                                      CallbackInfoReturnable<SpawnGroupData> cir) {
        makeChampionOnSpawn((Mob) (Object)this,serverLevelAccessor);
    }

}
