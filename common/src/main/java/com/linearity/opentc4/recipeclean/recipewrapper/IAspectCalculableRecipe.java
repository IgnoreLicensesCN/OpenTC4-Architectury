package com.linearity.opentc4.recipeclean.recipewrapper;


import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.CentiVisList;

import java.util.List;

public interface IAspectCalculableRecipe {
    //true if this recipe supports aspect calculation
    boolean supportsAspectCalculation();

    //note that we just want count and item in stack not CompoundTag
    @Nullable("when supportsAspectCalculation returns false")
    List<List<ItemStack>> getAspectCalculationInputs();
    @Nullable("when supportsAspectCalculation returns false")
    ItemStack getAspectCalculationOutput();
    @Nullable("when supportsAspectCalculation returns false")
    List<List<ItemStack>> getAspectCalculationRemaining();

    @Nullable("when supportsAspectCalculation returns false")
    AspectList<Aspect> getAspectCalculationAspectsList();
    @Nullable("when supportsAspectCalculation returns false")
    CentiVisList<Aspect> getAspectCalculationCentiVisList();

}
