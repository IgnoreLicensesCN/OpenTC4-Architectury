package thaumcraft.api.listeners.lamp.fertility.check;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import thaumcraft.api.listeners.lamp.fertility.IFertilityAffectiveEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.EntityTags.FERTILITY_LAMP_AFFECTIVE;
import static thaumcraft.common.entities.ThaumcraftEntities.EntityTags.FERTILITY_LAMP_NOT_AFFECTIVE;


public class FertilityLampAffectiveManager {
    public static final ListenerManager<FertilityLampAffectiveChecker> affectiveConditions = new ListenerManager<>();

    static {
        affectiveConditions.registerListener(new FertilityLampAffectiveChecker(0) {
            @Override
            public void test(FertilityLampAffectiveContext context) {
                if (context.entity.getType().is(FERTILITY_LAMP_NOT_AFFECTIVE)){
                    context.recommendAffective = false;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new FertilityLampAffectiveChecker(100) {
            @Override
            public void test(FertilityLampAffectiveContext context) {
                if (context.entity.getType().is(FERTILITY_LAMP_AFFECTIVE)){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new FertilityLampAffectiveChecker(200) {
            @Override
            public void test(FertilityLampAffectiveContext context) {
                if (context.entity instanceof Animal){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
        affectiveConditions.registerListener(new FertilityLampAffectiveChecker(300) {
            @Override
            public void test(FertilityLampAffectiveContext context) {
                if (context.entity instanceof IFertilityAffectiveEntity){
                    context.recommendAffective = true;
                    context.endCheck = true;
                }
            }
        });
    }
    public static boolean isAffectiveEntity(Entity entity) {
        FertilityLampAffectiveContext context = new FertilityLampAffectiveContext(entity);
        for (var checker : affectiveConditions.getListeners()){
            checker.test(context);
            if (context.endCheck){
                return context.recommendAffective;
            }
        }
        return context.recommendAffective;
    }
}
