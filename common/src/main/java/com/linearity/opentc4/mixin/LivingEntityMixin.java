package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinstackhelper.MilkContext;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
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
import thaumcraft.api.effects.PreventMilkRemoveEffect;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
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

    @Unique
    public void opentc4$performChampionMobEffect(Monster monster) {
        if (opentc4$checkedNoEffect){return;}
        if (!monster.isDeadOrDying()) {
            var instance = monster.getAttribute(EntityUtils.CHAMPION_MOD);
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
    @Unique private final Map<MobEffect, MobEffectInstance> storedEffectsToPreventRemove = new ConcurrentHashMap<>();
    @Inject(method = "removeAllEffects",at=@At("HEAD"))
    public void opentc4$preventMilkRemoveEffect(CallbackInfoReturnable<Boolean> cir) {
        if (!MilkContext.FROM_MILK.get()) {return;}//a bit trick from chatGPT
        for (var entry : activeEffects.entrySet()) {
            var effect = entry.getKey();
            var effectInstance = entry.getValue();
            if (effect instanceof PreventMilkRemoveEffect preventMilkRemoveEffect){
                if (preventMilkRemoveEffect.preventMilkRemove(effectInstance,(LivingEntity)(Object)this)){
                    storedEffectsToPreventRemove.put(effect, effectInstance);
                }
            }
        }
        for (var effect:storedEffectsToPreventRemove.keySet()){
            activeEffects.remove(effect);
        }
    }
    @Inject(method = "removeAllEffects",at=@At("TAIL"))
    public void opentc4$preventMilkRemoveEffect_restore(CallbackInfoReturnable<Boolean> cir) {
        activeEffects.putAll(storedEffectsToPreventRemove);
        storedEffectsToPreventRemove.clear();
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
        var unnaturalHungerInstance = living.getEffect(ThaumcraftEffects.UNNATURAL_HUNGER);
        if (unnaturalHungerInstance != null){
            if (item == Items.ROTTEN_FLESH || item == ThaumcraftItems.ZOMBIE_BRAIN){
                int amp = unnaturalHungerInstance.getAmplifier() - 1;
                int duration = unnaturalHungerInstance.getDuration() - 600;
                living.removeEffect(ThaumcraftEffects.UNNATURAL_HUNGER);
                if (duration > 0 && amp >= 0) {
                    living.addEffect(new MobEffectInstance(ThaumcraftEffects.UNNATURAL_HUNGER, duration, amp, true,true));
                }

                if (living instanceof ServerPlayer serverPlayer){
                    serverPlayer.sendSystemMessage(Component.literal("§2§o" + Component.translatable("warp.text.hunger.2")));
                }
            }else if (item.getFoodProperties() != null && living instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.literal("§4§o" + Component.translatable("warp.text.hunger.1")));
            }
        }

    }
}

