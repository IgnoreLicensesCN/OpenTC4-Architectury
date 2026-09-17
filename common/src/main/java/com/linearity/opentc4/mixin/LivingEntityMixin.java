package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.InMilkContextAccessor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.entities.ThaumcraftEntityEvents;
import thaumcraft.common.entities.championmod.ChampionModifierManager;
import thaumcraft.common.entities.championmod.abstracts.entity.IChampionModifierOwnerLivingEntity;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.*;

@Mixin(value=LivingEntity.class,priority = 214748)
public abstract class LivingEntityMixin implements IChampionModifierOwnerLivingEntity, InMilkContextAccessor {


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

    @Inject(method = "tick",at=@At("RETURN"))
    public void opentc4$livingTickBefore(CallbackInfo ci) {
        ThaumcraftEntityEvents.TickEvents.onLivingTickAfter((LivingEntity)(Object)this);
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
    @Shadow
    @Final
    private Map<MobEffect, MobEffectInstance> activeEffects;
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
    private float opentc4$getDamageAfterArmorAbsorb(float originalOut, DamageSource damageSource, float originalIn) {
        return ThaumcraftEntityEvents.DamageEvents.getDamageAfterArmorAbsorb((LivingEntity)(Object)this,originalOut,damageSource,originalIn);
    }
    @ModifyReturnValue(
            method = "getDamageAfterMagicAbsorb",
            at = @At("RETURN")
    )
    private float opentc4$getDamageAfterMagicAbsorb(float originalOut,DamageSource damageSource,float originalIn) {
        return ThaumcraftEntityEvents.DamageEvents.getDamageAfterMagicAbsorb((LivingEntity)(Object)this, originalOut, damageSource, originalIn);
    }

    @Inject(
            method = "actuallyHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V",shift = At.Shift.AFTER)
    )
    private void opentc4$onBeingDamaged(
            DamageSource damageSource,
            float damageCausedNoArmorReduce,
            CallbackInfo ci,
            @Local(ordinal = 0, argsOnly = true)float damageCausedReduced
    ) {
        ThaumcraftEntityEvents.DamageEvents.onBeingDamaged((LivingEntity)(Object)this,damageSource,damageCausedNoArmorReduce,damageCausedReduced);
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

    @Unique
    private final Map<Class<?>, Map<Class<? extends BiPredicate<LivingEntity, ?>>, Set<?>>>
            opentc4$championModifiers =
            new ConcurrentHashMap<>();

    @Unique
    @Override
    public void opentc4$refreshChampionModifierCheckedState(BiPredicate<LivingEntity, ?> checker, Class<?> modifierClass) {
        var mapOrNull = opentc4$championModifiers.get(modifierClass);
        if (mapOrNull != null) {
            mapOrNull.remove(checker.getClass());
        }
    }

    @Unique
    @Override
    public <ModifierClass> Set<ModifierClass>
    opentc4$getChampionModifiersForChecker(
            BiPredicate<LivingEntity, ModifierClass> checker,
            Class<ModifierClass> championModifierClass
    ) {
        var thiz = (LivingEntity) (Object) this;
        return (
                (Map<Class<BiPredicate<LivingEntity, ModifierClass>>, Set<ModifierClass>>)
                        (Map<?, ?>) opentc4$championModifiers.computeIfAbsent(
                                championModifierClass, _ignored -> new ConcurrentHashMap<>()
                        ))
                .computeIfAbsent(
                        (Class<BiPredicate<LivingEntity, ModifierClass>>) checker.getClass(),
                        checkerClass -> {
                    Set<ModifierClass> result = ConcurrentHashMap.newKeySet();
                    ChampionModifierManager.forEachChampionModifierOnEntity(
                            thiz
                            , modifier -> {
                                if (championModifierClass.isInstance(modifier)) {
                                    var modifierCasted = (ModifierClass) modifier;
                                    if (checker.test(thiz, modifierCasted)) {
                                        result.add(modifierCasted);
                                    }
                                }
                            }
                    );
                    return result;
                });
    }

}
