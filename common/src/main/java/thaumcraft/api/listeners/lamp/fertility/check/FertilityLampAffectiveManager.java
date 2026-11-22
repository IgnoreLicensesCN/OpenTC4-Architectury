package thaumcraft.api.listeners.lamp.fertility.check;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import com.linearity.opentc4.utils.collectionlike.parameterizedtypechaos.EntityCategoryToEntitiesMap;
import net.minecraft.world.entity.Entity;


public class FertilityLampAffectiveManager {
    public static final ListenerManager<FertilityLampAffectiveChecker> AFFECTIVE_CONDITIONS = new ListenerManager<>();

    public static boolean isAffectiveEntity(Entity entity, EntityCategoryToEntitiesMap entityMap) {
        FertilityLampAffectiveContext context = new FertilityLampAffectiveContext(entity,entityMap);
        for (var checker : AFFECTIVE_CONDITIONS.getListeners()){
            checker.test(context);
            if (context.endCheck){
                return context.recommendAffective;
            }
        }
        return context.recommendAffective;
    }

    public static void init(){
        for (var checker: FertilityLampAffectiveChecker.DefaultImpl.values()){
            AFFECTIVE_CONDITIONS.registerListener(checker.checker);
        }
    }
}
