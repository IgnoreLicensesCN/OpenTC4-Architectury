package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.common.items.wands.ThaumcraftWandCapItem;

public class GoldWandCapItem extends ThaumcraftWandCapItem {//itemWandCap:1
    public GoldWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 1.f;
    }

}
