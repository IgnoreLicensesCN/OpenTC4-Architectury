package com.linearity.opentc4.mixinaccessors.clientbe;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.ArcaneBoreBlockEntity;
import thaumcraft.common.tiles.crafted.infusion.InfusionMatrixBlockEntity;

public interface InfusionMatrixBlockEntityClientAccessor {
    @Unique
    InfusionMatrixBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
