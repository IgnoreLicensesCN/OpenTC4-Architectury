package thaumcraft.api.scan.entity;

import org.jetbrains.annotations.NotNull;

public abstract class EntityScanListener implements Comparable<EntityScanListener>{
    public final int weight;
    public EntityScanListener(int weight) {
        this.weight = weight;
    }
    public abstract void onEntityScan(EntityScanContext context);

    @Override
    public int compareTo(@NotNull EntityScanListener o) {
        return Integer.compare(weight, o.weight);
    }
}
