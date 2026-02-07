package thaumcraft.common.gui.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.gui.ThaumcraftGUI;
import thaumcraft.common.gui.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.gui.slot.ResearchTableInkSlot;
import thaumcraft.common.gui.slot.ResearchTableNoteSlot;
import thaumcraft.common.tiles.crafted.ResearchTableBlockEntity;

public class ResearchTableMenu extends AbstractThaumcraftMenu<ResearchTableBlockEntity> {

    protected final @NotNull ResearchTableBlockEntity table;
    protected final Player player;
    public ResearchTableMenu(
            int containerID,
            Inventory inventory,
            ResearchTableBlockEntity table){
        this(ThaumcraftGUI.RESEARCH_TABLE,containerID,inventory,table);
    }

    public ResearchTableMenu(
            MenuType<ResearchTableMenu> menuType,
            int containerID,
            Inventory inventory,
            @NotNull ResearchTableBlockEntity table) {
        super(menuType, containerID,table,ResearchTableBlockEntity.SLOTS.length);
        this.table = table;
        this.player = inventory.player;

        this.addSlot(new ResearchTableInkSlot(this.table, 0, 56, 17,table.getLevel(),table.getBlockPos()));//TODO:Offset
        this.addSlot(new ResearchTableNoteSlot(this.table, 1, 96, 17,table.getLevel(),table.getBlockPos()));//TODO:Offset
        addPlayerInventorySlots(inventory);
    }

    //TODO:place aspect on research note,combine aspects and copy researches
}
