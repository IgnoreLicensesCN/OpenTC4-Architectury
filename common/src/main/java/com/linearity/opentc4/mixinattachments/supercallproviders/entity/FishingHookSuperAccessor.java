package com.linearity.opentc4.mixinattachments.supercallproviders.entity;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;

public interface FishingHookSuperAccessor {
    default void opentc4$projectileSuper$recreateFromPacket(ClientboundAddEntityPacket packet){
        throw new IllegalAccessError("shouldNotReachHere");
    }
    default void opentc4$projectileSuper$tick(){
        throw new IllegalAccessError("shouldNotReachHere");
    }
}
