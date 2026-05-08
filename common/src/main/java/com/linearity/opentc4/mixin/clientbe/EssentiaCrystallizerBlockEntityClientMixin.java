package com.linearity.opentc4.mixin.clientbe;

import com.linearity.opentc4.mixinaccessors.EssentiaCrystallizerBlockEntityClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaCrystallizerBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(EssentiaCrystallizerBlockEntity.class)
public abstract class EssentiaCrystallizerBlockEntityClientMixin implements EssentiaCrystallizerBlockEntityClientAccessor {
    @Unique
    private final EssentiaCrystallizerBlockEntity.ClientTickContext opentc4$clientTickContext = new EssentiaCrystallizerBlockEntity.ClientTickContext();

    @Override
    public EssentiaCrystallizerBlockEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
