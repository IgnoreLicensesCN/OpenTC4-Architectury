package com.linearity.opentc4.mixinattachments.supercallproviders.entity;

import net.minecraft.world.entity.Entity;

//maybe someone:"Oh i want super method not what you've overridden!"
//since this ThaumcraftFishingHook.class is trying to expand FishingHook.class i don't want to make it a destruction.
public interface ThaumcraftFishingHookSuperAccessor {
    default boolean opentc4$fishingHookSuper$canHitEntity(Entity beingHit){
        throw new IllegalAccessError("shouldNotReachHere");
    }
}
