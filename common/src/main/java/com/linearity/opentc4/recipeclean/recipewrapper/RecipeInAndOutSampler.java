package com.linearity.opentc4.recipeclean.recipewrapper;

import net.minecraft.world.item.ItemStack;

public interface RecipeInAndOutSampler {
    //you know we can just get inputs
    ItemStack[] getInputSample();
}
