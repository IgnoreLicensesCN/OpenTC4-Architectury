package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("lastHurtByPlayerTime")
    int opentc4$getLastHurtByPlayerTime();

    @Accessor("lastHurtByPlayerTime")
    void opentc4$setLastHurtByPlayerTime(int time);

}

