package thaumcraft.api.listeners.lamp.fertility.apply;

import com.linearity.opentc4.annotations.ShouldNotModify;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import com.linearity.opentc4.utils.collectionlike.parameterizedtypechaos.ClassToEntitiesMap;
import net.minecraft.world.entity.Entity;

import java.util.Set;

public class FertilityLampAffectManager {


    public static final ListenerManager<FertilityAffectApplier> AFFECT_ACTIONS = new ListenerManager<>();

    public static <E extends Entity> boolean affectEntity(ClassToEntitiesMap.UnifiedEntry<E> entry, int recommendedEntityLimit) {
        return affectEntity(entry.getKey(), entry.getValue(), recommendedEntityLimit);
    }
    public static<E extends Entity>  boolean affectEntity(
            Class<E> entity, @ShouldNotModify Set<E> entitiesOfClass, int recommendedEntityLimit) {
        var context = new FertilityLampAffectContext<>(entity,entitiesOfClass,recommendedEntityLimit);
        for (var checker : AFFECT_ACTIONS.getListeners()){
            checker.apply(context);
            if (context.endAffect){
                break;
            }
        }
        return context.affected;
    }

    public static void init(){
        for (var checkerEnum:FertilityAffectApplier.DefaultImpl.values()){
            AFFECT_ACTIONS.registerListener(checkerEnum.context);
        }
    }
}
