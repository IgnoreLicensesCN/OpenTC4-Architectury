package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.IAspectReducibleToPrimal;
import thaumcraft.common.tiles.TileThaumcraftWithMenu;
import thaumcraft.common.menu.menu.DeconstructionTableMenu;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IDefaultWorldlyContainer;

import static com.linearity.opentc4.Consts.DeconstructionTableBlockEntityTagAccessors.BREAK_TIME_ACCESSOR;
import static com.linearity.opentc4.Consts.DeconstructionTableBlockEntityTagAccessors.STORING_ASPECT_ACCESSOR;
import static thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter.*;
import static thaumcraft.api.listeners.aspects.item.bonus.ItemBonusAspectCalculator.getBonusAspects;

public class DeconstructionTableBlockEntity
        extends TileThaumcraftWithMenu<DeconstructionTableMenu,DeconstructionTableBlockEntity>
        implements IDefaultWorldlyContainer {
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

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        ContainerHelper.loadAllItems(compoundTag, inventory);
        storingAspect = STORING_ASPECT_ACCESSOR.readFromCompoundTag(compoundTag);
        breakTimeRemaining = BREAK_TIME_ACCESSOR.readIntFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
        STORING_ASPECT_ACCESSOR.writeToCompoundTag(compoundTag,storingAspect);
        BREAK_TIME_ACCESSOR.writeIntToCompoundTag(compoundTag,breakTimeRemaining);
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public int @NotNull [] getSlots() {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        if (this.level == null){return false;}
        if (this.level.isClientSide()) {return getBasicAspectsClient(itemStack.getItem()) != null;}
        return getBasicAspectsServer(itemStack.getItem()) != null;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.deconstruction_table");//TODO:Separate a new name
    }

    @SuppressWarnings("SequencedCollectionMethodCanBeUsed")//will cause lack of design(we're using psf int now)
    public void tick(){
        if (this.level == null){return;}
        if (!storingAspect.isEmpty()) {
            breakTimeRemaining = 0;
            return;
        }
        var deconstructingStack = inventory.get(THE_ONLY_SLOT);
        if (deconstructingStack.isEmpty()) {
            breakTimeRemaining = 0;
            return;
        }
        var itemAspects = getBasicAspects(deconstructingStack.getItem(),this.level.isClientSide);
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
        if (!this.level.isClientSide){
            deconstructingStack.shrink(1);
            if (deconstructingStack.isEmpty()){
                inventory.set(THE_ONLY_SLOT,ItemStack.EMPTY);
            }
            var randomSource = this.level != null? this.level.random : RandomSource.createNewThreadLocalInstance();
            var reducedAspects = IAspectReducibleToPrimal.reduceToPrimals(additionalAspects);
            if (randomSource.nextInt(80) < reducedAspects.visSize()) {
                var toStore = reducedAspects.randomAspect(randomSource);
                if (toStore == null){
                    toStore = Aspects.EMPTY_PRIMAL;
                }
                this.storingAspect = toStore;
                markDirtyAndUpdateSelf();
            }
        }
    }

}
