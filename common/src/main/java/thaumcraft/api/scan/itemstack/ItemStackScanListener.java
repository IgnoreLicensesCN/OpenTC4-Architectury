package thaumcraft.api.scan.itemstack;

import org.jetbrains.annotations.NotNull;

public abstract class ItemStackScanListener implements Comparable<ItemStackScanListener>{
    public final int weight;
    public ItemStackScanListener(int weight) {
        this.weight = weight;
    }
    public abstract void onItemStackScan(ItemStackScanContext context);

    @Override
    public int compareTo(@NotNull ItemStackScanListener o) {
        return Integer.compare(weight, o.weight);
    }
}
