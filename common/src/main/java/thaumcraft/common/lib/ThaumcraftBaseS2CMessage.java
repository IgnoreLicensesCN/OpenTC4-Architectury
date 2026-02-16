package thaumcraft.common.lib;

import dev.architectury.networking.simple.BaseS2CMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public abstract class ThaumcraftBaseS2CMessage extends BaseS2CMessage {

    public void sendToAllAround(ServerLevel levelAt, BlockPos posAt, double rangeSq){
        sendToAllAround(
                levelAt,
                new Vec3(posAt.getX() + .5, posAt.getY() + .5, posAt.getZ() + .5),
                rangeSq
        );
    }
    public void sendToAllAround(ServerLevel levelAt, Vec3 posAt, double rangeSq){
        for (Player player : levelAt.players()) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (player.distanceToSqr(posAt) <= rangeSq) {
                    this.sendTo(serverPlayer);
                }
            }
        }
    }
}
