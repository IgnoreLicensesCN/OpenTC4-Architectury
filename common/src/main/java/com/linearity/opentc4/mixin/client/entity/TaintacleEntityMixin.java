package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintacleEntityClientAccessor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import thaumcraft.common.entities.monster.tainted.TaintacleEntity;

import java.util.Objects;

@Mixin(TaintacleEntity.class)
public abstract class TaintacleEntityMixin implements TaintacleEntityClientAccessor {
    @Unique
    private final TaintacleEntity.ClientTickContext opentc4$clientTickContext = new TaintacleEntity.ClientTickContext();

    @Override
    public TaintacleEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }

    @Shadow
    public abstract Level level();

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void opentc4$clientTick(){
        var thiz = (TaintacleEntity)(Object) this;
        if (level().isClientSide) {
            TaintacleEntity.ClientTickContext.clientTick(thiz);
        }
    }
}
