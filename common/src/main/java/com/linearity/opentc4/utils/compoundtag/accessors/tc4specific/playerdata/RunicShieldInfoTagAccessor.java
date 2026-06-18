package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.RunicShieldTypeResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.collection.ModifiableMapValueAccessorFromKeyTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.collection.Object2IntLinkedOpenHashMapAccessor;
import it.unimi.dsi.fastutil.Function;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.RunicShieldInfo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType.RUNIC_SHIELD_TYPES_VIEW;

public class RunicShieldInfoTagAccessor extends CompoundTagAccessor<RunicShieldInfo> {
    private static final Map<RunicShieldTypeResourceLocation,CompoundTagAccessor<?>> additionalInfoAccessors = new HashMap<>();
    {
        RUNIC_SHIELD_TYPES_VIEW.forEach((key,value)->{
            var accessor = value.getOwningTagAccessor();
            if (accessor != null) {
                additionalInfoAccessors.put(key, accessor);
            }
        });
    }

    private final CompoundTagAccessorImpl warpTagAccessor = new CompoundTagAccessorImpl(tagKey);
    private final IntTagAccessor rechargeDelayAccessor = new IntTagAccessor(tagKey + "_recharge_delay");
    private final Object2IntLinkedOpenHashMapAccessor<RunicShieldTypeResourceLocation> shieldChargedAccessor
            = new Object2IntLinkedOpenHashMapAccessor<>(
                    tagKey + "_shield_charged",
            new RunicShieldTypeResourceLocationTagAccessor("r"),
            new IntTagAccessor("i")
    );
    private final Object2IntLinkedOpenHashMapAccessor<RunicShieldTypeResourceLocation> shieldCapacityAccessor
            = new Object2IntLinkedOpenHashMapAccessor<>(
            tagKey + "_shield_capacity",
            new RunicShieldTypeResourceLocationTagAccessor("r"),
            new IntTagAccessor("i")
    );
    private final ModifiableMapValueAccessorFromKeyTagAccessor<RunicShieldTypeResourceLocation> additionalInfoAccessor
            = new ModifiableMapValueAccessorFromKeyTagAccessor<>(
            tagKey + "_additional_info",
            new RunicShieldTypeResourceLocationTagAccessor("r"),
            (Function<RunicShieldTypeResourceLocation, CompoundTagAccessor<Object>>)
                    key -> (CompoundTagAccessor<Object>) additionalInfoAccessors.get(key)
    );

    public RunicShieldInfoTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public RunicShieldInfo readFromCompoundTag(CompoundTag tag) {
        tag = warpTagAccessor.readFromCompoundTag(tag);
        RunicShieldInfo result = new RunicShieldInfo();
        result.rechargeDelay = rechargeDelayAccessor.readIntFromCompoundTag(tag);
        {
            var capacityMap = shieldCapacityAccessor.readFromCompoundTag(tag);
            capacityMap.forEach((key, value) -> {
                var type = RUNIC_SHIELD_TYPES_VIEW.get(key);
                if (type != null) {
                    result.setShieldCapacityFor(type, value);
                }
            });
        }
        {
            var chargedMap = shieldChargedAccessor.readFromCompoundTag(tag);
            chargedMap.forEach((key, value) -> {
                var type = RUNIC_SHIELD_TYPES_VIEW.get(key);
                if (type != null) {
                    result.setShieldChargedFor(type, value);
                }
            });
        }
        {
            var additionalInfoMap = additionalInfoAccessor.readFromCompoundTag(tag);
            additionalInfoMap.forEach((key, value) -> {
                var type = RUNIC_SHIELD_TYPES_VIEW.get(key);
                if (type != null) {
                    result.runicShieldAdditionalInfo.put(type, value);
                }
            });
        }
        return result;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tagOuter, RunicShieldInfo value) {
        var tag = new CompoundTag();
        rechargeDelayAccessor.writeIntToCompoundTag(tag,value.rechargeDelay);
        {
            var capacityToWrite = new Object2IntLinkedOpenHashMap<RunicShieldTypeResourceLocation>();
            value.shieldCapacityForEach(
                    (k, v) -> capacityToWrite.put(k.id, v)
            );
            shieldCapacityAccessor.writeToCompoundTag(tag, capacityToWrite);
        }
        {
            var chargedToWrite = new Object2IntLinkedOpenHashMap<RunicShieldTypeResourceLocation>();
            value.shieldChargedForEach(
                    (k, v) -> chargedToWrite.put(k.id, v)
            );
            shieldChargedAccessor.writeToCompoundTag(tag, chargedToWrite);
        }
        {
            Map<RunicShieldTypeResourceLocation,Object> additionalToWrite = new LinkedHashMap<>(value.runicShieldAdditionalInfo.size());
            value.runicShieldAdditionalInfo.forEach(
                    (k, v) -> additionalToWrite.put(k.id, v)
            );
            additionalInfoAccessor.writeToCompoundTag(tag,additionalToWrite);
        }
        warpTagAccessor.writeToCompoundTag(tagOuter,tag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return warpTagAccessor.compoundTagHasKey(tag);
    }
}
