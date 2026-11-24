package com.linearity.opentc4.mixinattachments.overrides.entity.fishinghook;

import com.linearity.annotations.InterfaceShouldAttachTo;
import com.linearity.opentc4.mixin.accessors.EntityAccessor;
import com.linearity.opentc4.mixin.accessors.FishingHookAccessor;
import com.linearity.opentc4.mixin.accessors.ProjectileAccessor;
import com.linearity.opentc4.mixinattachments.FishingHookMixinAccessor;
import com.linearity.opentc4.mixinattachments.supercallproviders.entity.FishingHookSuperAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

@InterfaceShouldAttachTo(FishingHook.class)
public interface FishingHookTickOverride {
    default void opentc4$fishingHook$tick(Operation<Void> original) {
        var thiz = (FishingHook) this;
        {
            Player player = thiz.getPlayerOwner();
            if (player != null) {
                original.call();
                return;
            }
        }
        var owner = thiz.getOwner();

        FishingHookAccessor fishingHookAccessor = (FishingHookAccessor) thiz;
        fishingHookAccessor.getSyncronizedRandom().setSeed(thiz.getUUID().getLeastSignificantBits() ^ thiz.level().getGameTime());
        var fishingHookMixinAccessor = (FishingHookMixinAccessor) thiz;
        ((FishingHookSuperAccessor)thiz).opentc4$projectileSuper$tick();
        if (owner == null || !(thiz instanceof FishingHookShouldStopFishingOverride shouldStopFishingOverride)) {
            thiz.discard();
            return;
        }

        if (thiz.level().isClientSide || !shouldStopFishingOverride.opentc4$fishingHook$shouldStopFishing(owner)) {
            if (thiz.onGround()) {
                fishingHookAccessor.setLife(fishingHookAccessor.getLife()+1);
                if (fishingHookAccessor.getLife() >= 1200) {
                    thiz.discard();
                    return;
                }
            } else {
                fishingHookAccessor.setLife(0);
            }

            float f = 0.0F;
            BlockPos blockPos = thiz.blockPosition();
            FluidState fluidState = thiz.level().getFluidState(blockPos);
            if (fluidState.is(FluidTags.WATER)) {
                f = fluidState.getHeight(thiz.level(), blockPos);
            }

            boolean bl = f > 0.0F;
            if (fishingHookMixinAccessor.opentc4$getCurrentState() == FishingHookMixinAccessor.FishHookState.FLYING) {
                if (thiz.getHookedIn() != null) {
                    thiz.setDeltaMovement(Vec3.ZERO);
                    fishingHookMixinAccessor.opentc4$setCurrentState(FishingHookMixinAccessor.FishHookState.HOOKED_IN_ENTITY);
                    return;
                }

                if (bl) {
                    thiz.setDeltaMovement(thiz.getDeltaMovement().multiply(0.3, 0.2, 0.3));
                    fishingHookMixinAccessor.opentc4$setCurrentState(FishingHookMixinAccessor.FishHookState.BOBBING);
                    return;
                }

                fishingHookAccessor.checkCollision();
            } else {
                if (fishingHookMixinAccessor.opentc4$getCurrentState() == FishingHookMixinAccessor.FishHookState.HOOKED_IN_ENTITY) {
                    var hookedIn = thiz.getHookedIn();
                    if (hookedIn != null) {
                        if (!hookedIn.isRemoved() && hookedIn.level().dimension() == thiz.level().dimension()) {
                            thiz.setPos(hookedIn.getX(), hookedIn.getY(0.8), hookedIn.getZ());
                        } else {
                            fishingHookAccessor.setHookedEntity(null);
                            fishingHookMixinAccessor.opentc4$setCurrentState(FishingHookMixinAccessor.FishHookState.FLYING);
                        }
                    }

                    return;
                }

                if (fishingHookMixinAccessor.opentc4$getCurrentState() == FishingHookMixinAccessor.FishHookState.BOBBING) {
                    Vec3 vec3 = thiz.getDeltaMovement();
                    double d = thiz.getY() + vec3.y - blockPos.getY() - f;
                    if (Math.abs(d) < 0.01) {
                        d += Math.signum(d) * 0.1;
                    }

                    thiz.setDeltaMovement(vec3.x * 0.9, vec3.y - d * ((EntityAccessor)thiz).opentc4$Entity$getRandom().nextFloat() * 0.2, vec3.z * 0.9);
                    if (fishingHookAccessor.getNibble() <= 0 && fishingHookAccessor.getTimeUntilHooked() <= 0) {
                        fishingHookAccessor.setOpenWater(true);
                    } else {
                        fishingHookAccessor.setOpenWater(
                                fishingHookAccessor.getOpenWater()
                                && fishingHookAccessor.getOutOfWaterTime() < 10
                                && fishingHookAccessor.calculateOpenWater(blockPos)
                        );
                    }

                    if (bl) {
                        fishingHookAccessor.setOutOfWaterTime(Math.max(0, fishingHookAccessor.getOutOfWaterTime() - 1));
                        if (fishingHookAccessor.getBiting()) {
                            thiz.setDeltaMovement(thiz.getDeltaMovement().add(0.0, -0.1 * fishingHookAccessor.getSyncronizedRandom().nextFloat() * fishingHookAccessor.getSyncronizedRandom().nextFloat(), 0.0));
                        }

                        if (!thiz.level().isClientSide) {
                            fishingHookAccessor.catchingFish(blockPos);
                        }
                    } else {

                        fishingHookAccessor.setOutOfWaterTime(Math.min(10, fishingHookAccessor.getOutOfWaterTime()) + 1);
                    }
                }
            }

            if (!fluidState.is(FluidTags.WATER)) {
                thiz.setDeltaMovement(thiz.getDeltaMovement().add(0.0, -0.03, 0.0));
            }

            thiz.move(MoverType.SELF, thiz.getDeltaMovement());
            ((ProjectileAccessor)thiz).updateRotation();
            if (((FishingHookMixinAccessor) thiz).opentc4$getCurrentState() == FishingHookMixinAccessor.FishHookState.FLYING && (thiz.onGround() || thiz.horizontalCollision)) {
                thiz.setDeltaMovement(Vec3.ZERO);
            }

            double e = 0.92;
            thiz.setDeltaMovement(thiz.getDeltaMovement().scale(0.92));
            ((EntityAccessor)thiz).reapplyPosition();
        }

    }
}
