package com.linearity.opentc4.mixin.accessors;

import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Projectile.class)
public interface ProjectileAccessor {

    @Invoker("updateRotation")
    void updateRotation();
}
