package thaumcraft.common.lib.events;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.runicshield.IRunicShieldProviderItem;
import thaumcraft.common.runicshield.EntityRunicShieldInfo;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBauble;

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
            var capacityMapOld = new Object2IntOpenHashMap<>(shieldInfo.shieldCapacity);
            final CalcCacheableObject2IntMap<AbstractRunicShieldType<?>>[] calcCacheable = new CalcCacheableObject2IntMap[]{CalcCacheableObject2IntMap.EMPTY};
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                    ItemStack stack = player.getItemBySlot(slot);
                    if (stack.isEmpty()) {
                        continue;
                    }
                    if (stack.getItem() instanceof IRunicShieldProviderItem shieldProvider) {
                        calcCacheable[0] = calcCacheable[0].add(shieldProvider.getRunicCharge(stack),IRunicShieldProviderItem.SORTED_SHIELD_MAP_PROVIDER);
                    }
                }
            }

            forEachBauble(player, (a, stack, item) -> {
                if (stack.isEmpty()) {
                    return false;
                }
                if (stack.getItem() instanceof IRunicShieldProviderItem shieldProvider) {
                    calcCacheable[0] = calcCacheable[0].add(shieldProvider.getRunicCharge(stack),IRunicShieldProviderItem.SORTED_SHIELD_MAP_PROVIDER);
                }
                return false;
            });

            if (calcCacheable[0].wrapped.size() != capacityMapOld.size()) {
                shieldInfo.syncCapacitySendPacket(player);
                return;
            }
            for (var entry : calcCacheable[0].wrapped.object2IntEntrySet()) {
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
