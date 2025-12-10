package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.common.items.wands.ThaumcraftWandCapItem;

public class VoidWandCapItem extends ThaumcraftWandCapItem {//itemWandCap:7
    public VoidWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return .8f;
    }

}
