package com.linearity.opentc4.mixinaccessors.clientbe;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.ArcaneBoreBlockEntity;

public interface ArcaneBoreBlockEntityClientAccessor {
    @Unique
    ArcaneBoreBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
