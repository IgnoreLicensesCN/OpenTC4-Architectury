package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintedSwarmEntityClientAccessor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import thaumcraft.common.entities.monster.boss.EldritchWardenEntity;
import thaumcraft.common.entities.monster.eldritch.EldritchGuardianEntity;
import thaumcraft.common.entities.monster.tainted.TaintedSwarmEntity;

@Mixin(TaintedSwarmEntity.class)
public abstract class TaintedSwarmEntityMixin implements TaintedSwarmEntityClientAccessor {
    @Unique
    private final TaintedSwarmEntity.ClientTickContext opentc4$clientTickContext = new TaintedSwarmEntity.ClientTickContext();

    @Override
    public TaintedSwarmEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }


    @Shadow
    public abstract Level level();
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void opentc4$clientTick(){
        var thiz = (TaintedSwarmEntity)(Object) this;
        if (level().isClientSide) {
            TaintedSwarmEntity.ClientTickContext.clientTick(thiz);
        }
    }
}
