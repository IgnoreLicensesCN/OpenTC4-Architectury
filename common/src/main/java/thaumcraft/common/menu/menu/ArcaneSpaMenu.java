package thaumcraft.common.menu.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import thaumcraft.common.menu.ThaumcraftGUI;
import thaumcraft.common.menu.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.tiles.crafted.ArcaneSpaBlockEntity;

//TODO:GUI
public class ArcaneSpaMenu extends AbstractThaumcraftMenu<ArcaneSpaBlockEntity> {

    public ArcaneSpaMenu(int containerID,
                         Inventory inventory,
                         ArcaneSpaBlockEntity be) {
        this(ThaumcraftGUI.ARCANE_SPA, containerID, inventory, be);
    }

    public ArcaneSpaMenu(
            MenuType<?> menuType,
            int containerID,
            Inventory inventory,
            ArcaneSpaBlockEntity be
    ) {
        super(menuType, containerID, be, be.getContainerSize());
        addSlot(new Slot(be, , 84, 25));
        addPlayerInventorySlots(inventory);
    }
}
