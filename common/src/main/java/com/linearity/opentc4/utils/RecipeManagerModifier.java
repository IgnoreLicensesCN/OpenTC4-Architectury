package com.linearity.opentc4.utils;

import com.linearity.opentc4.mixinaccessors.RecipeManagerAccessor;
import net.minecraft.world.item.crafting.Recipe;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketAddRecipesS2C;

import java.util.Collection;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class RecipeManagerModifier {

    public static void addRecipesServer(Collection<Recipe<?>> recipes){
        var server = platformUtils.getServer();
        ((RecipeManagerAccessor)server.getRecipeManager()).opentc4$addRecipes(recipes);
        new PacketAddRecipesS2C(recipes).sendToAll(server);
    }
}
