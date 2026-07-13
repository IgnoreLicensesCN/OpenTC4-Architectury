package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintacleEntityClientAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.entities.monster.tainted.TaintacleEntity;

@Mixin(TaintacleEntity.class)
public class TaintacleEntityMixin implements TaintacleEntityClientAccessor {
    @Unique
    private final TaintacleEntity.ClientTickContext opentc4$clientTickContext = new TaintacleEntity.ClientTickContext();

    @Override
    public TaintacleEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
