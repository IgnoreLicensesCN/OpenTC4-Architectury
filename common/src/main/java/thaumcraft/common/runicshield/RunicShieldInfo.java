package thaumcraft.common.runicshield;

import com.linearity.opentc4.annotations.Modifiable;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketUpdateRunicCapacityS2C;
import thaumcraft.common.lib.network.playerdata.updatedata.PacketUpdateRunicChargeS2C;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//put into Player
//for other entities it's not supported yet.for TLM there might be TODO:[maybe wont finished]Make impl for maid(in another mod?)
//instance should bind to entity
public class RunicShieldInfo {
    public int rechargeDelay = 0;
    @ApiStatus.Internal
    public final @Modifiable Object2IntMap<AbstractRunicShieldType<?>> shieldCharged = new Object2IntOpenHashMap<>();
    @ApiStatus.Internal
    public final @Modifiable Object2IntSortedMap<AbstractRunicShieldType<?>> shieldCapacity = new Object2IntRBTreeMap<>();
    @ApiStatus.Internal
    public final Map<AbstractRunicShieldType<?>, Object /*needs Imagination--maybe we should not access others'*/>
            runicShieldAdditionalInfo = new ConcurrentHashMap<>();

    public int getShieldChargedFor(AbstractRunicShieldType<?> type) {
        return shieldCharged.getInt(type);
    }

    public int getShieldCapacityFor(AbstractRunicShieldType<?> type) {
        return shieldCapacity.getInt(type);
    }

    public void setShieldChargedFor(AbstractRunicShieldType<?> type, int charged) {
        shieldCharged.put(type, charged);
    }

    public void setShieldCapacityFor(AbstractRunicShieldType<?> type, int capacity) {
        shieldCapacity.put(type, capacity);
    }

    public int removeShieldChargedFor(AbstractRunicShieldType<?> type) {
        return shieldCharged.removeInt(type);
    }

    public int removeShieldCapacityFor(AbstractRunicShieldType<?> type) {
        return shieldCapacity.removeInt(type);
    }

    public void shieldChargedForEach(ObjectIntBiConsumer<AbstractRunicShieldType<?>> consumer) {
        shieldCharged.forEach(consumer);
    }

    public void shieldCapacityForEach(ObjectIntBiConsumer<AbstractRunicShieldType<?>> consumer) {
        shieldCapacity.forEach(consumer);
    }

    @FunctionalInterface
    public interface ShieldTypeAndAmountPredicate {
        boolean test(AbstractRunicShieldType<?> type, int amount);
    }

    //return true if broken
    public void shieldChargedForEachWithBreak(ShieldTypeAndAmountPredicate predicate) {
        for (var entry : shieldCharged.object2IntEntrySet()) {
            if (predicate.test(entry.getKey(), entry.getIntValue())) {
                break;
            }
        }
    }

    public void shieldCapacityForEachWithBreak(ShieldTypeAndAmountPredicate predicate) {
        for (var entry : shieldCapacity.object2IntEntrySet()) {
            if (predicate.test(entry.getKey(), entry.getIntValue())) {
                break;
            }
        }
    }


    public @Nullable <AdditionalInfoClass> AdditionalInfoClass getAdditionalInfo(AbstractRunicShieldType<AdditionalInfoClass> shieldType) {
        return (AdditionalInfoClass) this.runicShieldAdditionalInfo.get(this);
    }

    public <AdditionalInfoClass> void putAdditionalInfo(AbstractRunicShieldType<AdditionalInfoClass> shieldType, AdditionalInfoClass info) {
        this.runicShieldAdditionalInfo.put(shieldType, info);
    }

    public RunicShieldInfo() {
        //but if you store that entity instance here i will kick your ass(memory leak).
    }

    public boolean shouldSyncCharge = false;

    public void syncCapacitySendPacket(ServerPlayer player) {
        new PacketUpdateRunicCapacityS2C(this.shieldCapacity).sendTo(player);
    }

    public void syncChargeSendPacket(ServerPlayer player) {
        new PacketUpdateRunicChargeS2C(this.shieldCharged).sendTo(player);
    }

    public void syncCapacityClientSide(
            Object2IntMap<AbstractRunicShieldType<?>> shieldCapacity
    ) {
        this.shieldCapacity.clear();
        this.shieldCapacity.putAll(shieldCapacity);
    }

    public void syncChargeClientSide(
            Object2IntMap<AbstractRunicShieldType<?>> shieldCharged
    ) {
        this.shieldCharged.clear();
        this.shieldCharged.putAll(shieldCharged);
    }

    public static @Nullable RunicShieldInfo getFromLiving(LivingEntity living) {
        if (living instanceof IRunicShieldInfoOwnerLivingEntity owner) {
            return owner.getRunicShieldInfo();
        }
        return null;
    }

    public static void setForLiving(LivingEntity living, RunicShieldInfo info) {
        if (living instanceof IRunicShieldInfoOwnerLivingEntity owner) {
            owner.setPlayerRunicShieldInfo(info);
        }
    }

    public void randomCharge(int amountToCharge, RandomSource random) {
        if (shieldCapacity.isEmpty()) {
            return;
        }
        Object2IntMap<AbstractRunicShieldType<?>> required = new Object2IntOpenHashMap<>(shieldCapacity.size());
        shieldCharged.forEach(
                (key, value) -> {
                    int capacity = shieldCapacity.getInt(key);
                    int room = capacity - value;
                    if (room > 0) {
                        required.put(key, value);
                    }
                }
        );
        if (required.isEmpty()) {
            return;
        }
        var keys = new ArrayList<>(required.keySet().stream().toList());
        for (int i = 0; i < amountToCharge; i++) {
            if (required.isEmpty()) {
                return;
            }
            int randomIndex = random.nextInt(required.size());
            var pickingType = keys.get(randomIndex);
            shieldCharged.put(pickingType, shieldCharged.getInt(pickingType));
            int requiredAfterAdded = required.getInt(pickingType) - 1;
            if (requiredAfterAdded <= 0) {
                required.put(pickingType, requiredAfterAdded);
            } else {
                required.removeInt(pickingType);
            }
        }
    }

}
