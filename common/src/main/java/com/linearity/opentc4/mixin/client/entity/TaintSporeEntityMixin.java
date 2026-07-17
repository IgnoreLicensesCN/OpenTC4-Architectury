package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintSporeEntityClientAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.entities.monster.tainted.TaintSporeEntity;

@Mixin(TaintSporeEntity.class)
public class TaintSporeEntityMixin implements TaintSporeEntityClientAccessor {
    @Unique
    private final TaintSporeEntity.ClientTickContext opentc4$clientTickContext = new TaintSporeEntity.ClientTickContext();

    @Override
    public TaintSporeEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
