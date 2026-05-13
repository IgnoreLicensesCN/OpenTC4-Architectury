package com.linearity.opentc4.mixin.clientbe;

import com.linearity.opentc4.mixinaccessors.ArcaneBoreBlockEntityClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.ArcaneBoreBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(ArcaneBoreBlockEntity.class)
public abstract class ArcaneBoreBlockEntityClientMixin implements ArcaneBoreBlockEntityClientAccessor {
    @Unique
    private final ArcaneBoreBlockEntity.ClientTickContext opentc4$clientTickContext = new ArcaneBoreBlockEntity.ClientTickContext((ArcaneBoreBlockEntity) (Object) this);

    @Override
    public ArcaneBoreBlockEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
