package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintedSwarmEntityClientAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.entities.monster.tainted.TaintedSwarmEntity;

@Mixin(TaintedSwarmEntity.class)
public class TaintedSwarmEntityMixin implements TaintedSwarmEntityClientAccessor {
    @Unique
    private final TaintedSwarmEntity.ClientTickContext opentc4$clientTickContext = new TaintedSwarmEntity.ClientTickContext();

    @Override
    public TaintedSwarmEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
