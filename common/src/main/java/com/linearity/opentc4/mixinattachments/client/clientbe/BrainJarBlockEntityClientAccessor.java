package com.linearity.opentc4.mixinattachments.client.clientbe;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.jars.BrainJarBlockEntity;

public interface BrainJarBlockEntityClientAccessor {
    @Unique
    BrainJarBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
