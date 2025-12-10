package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.common.items.wands.ThaumcraftWandCapItem;

public class IronWandCapItem extends ThaumcraftWandCapItem {//itemWandCap:0
    public IronWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 1.1f;
    }

}
