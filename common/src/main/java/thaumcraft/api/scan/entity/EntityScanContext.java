package thaumcraft.api.scan.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EntityScanContext {
    public final LivingEntity livingScanning;
    public final Entity entity;
    public boolean shouldBreak = false;

    protected EntityScanContext(LivingEntity livingScanning, Entity entity) {
        this.livingScanning = livingScanning;
        this.entity = entity;
    }
}
