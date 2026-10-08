package com.linearity.opentc4.mixin;

import com.linearity.opentc4.datautils.EntityDataSerializerAdditions;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDataSerializers.class)
public class EntityDataSerializersMixin {
    @Inject(
            method = "<clinit>",
            at = @At("TAIL")
    )
    private static void opentc4$registerEntityDataSerializers(CallbackInfo ci) {
        EntityDataSerializerAdditions.registerSerializers();
    }
}
