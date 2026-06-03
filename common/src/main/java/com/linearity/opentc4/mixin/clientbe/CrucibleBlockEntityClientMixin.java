package com.linearity.opentc4.mixin.clientbe;

import com.linearity.opentc4.mixinaccessors.clientbe.CrucibleBlockEntityClientAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.common.tiles.crafted.CrucibleBlockEntity;

@Environment(EnvType.CLIENT)
@Mixin(CrucibleBlockEntity.class)
public abstract class CrucibleBlockEntityClientMixin implements CrucibleBlockEntityClientAccessor {
    @Unique
    private final CrucibleBlockEntity.ClientTickContext opentc4$clientTickContext = new CrucibleBlockEntity.ClientTickContext();

    @Override
    public CrucibleBlockEntity.ClientTickContext opentc4$getClientTickContext() {
        return opentc4$clientTickContext;
    }
}
