package com.linearity.opentc4.mixinattachments.client.clientbe;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.vis.visnet.VisNetRelayBlockEntity;

public interface VisNetRelayBlockEntityClientAccessor {
    @Unique
    VisNetRelayBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
