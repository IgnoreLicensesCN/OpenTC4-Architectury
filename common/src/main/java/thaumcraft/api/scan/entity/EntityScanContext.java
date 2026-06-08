package thaumcraft.api.scan.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class EntityScanContext {
    public final ServerPlayer playerScanning;
    public final Entity entity;
    public boolean shouldBreak = false;

    protected EntityScanContext(ServerPlayer playerScanning, Entity entity) {
        this.playerScanning = playerScanning;
        this.entity = entity;
    }

}
