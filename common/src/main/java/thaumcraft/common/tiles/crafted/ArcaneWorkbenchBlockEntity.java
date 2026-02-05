package thaumcraft.common.tiles.crafted;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.tile.TileThaumcraftWithMenu;
import thaumcraft.api.wands.IArcaneCraftingWand;
import thaumcraft.api.wands.ICentiVisContainer;
import thaumcraft.common.gui.menu.ArcaneWorkbenchMenu;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import java.util.List;

public class ArcaneWorkbenchBlockEntity extends TileThaumcraftWithMenu<ArcaneWorkbenchMenu,ArcaneWorkbenchBlockEntity> implements WorldlyContainer, ExtendedMenuProvider {
    public static final int SIZE = 11;
    public static final int INPUT_SIZE = 11;
    public static final int WAND_SLOT = 9;
    public static final int[] INPUT_SLOTS = {0,1,2,3,4,5,6,7,8};
    public static final int[] INPUT_AND_WAND_SLOTS = {WAND_SLOT,0,1,2,3,4,5,6,7,8};
    public static final int[] WAND_SLOT_ARR = {WAND_SLOT};
    protected final NonNullList<ItemStack> inventory = NonNullList.withSize(INPUT_SIZE, ItemStack.EMPTY);
    public ArcaneWorkbenchBlockEntity(BlockEntityType<ArcaneWorkbenchBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState,ArcaneWorkbenchMenu::new);
    }
    public ArcaneWorkbenchBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ARCANE_WORKBENCH, blockPos, blockState);
    }
    @Override
    public int getContainerSize() {
        return SIZE;
    }
    @Override
    public boolean isEmpty() {
        for (int i:INPUT_AND_WAND_SLOTS) {
            if (!inventory.get(i).isEmpty()) return false;
        }
        return true;
    }

    public boolean isInventoryIndexOutOfBound(int slot) {
        return slot < 0 || slot >= INPUT_AND_WAND_SLOTS.length;
    }
    public void ensureInventoryIndexInBound(int slot) {
        if (isInventoryIndexOutOfBound(slot)) {
            throw new IndexOutOfBoundsException("Index: " + slot);
        }
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        ensureInventoryIndexInBound(slot);
        return inventory.get(slot);
    }

    @Override
    @NotNull
    public ItemStack removeItem(int slot, int amount) {
        ensureInventoryIndexInBound(slot);
        ItemStack stack = getItem(slot);
        if (stack.getCount() <= amount) {
            setItem(slot, ItemStack.EMPTY);
            setChanged();
            return stack;
        }
        else {
            stack.shrink(amount);
            stack = stack.copy();
            stack.setCount(amount);
            setChanged();
            return stack;
        }
    }

    @Override
    @NotNull
    public ItemStack removeItemNoUpdate(int i) {
        var stack = getItem(i);
        setItem(i, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        inventory.set(i, itemStack);
        setChanged();

        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        ContainerHelper.loadAllItems(compoundTag, inventory);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction direction) {
        if (direction == Direction.UP) {
            return WAND_SLOT_ARR;
        }
        return INPUT_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        if (direction == Direction.UP || slot == WAND_SLOT) {
            var item = itemStack.getItem();
            if (item instanceof IArcaneCraftingWand craftingWand
            && craftingWand.canInsertIntoArcaneCraftingTable(itemStack)) {
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return true;
    }

    public ItemStack getStackInRowAndColumn(int par1, int par2) {
        if (par1 >= 0 && par1 < 3) {
            int var3 = par1 + par2 * 3;
            return this.getItem(var3);
        } else {
            return null;
        }
    }

    public List<ItemStack> getCraftingGridItems() {
        return List.copyOf(inventory.subList(0, WAND_SLOT));
    }

    public ItemStack getWand(){
        return inventory.get(WAND_SLOT);
    }

    public boolean canWandSatisfyCentiVisConsumption(CentiVisList<Aspect> centiVisList){
        if (centiVisList.isEmpty()){
            return true;
        }
        var wandStack = getWand();
        if (wandStack.isEmpty()){
            return false;
        }
        var wandItem = wandStack.getItem();
        if (!(wandItem instanceof ICentiVisContainer visContainer)){
            return false;
        }
        var visOwning = visContainer.getAllCentiVisOwning(wandStack);
        for (var centiVisAndAmount:centiVisList.getAspects().entrySet()){
            var aspect = centiVisAndAmount.getKey();
            var amount = centiVisAndAmount.getValue();
            if (visOwning.getOrDefault(aspect,0) < amount){
                return false;
            }
        }
        return true;
    }
    public boolean consumeCentiVisNoThrow(Player player, CentiVisList<Aspect> centiVisList) {
        if (centiVisList.isEmpty()){
            return true;
        }
        var wandStack = getWand();
        if (wandStack.isEmpty()){
            OpenTC4.LOGGER.warn("wand not found but centiVis cost required:" + centiVisList
                    + " player:" + player.getStringUUID()
                    + "(" + player.getGameProfile().getName() + ")"
                    + " blockEntityPos:" + this.getBlockPos()
            );
            return false;
        }
        var wandItem = wandStack.getItem();
        if (!(wandItem instanceof ICentiVisContainer centiVisContainer)){
            OpenTC4.LOGGER.warn("wand not found but centiVis cost required:" + centiVisList
                    + " player:" + player.getStringUUID()
                    + "(" + player.getGameProfile().getName() + ")"
                    + " blockEntityPos:" + this.getBlockPos()
            );
            return false;
        }
        if (!centiVisContainer.consumeAllCentiVisWithoutModifier(wandStack,centiVisList,true)){
            OpenTC4.LOGGER.warn("wand vis not enough but centiVis cost required:" + centiVisList
                    + " required" + centiVisList
                    + " player:" + player.getStringUUID()
                    + "(" + player.getGameProfile().getName() + ")"
                    + " blockEntityPos:" + this.getBlockPos()
            );
            return false;
        }
        return true;
    }


    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.arcane_workbench");//TODO:Separate a new name
    }

}
