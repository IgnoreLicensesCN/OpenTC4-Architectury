package thaumcraft.common.tiles.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.*;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.blocks.crafted.jars.EssentiaJarBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;

import static com.linearity.opentc4.Consts.EssentiaJarBlockEntityTagAccessors.*;

public class EssentiaJarBlockEntity extends TileThaumcraft
        implements
        IEssentiaTransportBlockEntity,
        IValueContainerBasedComparatorSignalProviderBlockEntity,
        IAspectFilterAccessible,
        IAspectDisplayBlockEntity<Aspect>,
        IRemoteDrainableAspectSourceBlockEntity<Aspect>{
    public EssentiaJarBlockEntity(BlockEntityType<? extends EssentiaJarBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    public EssentiaJarBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ESSENTIA_JAR, blockPos, blockState);
    }

    public static final int ASPECT_CAPACITY = 64;

    public Direction getConnectableDirection() {
        return Direction.UP;
    }

    public int getAspectCapacity(){
        return ASPECT_CAPACITY;
    }

    private @NotNull Aspect aspectCurrent = Aspects.EMPTY;
    private @NotNull Aspect aspectFilter = Aspects.EMPTY;
    private int aspectAmountCurrent = 0;
    private final UnmodifiableSingleAspectListFromSupplier<Aspect> aspOwningCurrent = new UnmodifiableSingleAspectListFromSupplier<>(
            () -> this.aspectCurrent,() -> this.aspectAmountCurrent
    );

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        ASPECT_CURRENT.writeToCompoundTag(compoundTag, aspectCurrent);
        ASPECT_FILTER.writeToCompoundTag(compoundTag, aspectFilter);
        ASPECT_AMOUNT.writeToCompoundTag(compoundTag, aspectAmountCurrent);
    }

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.aspectCurrent = ASPECT_CURRENT.readFromCompoundTag(compoundTag);
        this.aspectFilter = ASPECT_FILTER.readFromCompoundTag(compoundTag);
        this.aspectAmountCurrent = ASPECT_AMOUNT.readFromCompoundTag(compoundTag);
    }

    public Direction getFacing(){
        return getBlockState().getValue(EssentiaJarBlock.FACING);
    }

    protected boolean aspectMatchesFilter(Aspect aspect) {
        if (aspect == Aspects.EMPTY) return false;
        if (aspectFilter == Aspects.EMPTY) return true;
        return aspect == aspectFilter;
    }

    @Override
    public boolean isConnectable(Direction face) {
        return face == getConnectableDirection();
    }

    @Override
    public boolean canInputFrom(Direction face) {
        return isConnectable(face);
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {}

    @Override
    public int getSuctionAmount(Direction face) {
        if (this.aspectAmountCurrent < getAspectCapacity()) {
            return !this.aspectFilter.isEmpty() ? 64 : 32;
        } else {
            return 0;
        }
    }

    @Override
    public @NotNull Aspect getSuctionType(Direction face) {
        return !aspectFilter.isEmpty() ? aspectFilter:aspectCurrent;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, Direction fromDirection) {
        if (aspect.isEmpty()) return 0;
        if (amount <= 0) return 0;
        if (!isConnectable(fromDirection)){
            return 0;
        }
        if (this.aspectAmountCurrent >= getAspectCapacity()){
            return 0;
        }
        if ((aspect == this.aspectCurrent || this.aspectAmountCurrent == 0) && aspectMatchesFilter(aspect)) {
            this.aspectCurrent = aspect;
            int added = Math.min(amount, getAspectCapacity() - this.aspectAmountCurrent);
            this.aspectAmountCurrent += added;
            markDirtyAndUpdateSelf();
            return added;
        }
        return 0;
    }

    @Override
    public @NotNull Aspect getEssentiaType(Direction face) {
        if (!isConnectable(face)){
            return Aspects.EMPTY;
        }
        return aspectCurrent;
    }

    @Override
    public int getEssentiaAmount(Direction face) {
        return aspectAmountCurrent;
    }

    @Override
    public int currentComparatorSignalValue() {
        return aspectAmountCurrent;
    }

    @Override
    public int comparatorSignalCapacity() {
        return getAspectCapacity();
    }

    @Override
    public AspectList<Aspect> getAspectsToDisplay() {
        return aspOwningCurrent;
    }

    @Override
    public @NotNull Aspect getAspectFilter() {
        return aspectFilter;
    }

    public boolean setAspectFilter(@NotNull Aspect aspectFilter) {
        if (!aspectFilter.isEmpty() && !this.aspectFilter.isEmpty()){
            return false;
        }
        if (aspectFilter != aspectCurrent && !aspectCurrent.isEmpty() && aspectAmountCurrent != 0) {
            return false;
        }
        this.aspectFilter = aspectFilter;
        this.markDirtyAndUpdateSelf();
        return true;
    }

    @Override
    public int canProvideAspectAmountForRemoteDrain(Aspect aspect) {
        if (aspect == aspectCurrent){
            return aspectAmountCurrent;
        }
        return 0;
    }

    @Override
    public boolean drainAspectRemote(Aspect aspect, int amount) {
        if (aspect == aspectCurrent && amount <= this.aspectAmountCurrent){
            this.aspectAmountCurrent -= amount;
            if (this.aspectAmountCurrent <= 0){
                this.aspectAmountCurrent = 0;
                this.aspectCurrent = Aspects.EMPTY;
            }
        }
        return false;
    }

    protected int tickCount = 0;
    public void serverTick(){
        if (level == null){return;}
        tickCount+=1;
        if (tickCount % 5 == 0){
            var posAbove = getBlockPos().above();
            var be = level.getBlockEntity(posAbove);
            if (be instanceof IEssentiaTransportOutBlockEntity outBE){
                var selfInDir = getConnectableDirection();
                var beOutToDir = selfInDir.getOpposite();
                var selfSuctionAmount = getSuctionAmount(selfInDir);
                boolean suctionAllowed = selfSuctionAmount >= outBE.getMinimumSuctionToDrainOut()
                        && !(be instanceof IEssentiaTransportInBlockEntity inBE && inBE.getSuctionAmount(beOutToDir) >= selfSuctionAmount);

                if (suctionAllowed && outBE.canOutputTo(beOutToDir)){
                    Aspect aspToGet = this.aspectFilter;
                    if (aspToGet.isEmpty() && !this.aspectCurrent.isEmpty() && this.aspectAmountCurrent > 0){
                        aspToGet = this.aspectCurrent;
                    }
                    if (aspToGet.isEmpty()){
                        var canOutAspect = outBE.getEssentiaType(beOutToDir);
                        var canOuAmount = outBE.getEssentiaAmount(beOutToDir);
                        if (!canOutAspect.isEmpty() && canOuAmount > 0){
                            aspToGet = canOutAspect;
                        }
                    }
                    if (!aspToGet.isEmpty()){
                        this.addEssentia(aspToGet,outBE.takeEssentia(aspToGet,1,beOutToDir),selfInDir);
                    }
                }
            }
        }
    }

    public void clear(){
        this.aspectCurrent = Aspects.EMPTY;
        this.aspectAmountCurrent = 0;
    }
    public boolean canFillAspectContainerItem(
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            Aspect aspect
    ) {
        return (aspect == this.aspectCurrent || aspect.isEmpty()) && this.aspectAmountCurrent != 0;
    }

    public boolean fillAspectContainerItem(
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            int minAmount
    ) {
        if (level == null){
            return false;
        }

        if (aspectCurrent.isEmpty() || aspectAmountCurrent < minAmount) {
            return false;
        }
        var amountBefore = aspectAmountCurrent;
        aspectAmountCurrent = itemToFill.storeAspect(level,getBlockPos(),stackToFill, aspectCurrent, amountBefore);
        if (aspectAmountCurrent == 0) {
            aspectCurrent = Aspects.EMPTY;
        }
        if (aspectAmountCurrent != amountBefore) {
            markDirtyAndUpdateSelf();
            if (level != null) {
                level.playSound(
                        null,
                        getBlockPos(),
                        SoundEvents.PLAYER_SWIM,
                        SoundSource.BLOCKS,
                        .5F,
                        1.F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.3F
                );
            }
        }

        return true;
    }



    @Override
    public boolean canOutputTo(Direction face) {
        return isConnectable(face);
    }


    @Override
    public int takeEssentia(Aspect aspect, int amount, Direction fromDirection) {
        if (aspect.isEmpty()) return 0;
        if (amount <= 0) return 0;
        if (!isConnectable(fromDirection)){
            return 0;
        }
        if (this.aspectAmountCurrent < amount){
            return 0;
        }
        if (aspect == this.aspectCurrent) {
            this.aspectAmountCurrent -= amount;
            if (this.aspectAmountCurrent == 0) {
                this.aspectCurrent = Aspects.EMPTY;
            }
            markDirtyAndUpdateSelf();
            return amount;
        }
        return 0;
    }

    @Override
    public int getMinimumSuctionToDrainOut() {
        return !this.aspectFilter.isEmpty() ? 64 : 32;
    }
}
