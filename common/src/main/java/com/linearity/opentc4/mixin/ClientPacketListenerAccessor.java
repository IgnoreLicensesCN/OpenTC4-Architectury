package com.linearity.opentc4.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface ClientPacketListenerAccessor {
    @Accessor("serverChunkRadius")
    int opentc4$getServerChunkRadius();
    @Accessor("recipeManager")
    RecipeManager opentc4$getRecipeManager();
}
