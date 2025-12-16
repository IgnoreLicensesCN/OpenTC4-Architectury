package com.linearity.opentc4.recipeclean.recipewrapper;

import net.minecraft.world.item.ItemStack;

public interface RecipeInAndOutSampler {
    //you know we can just get inputs
    ItemStack[] getInputSample();
    //in fact,not all,just as many as possible.
    //calculate aspect needs this.
    ItemStack[][] getAllInputSample();

    //i would suggest to use with above
    //warning:no need to consider match
    ItemStack[] getOutputSample(ItemStack[] inputSample);
}
