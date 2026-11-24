package com.linearity.opentc4.mixinattachments.overrides.entity.fishinghook;

import com.linearity.annotations.InterfaceShouldAttachTo;
import com.linearity.opentc4.mixinattachments.supercallproviders.entity.FishingHookSuperAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;

@InterfaceShouldAttachTo(FishingHook.class)
public interface FishingHookRecreateFromPacketOverride {
    default void opentc4$fishingHook$recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket, Operation<Void> original){
        var thiz = (FishingHook) this;
        var owner = thiz.getOwner();
        if (!(owner instanceof Player) && owner != null){
            ((FishingHookSuperAccessor)thiz).opentc4$projectileSuper$recreateFromPacket(clientboundAddEntityPacket);
            return;
        }
        original.call();
    }
}
