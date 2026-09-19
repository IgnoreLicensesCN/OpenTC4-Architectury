package com.linearity.opentc4.mixin.client;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.entities.abstracts.IItemStackBreakAnimationPlayable;
import thaumcraft.common.entities.championmod.abstracts.entity.IChampionModifierOwnerLivingEntity;
import thaumcraft.common.entities.championmod.abstracts.modifier.IClientTickableChampionModifier;

@Mixin(LivingEntity.class)
public abstract class LivingEntityClientMixin implements IItemStackBreakAnimationPlayable {

    @Shadow
    protected abstract void breakItem(ItemStack stack);

    @Unique
    public void opentc4$playBreakItemAnimation(ItemStack stack) {
        breakItem(stack);
    }

    @Inject(method = "tick",at=@At("HEAD"))
    public void opentc4$livingClientTickBefore(CallbackInfo ci) {
    }
    @Inject(method = "tick",at=@At("TAIL"))
    public void opentc4$livingClientTickAfter(CallbackInfo ci) {
        opentc4$renderChampionMob((LivingEntity)(Object)this);
    }
    @Unique
    public void opentc4$renderChampionMob(LivingEntity entity) {
        if (this instanceof IChampionModifierOwnerLivingEntity owner){
            owner.opentc4$getChampionModifiersForChecker(
                    (ignoredA,ignoredB) -> true,
                    IClientTickableChampionModifier.class
            ).forEach(
                    clientTickable -> clientTickable.onClientTick(entity)
            );
        }
    }

}

