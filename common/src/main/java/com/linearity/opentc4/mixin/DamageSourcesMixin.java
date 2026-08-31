package com.linearity.opentc4.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.entities.abstracts.IMobAttackDamageTypeReplaceable;
import thaumcraft.common.entities.projectile.AspectArrowEntity;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin {

    @Shadow
    protected abstract DamageSource source(ResourceKey<DamageType> resourceKey,@Nullable Entity e);
    @Shadow
    protected abstract DamageSource source(ResourceKey<DamageType> resourceKey,@Nullable Entity e0,@Nullable Entity e);

    @ModifyArg(
            method = "mobAttack",
            at = @At("HEAD")
    )
    private void opentc4$mobDamageSourceReplacement(LivingEntity livingEntity, CallbackInfoReturnable<DamageSource> cir) {
        if (livingEntity instanceof IMobAttackDamageTypeReplaceable replaceable){
            cir.setReturnValue(source(replaceable.replaceDamageTypeWith(),livingEntity));
        }
    }

    @Inject(
            method = "arrow",
            at = @At("HEAD")
    )
    private void opentc4$modifyArrowDamageSource(
            AbstractArrow abstractArrow,
            Entity entity,
            CallbackInfoReturnable<DamageSource> cir){
        if (abstractArrow instanceof AspectArrowEntity aspectArrow){
            var aspectArrowProperties = aspectArrow.getAspectArrowProperties();
            var damageSource = aspectArrowProperties.getModifiedDamageType(
                    DamageTypes.ARROW
            );
            cir.setReturnValue(this.source(damageSource, abstractArrow, entity));
        }
    }
}
