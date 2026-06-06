package thaumcraft.common.runicshield;

import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.mixinaccessors.PlayerRunicShieldInfoMixinAccessor;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.network.playerdata.PacketRunicCapacityS2C;
import thaumcraft.common.lib.network.playerdata.PacketRunicChargeS2C;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//put into Player
//for other entities it's not supported yet.for TLM there might be TODO:[maybe wont finished]Make impl for maid(in another mod?)
//instance should bind to entity
public class EntityRunicShieldInfo {
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

    public EntityRunicShieldInfo() {
        //but if you store that entity instance here i will kick your ass(memory leak).
    }

    public void setTo(EntityRunicShieldInfo info) {
        this.rechargeDelay = info.rechargeDelay;
        this.shieldCharged.clear();
        this.shieldCapacity.clear();
        this.runicShieldAdditionalInfo.clear();
        this.shieldCharged.putAll(info.shieldCharged);
        this.shieldCapacity.putAll(info.shieldCapacity);
        this.runicShieldAdditionalInfo.putAll(info.runicShieldAdditionalInfo);
    }

    public boolean shouldSyncCharge = false;

    public void syncCapacitySendPacket(ServerPlayer player) {
        new PacketRunicCapacityS2C(this.shieldCapacity).sendTo(player);
    }

    public void syncChargeSendPacket(ServerPlayer player) {
        new PacketRunicChargeS2C(this.shieldCharged).sendTo(player);
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

    public static EntityRunicShieldInfo getFromPlayer(Player player) {
        return ((PlayerRunicShieldInfoMixinAccessor) player).opentc4$getPlayerRunicShieldInfo();
    }

    public static void setForPlayer(Player player, EntityRunicShieldInfo info) {
        getFromPlayer(player).setTo(info);
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
