package thaumcraft.api.listeners.wandconsumption;

import com.linearity.opentc4.OpenTC4;
import thaumcraft.common.lib.resourcelocations.WandConsumptionTypeResourceLocation;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.OpenTC4.logDuplicate;
import static com.linearity.opentc4.OpenTC4.throwDuplicate;

public class WandConsumptionType {
    public final WandConsumptionTypeResourceLocation id;

    public WandConsumptionType(WandConsumptionTypeResourceLocation id) {
        this.id = id;
        if (WAND_CONSUMPTION_TYPES.putIfAbsent(id, this) != this){
            var exception = new IllegalStateException("Duplicate Wand Consumption Type!" + id);
            if (throwDuplicate){
                throw exception;
            }else if (logDuplicate){
                OpenTC4.LOGGER.error(exception);
            }
        }
    }

    private static final Map<WandConsumptionTypeResourceLocation,WandConsumptionType> WAND_CONSUMPTION_TYPES = new ConcurrentHashMap<>();
    public static final Map<WandConsumptionTypeResourceLocation,WandConsumptionType> WAND_CONSUMPTION_TYPES_VIEW = Collections.unmodifiableMap(WAND_CONSUMPTION_TYPES);
}
