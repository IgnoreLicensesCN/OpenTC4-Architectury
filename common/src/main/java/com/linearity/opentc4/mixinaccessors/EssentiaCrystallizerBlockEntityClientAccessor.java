package com.linearity.opentc4.mixinaccessors;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaCrystallizerBlockEntity;

public interface EssentiaCrystallizerBlockEntityClientAccessor {
    @Unique
    EssentiaCrystallizerBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
