package thaumcraft.api.scan.block;

import org.jetbrains.annotations.NotNull;

public abstract class BlockPosScanListener implements Comparable<BlockPosScanListener>{
    public final int weight;
    public BlockPosScanListener(int weight) {
        this.weight = weight;
    }
    public abstract void onBlockScan(BlockPosScanContext context);

    @Override
    public int compareTo(@NotNull BlockPosScanListener o) {
        return Integer.compare(weight, o.weight);
    }
}
