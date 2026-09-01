package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import thaumcraft.common.entities.projectile.AspectArrowEntity;

import static thaumcraft.common.entities.projectile.AspectArrowEntity.AspectArrowManager.*;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @ModifyReceiver(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private Entity modifyDamageReceiverForAspectArrow(Entity victim, DamageSource source, float amount) {

        AbstractArrow arrow = (AbstractArrow) (Object)this;
        if (arrow instanceof AspectArrowEntity aspectArrowEntity) {
            return aspectArrowOnModifyReceiver(aspectArrowEntity, victim, source, amount);
        }
        return victim;
    }
    @ModifyArg(
            method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private float modifyDamageForAspectArrow(DamageSource source,float damage) {

        AbstractArrow arrow = (AbstractArrow) (Object)this;
        if (arrow instanceof AspectArrowEntity aspectArrowEntity) {
            return aspectArrowModifyDamage(aspectArrowEntity, source, damage);
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
        if (arrow instanceof AspectArrowEntity aspectArrowEntity) {
            return aspectArrowOnHitEntity(aspectArrowEntity,victim,source,causedDamage,original);
        }
        return original;
    }
}
