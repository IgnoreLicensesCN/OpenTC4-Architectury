package com.linearity.opentc4.utils.collectionlike;

import net.minecraft.world.item.ItemStack;

public class UpgradePair<A> extends WeightedSimplePair<A, ItemStack> {
    public UpgradePair(A a, ItemStack stack, int weight) {
        super(a, stack, weight);
    }

    public A getUpgradeType(){
        return a();
    }
    public ItemStack getUpgradeStack(){
        return b();
    }
}
