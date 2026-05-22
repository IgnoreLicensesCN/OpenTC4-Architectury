package thaumcraft.common.tiles.abstracts;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;

public interface IArcaneWorkbenchContainer {
    @NotNull("null->empty")
    ItemStack getStackInRowAndColumn(@Range(from = 0,to = 2) int row,@Range(from = 0,to = 2) int column);
    @NotNull("null->empty")
    ItemStack getStackInWandSlot();
    @NotNull("null->empty")
    List<ItemStack> getInputItemStacks();
    @NotNull("null->empty")
    ItemStack getItem(int slot);
}
