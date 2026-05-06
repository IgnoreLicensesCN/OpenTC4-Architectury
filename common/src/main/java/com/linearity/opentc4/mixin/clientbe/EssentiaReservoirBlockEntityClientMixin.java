package com.linearity.opentc4.mixin.clientbe;

import com.linearity.opentc4.mixinaccessors.EssentiaReservoirBlockEntityClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaReservoirBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(EssentiaReservoirBlockEntity.class)
public abstract class EssentiaReservoirBlockEntityClientMixin implements EssentiaReservoirBlockEntityClientAccessor {
    @Unique
    private final EssentiaReservoirBlockEntity.ClientTickContext opentc4$clientTickContext = new EssentiaReservoirBlockEntity.ClientTickContext();

    @Override
    public EssentiaReservoirBlockEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
