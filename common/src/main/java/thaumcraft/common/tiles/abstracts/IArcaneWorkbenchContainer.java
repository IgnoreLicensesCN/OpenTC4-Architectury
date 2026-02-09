package thaumcraft.common.tiles.abstracts;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.ICentiVisContainer;

import java.util.List;

public interface IArcaneWorkbenchContainer {
    @NotNull("null->empty")
    ItemStack getStackInRowAndColumn(int row, int column);
    @NotNull("null->empty")
    ItemStack getStackInWandSlot();
    @NotNull("null->empty")
    List<ItemStack> getInputItemStacks();
    @NotNull("null->empty")
    ItemStack getItem(int slot);
}
