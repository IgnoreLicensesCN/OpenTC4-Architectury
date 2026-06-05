package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.tiles.TileThaumcraft;
import thaumcraft.common.tiles.abstracts.IDefaultWorldlyContainer;

import static com.linearity.opentc4.Consts.AbstractPedestalBlockEntityTagAccessors.STORED_ITEM;

public abstract class AbstractPedestalBlockEntity extends TileThaumcraft implements IDefaultWorldlyContainer {

    public AbstractPedestalBlockEntity(BlockEntityType<? extends AbstractPedestalBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public static final int[] SLOTS = {0};

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        inventory.clear();
        inventory.set(0, STORED_ITEM.readFromCompoundTag(compoundTag));
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        STORED_ITEM.writeToCompoundTag(compoundTag,inventory.getFirst());
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public int[] getSlots() {
        return SLOTS;
    }
}
