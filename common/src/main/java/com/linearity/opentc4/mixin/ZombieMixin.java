package com.linearity.opentc4.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.common.entities.abstracts.IZombieConvertableEntity;

@Mixin(Zombie.class)
public class ZombieMixin {

    @Inject(
            method = "killedEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    private void opentc4$zombieKilledEntity(ServerLevel level, LivingEntity victim, CallbackInfoReturnable<Boolean> cir){
        if (victim instanceof IZombieConvertableEntity convertable){
            cir.setReturnValue(convertable.onKilledByZombie(level,((Zombie)(Object)this)));
        }
    }
}
