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
import thaumcraft.common.gui.menu.abstracts.AbstractThaumcraftMenu;
import thaumcraft.common.gui.slot.ArcaneWorkbenchOutputSlot;
import thaumcraft.common.gui.slot.ArcaneWorkbenchWandSlot;
import thaumcraft.common.inventory.ArcaneWorkbenchResultContainer;
import thaumcraft.common.tiles.crafted.ArcaneWorkbenchBlockEntity;

import java.util.List;
import java.util.Optional;

import static thaumcraft.api.ThaumcraftApi.getIArcaneRecipes;

public class ArcaneWorkbenchMenu extends AbstractThaumcraftMenu<ArcaneWorkbenchBlockEntity> {

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
        super(menuType, containerID,workbench,ArcaneWorkbenchBlockEntity.INPUT_AND_WAND_SLOTS.length);
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

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                this.addSlot(new Slot(this.craftingContainer,k+j * 3, 30 + k * 18, 17 + j * 18));
            }
        }

        this.addSlot(new ArcaneWorkbenchOutputSlot(inventory.player,
                this.blockEntity,
                this.craftingContainer,
                this.resultContainer,
                ArcaneWorkbenchBlockEntity.WAND_SLOT + 1, 124, 35)
        );

        addPlayerInventorySlots(inventory);
    }

    @Override
    public void slotsChanged(Container container) {
        slotChangedCraftingGrid(this, this.blockEntity.getLevel(),
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
            var workbench = arcaneWorkbenchMenu.blockEntity;
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
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            int containerSlotCount = ArcaneWorkbenchBlockEntity.INPUT_AND_WAND_SLOTS.length/* 你的容器槽位数 */;

            if (index < containerSlotCount) {
                // 容器 → 玩家背包
                if (!this.moveItemStackTo(stack,
                        containerSlotCount,
                        this.slots.size(),
                        true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 玩家背包 → 容器
                if (!this.moveItemStackTo(stack,
                        0,
                        containerSlotCount,
                        false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return ret;
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
