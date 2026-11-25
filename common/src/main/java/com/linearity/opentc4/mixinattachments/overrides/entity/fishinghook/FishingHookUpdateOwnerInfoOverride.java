package com.linearity.opentc4.mixinattachments.overrides.entity.fishinghook;

import com.linearity.annotations.InterfaceShouldAttachTo;
import com.linearity.opentc4.mixinattachments.FishingHookOwner;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.projectile.FishingHook;

@InterfaceShouldAttachTo(FishingHook.class)
public interface FishingHookUpdateOwnerInfoOverride {
    default void opentc4$fishingHook$updateOwnerInfo(Operation<Void> original) {
        var thiz = (FishingHook) this;
        if (thiz.getOwner() instanceof FishingHookOwner owner) {
            owner.opentc4$onUpdateFishingHookOwning(thiz);
            return;
        }
        original.call(thiz);
    }
}
