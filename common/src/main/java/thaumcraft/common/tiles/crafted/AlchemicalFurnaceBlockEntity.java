package thaumcraft.common.tiles.crafted;

import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.listeners.aspects.item.bonus.ItemBonusAspectCalculator;
import thaumcraft.api.tile.TileThaumcraftWithMenu;
import thaumcraft.common.blocks.abstracts.IFurnaceAttachmentBlock;
import thaumcraft.common.blocks.crafted.AlchemicalFurnaceBlock;
import thaumcraft.common.items.abstracts.IAlchemicalFurnaceSpeeder;
import thaumcraft.common.menu.menu.AlchemicalFurnaceMenu;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IAlembic;
import thaumcraft.common.tiles.abstracts.IThaumcraftFurnace;

import static com.linearity.opentc4.Consts.AlchemicalFurnaceBlockEntityTagAccessors.*;
import static thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter.getBasicAspectsClient;
import static thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter.getBasicAspectsServer;

public class AlchemicalFurnaceBlockEntity extends TileThaumcraftWithMenu<AlchemicalFurnaceMenu,AlchemicalFurnaceBlockEntity>
        implements ExtendedMenuProvider,
        IThaumcraftFurnace,
        WorldlyContainer
{
    public AlchemicalFurnaceBlockEntity(BlockEntityType<? extends AlchemicalFurnaceBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState,AlchemicalFurnaceMenu::new);
    }
    public AlchemicalFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ALCHEMICAL_FURNACE, blockPos, blockState);
    }
    public static final int ASPECT_GIVEN_ITEM_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int[] BOTTOM_SLOTS = new int[]{FUEL_SLOT};
    public static final int[] TOP_SLOTS = new int[ASPECT_GIVEN_ITEM_SLOT];
    public static final int[] SIDE_SLOTS = new int[]{ASPECT_GIVEN_ITEM_SLOT};
    public static final int[] SLOTS = new int[]{ASPECT_GIVEN_ITEM_SLOT, FUEL_SLOT};
    public @NotNull AspectList<Aspect> aspects = new AspectList<>();
    public static final  int MAX_VIS_SIZE = 50;
    boolean speedBoost = false;
    public final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);//furnaceItemStacks
    public int furnaceCookTime = 0;
    public int furnaceRequiredCookTime = 100;
    public int furnaceFuelRemainingBurnTime = 0;
    public int furnaceFuelBurnTotalTime = 0;
    int count = 0;

    public static final int ALEMBIC_RANGE = 5;
    protected int speedBoostAspectExtractTime = 20;
    protected int defaultAspectExtractTime = 40;

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
        ASPECTS_OWNING.writeToCompoundTag(compoundTag,aspects);
        COOKED_TIME.writeToCompoundTag(compoundTag,furnaceCookTime);
        REQUIRED_COOK_TIME.writeToCompoundTag(compoundTag,furnaceRequiredCookTime);
        FUEL_REMAINING_TIME.writeToCompoundTag(compoundTag,furnaceFuelRemainingBurnTime);
        FUEL_TOTAL_TIME.writeToCompoundTag(compoundTag,furnaceFuelBurnTotalTime);
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        ContainerHelper.loadAllItems(compoundTag, inventory);
        aspects = ASPECTS_OWNING.readFromCompoundTag(compoundTag);
        furnaceCookTime = COOKED_TIME.readFromCompoundTag(compoundTag);
        furnaceRequiredCookTime = REQUIRED_COOK_TIME.readFromCompoundTag(compoundTag);
        furnaceFuelRemainingBurnTime = FUEL_REMAINING_TIME.readFromCompoundTag(compoundTag);
        furnaceFuelBurnTotalTime = FUEL_TOTAL_TIME.readFromCompoundTag(compoundTag);
    }

    public void serverTick() {
        if (this.level == null){return;}
        boolean fuelBurningStateWhenStartTick = this.furnaceFuelRemainingBurnTime > 0;
        boolean updateSelfFlag = false;
        ++this.count;
        if (this.furnaceFuelRemainingBurnTime > 0) {
            --this.furnaceFuelRemainingBurnTime;
        }

        if (!level.isClientSide) {

            if (this.count % (this.speedBoost ? speedBoostAspectExtractTime : defaultAspectExtractTime) == 0
                    && !this.aspects.isEmpty()) {
                AspectList<Aspect> exlude = new AspectList<>();
                for (int deep = 1; deep <= ALEMBIC_RANGE; deep++) {
                    var probablyAlembicPos = getBlockPos().above(deep);
                    var tile = this.level.getBlockEntity(probablyAlembicPos);
                    if (!(tile instanceof IAlembic alembic)) {
                        break;
                    }
                    var alembicState = this.level.getBlockState(probablyAlembicPos);
                    var alembicAspect = alembic.getAspect();
                    if (!alembicAspect.isEmpty()
                            && alembic.getAmount() < alembic.getMaxAmount()
                            && this.aspects.getAmount(alembicAspect) > 0) {

                        this.takeAspectFromContainer(alembicAspect, 1);
                        alembic.addIntoContainer(alembicAspect, 1);
                        exlude.mergeWithHighest(alembicAspect, 1);

                        this.markDirtyAndUpdateSelf();
                        level.sendBlockUpdated(probablyAlembicPos,alembicState,alembicState,3);
                    }
                }
                for (int deep = 1; deep <= ALEMBIC_RANGE; deep++) {
                    var probablyAlembicPos = getBlockPos().above(deep);
                    var tile = this.level.getBlockEntity(probablyAlembicPos);
                    if (!(tile instanceof IAlembic alembic)) {
                        break;
                    }

                    var alembicState = this.level.getBlockState(probablyAlembicPos);
                    if (alembic.getAspect().isEmpty() || alembic.getAmount() == 0) {
                        Aspect aspectToInsert = Aspects.EMPTY;
                        var aspFilter = alembic.getAspectFilter();
                        if (aspFilter.isEmpty()) {
                            aspectToInsert = this.takeRandomAspect(exlude);
                        } else if (this.takeAspectFromContainer(aspFilter, 1)) {
                            aspectToInsert = aspFilter;
                        }

                        if (aspectToInsert != null) {
                            alembic.addIntoContainer(aspectToInsert, 1);

                            this.markDirtyAndUpdateSelf();
                            level.sendBlockUpdated(probablyAlembicPos,alembicState,alembicState,3);

                            break;
                        }
                    }
                }
            }

            if (this.furnaceFuelRemainingBurnTime == 0 && this.canSmelt()) {
                var fuelStack = getFuelStack();
                var burnTime = getItemBurnTime(fuelStack);
                this.furnaceFuelBurnTotalTime = this.furnaceFuelRemainingBurnTime = getItemBurnTime(fuelStack);
                if (this.furnaceFuelRemainingBurnTime > 0) {
                    updateSelfFlag = true;
                    this.speedBoost = false;
                    if (!fuelStack.isEmpty()) {
                        if (fuelStack.getItem() instanceof IAlchemicalFurnaceSpeeder speeder) {
                            this.speedBoost = speeder.canSpeedUpAlchemicalFurnace(fuelStack);
                        }
                        fuelStack.shrink(1);
                        attachmentOnStartBurningFuel(burnTime);

                        if (fuelStack.isEmpty()) {
                            var remainingItem = fuelStack.getItem().getCraftingRemainingItem();
                            if (remainingItem != null){
                                ItemStack container = remainingItem.getDefaultInstance();
                                this.inventory.set(FUEL_SLOT, container);
                            }
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt()) {
                ++this.furnaceCookTime;
                attachmentOnBurning();
                if (this.furnaceCookTime >= this.furnaceRequiredCookTime) {
                    this.furnaceCookTime = 0;
                    this.smeltItem();
                    updateSelfFlag = true;
                }
            } else {
                this.furnaceCookTime = 0;
            }

            if (fuelBurningStateWhenStartTick != this.furnaceFuelRemainingBurnTime > 0) {
                updateSelfFlag = true;
            }
        }

        if (updateSelfFlag) {
            this.markDirtyAndUpdateSelf();
        }

    }

    public void attachmentOnBurning(){
        if (this.level == null){return;}
        for (var dir:Direction.values()){
            var selfPos = getBlockPos();
            var pos = selfPos.relative(dir);
            var dirState = this.level.getBlockState(pos);
            if (dirState.getBlock() instanceof IFurnaceAttachmentBlock attachment){
                attachment.onThaumcraftFurnaceBurning(
                        level,
                        this,
                        this.getBlockState(),
                        selfPos,
                        dirState,
                        pos
                );
            }
        }
    }
    public void attachmentOnCalculatingRequiredCookTime(int defaultRequiredCookTime){
        if (this.level == null){return;}
        for (var dir:Direction.values()){
            var selfPos = getBlockPos();
            var pos = selfPos.relative(dir);
            var dirState = this.level.getBlockState(pos);
            if (dirState.getBlock() instanceof IFurnaceAttachmentBlock attachment){
                attachment.attachmentOnCalculatingRequiredCookTime(
                        level,
                        this,
                        this.getBlockState(),
                        selfPos,
                        dirState,
                        pos,
                        defaultRequiredCookTime
                );
            }
        }
    }
    public void attachmentOnStartBurningFuel(int defaultFuelBurnTime){
        if (this.level == null){return;}
        for (var dir:Direction.values()){
            var selfPos = getBlockPos();
            var pos = selfPos.relative(dir);
            var dirState = this.level.getBlockState(pos);
            if (dirState.getBlock() instanceof IFurnaceAttachmentBlock attachment){
                attachment.onThaumcraftFurnaceStartBurningFuel(
                        level,
                        this,
                        this.getBlockState(),
                        selfPos,
                        dirState,
                        pos,
                        defaultFuelBurnTime
                );
            }
        }
    }
    public boolean isBurning() {
        return this.furnaceFuelRemainingBurnTime > 0;
    }
    public ItemStack getFuelStack(){
        return getItem(FUEL_SLOT);
    }
    public ItemStack getBurnToAspectStack(){
        return getItem(ASPECT_GIVEN_ITEM_SLOT);
    }
    public static boolean canBurnAsFuel(ItemStack stack){
        return FurnaceBlockEntity.isFuel(stack);
    }
    public static int getItemBurnTime(ItemStack stack){
        return FurnaceBlockEntity.getFuel().getOrDefault(stack.getItem(), 0);
    }
    public AspectList<Aspect> getBurnAspectResult(ItemStack stack){
        var serverFlag = this.level != null && !this.level.isClientSide();
        return ItemBonusAspectCalculator.getBonusAspects(stack,serverFlag?getBasicAspectsServer(stack.getItem()):getBasicAspectsClient(stack.getItem()));

    }
    public boolean canBurnIntoAspect(ItemStack stack){
        return !getBurnAspectResult(stack).isEmpty();
    }
    private boolean canSmelt() {
        if (!canBurnAsFuel(getFuelStack())) {
            return false;
        }
        var burnResult = getBurnAspectResult(getBurnToAspectStack());
        if (burnResult.isEmpty()) {
            return false;
        }
        var visSize = burnResult.visSize();
        if (visSize > MAX_VIS_SIZE - this.aspects.visSize()) {
            return false;
        }
        var defaultRequiredCookTime = visSize * 10;
        this.furnaceRequiredCookTime = defaultRequiredCookTime;
        attachmentOnCalculatingRequiredCookTime(defaultRequiredCookTime);
        return true;
    }

    public void smeltItem() {
        if (!canBurnAsFuel(getFuelStack())) {
            return;
        }
        var burnResult = getBurnAspectResult(getBurnToAspectStack());
        if (burnResult.isEmpty()) {
            return;
        }
        var visSize = burnResult.visSize();
        if (visSize > MAX_VIS_SIZE - this.aspects.visSize()) {
            return;
        }

        this.aspects.addAll(burnResult);
        removeItem(ASPECT_GIVEN_ITEM_SLOT,1);
        var usedItem = getItem(ASPECT_GIVEN_ITEM_SLOT);
        usedItem.shrink(1);
        if (usedItem.isEmpty()){
            setItem(ASPECT_GIVEN_ITEM_SLOT,ItemStack.EMPTY);
        }
    }


    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.thaumcraft.alchemical_furnace");//TODO
    }

    @Override
    public int getCookedTime() {
        return furnaceCookTime;
    }

    @Override
    public int getRequiredCookTime() {
        return furnaceRequiredCookTime;
    }

    @Override
    public int getFuelBurnRemainingTime() {
        return furnaceFuelRemainingBurnTime;
    }

    @Override
    public int getFuelBurnTotalTime() {
        return furnaceFuelBurnTotalTime;
    }

    @Override
    public boolean getSpeedBoost() {
        return speedBoost;
    }

    @Override
    public void setCookedTime(int cookTime) {
        this.furnaceCookTime = cookTime;
    }

    @Override
    public void setRequiredCookTime(int requiredCookTime) {
        this.furnaceRequiredCookTime = requiredCookTime;
    }

    @Override
    public void setFuelBurnTimeRemaining(int fuelBurnTime) {
        this.furnaceFuelRemainingBurnTime = fuelBurnTime;
    }

    @Override
    public void setFuelBurnTotalTime(int fuelBurnTotalTime) {
        this.furnaceFuelBurnTotalTime = fuelBurnTotalTime;
    }

    @Override
    public void setSpeedBoost(boolean speedBoost) {
        this.speedBoost = speedBoost;
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction direction) {
        return switch (direction){
            case DOWN -> BOTTOM_SLOTS;
            case UP -> TOP_SLOTS;
            default -> SIDE_SLOTS;
        };
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
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        if (direction != Direction.DOWN && i != ASPECT_GIVEN_ITEM_SLOT) {
            return false;
        }
        if (direction == Direction.DOWN && i != FUEL_SLOT) {
            return false;
        }
        return canPlaceItem(i,itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        if (i == FUEL_SLOT) {
            return canBurnAsFuel(itemStack);
        }
        if (i == ASPECT_GIVEN_ITEM_SLOT) {
            return canBurnIntoAspect(itemStack);
        }
        return false;
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.getFirst().isEmpty() && inventory.getLast().isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        ensureInventoryIndexInBound(i);
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



    public Aspect takeRandomAspect(AspectList<Aspect> exlude) {
        if (!this.aspects.isEmpty()) {
            AspectList<Aspect>temp = this.aspects.copy();
            if (!exlude.isEmpty()) {
                for(Aspect a : exlude.keySet()) {
                    temp.remove(a);
                }
            }

            if (!temp.isEmpty()) {
                var randomSource = this.level != null?this.level.random: RandomSource.createNewThreadLocalInstance();
                Aspect tag = temp.randomAspect(randomSource);
                this.aspects.reduceAndRemoveIfNotPositive(tag, 1);
                return tag;
            }
        }

        return null;
    }
    public boolean takeAspectFromContainer(Aspect tag, int amount) {
        if (this.aspects.getAmount(tag) >= amount) {
            this.aspects.reduceAndRemoveIfNotPositive(tag, amount);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void markDirtyAndUpdateSelf() {
        super.markDirtyAndUpdateSelf();
        if (this.level != null) {
            level.setBlockAndUpdate(
                    worldPosition,
                    this.getBlockState()
                            .setValue(AlchemicalFurnaceBlock.HAS_ASPECT,!this.aspects.isEmpty())
                            .setValue(AlchemicalFurnaceBlock.LIT,this.furnaceFuelRemainingBurnTime > 0)
            );
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null && !level.isClientSide ) {
            Containers.dropContents(level, worldPosition, inventory);
        }
    }
}
