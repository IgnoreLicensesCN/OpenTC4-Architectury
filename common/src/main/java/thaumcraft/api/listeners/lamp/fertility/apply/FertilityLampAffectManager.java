package thaumcraft.api.listeners.lamp.fertility.apply;

import com.linearity.annotations.ShouldNotModify;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import com.linearity.opentc4.utils.collectionlike.parameterizedtypechaos.EntityCategoryToEntitiesMap;
import com.linearity.opentc4.utils.entity.category.EntityCategory;
import net.minecraft.world.entity.Entity;

import java.util.Set;

public class FertilityLampAffectManager {


    public static final ListenerManager<FertilityAffectApplier> AFFECT_ACTIONS = new ListenerManager<>();

    public static <E extends Entity> boolean affectEntity(EntityCategoryToEntitiesMap.UnifiedEntry<E> entry, int recommendedEntityLimit) {
        return affectEntity(entry.getKey(), entry.getValue(), recommendedEntityLimit);
    }
    public static<E extends Entity>  boolean affectEntity(
            EntityCategory<E> entity, @ShouldNotModify Set<E> entitiesOfType, int recommendedEntityLimit) {
        var context = new FertilityLampAffectContext<>(entity,entitiesOfType,recommendedEntityLimit);
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
