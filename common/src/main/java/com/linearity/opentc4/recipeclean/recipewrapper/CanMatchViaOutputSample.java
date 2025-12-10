package com.linearity.opentc4.recipeclean.recipewrapper;

import net.minecraft.world.item.ItemStack;

//if an itemstack can be output,matchViaOutput should return "true"
//or,false
public interface CanMatchViaOutputSample {

    boolean matchViaOutput(ItemStack res);
}
