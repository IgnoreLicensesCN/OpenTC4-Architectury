package thaumcraft.api.aspects;

public interface IEssentiaComparatorSignalProviderBlockEntity {

    int getRedstoneSignalCalculationVisOwning();
    int getRedstoneSignalCalculationVisCapacity();
    default int getRedstoneSignalCalculationVis() {
        return Math.max(0,Math.min(15,Math.ceilDiv(getRedstoneSignalCalculationVisOwning()*15,getRedstoneSignalCalculationVisCapacity())));
    }
}
