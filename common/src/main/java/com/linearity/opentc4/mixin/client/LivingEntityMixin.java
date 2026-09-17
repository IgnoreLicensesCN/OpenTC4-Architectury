package com.linearity.opentc4.mixin.client;

import com.linearity.opentc4.mixinaccessors.InMilkContextAccessor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.entities.ThaumcraftEntityEvents;
import thaumcraft.common.entities.abstracts.IItemStackBreakAnimationPlayable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.*;

@Mixin(value=LivingEntity.class)
public abstract class LivingEntityMixin implements IItemStackBreakAnimationPlayable {

    @Shadow
    protected abstract void breakItem(ItemStack stack);

    @Unique
    public void opentc4$playBreakItemAnimation(ItemStack stack) {
        breakItem(stack);
    }

}

