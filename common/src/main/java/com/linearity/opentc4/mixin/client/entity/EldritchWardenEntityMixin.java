package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.EldritchGuardianEntityClientAccessor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import thaumcraft.common.entities.monster.boss.EldritchWardenEntity;
import thaumcraft.common.entities.monster.eldritch.EldritchGuardianEntity;


@Mixin(EldritchWardenEntity.class)
public abstract class EldritchWardenEntityMixin implements EldritchGuardianEntityClientAccessor {
    @Unique
    EldritchGuardianEntity.ClientTickContext opentc4$clientTickContext = new EldritchGuardianEntity.ClientTickContext();


    @Override
    public EldritchGuardianEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }

    @Shadow
    public abstract Level level();
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void opentc4$clientTick(){
        var thiz = (EldritchWardenEntity)(Object) this;
        if (level().isClientSide) {
            EldritchGuardianEntity.ClientTickContext.clientTick(thiz);
        }
    }
}
