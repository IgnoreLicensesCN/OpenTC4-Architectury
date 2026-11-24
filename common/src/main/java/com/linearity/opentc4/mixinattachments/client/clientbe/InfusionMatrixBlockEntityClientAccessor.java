package com.linearity.opentc4.mixinattachments.client.clientbe;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.infusion.InfusionMatrixBlockEntity;

public interface InfusionMatrixBlockEntityClientAccessor {
    @Unique
    InfusionMatrixBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
