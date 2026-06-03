package com.linearity.opentc4.mixinaccessors.clientbe;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.visnet.VisNetRelayBlockEntity;

public interface VisNetRelayBlockEntityClientAccessor {
    @Unique
    VisNetRelayBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
