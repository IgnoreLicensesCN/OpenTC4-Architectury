package com.linearity.opentc4.mixin;

import com.google.common.collect.ImmutableMap;
import com.linearity.opentc4.mixinaccessors.RecipeManagerAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin implements RecipeManagerAccessor {
    @Shadow
    private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes = ImmutableMap.of();
    @Shadow
    private Map<ResourceLocation, Recipe<?>> byName = ImmutableMap.of();
    @Override
    public void opentc4$addRecipes(Collection<Recipe<?>> recipesToAdd) {
        opentc4$ensureModification();
        recipesToAdd.forEach(recipe -> {
            var type = recipe.getType();
            recipes.computeIfAbsent(type,_ignored -> new ConcurrentHashMap<>()).put(recipe.getId(), recipe);
            byName.put(recipe.getId(), recipe);
        });
    }

    @Unique
    private void opentc4$ensureModification(){
        if (!(this.recipes instanceof ConcurrentHashMap)) {
            this.recipes = new ConcurrentHashMap<>(this.recipes);
        }
        for (var key: this.recipes.keySet()) {
            recipes.computeIfPresent(key,(recipeType, resourceLocationRecipeMap) -> {
                if (resourceLocationRecipeMap instanceof ConcurrentHashMap) {
                    return resourceLocationRecipeMap;
                }
                return new ConcurrentHashMap<>(resourceLocationRecipeMap);
            });
        }
        if (!(this.byName instanceof ConcurrentHashMap)) {
            this.byName = new ConcurrentHashMap<>(this.byName);
        }
    }
}
