package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintSporeSwarmerEntityClientAccessor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import thaumcraft.common.entities.monster.tainted.TaintSporeEntity;
import thaumcraft.common.entities.monster.tainted.TaintSporeSwarmerEntity;

@Mixin(TaintSporeSwarmerEntity.class)
public abstract class TaintSporeSwarmerEntityMixin implements TaintSporeSwarmerEntityClientAccessor {
    @Unique
    private final TaintSporeSwarmerEntity.ClientTickContext opentc4$clientTickContext = new TaintSporeSwarmerEntity.ClientTickContext();

    @Override
    public TaintSporeSwarmerEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
    @Shadow
    public abstract Level level();

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void opentc4$clientTick(){
        var thiz = (TaintSporeSwarmerEntity)(Object) this;
        if (level().isClientSide) {
            TaintSporeSwarmerEntity.ClientTickContext.clientTick(thiz);
        }
    }
}
