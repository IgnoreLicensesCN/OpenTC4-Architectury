package thaumcraft.common.lib.events;

import com.linearity.opentc4.mixinaccessors.PlayerRunicShieldInfoMixinAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.lib.network.playerdata.PacketRunicCapacityS2C;
import thaumcraft.common.runicshield.IRunicShieldProviderItem;
import thaumcraft.common.runicshield.RunicShieldType;

import static com.linearity.opentc4.simpleutils.bauble.BaubleUtils.forEachBauble;

public class RunicShieldHandler {

    public static void updateRunicShieldForPlayer(ServerPlayer player) {
        if (player.tickCount % 40 == 0) {
            updateRunicShieldCapacityForPlayer(player);
        }
        rechargeRunicShieldForPlayer(player);
    }
    public static void rechargeRunicShieldForPlayer(ServerPlayer player) {
        var shieldInfo = ((PlayerRunicShieldInfoMixinAccessor)player).opentc4$getPlayerRunicShieldInfo();
        if (shieldInfo.rechargeDelay > 0) {
            shieldInfo.rechargeDelay -= 1;
        }

        shieldInfo.shieldCapacity.keySet().forEach(
                runicShieldType -> runicShieldType.rechargeTickForPlayer(player)
        );
        if (shieldInfo.shouldSync){
            shieldInfo.shouldSync = false;
            shieldInfo.scheduleSyncTo(player);
        }
    }
    public static void updateRunicShieldCapacityForPlayer(ServerPlayer player) {
        var shieldInfo = ((PlayerRunicShieldInfoMixinAccessor)player).opentc4$getPlayerRunicShieldInfo();

        var capacityMap = shieldInfo.shieldCapacity;
        var capacityMapOld = new Object2IntOpenHashMap<>(shieldInfo.shieldCapacity);
        capacityMap.clear();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem() instanceof IRunicShieldProviderItem shieldProvider) {
                    capacityMap.putAll(shieldProvider.getRunicCharge(stack));
                }
            }
        }

        forEachBauble(player,(a,stack,item) -> {
            if (stack.isEmpty()) {
                return false;
            }
            if (stack.getItem() instanceof IRunicShieldProviderItem shieldProvider) {
                capacityMap.putAll(shieldProvider.getRunicCharge(stack));
            }
            return false;
        });
        if (capacityMap.size() != capacityMapOld.size()) {
            new PacketRunicCapacityS2C(capacityMap).sendTo(player);
            return;
        }
        for (var entry : capacityMap.object2IntEntrySet()) {
            var type = entry.getKey();
            var capacity = entry.getIntValue();
            if (capacityMapOld.getInt(type) != capacity) {
                new PacketRunicCapacityS2C(capacityMap).sendTo(player);
                break;
            }
        }
    }
    public static void putShieldForPlayer(Player player, Object2IntMap<RunicShieldType> runicShieldWithType){
        //TODO
    }
}
