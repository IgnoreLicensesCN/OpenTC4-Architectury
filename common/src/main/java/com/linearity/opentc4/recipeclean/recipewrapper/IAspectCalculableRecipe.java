package com.linearity.opentc4.recipeclean.recipewrapper;


import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.CentiVisList;

import java.util.List;
import java.util.function.Function;

public interface IAspectCalculableRecipe {
    //true if this recipe supports aspect calculation
    default boolean supportsAspectCalculation(){
        return false;
    };

    //note that we just want count and item in stack not CompoundTag
    //inner List<ItemStack>:what stack could be for the slot,called probableStacks
    @NotNull
    default List<List<ItemStack>> getAspectCalculationInputs(){
        throw new RuntimeException("check supportsAspectCalculation() first!");
    }
    @NotNull
    default ItemStack getAspectCalculationOutput(){
        throw new RuntimeException("check supportsAspectCalculation() first!");
    }
    @NotNull
    default List<List<Function<ItemStack,ItemStack>>> getAspectCalculationRemaining(){
        throw new RuntimeException("check supportsAspectCalculation() first!");
    }

    @NotNull
    default AspectList<Aspect> getAspectCalculationAspectsList(){
        throw new RuntimeException("check supportsAspectCalculation() first!");
    }//of course only for input(no aspects output for now)
    @NotNull
    default CentiVisList<Aspect> getAspectCalculationCentiVisList(){
        throw new RuntimeException("check supportsAspectCalculation() first!");
    }//of course only for input(no aspects output for now)

}
