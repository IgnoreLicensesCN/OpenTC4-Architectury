package thaumcraft.common.menu.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import thaumcraft.common.menu.ThaumcraftGUI;
import thaumcraft.common.menu.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.tiles.crafted.vis.FocalManipulatorBlockEntity;

//TODO:GUI
public class FocalManipulatorMenu extends AbstractThaumcraftMenu<FocalManipulatorBlockEntity> {

    public FocalManipulatorMenu(int containerID,
                            Inventory inventory,
                                FocalManipulatorBlockEntity be) {
        this(ThaumcraftGUI.ThaumcraftMenuTypeInstances.FOCAL_MANIPULATOR,containerID,inventory,be);
    }
    public FocalManipulatorMenu(
            MenuType<?> menuType,
            int containerID,
            Inventory inventory,
            FocalManipulatorBlockEntity be
    ) {
        super(menuType, containerID, be, be.getContainerSize());
        addSlot(new Slot(be,0, 84, 25));
        addPlayerInventorySlots(inventory);
    }
}
