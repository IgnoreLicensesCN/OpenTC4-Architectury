package com.linearity.opentc4.mixinattachments.overrides.entity.fishinghook;

import com.linearity.annotations.InterfaceShouldAttachTo;
import com.linearity.opentc4.mixin.accessors.FishingHookAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@InterfaceShouldAttachTo(FishingHook.class)
public interface FishingHookShouldStopFishingOverride {
    default boolean opentc4$fishingHook$shouldStopFishing(Entity entityUsingFishingHook) {
        return opentc4$fishingHook$shouldStopFishing(entityUsingFishingHook,
                    /*original call requires player,
                    maybe someone who wants to argue about this
                     should argue with mojang about the original one?*/
                arr -> ((FishingHookAccessor)this).shouldStopFishing((Player) arr[0]));
    }
    default boolean opentc4$fishingHook$shouldStopFishing(
            Entity entityUsingFishingHook,
            Operation<Boolean> /*(Player playerUsingFishingHook)*/ original /*super*/
    ) {
        FishingHook fishingHook = (FishingHook) this;
        if (entityUsingFishingHook instanceof Player player) {
            return original.call(player);
        }
        if (entityUsingFishingHook instanceof LivingEntity livingEntity) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            ItemStack itemStack2 = livingEntity.getOffhandItem();
            boolean bl = itemStack.is(Items.FISHING_ROD);
            boolean bl2 = itemStack2.is(Items.FISHING_ROD);
            if (!livingEntity.isRemoved()
                    && livingEntity.isAlive() && (bl || bl2)
                    && !(fishingHook.distanceToSqr(livingEntity) > 1024.0)
            ) {
                return false;
            } else {
                fishingHook.discard();
                return true;
            }
        }
        return fishingHook.isRemoved() || entityUsingFishingHook.isRemoved();
    }
}
