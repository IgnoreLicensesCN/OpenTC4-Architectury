package com.linearity.opentc4.mixin.client.entity;

import com.linearity.opentc4.mixinaccessors.cliententity.BaseGolemEntityClientAccessor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import thaumcraft.common.entities.golems.BaseGolemEntity;

@Mixin(BaseGolemEntity.class)
public abstract class BaseGolemEntityMixin implements BaseGolemEntityClientAccessor {
    @Unique
    private final BaseGolemEntity.ClientTickContext opentc4$clientTickContext = new BaseGolemEntity.ClientTickContext();

    @Override
    public BaseGolemEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }

    @Shadow
    public abstract Level level();
    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void opentc4$clientTick(){
        var thiz = (BaseGolemEntity)(Object) this;
        if (level().isClientSide) {
            BaseGolemEntity.ClientTickContext.clientTick(thiz);
        }
    }
}
