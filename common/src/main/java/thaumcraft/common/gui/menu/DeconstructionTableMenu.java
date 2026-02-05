package thaumcraft.common.gui.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.gui.ThaumcraftGUI;
import thaumcraft.common.gui.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.gui.slot.DeconstructionTableResultSlot;
import thaumcraft.common.tiles.crafted.DeconstructionTableBlockEntity;

public class DeconstructionTableMenu extends AbstractThaumcraftMenu<DeconstructionTableBlockEntity> {
    protected final Inventory inventory;
    protected final DeconstructionTableBlockEntity deconstructionTable;

    public DeconstructionTableMenu(
            int containerID,
            Inventory inventory,
            DeconstructionTableBlockEntity deconstructionTable){
        this(ThaumcraftGUI.DECONSTRUCTION_TABLE,containerID,inventory,deconstructionTable);
    }
    public DeconstructionTableMenu(
            @Nullable MenuType<DeconstructionTableMenu> menuType,
            int containerID,
            Inventory inventory,
            DeconstructionTableBlockEntity deconstructionTable) {
        super(menuType, containerID,deconstructionTable,DeconstructionTableBlockEntity.SLOTS.length);
        this.inventory = inventory;
//        this.containerLevelAccess = containerLevelAccess;
        this.deconstructionTable = deconstructionTable;
        this.addSlot(
                new Slot(deconstructionTable,DeconstructionTableBlockEntity.THE_ONLY_SLOT, 84, 25)//TODO:Offset
        );
        this.addSlot(
                new DeconstructionTableResultSlot(deconstructionTable,1,124,25)//TODO:Offset
        );
        addPlayerInventorySlots(inventory);
    }
}
