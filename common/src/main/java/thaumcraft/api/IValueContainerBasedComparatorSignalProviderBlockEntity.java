package thaumcraft.api;

public interface IValueContainerBasedComparatorSignalProviderBlockEntity {

    int currentValueForComparatorSignal();
    int comparatorSignalCapacity();
    int SMALL_SIGNAL_CAPACITY_LIMIT = 65536;
    byte[][] CACHE = new byte[SMALL_SIGNAL_CAPACITY_LIMIT + 1][];

    default int getComparatorSignal() {
        int c = comparatorSignalCapacity();
        int v = currentValueForComparatorSignal();
        if (c > SMALL_SIGNAL_CAPACITY_LIMIT){
            return v == 0 ? 0 : (byte) Math.max(1, Math.min(15, Math.ceilDiv(v * 15, c)));
        }

        if (c <= 0) return 0;

        byte[] row = CACHE[c];
        if (row == null) {
            row = CACHE[c] = new byte[c + 1];
            for (int i = 0; i <= c; i++) {
                row[i] = i == 0 ? 0 : (byte) Math.max(1, Math.min(15, Math.ceilDiv(i * 15, c)));
            }
        }

        return row[Math.min(v, c)] & 0xFF;
    }
}
