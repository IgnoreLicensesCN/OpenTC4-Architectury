package com.linearity.opentc4.mixin.clientbe;

import com.linearity.opentc4.mixinaccessors.BrainJarBlockEntityClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.jars.BrainJarBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(BrainJarBlockEntity.class)
public abstract class BrainJarBlockEntityClientMixin implements BrainJarBlockEntityClientAccessor {
    @Unique
    private final BrainJarBlockEntity.ClientTickContext opentc4$clientTickContext = new BrainJarBlockEntity.ClientTickContext();

    @Override
    public BrainJarBlockEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
