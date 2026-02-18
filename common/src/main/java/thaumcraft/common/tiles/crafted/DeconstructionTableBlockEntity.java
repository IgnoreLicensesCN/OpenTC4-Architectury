package thaumcraft.common.tiles.crafted;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.tile.TileThaumcraftWithMenu;
import thaumcraft.common.menu.menu.DeconstructionTableMenu;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.DeconstructionTableBlockEntityTagAccessors.BREAK_TIME_ACCESSOR;
import static com.linearity.opentc4.Consts.DeconstructionTableBlockEntityTagAccessors.STORING_ASPECT_ACCESSOR;
import static thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.getBonusAspects;
import static thaumcraft.common.lib.crafting.ThaumcraftCraftingManager.getObjectTags;

public class DeconstructionTableBlockEntity extends TileThaumcraftWithMenu<DeconstructionTableMenu,DeconstructionTableBlockEntity> implements WorldlyContainer {
    public @NotNull Aspect storingAspect = Aspects.EMPTY;
    public int breakTimeRemaining = 0;
    public static final int MAX_BREAK_TIME = 40;
    protected NonNullList<ItemStack> inventory = NonNullList.withSize(1,ItemStack.EMPTY);
    public static final int[] SLOTS = {0};
    public static final int THE_ONLY_SLOT = 0;

    public DeconstructionTableBlockEntity(BlockEntityType<? extends DeconstructionTableBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState,DeconstructionTableMenu::new);
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
        ContainerHelper.loadAllItems(compoundTag, inventory);
        storingAspect = STORING_ASPECT_ACCESSOR.readFromCompoundTag(compoundTag);
        breakTimeRemaining = BREAK_TIME_ACCESSOR.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
        STORING_ASPECT_ACCESSOR.writeToCompoundTag(compoundTag,storingAspect);
        BREAK_TIME_ACCESSOR.writeToCompoundTag(compoundTag,breakTimeRemaining);
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction direction) {
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
    public @NotNull ItemStack getItem(int i) {
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
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.deconstruction_table");//TODO:Separate a new name
    }


    public void tick(){
        if (!storingAspect.isEmpty()) {
            breakTimeRemaining = 0;
            return;
        }
        var deconstructingStack = inventory.get(THE_ONLY_SLOT);
        if (deconstructingStack.isEmpty()) {
            breakTimeRemaining = 0;
            return;
        }
        var itemAspects = getObjectTags(deconstructingStack);
        var additionalAspects = getBonusAspects(deconstructingStack, itemAspects);
        if (additionalAspects.isEmpty()) {
            breakTimeRemaining = 0;
            return;
        }
        if (breakTimeRemaining > 0){
            breakTimeRemaining-=1;
            return;
        }
        breakTimeRemaining = MAX_BREAK_TIME;
        if (Platform.getEnvironment() == Env.SERVER){
            deconstructingStack.shrink(1);
            if (deconstructingStack.isEmpty()){
                inventory.set(THE_ONLY_SLOT,ItemStack.EMPTY);
            }
            var randomSource = this.level != null? this.level.random : RandomSource.createNewThreadLocalInstance();
            var reducedAspects = ResearchManager.reduceToPrimals(additionalAspects);
            if (randomSource.nextInt(80) < reducedAspects.visSize()) {
                this.storingAspect = reducedAspects.randomAspect(randomSource);
            }
        }
        markDirtyAndUpdateSelf();
    }

}
