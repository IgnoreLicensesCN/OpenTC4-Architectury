package thaumcraft.common.runicshield;

import com.linearity.opentc4.annotations.Modifiable;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.lib.network.playerdata.PacketRunicChargeS2C;

//put into Player
public class PlayerRunicShieldInfo {
    public int rechargeDelay = 0;
    public final @Modifiable Object2IntMap<RunicShieldType> shieldCharged = new Object2IntOpenHashMap<>();
    public final @Modifiable Object2IntMap<RunicShieldType> shieldCapacity = new Object2IntOpenHashMap<>();
    public PlayerRunicShieldInfo(Player player) {
        //but if you store that serverPlayer instance here i will kick your ass(memory leak).
    }
    public boolean shouldSync = false;
    public void scheduleSyncTo(ServerPlayer player) {
        new PacketRunicChargeS2C(this.shieldCapacity).sendTo(player);
    }
    public void syncCapacityS2C(
            Object2IntMap<RunicShieldType> shieldCapacity
    ){
        this.shieldCapacity.clear();
        this.shieldCapacity.putAll(shieldCapacity);
    }
    public void syncChargeS2C(
            Object2IntMap<RunicShieldType> shieldCharged
    ){
        this.shieldCharged.clear();
        this.shieldCharged.putAll(shieldCharged);
    }
}
