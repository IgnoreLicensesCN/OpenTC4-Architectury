package com.linearity.opentc4.mixin.clientbe;

import com.linearity.opentc4.mixinaccessors.clientbe.EssentiaCentrifugeBlockEntityClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaCentrifugeBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(EssentiaCentrifugeBlockEntity.class)
public abstract class EssentiaCentrifugeBlockEntityClientMixin implements EssentiaCentrifugeBlockEntityClientAccessor {
    @Unique
    private final EssentiaCentrifugeBlockEntity.ClientTickContext opentc4$clientTickContext = new EssentiaCentrifugeBlockEntity.ClientTickContext();

    @Override
    public EssentiaCentrifugeBlockEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
