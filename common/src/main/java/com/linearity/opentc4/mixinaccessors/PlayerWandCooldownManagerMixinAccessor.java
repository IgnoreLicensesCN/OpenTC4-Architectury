package com.linearity.opentc4.mixinaccessors;

import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.wands.IWandCooldownManagerOwnerLivingEntity;
import thaumcraft.common.items.wands.WandCooldownManager;

public interface PlayerWandCooldownManagerMixinAccessor extends IWandCooldownManagerOwnerLivingEntity {
    WandCooldownManager opentc4$getWandCooldownManager();

    @Override
    default @Nullable WandCooldownManager getWandCooldownManager(){
        return opentc4$getWandCooldownManager();
    }
}
