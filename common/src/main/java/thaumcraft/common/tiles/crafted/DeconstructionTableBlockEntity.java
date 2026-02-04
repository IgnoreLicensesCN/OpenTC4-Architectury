package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.blocks.crafted.DeconstructionTableBlock;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.getBonusTags;
import static thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.getObjectTags;

public class DeconstructionTableBlockEntity extends TileThaumcraft implements WorldlyContainer, MenuProvider {

    public Aspect currentContainingAspect;
    public int breakTimeRemaining = 0;
    public static final int MAX_BREAK_TIME = 40;
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(1,ItemStack.EMPTY);
    public static final int[] SLOTS = {0};
    public static final int THE_ONLY_SLOT = 0;

    public DeconstructionTableBlockEntity(BlockEntityType<DeconstructionTableBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public DeconstructionTableBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.DECONSTRUCTION_TABLE, blockPos, blockState);
    }

    public boolean isInventoryIndexOutOfBound(int slot) {
        return slot < 0 || slot >= SLOTS.length;
    }
    public void ensureInventoryIndexInBound(int slot) {
        if (isInventoryIndexOutOfBound(slot)) {
            throw new IndexOutOfBoundsException("Index: " + slot);
        }
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);

    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return getObjectTags(itemStack) != null;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return inventory.get(i);
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
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.thaumcraft.deconstruction_table");
    }

    @Override
    public @NotNull DeconstructionTableMenu createMenu(int i, Inventory inventory, Player player) {
        return ;
    }

    public void tick(){
        if (currentContainingAspect != null) {
            breakTimeRemaining = 0;
            return;
        }
        var deconstructingStack = inventory.get(THE_ONLY_SLOT);
        if (deconstructingStack.isEmpty()) {
            breakTimeRemaining = 0;
            return;
        }
        var itemAspects = getObjectTags(deconstructingStack);
        var additionalAspects = getBonusTags(deconstructingStack, itemAspects);
        if (additionalAspects.isEmpty()) {
            breakTimeRemaining = 0;
            return;
        }
        if (breakTimeRemaining > 0){
            breakTimeRemaining-=1;
            return;
        }
        deconstructingStack.shrink(1);
        if (deconstructingStack.isEmpty()){
            inventory.set(THE_ONLY_SLOT,ItemStack.EMPTY);
        }
        var reducedAspects = ResearchManager.reduceToPrimals(additionalAspects);
        if (this.level.random.nextInt(80) < reducedAspects.visSize()) {
            this.currentContainingAspect = reducedAspectsArray[this.level.random.nextInt(reducedAspectsArray.length)];
        }
    }
}
