package thaumcraft.common.runicshield;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import static com.linearity.opentc4.utils.equip.bauble.BaubleUtils.forEachBauble;

public class RunicShieldHandler {

    public static void updateRunicShieldFor(LivingEntity living) {
        var shieldInfo = RunicShieldInfo.getFromLiving(living);
        if (shieldInfo == null) {
            return;
        }
        if (living.tickCount % 40 == 0) {
            updateRunicShieldCapacityForPlayer(living,shieldInfo);
        }
        rechargeRunicShieldForPlayer(living,shieldInfo);
    }
    public static void rechargeRunicShieldForPlayer(LivingEntity living, RunicShieldInfo shieldInfo) {
        if (shieldInfo.rechargeDelay > 0) {
            shieldInfo.rechargeDelay -= 1;
        }

        shieldInfo.shieldCapacity.keySet().forEach(
                runicShieldType -> runicShieldType.rechargeTickForLiving(living,shieldInfo)
        );
        if (shieldInfo.shouldSyncCharge){
            shieldInfo.shouldSyncCharge = false;
            if (living instanceof ServerPlayer serverPlayer) {
                shieldInfo.syncChargeSendPacket(serverPlayer);
            }
        }
    }
    public static void updateRunicShieldCapacityForPlayer(LivingEntity entity, RunicShieldInfo shieldInfo) {

        var capacityMapOld = new Object2IntOpenHashMap<>(shieldInfo.shieldCapacity);
        final CalcCacheableObject2IntMap<AbstractRunicShieldType<?>>[] calcCacheable = new CalcCacheableObject2IntMap[]{CalcCacheableObject2IntMap.EMPTY};
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = entity.getItemBySlot(slot);
                if (stack.isEmpty()) {
                    continue;
                }
                if (stack.getItem() instanceof IRunicShieldProviderItem shieldProvider) {
                    calcCacheable[0] = calcCacheable[0].add(shieldProvider.getRunicCharge(stack),IRunicShieldProviderItem.SORTED_SHIELD_MAP_PROVIDER);
                }
            }
        }

        forEachBauble(entity, (a, stack, item) -> {
            if (stack.isEmpty()) {
                return false;
            }
            if (stack.getItem() instanceof IRunicShieldProviderItem shieldProvider) {
                calcCacheable[0] = calcCacheable[0].add(shieldProvider.getRunicCharge(stack),IRunicShieldProviderItem.SORTED_SHIELD_MAP_PROVIDER);
            }
            return false;
        });

        if (entity instanceof ServerPlayer serverPlayer) {
            if (calcCacheable[0].wrapped.size() != capacityMapOld.size()) {
                shieldInfo.syncCapacitySendPacket(serverPlayer);
                return;
            }
            for (var entry : calcCacheable[0].wrapped.object2IntEntrySet()) {
                var type = entry.getKey();
                var capacity = entry.getIntValue();
                if (capacityMapOld.getInt(type) != capacity) {
                    shieldInfo.syncCapacitySendPacket(serverPlayer);
                    break;
                }
            }
        }
    }
}
