package thaumcraft.common.lib.events;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntRBTreeMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.runicshield.IRunicShieldProviderItem;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import static com.linearity.opentc4.utils.bauble.BaubleUtils.forEachBauble;

public class RunicShieldHandler {

    public static void updateRunicShieldForPlayer(ServerPlayer player) {
        var shieldInfo = EntityRunicShieldInfo.getFromPlayer(player);
        if (player.tickCount % 40 == 0) {
            updateRunicShieldCapacityForPlayer(player,shieldInfo);
        }
        rechargeRunicShieldForPlayer(player,shieldInfo);
    }
    public static void rechargeRunicShieldForPlayer(Entity entity,EntityRunicShieldInfo shieldInfo) {
        if (shieldInfo.rechargeDelay > 0) {
            shieldInfo.rechargeDelay -= 1;
        }

        shieldInfo.shieldCapacity.keySet().forEach(
                runicShieldType -> runicShieldType.rechargeTickForEntity(entity,shieldInfo)
        );
        if (shieldInfo.shouldSyncCharge){
            shieldInfo.shouldSyncCharge = false;
            if (entity instanceof ServerPlayer serverPlayer) {
                shieldInfo.syncChargeSendPacket(serverPlayer);
            }
        }
    }
    public static void updateRunicShieldCapacityForPlayer(Entity entity,EntityRunicShieldInfo shieldInfo) {

        if (entity instanceof ServerPlayer player) {
            var capacityMap = shieldInfo.shieldCapacity;

            var capacityMapOld = new Object2IntOpenHashMap<>(shieldInfo.shieldCapacity);
            capacityMap.clear();
            var capacityMapCache = new Object2IntRBTreeMap<>(AbstractRunicShieldType::compareTo);//i want its order
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack stack = player.getItemBySlot(slot);
                    if (stack.isEmpty()) {
                        continue;
                    }
                    if (stack.getItem() instanceof IRunicShieldProviderItem shieldProvider) {
                        capacityMapCache.putAll(shieldProvider.getRunicCharge(stack));
                    }
                }
            }

            forEachBauble(player, (a, stack, item) -> {
                if (stack.isEmpty()) {
                    return false;
                }
                if (stack.getItem() instanceof IRunicShieldProviderItem shieldProvider) {
                    capacityMapCache.putAll(shieldProvider.getRunicCharge(stack));
                }
                return false;
            });
            capacityMap.putAll(capacityMapCache);


            if (capacityMap.size() != capacityMapOld.size()) {
                shieldInfo.syncCapacitySendPacket(player);
                return;
            }
            for (var entry : capacityMap.object2IntEntrySet()) {
                var type = entry.getKey();
                var capacity = entry.getIntValue();
                if (capacityMapOld.getInt(type) != capacity) {
                    shieldInfo.syncCapacitySendPacket(player);
                    break;
                }
            }
        }
    }
}
