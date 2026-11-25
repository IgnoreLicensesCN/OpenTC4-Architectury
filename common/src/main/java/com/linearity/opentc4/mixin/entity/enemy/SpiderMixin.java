package com.linearity.opentc4.mixin.entity.enemy;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import thaumcraft.common.entities.abstracts.ISpiderWithoutSkeletonRiding;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_SPIDER;
import static thaumcraft.common.entities.ThaumcraftEntities.usualCanConvertToTaintedMob;
import static thaumcraft.common.entities.ThaumcraftEntities.usualTaintedMobConversion;

@Mixin(Spider.class)
public class SpiderMixin implements ITaintConvertableEntity {

    @Override
    public boolean opentc4$canConvertToTaintedMob() {
        return usualCanConvertToTaintedMob((LivingEntity)(Object)this);
    }

    @Override
    public void opentc4$convertToTaintedMob() {
        usualTaintedMobConversion((LivingEntity)(Object)this,TAINTED_SPIDER());
    }

    @ModifyReturnValue(
            method = "finalizeSpawn",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;")
    )
    public Skeleton createSkeleton(Skeleton original) {
        if (this instanceof ISpiderWithoutSkeletonRiding spiderWithoutSkeletonRiding && !spiderWithoutSkeletonRiding.canBeRiddenBySkeletonWhenSpawn()){
            return null;
        }
        return original;
    }
}
