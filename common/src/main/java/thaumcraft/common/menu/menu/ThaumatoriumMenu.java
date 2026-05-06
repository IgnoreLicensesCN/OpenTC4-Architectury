package thaumcraft.common.menu.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import thaumcraft.common.menu.ThaumcraftGUI;
import thaumcraft.common.menu.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.tiles.crafted.essentiabe.ThaumatoriumBlockEntity;

//TODO:set recipe
public class ThaumatoriumMenu extends AbstractThaumcraftMenu<ThaumatoriumBlockEntity> {
    public ThaumatoriumMenu(int containerID,
                            Inventory inventory,
                            ThaumatoriumBlockEntity thaumatoriumBlockEntity) {
        this(ThaumcraftGUI.THAUMATORIUM,containerID,inventory,thaumatoriumBlockEntity);
    }
    public ThaumatoriumMenu(
            MenuType<?> menuType,
            int containerID,
            Inventory inventory,
            ThaumatoriumBlockEntity thaumatoriumBlockEntity
    ) {
        super(menuType, containerID, thaumatoriumBlockEntity, thaumatoriumBlockEntity.getContainerSize());
        addSlot(new Slot(thaumatoriumBlockEntity,ThaumatoriumBlockEntity.INPUT_SLOT, 84, 25));
        addPlayerInventorySlots(inventory);
    }
}
