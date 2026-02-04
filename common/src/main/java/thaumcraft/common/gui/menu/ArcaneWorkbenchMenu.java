package thaumcraft.common.gui.menu;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.common.gui.ThaumcraftGUI;
import thaumcraft.common.gui.slot.ArcaneWorkbenchOutputSlot;
import thaumcraft.common.gui.slot.ArcaneWorkbenchWandSlot;
import thaumcraft.common.inventory.ArcaneWorkbenchResultContainer;
import thaumcraft.common.tiles.crafted.ArcaneWorkbenchBlockEntity;

import java.util.List;
import java.util.Optional;

import static thaumcraft.api.ThaumcraftApi.getIArcaneRecipes;

public class ArcaneWorkbenchMenu extends AbstractContainerMenu {


    protected final @NotNull ArcaneWorkbenchBlockEntity workbench;
    protected final CraftingContainer craftingContainer;
    protected final ArcaneWorkbenchResultContainer resultContainer = new ArcaneWorkbenchResultContainer();
    protected final Player player;

    public ArcaneWorkbenchMenu(
            int containerID,
            Inventory inventory,
            ArcaneWorkbenchBlockEntity workbench){
        this(ThaumcraftGUI.ARCANE_WORKBENCH,containerID,inventory,workbench);
    }
    public ArcaneWorkbenchMenu(
            MenuType<ArcaneWorkbenchMenu> menuType,
            int containerID,
            Inventory inventory,
            @NotNull ArcaneWorkbenchBlockEntity workbench) {
        super(menuType, containerID);
        this.workbench = workbench;
        this.player = inventory.player;

        this.craftingContainer = new CraftingContainer(){

            @Override
            public void fillStackedContents(StackedContents stackedContents) {
                for (int i:ArcaneWorkbenchBlockEntity.INPUT_SLOTS) {
                    ItemStack stack = getItem(i);
                    if (!stack.isEmpty()) {
                        stackedContents.accountStack(stack);
                    }
                }
            }

            @Override
            public void clearContent() {
                workbench.clearContent();
            }

            @Override
            public int getContainerSize() {
                return 9;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public @NotNull ItemStack getItem(int i) {
                return workbench.getItem(i);
            }

            @Override
            public @NotNull ItemStack removeItem(int i, int j) {
                return workbench.removeItem(i, j);
            }

            @Override
            public @NotNull ItemStack removeItemNoUpdate(int i) {
                return workbench.removeItemNoUpdate(i);
            }

            @Override
            public void setItem(int i, ItemStack itemStack) {
                workbench.setItem(i, itemStack);
            }

            @Override
            public void setChanged() {
                workbench.setChanged();
            }

            @Override
            public boolean stillValid(Player player) {
                return workbench.stillValid(player);
            }

            @Override
            public int getWidth() {
                return 3;
            }

            @Override
            public int getHeight() {
                return 3;
            }

            @Override
            public @NotNull List<ItemStack> getItems() {
                return List.copyOf(workbench.getCraftingGridItems());
            }
        };

        this.addSlot(
                new ArcaneWorkbenchWandSlot(
                        workbench,
                        ArcaneWorkbenchBlockEntity.WAND_SLOT,
                        124, 25)//TODO:Offset
        );

        this.addSlot(new ArcaneWorkbenchOutputSlot(inventory.player,
                this.workbench,
                this.craftingContainer,
                this.resultContainer,
                ArcaneWorkbenchBlockEntity.WAND_SLOT + 1, 124, 35)
        );

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                this.addSlot(new Slot(this.craftingContainer, k + j * 3, 30 + k * 18, 17 + j * 18));
            }
        }

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (int j = 0; j < 9; j++) {
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
        }
    }



    @Override
    public void slotsChanged(Container container) {
        slotChangedCraftingGrid(this, this.workbench.getLevel(),
                this.player, this.craftingContainer, this.resultContainer);
    }


    protected static void slotChangedCraftingGrid(
            ArcaneWorkbenchMenu arcaneWorkbenchMenu,
            Level level,
            Player player,
            CraftingContainer craftingContainer,
            ArcaneWorkbenchResultContainer resultContainer
    ) {
        assert level != null;
        if (!level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            ItemStack itemStack = ItemStack.EMPTY;
            var aspects = CentiVisList.of();
            var workbench = arcaneWorkbenchMenu.workbench;
            for (var arcaneRecipe: getIArcaneRecipes()) {
                if (arcaneRecipe.matches(workbench,level,serverPlayer)){
                    itemStack = arcaneRecipe.getCraftingResult(workbench);
                    if (!itemStack.isEmpty()){
                        aspects = arcaneRecipe.getAspects(workbench);
                        break;
                    }
                }
            }
            if (itemStack.isEmpty()) {
                aspects = CentiVisList.of();
                Optional<CraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingContainer, level);
                if (optional.isPresent()) {
                    CraftingRecipe craftingRecipe = optional.get();
                    if (resultContainer.setRecipeUsed(level, serverPlayer, craftingRecipe)) {
                        ItemStack itemStack2 = craftingRecipe.assemble(craftingContainer, level.registryAccess());
                        if (itemStack2.isItemEnabled(level.enabledFeatures())) {
                            itemStack = itemStack2;
                        }
                    }
                }
            }

            resultContainer.setItem(0, itemStack);
            resultContainer.setCostsAspects(aspects);
            arcaneWorkbenchMenu.setRemoteSlot(0, itemStack);
            serverPlayer.connection
                    .send(
                            new ClientboundContainerSetSlotPacket(
                                    arcaneWorkbenchMenu.containerId,
                                    arcaneWorkbenchMenu.incrementStateId(),
                                    0,
                                    itemStack)
                    );
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i == 0) {
                itemStack2.getItem().onCraftedBy(itemStack2, this.workbench.getLevel(), player);
                if (!this.moveItemStackTo(itemStack2, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);
            } else if (i >= 10 && i < 46) {
                if (!this.moveItemStackTo(itemStack2, 1, 10, false)) {
                    if (i < 37) {
                        if (!this.moveItemStackTo(itemStack2, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemStack2, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemStack2, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
            if (i == 0) {
                player.drop(itemStack2, false);
            }
        }

        return itemStack;
    }


    @Override
    public boolean stillValid(Player player) {
        var blockPos = workbench.getBlockPos();
        if (player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) <= 64.0){
            return true;
        }
        var level = workbench.getLevel();
        return level != null && level.getBlockEntity(blockPos) == workbench;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return slot.container != this.resultContainer && super.canTakeItemForPickAll(itemStack, slot);
    }

    @Override
    public void removed(Player player) {
        //item should be in workbench
    }
}
