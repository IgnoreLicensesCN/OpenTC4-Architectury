package thaumcraft.common.menu.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.menu.ThaumcraftGUI;
import thaumcraft.common.menu.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.tiles.crafted.ArcaneBoreBlockEntity;

//TODO
public class ArcaneBoreMenu extends AbstractThaumcraftMenu<ArcaneBoreBlockEntity> {
    public ArcaneBoreMenu(@Nullable MenuType<? extends ArcaneBoreMenu> menuType, int i, ArcaneBoreBlockEntity blockEntity, int interactingContainerSize) {
        super(menuType, i, blockEntity, interactingContainerSize);
    }
    public ArcaneBoreMenu(int containerID,
                            Inventory inventory,
                          ArcaneBoreBlockEntity blockEntity) {
        this(ThaumcraftGUI.ARCANE_BORE,containerID,blockEntity,blockEntity.getContainerSize());
//        addSlot(new Slot(thaumatoriumBlockEntity,ThaumatoriumBlockEntity.INPUT_SLOT, 84, 25));
        addPlayerInventorySlots(inventory);
    }

}
