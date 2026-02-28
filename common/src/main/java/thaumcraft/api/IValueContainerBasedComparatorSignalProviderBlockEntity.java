package thaumcraft.api;

public interface IValueContainerBasedComparatorSignalProviderBlockEntity {

    int currentComparatorSignalValue();
    int comparatorSignalCapacity();
    default int getComparatorSignal() {
        return Math.max(0,Math.min(15,Math.ceilDiv(currentComparatorSignalValue()*15, comparatorSignalCapacity())));
    }
}
