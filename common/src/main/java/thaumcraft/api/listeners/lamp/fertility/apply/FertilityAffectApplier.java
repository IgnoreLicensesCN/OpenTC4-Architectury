package thaumcraft.api.listeners.lamp.fertility.apply;

import lombok.AllArgsConstructor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.lamp.fertility.IFertilityAffectiveEntity;

public abstract class FertilityAffectApplier implements Comparable<FertilityAffectApplier> {
    public final int priority;

    public FertilityAffectApplier(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull FertilityAffectApplier o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void apply(FertilityLampAffectContext<? extends Entity> context);

    @AllArgsConstructor
    public enum DefaultImpl {
        INSTANCE_APPLY(new FertilityAffectApplier(0) {
            @Override
            public void apply(FertilityLampAffectContext<? extends Entity> context) {
                instanceApply(context);
            }
        }),
        ANIMAL_APPLY(new FertilityAffectApplier(100) {
            @Override
            public void apply(FertilityLampAffectContext<? extends Entity> context) {
                animalApply(context);
            }
        })
        ;
        public final FertilityAffectApplier context;
    }

    private static void animalApply(FertilityLampAffectContext<? extends Entity> context) {
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
                    } else {
                        partner.setInLove(null);
                        entity.setInLove(null);
                        context.affected = true;
                        partner = null;
//                        break;
                    }
                }
            }
            context.endAffect = true;
        }
    }

    public static void instanceApply(FertilityLampAffectContext<? extends Entity> context) {
        if (IFertilityAffectiveEntity.class.isAssignableFrom(context.entityClass)) {
            context.endAffect = true;
            context.entities.forEach(e -> context.affected |= ((IFertilityAffectiveEntity)e).fertilityLampAffect(context));
        }
    }

}
