package thaumcraft.common.tiles.abstracts;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.tile.TileThaumcraft;

import static com.linearity.opentc4.Consts.SingleFluidContainerBlockEntityTagAccessors.FLUID;
import static dev.architectury.fluid.FluidStack.create;

public abstract class SingleFluidContainerBlockEntity extends TileThaumcraft implements IValueContainerBasedComparatorSignalProviderBlockEntity {

    public SingleFluidContainerBlockEntity(BlockEntityType<? extends SingleFluidContainerBlockEntity> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
    protected @NotNull FluidStack fluidStack = create(Fluids.EMPTY, 0);//keep it a instance

    @Override
    public void readCustomNBT(CompoundTag compoundTag) {
        super.readCustomNBT(compoundTag);
        this.fluidStack = FLUID.readFromCompoundTag(compoundTag);
    }

    @Override
    public void writeCustomNBT(CompoundTag compoundTag) {
        super.writeCustomNBT(compoundTag);
        FLUID.writeToCompoundTag(compoundTag, fluidStack);
    }

    public abstract int getLiquidCapacity();


    public @NotNull FluidStack getFluidStack() {
        return fluidStack;
    }

    public long getFluidAmount() {
        return fluidStack.getAmount();
    }

    //return inserted
    public long insertFluid(Fluid fluid, long maxCanInsert){
        return insertFluid(fluid,maxCanInsert,true);
    }
    public long insertFluid(Fluid fluid,long maxCanInsert,boolean doIt) {
        if (!canAcceptFluid(fluid)) {
            return 0;
        }
        if (fluid != fluidStack.getFluid() && !fluidStack.isEmpty()) {
            return 0;
        }
        long currentAmount = fluidStack.getAmount();
        if (currentAmount < 0){
            currentAmount = 0;
            fluidStack.setAmount(0);
        }
        long spaceToInsert = getLiquidCapacity() - currentAmount;
        if (spaceToInsert < 0){
            spaceToInsert = 0;
            fluidStack.setAmount(getLiquidCapacity());
        }
        long inserted = Math.min(spaceToInsert, maxCanInsert);
        if (doIt){
            fluidStack.setAmount(currentAmount + inserted);
        }
        return inserted;
    }

    public long extractFluid(Fluid fluid, long maxCanExtract, boolean doIt) {
        return 0L;//default no out
    }
    public long extractFluid(Fluid fluid, long maxCanExtract) {
        return extractFluid(fluid,maxCanExtract,true);//default no out
    }
    protected void decreaseFluid(long amount) {
        this.fluidStack.setAmount(Math.max(0, this.fluidStack.getAmount() - amount));
    }

    public void setFluidStack(@NotNull FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }
    public abstract boolean canAcceptFluid(@NotNull Fluid fluid);

    @Override
    public int currentValueForComparatorSignal() {
        return Math.toIntExact(Math.min(getFluidAmount(), comparatorSignalCapacity()));
    }

    @Override
    public int comparatorSignalCapacity() {
        return getLiquidCapacity();
    }
}
