package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.CrucibleRecipeResourceLocationTagAccessor;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.crafting.crucible.CrucibleRecipe;

import static thaumcraft.api.crafting.crucible.CrucibleRecipe.CRUCIBLE_RECIPES_VIEW;

public class NullableCrucibleRecipeAccessor extends CompoundTagAccessor<CrucibleRecipe> {
    private final CrucibleRecipeResourceLocationTagAccessor internalAccessor;
    public NullableCrucibleRecipeAccessor(String tagKey) {
        super(tagKey);
        this.internalAccessor = new CrucibleRecipeResourceLocationTagAccessor(tagKey);
    }

    @Override
    @Nullable
    public CrucibleRecipe readFromCompoundTag(CompoundTag tag) {
        var recipeID = internalAccessor.readFromCompoundTag(tag);
        if (recipeID == null) {
            return null;
        }
        return CRUCIBLE_RECIPES_VIEW.get(recipeID);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, CrucibleRecipe value) {
        internalAccessor.writeToCompoundTag(tag,value.recipeID);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return internalAccessor.compoundTagHasKey(tag);
    }
}
