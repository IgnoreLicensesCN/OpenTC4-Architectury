package com.linearity.opentc4.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.lib.utils.EntityUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityClientMixin {
    @Inject(method = "tick",at=@At("HEAD"))
    public void opentc4$livingClientTickBefore(CallbackInfo ci) {
    }
    @Inject(method = "tick",at=@At("TAIL"))
    public void opentc4$livingClientTickAfter(CallbackInfo ci) {
        var entity = (LivingEntity)(Object)this;
        if (entity instanceof Monster monster) {
            opentc4$renderChampionMob(monster);
        }
    }

    @Unique
    public void opentc4$renderChampionMob(Monster monster) {
        if (opentc4$checkedNoEffect){return;}
        if (!monster.isDeadOrDying()) {
            AttributeInstance championModInstance = monster.getAttribute(EntityUtils.CHAMPION_MOD);
            int t = (int) championModInstance.getBaseValue();
            if (t >= 0) {
                ChampionModifier.mods[t].effect.showFX(monster);
            }else {
                opentc4$checkedNoEffect = true;
            }
        }
    }
    @Unique private boolean opentc4$checkedNoEffect = false;
}

