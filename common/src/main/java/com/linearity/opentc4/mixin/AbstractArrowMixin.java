package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import thaumcraft.common.entities.projectile.AspectArrowEntity;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @ModifyArg(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private float modifyDamageForAspectArrow(float damage) {

        AbstractArrow arrow = (AbstractArrow) (Object)this;
        if (arrow instanceof AspectArrowEntity aspectArrowEntity) {
            damage *= (float) aspectArrowEntity.getAspectArrowProperties().getDamageMultiplier();
        }
        return damage;
    }

    @ModifyReturnValue(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private boolean onHitSuccess(boolean original,Entity victim,DamageSource source,float causedDamage) {
        AbstractArrow arrow = (AbstractArrow) (Object)this;
        if (original && arrow instanceof AspectArrowEntity aspectArrowEntity) {
            aspectArrowEntity.getAspectArrowProperties().onArrowHitEntity(aspectArrowEntity,victim);
        }
        return original;
    }
}
