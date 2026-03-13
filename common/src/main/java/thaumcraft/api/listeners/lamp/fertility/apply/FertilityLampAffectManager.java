package thaumcraft.api.listeners.lamp.fertility.apply;

import com.linearity.opentc4.simpleutils.ListenerManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.listeners.lamp.fertility.IFertilityAffectiveEntity;

import java.util.Set;

public class FertilityLampAffectManager {


    public static final ListenerManager<FertilityAffectApplier> affectActions = new ListenerManager<>();

    static {
        affectActions.registerListener(new FertilityAffectApplier(0) {
            @Override
            public void apply(FertilityLampAffectContext<? extends Entity> context) {
                if (IFertilityAffectiveEntity.class.isAssignableFrom(context.entityClass)) {
                    context.endAffect = true;
                    context.entities.forEach(e -> context.affected |= ((IFertilityAffectiveEntity)e).fertilityLampAffect(context));
                }
            }
        });
        affectActions.registerListener(new FertilityAffectApplier(100) {
            @Override
            public void apply(FertilityLampAffectContext<? extends Entity> context) {
                if (Animal.class.isAssignableFrom(context.entityClass)
                        && context.entities.size() < context.recommendedEntityLimit) {
                    var contextCasted = (FertilityLampAffectContext<Animal>) context;
                    var iterator = contextCasted.entities.iterator();
                    Animal partner = null;
                    while (iterator.hasNext()) {
                        var entity = iterator.next();
                        if (entity.getAge() != 0 && entity.canFallInLove()){
                            if (partner == null) {
                                partner = entity;
                            }else {
                                partner.setInLove(null);
                                entity.setInLove(null);
                                partner = null;
                                context.affected = true;
                            }
                        }
                    }
                    context.endAffect = true;
                }
            }
        });
    }

    public static<E extends Entity>  boolean affectEntity(
            Class<E> entity, @UnmodifiableView Set<E> entitiesOfClass, int recommendedEntityLimit) {
        var context = new FertilityLampAffectContext<>(entity,entitiesOfClass,recommendedEntityLimit);
        for (var checker : affectActions.getListeners()){
            checker.apply(context);
            if (context.endAffect){
                break;
            }
        }
        return context.affected;
    }
}
