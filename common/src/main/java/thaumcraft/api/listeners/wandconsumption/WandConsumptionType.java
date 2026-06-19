package thaumcraft.api.listeners.wandconsumption;

import thaumcraft.common.lib.resourcelocations.WandConsumptionTypeResourceLocation;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WandConsumptionType {
    public final WandConsumptionTypeResourceLocation id;

    public WandConsumptionType(WandConsumptionTypeResourceLocation id) {
        this.id = id;
        if (WAND_CONSUMPTION_TYPES.putIfAbsent(id, this) != this){
            throw new IllegalStateException("Duplicate Wand Consumption Type!" + id);
        }
    }

    private static final Map<WandConsumptionTypeResourceLocation,WandConsumptionType> WAND_CONSUMPTION_TYPES = new ConcurrentHashMap<>();
    public static final Map<WandConsumptionTypeResourceLocation,WandConsumptionType> WAND_CONSUMPTION_TYPES_VIEW = Collections.unmodifiableMap(WAND_CONSUMPTION_TYPES);
}
