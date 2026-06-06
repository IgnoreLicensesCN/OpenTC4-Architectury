package thaumcraft.api.warp;

import com.linearity.opentc4.mixinaccessors.PlayerWarpInfoMixinAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.lib.network.playerdata.PacketSyncWarpS2C;

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

    public void setTo(WarpInfo copyFrom){
        this.tempWarp = copyFrom.tempWarp;
        this.permWarp = copyFrom.permWarp;
        this.stickyWarp = copyFrom.stickyWarp;
        this.warpEventCounter = copyFrom.warpEventCounter;
    }

    public static WarpInfo getFromPlayer(Player player){
        return ((PlayerWarpInfoMixinAccessor)player).opentc4$getWarpInfo();
    }
    public static void setForPlayer(Player player, WarpInfo info){
        getFromPlayer(player).setTo(info);
    }
    public void syncTo(ServerPlayer player){
        new PacketSyncWarpS2C(getFromPlayer(player)).sendTo(player);
    }
}
