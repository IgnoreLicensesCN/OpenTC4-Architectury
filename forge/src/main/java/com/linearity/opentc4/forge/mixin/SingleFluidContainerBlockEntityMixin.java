package com.linearity.opentc4.forge.mixin;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.common.tiles.abstracts.SingleFluidContainerBlockEntity;

@Mixin(SingleFluidContainerBlockEntity.class)
public class SingleFluidContainerBlockEntityMixin implements IFluidHandler {
    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        var be = (SingleFluidContainerBlockEntity)(Object)this;
        var beFluidStack = be.getFluidStack();
        return new FluidStack(
                beFluidStack.getFluid(),
                (int) beFluidStack.getAmount()
        );
    }

    @Override
    public int getTankCapacity(int i) {
        var be = (SingleFluidContainerBlockEntity)(Object)this;
        return be.getLiquidCapacity();
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        var be = (SingleFluidContainerBlockEntity)(Object)this;
        return be.canAcceptFluid(fluidStack.getFluid());
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        var be = (SingleFluidContainerBlockEntity)(Object)this;
        return (int) be.insertFluid(
                fluidStack.getFluid(),fluidStack.getAmount(),
                fluidAction != FluidAction.SIMULATE);
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        var be = (SingleFluidContainerBlockEntity)(Object)this;
        long amount = be.extractFluid(fluidStack.getFluid(), fluidStack.getAmount(),fluidAction == FluidAction.EXECUTE);
        if (amount > 0) {
            return new FluidStack(fluidStack.getFluid(), Math.toIntExact(amount));
        }
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        var be = (SingleFluidContainerBlockEntity)(Object)this;
        var fluid = be.getFluidStack().getFluid();
        if (fluid == Fluids.EMPTY) {
            return FluidStack.EMPTY;
        }
        long amount = be.extractFluid(fluid, i,fluidAction == FluidAction.EXECUTE);
        if (amount > 0) {
            return new FluidStack(fluid, Math.toIntExact(amount));
        }
        return FluidStack.EMPTY;
    }
}
