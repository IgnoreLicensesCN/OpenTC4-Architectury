package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.common.items.wands.ThaumcraftWandCapItem;

public class ThaumiumWandCapItem extends ThaumcraftWandCapItem {//itemWandCap:2
    public ThaumiumWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return .9f;
    }

}
