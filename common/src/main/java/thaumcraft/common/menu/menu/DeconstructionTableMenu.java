package thaumcraft.common.menu.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.menu.ThaumcraftGUI;
import thaumcraft.common.menu.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.menu.slot.DeconstructionTableResultSlot;
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
            @Nullable MenuType<? extends DeconstructionTableMenu> menuType,
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
