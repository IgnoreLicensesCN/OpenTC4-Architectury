package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Accessor("lastHurtByPlayerTime")
    int opentc4$getLastHurtByPlayerTime();

    @Accessor("lastHurtByPlayerTime")
    void opentc4$setLastHurtByPlayerTime(int time);


}

