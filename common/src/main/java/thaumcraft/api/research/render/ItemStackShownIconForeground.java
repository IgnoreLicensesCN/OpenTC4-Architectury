package thaumcraft.api.research.render;

import net.minecraft.world.item.ItemStack;

public class ItemStackShownIconForeground extends ShownIconForeground {

    protected final ItemStack stack;
    public ItemStackShownIconForeground(ItemStack stack) {
        this.stack = stack;
    }
}
