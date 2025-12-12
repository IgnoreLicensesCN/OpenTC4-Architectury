package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.lib.utils.EntityUtils;

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
}

