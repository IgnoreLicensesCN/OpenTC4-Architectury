package com.linearity.opentc4.recipeclean.recipewrapper;

import com.linearity.opentc4.annotations.JEILikeOnly;
import net.minecraft.world.item.ItemStack;

@JEILikeOnly
public interface RecipeInAndOutSampler {
    //you know we can just get inputs
    @JEILikeOnly
    ItemStack[] getInputSample();

    //i would suggest to use with above
    //warning:no need to consider match
    @JEILikeOnly
    ItemStack[] getOutputSample(ItemStack[] inputSample);
}
