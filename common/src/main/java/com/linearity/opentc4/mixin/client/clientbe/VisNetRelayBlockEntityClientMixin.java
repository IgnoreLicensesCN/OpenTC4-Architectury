package com.linearity.opentc4.mixin.client.clientbe;

import com.linearity.opentc4.mixinaccessors.clientbe.VisNetRelayBlockEntityClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.vis.visnet.VisNetRelayBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(VisNetRelayBlockEntity.class)
public abstract class VisNetRelayBlockEntityClientMixin implements VisNetRelayBlockEntityClientAccessor {
    @Unique
    private final VisNetRelayBlockEntity.ClientTickContext opentc4$clientTickContext = new VisNetRelayBlockEntity.ClientTickContext();

    @Override
    public VisNetRelayBlockEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
