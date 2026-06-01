package thaumcraft.common.tiles.crafted;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.tile.TileThaumcraftWithMenu;
import thaumcraft.api.visnet.IVisNetChargeRelayChargeableContainer;
import thaumcraft.api.wands.IArcaneCraftingWandItem;
import thaumcraft.api.wands.ICentiVisContainerItem;
import thaumcraft.common.menu.menu.ArcaneWorkbenchMenu;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IArcaneWorkbenchContainer;
import thaumcraft.common.tiles.abstracts.IDefaultWorldlyContainer;

import java.util.List;

//TODO:Cache recipes to make this faster?
public class ArcaneWorkbenchBlockEntity extends TileThaumcraftWithMenu<ArcaneWorkbenchMenu,ArcaneWorkbenchBlockEntity>
        implements
        IDefaultWorldlyContainer,
        ExtendedMenuProvider,
        IArcaneWorkbenchContainer,
        IVisNetChargeRelayChargeableContainer {
    public static final int WAND_SLOT = 9;
    public static final int[] INPUT_SLOTS = {0,1,2,3,4,5,6,7,8};
    public static final int[] INPUT_AND_WAND_SLOTS = {0,1,2,3,4,5,6,7,8,WAND_SLOT};
    public static final int[] WAND_SLOT_ARR = {WAND_SLOT};
    public static final int SIZE = INPUT_AND_WAND_SLOTS.length;
    public static final int INPUT_SIZE = INPUT_AND_WAND_SLOTS.length;
    protected final NonNullList<ItemStack> inventory = NonNullList.withSize(INPUT_SIZE, ItemStack.EMPTY);
    protected final List<ItemStack> inputSlotsView = inventory.subList(0,INPUT_SLOTS.length);
    public ArcaneWorkbenchBlockEntity(BlockEntityType<? extends ArcaneWorkbenchBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState,ArcaneWorkbenchMenu::new);
    }
    public ArcaneWorkbenchBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ARCANE_WORKBENCH, blockPos, blockState);
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
            if (item instanceof IArcaneCraftingWandItem craftingWand
            && craftingWand.canInsertIntoArcaneCraftingTable(itemStack)) {
                return true;
            }
            return false;
        }
        return true;
    }
    @Override
    public @NotNull ItemStack getItem(int i) {
        return IDefaultWorldlyContainer.super.getItem(i);
    }


    @Override
    @NotNull("null->empty")
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if ((0 <= row && row <= 2) && (0 <= column && column <= 2)) {
            int var3 = row + column * 3;
            return this.getItem(var3);
        } else {
            throw new IndexOutOfBoundsException("row: " + row + ", column: " + column + "out of bound");
        }
    }
    @Override
    @NotNull("null->empty")
    public ItemStack getStackInWandSlot(){
        return inventory.get(WAND_SLOT);
    }

    @Override
    @NotNull("null->empty")
    public ItemStack getStackToCharge() {
        return getStackInWandSlot();
    }

    @Override
    public @NotNull List<ItemStack> getInputItemStacks() {
        return inputSlotsView;
    }

    public List<ItemStack> getCraftingGridItems() {
        return List.copyOf(inventory.subList(0, WAND_SLOT));
    }


    public boolean canWandSatisfyCentiVisConsumption(CentiVisList<Aspect> centiVisList){
        if (centiVisList.isEmpty()){
            return true;
        }
        var wandStack = getStackInWandSlot();
        if (wandStack.isEmpty()){
            return false;
        }
        var wandItem = wandStack.getItem();
        if (!(wandItem instanceof ICentiVisContainerItem<? extends Aspect> visContainerNotCasted)){
            return false;
        }
        var visContainer = (ICentiVisContainerItem<Aspect>) visContainerNotCasted;
        var visOwning = visContainer.getAllCentiVisOwning(wandStack);
        return !centiVisList.forEachWithBreak((asp, amount) -> visOwning.get(asp) < amount);
    }
    public boolean consumeCentiVisNoThrow(Player player, CentiVisList<Aspect> centiVisList) {
        if (centiVisList.isEmpty()){
            return true;
        }
        var wandStack = getStackInWandSlot();
        if (wandStack.isEmpty()){
            OpenTC4.LOGGER.warn("wand not found but centiVis cost required:" + centiVisList
                    + " player:" + player.getStringUUID()
                    + "(" + player.getGameProfile().getName() + ")"
                    + " blockEntityPos:" + this.getBlockPos()
            );
            return false;
        }
        var wandItem = wandStack.getItem();
        if (!(wandItem instanceof ICentiVisContainerItem<? extends Aspect> centiVisContainerNotCasted)){
            OpenTC4.LOGGER.warn("wand not found but centiVis cost required:" + centiVisList
                    + " player:" + player.getStringUUID()
                    + "(" + player.getGameProfile().getName() + ")"
                    + " blockEntityPos:" + this.getBlockPos()
            );
            return false;
        }
        var centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerNotCasted;
        if (!centiVisContainer.consumeAllCentiVisWithoutModifier(wandStack,centiVisList,true,player instanceof ServerPlayer)){
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

    @Override
    public int[] getSlots() {
        return INPUT_AND_WAND_SLOTS;
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

}
