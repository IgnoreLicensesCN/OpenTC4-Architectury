package thaumcraft.common.lib.network;

import dev.architectury.networking.simple.BaseS2CMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

public abstract class ThaumcraftBaseS2CMessage extends BaseS2CMessage {

    public void sendToAllAround(ServerLevel levelAt, BlockPos posAt, double rangeSq){
        sendToAllAround(
                levelAt,
                new Vec3(posAt.getX() + .5, posAt.getY() + .5, posAt.getZ() + .5),
                rangeSq
        );
    }
    public static final EntityTypeTest<Entity, ServerPlayer> serverPlayerTest = EntityTypeTest.forClass(ServerPlayer.class);
    public void sendToAllAround(ServerLevel levelAt, Vec3 posAt, double rangeSq){
        levelAt.getEntities(
                serverPlayerTest,
                player -> player.distanceToSqr(posAt) <= rangeSq
        ).forEach(this::sendTo);
    }
}
