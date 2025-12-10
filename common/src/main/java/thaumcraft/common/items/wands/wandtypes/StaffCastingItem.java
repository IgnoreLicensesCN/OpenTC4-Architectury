package thaumcraft.common.items.wands;

public class StaffCastingItem extends WandCastingItem{
    public StaffCastingItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canInsertIntoArcaneCraftingTable() {
        return false;
    }
}
