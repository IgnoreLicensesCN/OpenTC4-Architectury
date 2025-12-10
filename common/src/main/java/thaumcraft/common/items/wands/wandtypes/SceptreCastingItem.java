package thaumcraft.common.items.wands;

import thaumcraft.api.wands.ArcaneCraftingVisDiscountOwner;

public class SceptreCastingItem extends WandCastingItem implements ArcaneCraftingVisDiscountOwner {
    public SceptreCastingItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canApplyFocus() {
        return false;
    }
}
