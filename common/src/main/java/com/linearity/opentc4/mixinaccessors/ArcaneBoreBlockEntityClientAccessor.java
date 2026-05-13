package com.linearity.opentc4.mixinaccessors;

import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.ArcaneBoreBlockEntity;
import thaumcraft.common.tiles.crafted.jars.BrainJarBlockEntity;

public interface ArcaneBoreBlockEntityClientAccessor {
    @Unique
    ArcaneBoreBlockEntity.ClientTickContext opentc4$getClientTickContext();
}
