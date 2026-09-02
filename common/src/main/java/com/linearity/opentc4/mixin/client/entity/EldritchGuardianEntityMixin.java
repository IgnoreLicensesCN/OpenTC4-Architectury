package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.EldritchGuardianEntityClientAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.entities.monster.eldritch.EldritchGuardianEntity;


@Mixin(EldritchGuardianEntity.class)
public class EldritchGuardianEntityMixin implements EldritchGuardianEntityClientAccessor {
    @Unique
    EldritchGuardianEntity.ClientTickContext opentc4$clientTickContext = new EldritchGuardianEntity.ClientTickContext();


    @Override
    public EldritchGuardianEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
