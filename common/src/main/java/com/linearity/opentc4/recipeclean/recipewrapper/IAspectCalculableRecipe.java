package com.linearity.opentc4.recipeclean.recipewrapper;


import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.CentiVisList;

import java.util.List;
import java.util.function.Function;

public interface IAspectCalculableRecipe {
    //true if this recipe supports aspect calculation
    boolean supportsAspectCalculation();

    //note that we just want count and item in stack not CompoundTag
    @NotNull
    List<List<ItemStack>> getAspectCalculationInputs();
    @NotNull
    ItemStack getAspectCalculationOutput();
    @NotNull
    List<List<Function<ItemStack,ItemStack>>> getAspectCalculationRemaining();

    @NotNull
    AspectList<Aspect> getAspectCalculationAspectsList();//of course only for input(no aspects output for now)
    @NotNull
    CentiVisList<Aspect> getAspectCalculationCentiVisList();//of course only for input(no aspects output for now)

}
