package com.linearity.opentc4.mixinaccessors.clientbe;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaCentrifugeBlockEntity;

public interface EssentiaCentrifugeBlockEntityClientAccessor {
    @Unique
    EssentiaCentrifugeBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
