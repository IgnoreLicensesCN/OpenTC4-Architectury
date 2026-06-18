package thaumcraft.api.warp;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.network.playerdata.syncdata.PacketSyncWarpS2C;

//instance should bind to entity
public class WarpInfo {
    private int tempWarp = 0;
    private int permWarp = 0;
    private int stickyWarp = 0;
    private int warpEventCounter = 0;

    public int getWarpEventCounter() {
        return warpEventCounter;
    }

    public void setWarpEventCounter(int warpEventCounter) {
        this.warpEventCounter = warpEventCounter;
    }

    public int getTotalWarp() {
        return tempWarp + permWarp + stickyWarp;
    }

    public int getTempWarp() {
        return tempWarp;
    }

    public void setTempWarp(int tempWarp) {
        this.tempWarp = tempWarp;
    }

    public int getPermWarp() {
        return permWarp;
    }

    public void setPermWarp(int permWarp) {
        this.permWarp = permWarp;
    }

    public int getStickyWarp() {
        return stickyWarp;
    }

    public void setStickyWarp(int stickyWarp) {
        this.stickyWarp = stickyWarp;
    }

    public void addTempWarp(int amount) {
        tempWarp += amount;
    }
    public void addPermWarp(int amount) {
        permWarp += amount;
    }

    public void addStickyWarp(int amount){
        stickyWarp += amount;
    }

    public static @Nullable WarpInfo getFromLivingEntity(LivingEntity livingEntity){
        if (livingEntity instanceof IWarpInfoOwnerLivingEntity warpInfoOwner){
            return warpInfoOwner.getWarpInfo();
        }
        return null;
    }
    public static void setForLivingEntity(LivingEntity living, WarpInfo info){
        if (living instanceof IWarpInfoOwnerLivingEntity warpInfoOwner){
            warpInfoOwner.setWarpInfo(info);
        }
    }
    public void syncSendPacket(ServerPlayer player){
        var info = getFromLivingEntity(player);
        if (info == null) return;
        new PacketSyncWarpS2C(info).sendTo(player);
    }
}
