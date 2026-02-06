package thaumcraft.common.gui.slot;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.researchtable.IResearchTableAspectEditTool;

public class ResearchTableInkSlot extends Slot {
    protected final Level atLevel;
    protected final BlockPos pos;
    public ResearchTableInkSlot(Container container, int i, int j, int k,Level atLevel, BlockPos pos) {
        super(container, i, j, k);
        this.atLevel = atLevel;
        this.pos = pos;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.getItem() instanceof IResearchTableAspectEditTool writeTool
                && writeTool.canPlaceIntoResearchTable(atLevel, pos, itemStack);
    }
}
