package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.InMilkContextAccessor;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.items.ThaumcraftItemInstances;
import thaumcraft.common.items.abstracts.ISpecialDamageCalculationEquipmentItem;
import thaumcraft.common.items.abstracts.armorcomponents.IAttackOthersListenerArmor;
import thaumcraft.common.items.abstracts.armorcomponents.IBeingAttackedListenerArmor;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBauble;
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
        var speedOverride = entity.getAttributeValue(FLYING_SPEED_CONTROL_OVERRIDE());
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
        //idk if it's suitable to make a interface for item so i didn't
        Item item = itemStack.getItem();
        var living = (LivingEntity)(Object)this;
        var unnaturalHungerInstance = living.getEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER());
        if (unnaturalHungerInstance != null){
            if (item == Items.ROTTEN_FLESH || item == ThaumcraftItemInstances.ZOMBIE_BRAIN()){
                int amp = unnaturalHungerInstance.getAmplifier() - 1;
                int duration = unnaturalHungerInstance.getDuration() - 600;
                living.removeEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER());
                if (duration > 0 && amp >= 0) {
                    living.addEffect(new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.UNNATURAL_HUNGER(), duration, amp, true,true));
                }

                if (living instanceof ServerPlayer serverPlayer){
                    serverPlayer.sendSystemMessage((Component.translatable("warp.text.hunger.2")
                            .withStyle(ChatFormatting.ITALIC)
                            .withStyle(ChatFormatting.DARK_GREEN)));
                }
            }else if (item.getFoodProperties() != null && living instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(
                        Component.translatable("warp.text.hunger.1")
                                .withStyle(ChatFormatting.ITALIC)
                                .withStyle(ChatFormatting.DARK_RED)
                );
            }
        }
    }

    @ModifyReturnValue(
            method = "createLivingAttributes",
            at = @At("RETURN")
    )
    private static AttributeSupplier.Builder opentc4$injectAttributes(AttributeSupplier.Builder builder) {
        builder
                .add(STEP_HEIGHT_ADDITION_NOT_SNEAKING())
                .add(FLYING_SPEED_CONTROL_OVERRIDE())
                .add(HARNESS_FLYING_SPEED_ADD_PERCENT())
                .add(HARNESS_FUEL_DURATION_ADD_PERCENT());
        return builder;
    }

    @ModifyReturnValue(
            method = "getDamageAfterArmorAbsorb",
            at = @At("RETURN")
    )
    private float opentc4$getDamageAfterArmorAbsorb(float originalOut,DamageSource damageSource,float originalIn) {
        var living = (LivingEntity)(Object)this;
        AtomicReference<Float> modifiedOut = new AtomicReference<>(originalOut);
        living.getArmorSlots().forEach(stack -> {
            if (stack.getItem() instanceof ISpecialDamageCalculationEquipmentItem equipment) {
                modifiedOut.updateAndGet(out -> equipment.modifyDamageAfterCalculatedArmorAbsorb(living,stack,out,damageSource,originalIn));

            }
        });
        if (living instanceof Player player){
            forEachBauble(player, ISpecialDamageCalculationEquipmentItem.class,((slot, stack, equipment) -> {
                    modifiedOut.updateAndGet(out -> equipment.modifyDamageAfterCalculatedArmorAbsorb(living,stack,out,damageSource,originalIn));
                    return false;
            }));
        }
        return modifiedOut.get();
    }
    @ModifyReturnValue(
            method = "getDamageAfterMagicAbsorb",
            at = @At("RETURN")
    )
    private float opentc4$getDamageAfterMagicAbsorb(float originalOut,DamageSource damageSource,float originalIn) {
        var living = (LivingEntity)(Object)this;
        AtomicReference<Float> modifiedOut = new AtomicReference<>(originalOut);
        living.getArmorSlots().forEach(stack -> {
            if (stack.getItem() instanceof ISpecialDamageCalculationEquipmentItem equipment) {
                modifiedOut.updateAndGet(out -> equipment.modifyDamageAfterCalculatedMagicAbsorb(living,stack,out,damageSource,originalIn));

            }
        });
        if (living instanceof Player player){
            forEachBauble(player, ISpecialDamageCalculationEquipmentItem.class,((slot, stack, equipment) -> {
                modifiedOut.updateAndGet(out -> equipment.modifyDamageAfterCalculatedMagicAbsorb(living,stack,out,damageSource,originalIn));
                return false;
            }));
        }
        return modifiedOut.get();
    }

    @Inject(
            method = "actuallyHurt",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V",shift = At.Shift.AFTER)
    )
    private void opentc4$onBeingDamaged(DamageSource damageSource, float damageCausedNoArmorReduce, CallbackInfo ci) {
        var self = (LivingEntity)(Object)this;
        var entityCausedDamage = damageSource.getEntity();
        if (entityCausedDamage != null) {

            for (var stack : entityCausedDamage.getArmorSlots()) {
                if (stack.getItem() instanceof IAttackOthersListenerArmor armor) {
                    armor.onAttackOtherEntity(
                            stack,
                            entityCausedDamage,
                            self,
                            damageSource,
                            damageCausedNoArmorReduce
                    );
                }
            }
        }
        for (var stack:self.getArmorSlots()) {
            if (stack.getItem() instanceof IBeingAttackedListenerArmor armor) {
                armor.onBeingAttackedByOtherEntity(
                        stack,
                        self,
                        damageSource,
                        damageCausedNoArmorReduce
                );
            }
        }
    }
}

