package com.linearity.opentc4.mixinaccessors.clientbe;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.CrucibleBlockEntity;

public interface CrucibleBlockEntityClientAccessor {
    @Unique
    CrucibleBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
