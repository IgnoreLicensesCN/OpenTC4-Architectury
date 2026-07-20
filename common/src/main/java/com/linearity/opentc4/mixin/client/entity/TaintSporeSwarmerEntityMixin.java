package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintSporeSwarmerEntityClientAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.entities.monster.tainted.TaintSporeSwarmerEntity;

@Mixin(TaintSporeSwarmerEntity.class)
public class TaintSporeSwarmerEntityMixin implements TaintSporeSwarmerEntityClientAccessor {
    @Unique
    private final TaintSporeSwarmerEntity.ClientTickContext opentc4$clientTickContext = new TaintSporeSwarmerEntity.ClientTickContext();

    @Override
    public TaintSporeSwarmerEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
