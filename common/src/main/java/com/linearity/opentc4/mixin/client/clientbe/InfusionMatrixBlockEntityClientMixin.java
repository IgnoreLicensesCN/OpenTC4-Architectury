package com.linearity.opentc4.mixin.client.clientbe;

import com.linearity.opentc4.mixinaccessors.clientbe.InfusionMatrixBlockEntityClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.infusion.InfusionMatrixBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(InfusionMatrixBlockEntity.class)
public abstract class InfusionMatrixBlockEntityClientMixin implements InfusionMatrixBlockEntityClientAccessor {
    @Unique
    private final InfusionMatrixBlockEntity.ClientTickContext opentc4$clientTickContext = new InfusionMatrixBlockEntity.ClientTickContext((InfusionMatrixBlockEntity) (Object) this);

    @Override
    public InfusionMatrixBlockEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
