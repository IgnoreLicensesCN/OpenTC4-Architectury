package thaumcraft.common.runicshield;

import com.linearity.opentc4.mixinaccessors.PlayerRunicShieldInfoMixinAccessor;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.LinkedHashCentiVisList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableCentiVisList;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RunicShieldType {
    private static final Map<RunicShieldTypeResourceLocation,RunicShieldType> RUNIC_SHIELD_TYPES = new ConcurrentHashMap<>();
    public static final @UnmodifiableView Map<RunicShieldTypeResourceLocation,RunicShieldType> RUNIC_SHIELD_TYPES_VIEW
            = Collections.unmodifiableMap(RUNIC_SHIELD_TYPES);
    public final RunicShieldTypeResourceLocation id;
    public RunicShieldType(RunicShieldTypeResourceLocation shieldTypeResourceLocation) {
        if (!RUNIC_SHIELD_TYPES.containsKey(shieldTypeResourceLocation)){
            RUNIC_SHIELD_TYPES.put(shieldTypeResourceLocation,this);
        }else {
            throw new IllegalArgumentException("RunicShieldType ID already exists:" + shieldTypeResourceLocation);
        }
        this.id = shieldTypeResourceLocation;
    }

    private static final @Unmodifiable CentiVisList<Aspect> rechargeCost = UnmodifiableCentiVisList.of(
            Aspects.AIR, 50,Aspects.EARTH, 50
    );
    public @Unmodifiable CentiVisList<Aspect> getRechargeCost(){
        return rechargeCost;
    }
    public int getTickCooldownAfterRegen(Player player){
        return 40;//TODO: - EventHandlerRunic.runicInfo.get(player)[1] * 10(ticks)
    }
    //called every tick
    public void rechargeTickForPlayer(Player player){
        var shieldInfo = ((PlayerRunicShieldInfoMixinAccessor)player).opentc4$getPlayerRunicShieldInfo();
        if (shieldInfo.rechargeDelay > 0) {
            return;
        }
        var shieldCapacityMap = shieldInfo.shieldCapacity;
        var shieldMap = shieldInfo.shieldCharged;
        if (shieldCapacityMap.getInt(this) > shieldMap.getInt(this)){
            if (WandManager.consumeCentiVisFromInventory(player,getRechargeCost())){
                shieldMap.mergeInt(this,1,Integer::sum);
                shieldInfo.rechargeDelay += getTickCooldownAfterRegen(player);
                shieldInfo.shouldSync = true;
            }
        }
    }

}
