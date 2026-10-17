package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintSporeEntityClientAccessor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import thaumcraft.common.entities.monster.tainted.TaintSporeEntity;
import thaumcraft.common.entities.monster.tainted.TaintacleEntity;

@Mixin(TaintSporeEntity.class)
public abstract class TaintSporeEntityMixin implements TaintSporeEntityClientAccessor {
    @Unique
    private final TaintSporeEntity.ClientTickContext opentc4$clientTickContext = new TaintSporeEntity.ClientTickContext();

    @Override
    public TaintSporeEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
    @Shadow
    public abstract Level level();

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void opentc4$clientTick(){
        var thiz = (TaintSporeEntity)(Object) this;
        if (level().isClientSide) {
            TaintSporeEntity.ClientTickContext.clientTick(thiz);
        }
    }
}
