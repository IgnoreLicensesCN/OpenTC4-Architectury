package thaumcraft.common.menu.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.menu.ThaumcraftGUI;
import thaumcraft.common.menu.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.tiles.crafted.AlchemicalFurnaceBlockEntity;

public class AlchemicalFurnaceMenu extends AbstractThaumcraftMenu<AlchemicalFurnaceBlockEntity> {

    protected final Inventory inventory;
    protected final AlchemicalFurnaceBlockEntity alchemicalFurnace;

    public AlchemicalFurnaceMenu(
            int containerID,
            Inventory inventory,
            AlchemicalFurnaceBlockEntity alchemicalFurnace){
        this(ThaumcraftGUI.ALCHEMICAL_FURNACE,containerID,inventory,alchemicalFurnace);
    }
    public AlchemicalFurnaceMenu(
            @Nullable MenuType<? extends AlchemicalFurnaceMenu> menuType,
            int containerID,
            Inventory inventory,
            AlchemicalFurnaceBlockEntity alchemicalFurnace) {
        super(menuType, containerID,alchemicalFurnace,AlchemicalFurnaceBlockEntity.SLOTS.length);
        this.inventory = inventory;
        this.alchemicalFurnace = alchemicalFurnace;
        this.addSlot(
                new Slot(alchemicalFurnace,AlchemicalFurnaceBlockEntity.ASPECT_GIVEN_ITEM_SLOT, 84, 25)//TODO:Offset
        );
        this.addSlot(
                new Slot(alchemicalFurnace,AlchemicalFurnaceBlockEntity.FUEL_SLOT, 84, 5)//TODO:Offset
        );
        addPlayerInventorySlots(inventory);
    }
}
