package thaumcraft.common.menu.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
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
                //TODO:Offset
                new Slot(alchemicalFurnace,AlchemicalFurnaceBlockEntity.ASPECT_GIVEN_ITEM_SLOT, 84, 25){
                    @Override
                    public boolean mayPlace(ItemStack itemStack) {
                        return AlchemicalFurnaceBlockEntity.canBurnIntoAspect(itemStack);
                    }
                }
        );
        this.addSlot(
                //TODO:Offset
                new Slot(alchemicalFurnace,AlchemicalFurnaceBlockEntity.FUEL_SLOT, 84, 5){
                    @Override
                    public boolean mayPlace(ItemStack itemStack) {
                        return AlchemicalFurnaceBlockEntity.canBurnAsFuel(itemStack);
                    }
                }
        );
        addPlayerInventorySlots(inventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return super.quickMoveStack(player, index);
    }
}
