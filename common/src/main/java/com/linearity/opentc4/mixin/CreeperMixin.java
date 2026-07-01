package com.linearity.opentc4.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.entities.abstracts.IExplodeOverrideCreeper;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_CREEPER;
import static thaumcraft.common.entities.ThaumcraftEntities.usualCanConvertToTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.usualTaintedMobConversion;

@Mixin(Creeper.class)
public class CreeperMixin implements ITaintConvertableEntity {
    @WrapMethod(
            method = "explodeCreeper"
    )
    private void opentc4$overrideExplodeCreeper(Operation<Void> original) {
        if (this instanceof IExplodeOverrideCreeper overrideCreeper) {
            overrideCreeper.explodeCreeperRewritten(original);
        }
    }
    @WrapMethod(
            method = "spawnLingeringCloud"
    )
    private void opentc4$overrideSpawnLingeringCloud(Operation<Void> original) {
        if (this instanceof IExplodeOverrideCreeper overrideCreeper) {
            overrideCreeper.spawnLingeringCloudRewritten(original);
        }
    }

    @Override
    public boolean canConvertToTaintedMob() {
        return usualCanConvertToTaintedMob((LivingEntity)(Object)this);
    }

    @Override
    public void convertToTaintedMob() {
        usualTaintedMobConversion((LivingEntity)(Object)this,TAINTED_CREEPER());
    }
}
