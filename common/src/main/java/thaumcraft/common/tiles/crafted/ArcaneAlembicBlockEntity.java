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
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.blocks.crafted.ArcaneAlembicBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.abstracts.IAlembic;

import static com.linearity.opentc4.Consts.ArcaneAlembicBlockEntityTagAccessors.*;

public class ArcaneAlembicBlockEntity extends TileThaumcraft
        implements IAlembic,IEssentiaTransportOutBlockEntity{
    public ArcaneAlembicBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public ArcaneAlembicBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ThaumcraftBlockEntities.ARCANE_ALEMBIC, blockPos, blockState);
    }

    public static final int ASPECT_CAPACITY = 32;

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

    @Override
    public @NotNull("null -> empty") Aspect getAspect() {
        return aspectCurrent;
    }

    public @NotNull Aspect getAspectFilter() {
        return aspectFilter;
    }

    @Override
    public int getAmount() {
        return aspectAmountCurrent;
    }

    @Override
    public int getMaxAmount() {
        return ASPECT_CAPACITY;
    }

    public void setAspectFilter(@NotNull Aspect aspectFilter) {
        this.aspectFilter = aspectFilter;
        this.markDirtyAndUpdateSelf();
    }

    @Override
    @UnmodifiableView
    public @NotNull AspectList<Aspect> getAspects() {
        return aspOwningCurrent;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        if (aspect != aspectCurrent
                || aspectCurrent.isEmpty()
                || aspect.isEmpty()
                || aspectAmountCurrent < amount
        ) {
            return false;
        }
        if (this.aspectAmountCurrent == amount) {
            this.aspectCurrent = Aspects.EMPTY;
            this.aspectAmountCurrent = 0;
        }
        else {
            this.aspectAmountCurrent -= amount;
        }
        markDirtyAndUpdateSelf();
        return true;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return aspect == aspectCurrent
                && !aspectCurrent.isEmpty()
                && !aspect.isEmpty()
                && aspectAmountCurrent >= amount;
    }

    @Override
    public int containerContains(Aspect aspect) {
        if (aspect != aspectCurrent || aspect.isEmpty() || aspectCurrent.isEmpty()) {
            return 0;
        }
        return aspectAmountCurrent;
    }

    @Override
    public int addIntoContainer(Aspect aspect, int amount) {
        if (!doesContainerAccept(aspect)) {
            return amount;
        }
        var added = amount + aspectAmountCurrent;
        aspectAmountCurrent = Math.min(getMaxAmount(), added);
        markDirtyAndUpdateSelf();
        return added - aspectAmountCurrent;
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        if (aspect.isEmpty()){
            return false;
        }
        if (!aspectFilter.isEmpty() && aspectFilter != aspect) {
            return false;
        }
        return aspectCurrent.isEmpty() || aspectCurrent == aspect;
    }

    @Override
    public void clear() {
        this.aspectAmountCurrent = 0;
        this.aspectCurrent = Aspects.EMPTY;
    }

    @Override
    public boolean canFillAspectContainerItem(
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill,
            Aspect aspect
    ) {
        return aspect == this.aspectCurrent && !aspect.isEmpty() && this.aspectAmountCurrent != 0;
    }

    @Override
    public void fillAspectContainerItem(
            ItemStack stackToFill,
            IAspectContainerItem<Aspect> itemToFill
    ) {

        var amountBefore = aspectAmountCurrent;
        if (!aspectCurrent.isEmpty() && aspectAmountCurrent != 0) {
            aspectAmountCurrent = itemToFill.storeAspect(stackToFill, aspectCurrent, amountBefore);
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
        }
    }


    @Override
    public boolean isConnectable(Direction face) {
        return face.getAxis() != Direction.Axis.Y && face != getBlockState().getValue(ArcaneAlembicBlock.FACING);
    }

    @Override
    public boolean canOutputTo(Direction face) {
        return isConnectable(face);
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, Direction face) {
        if (!isConnectable(face)) {
            return 0;
        }
        return takeFromContainer(aspect,amount)?amount:0;
    }

    @Override
    public int getMinimumSuctionToDrainOut() {
        return 0;
    }

    @Override
    public int getEssentiaAmount(Direction face) {
        if (!isConnectable(face)) {
            return 0;
        }
        return this.aspectAmountCurrent;
    }

    @Override
    public Aspect getEssentiaType(Direction face) {
        if (!isConnectable(face)) {
            return Aspects.EMPTY;
        }
        return this.aspectCurrent;
    }
}
