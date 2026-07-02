package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.InMilkContextAccessor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.entities.ThaumcraftEntityEvents;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.*;

@Mixin(value=LivingEntity.class,priority = 214748)
public abstract class LivingEntityMixin implements InMilkContextAccessor {

    @ModifyReturnValue(
            method = "maxUpStep",
            at = @At("RETURN")
    )
    private float opentc4$maxUpStepAddition(float prev){
        var living = (LivingEntity)(Object)this;
        if (living.isShiftKeyDown()) {
            return prev;
        }
        return (float) (prev + living.getAttributeValue(STEP_HEIGHT_ADDITION_NOT_SNEAKING()));
    }
    @ModifyReturnValue(
            method = "getJumpPower",
            at = @At("RETURN")
    )
    private float opentc4$getJumpPower(float prev){
        var living = (LivingEntity)(Object)this;
        if (living.isShiftKeyDown()) {
            return prev;
        }
        return (float) (prev + living.getAttributeValue(JUMP_Y_VELOCITY_ADDITION_NOT_SNEAKING()));
    }

    @Unique
    private final ThreadLocal<Boolean> opentc4$isInMilkContext = new ThreadLocal<>();

    @Override
    public void opentc4$setInMilkContext(boolean inMilkContext) {
        this.opentc4$isInMilkContext.set(inMilkContext);
    }

    @Inject(method = "tick",at=@At("HEAD"))
    public void opentc4$livingTickBefore(CallbackInfo ci) {
    }
    @Inject(method = "tick",at=@At("TAIL"))
    public void opentc4$livingTickAfter(CallbackInfo ci) {
        var entity = (LivingEntity)(Object)this;
        if (entity instanceof Monster monster){
            opentc4$performChampionMobEffect(monster);
        }
    }
    
    @ModifyReturnValue(
            method = "getFlyingSpeed",
            at = @At("RETURN")
    )
    private float opentc4$overrideFlyingSpeed(float prev){
        var entity = (LivingEntity)(Object)this;
        var speedOverride = entity.getAttributeValue(HARNESS_FLYING_SPEED_ADD_PERCENT());
        if (speedOverride > 10E-4){
            return (float) speedOverride;
        }
        return prev;
    }

    @Unique
    public void opentc4$performChampionMobEffect(Monster monster) {
        if (opentc4$checkedNoEffect){return;}
        if (!monster.isDeadOrDying()) {
            var instance = monster.getAttribute(EntityUtils.ThaumcraftAttributeCategoryInstances.CHAMPION_MOD());
            if (instance == null) {
                opentc4$checkedNoEffect = true;
                return;
            }
            int t = (int)instance.getBaseValue();
            if (t >= 0 && ChampionModifier.mods[t].type == 0) {
                ChampionModifier.mods[t].effect.performEffect(monster, null, null, 0.0F);
            }else{
                opentc4$checkedNoEffect = true;
            }
        }
    }
    @Unique private boolean opentc4$checkedNoEffect = false;

    @Shadow @Final private Map<MobEffect, MobEffectInstance> activeEffects;
    @Unique private final Map<MobEffect, MobEffectInstance> opentc4$storedEffectsToPreventRemove = new ConcurrentHashMap<>();
    @Inject(
            method = "onEffectRemoved",at=@At("HEAD"),cancellable = true
    )
    public void opentc4$preventMilkRemoveEffect(MobEffectInstance mobEffectInstance, CallbackInfo ci) {
        if (!opentc4$isInMilkContext.get()) {return;}
        opentc4$storedEffectsToPreventRemove.put(mobEffectInstance.getEffect(), mobEffectInstance);
        ci.cancel();
    }
    @Inject(method = "removeAllEffects",at=@At("TAIL"))
    public void opentc4$preventMilkRemoveEffect_restore(CallbackInfoReturnable<Boolean> cir) {
        activeEffects.putAll(opentc4$storedEffectsToPreventRemove);
        opentc4$storedEffectsToPreventRemove.clear();
    }

    @Inject(
            method = "eat",
            at = @At("HEAD")
    )
    private void opentc4$onFinishUsing(
            Level level, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir
    ) {
        ThaumcraftEntityEvents.onHandlingUnnaturalHungerForEating((LivingEntity)(Object)this,itemStack);
    }

    @ModifyReturnValue(
            method = "createLivingAttributes",
            at = @At("RETURN")
    )
    private static AttributeSupplier.Builder opentc4$injectAttributes(AttributeSupplier.Builder builder) {
        return ThaumcraftEntityEvents.injectLivingAttributes(builder);
    }

    @ModifyReturnValue(
            method = "getDamageAfterArmorAbsorb",
            at = @At("RETURN")
    )
    private float opentc4$getDamageAfterArmorAbsorb(float originalOut,DamageSource damageSource,float originalIn) {
        return ThaumcraftEntityEvents.DamageEvents.getDamageAfterArmorAbsorb((LivingEntity)(Object)this,originalOut,damageSource,originalIn);
    }
    @ModifyReturnValue(
            method = "getDamageAfterMagicAbsorb",
            at = @At("RETURN")
    )
    private float opentc4$getDamageAfterMagicAbsorb(float originalOut,DamageSource damageSource,float originalIn) {
        return ThaumcraftEntityEvents.DamageEvents.getDamageAfterMagicAbsorb((LivingEntity)(Object)this,originalOut,damageSource,originalIn);
    }

    @Inject(
            method = "actuallyHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V",shift = At.Shift.AFTER)
    )
    private void opentc4$onBeingDamaged(DamageSource damageSource, float damageCausedNoArmorReduce, CallbackInfo ci) {
        ThaumcraftEntityEvents.DamageEvents.onBeingDamaged((LivingEntity)(Object)this,damageSource,damageCausedNoArmorReduce);
    }
    @Shadow
    public abstract RandomSource getRandom();

    @Inject(
            method = "dropAllDeathLoot",
            at = @At("RETURN")
    )
    private void opentc4$onDropAll(DamageSource damageSource, CallbackInfo ci) {
        ThaumcraftEntityEvents.DropEvents.onDropAllDeathLoot((LivingEntity)(Object)this,damageSource);
    }
}

