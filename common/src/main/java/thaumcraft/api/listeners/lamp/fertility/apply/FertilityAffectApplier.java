package thaumcraft.api.listeners.lamp.fertility.apply;

import com.linearity.opentc4.utils.entity.category.EntityCategoryByClass;
import lombok.AllArgsConstructor;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.lamp.fertility.IFertilityAffectiveEntity;

import java.util.HashSet;
import java.util.Set;

public abstract class FertilityAffectApplier implements Comparable<FertilityAffectApplier> {
    public final int priority;

    public FertilityAffectApplier(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull FertilityAffectApplier o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void apply(FertilityLampAffectContext<?,?> context);

    @AllArgsConstructor
    public enum DefaultImpl {
        INSTANCE_APPLY(new FertilityAffectApplier(0) {
            @Override
            public void apply(FertilityLampAffectContext<?,?> context) {
                instanceApply(context);
            }
        }),
        ANIMAL_APPLY(new FertilityAffectApplier(100) {
            @Override
            public void apply(FertilityLampAffectContext<?,?> context) {
                animalApply(context);
            }
        })
        ;
        public final FertilityAffectApplier context;
    }

    public static Set<Class<? extends Animal>> ANIMAL_FERTILITY_BLACKLISTS = new HashSet<>();
    private static void animalApply(FertilityLampAffectContext<?,?> context) {
        if (context.entities.size() < context.recommendedEntityLimit
                && context.entityCategory instanceof EntityCategoryByClass<?> categoryByClass
                && Animal.class.isAssignableFrom(categoryByClass.clz)
                && !ANIMAL_FERTILITY_BLACKLISTS.contains(categoryByClass.clz)
        ) {
            var contextCasted = (FertilityLampAffectContext<?,? extends Animal>) context;
            var iterator = contextCasted.entities.iterator();
            while(true){
                Animal one = null;
                Animal another = null;
                if (iterator.hasNext()){
                    one = iterator.next();
                } else {
                    break;
                }
                if (iterator.hasNext()){
                    another = iterator.next();
                } else {
                    break;
                }
                one.setInLove(null);
                another.setInLove(null);
                context.affected = true;
            }
            context.endAffect = true;
        }
    }

    public static void instanceApply(FertilityLampAffectContext<?,?> context) {
        if (context.entityCategory instanceof IFertilityAffectiveEntity.IFertilityAffectiveCategory<?,?> affectiveCategory
        ) {
            var contextCasted = context.castWith(affectiveCategory);
            var iterator = contextCasted.entities.iterator();
            while(true){
                IFertilityAffectiveEntity<?,?> one = null;
                IFertilityAffectiveEntity<?,?> another = null;
                if (iterator.hasNext()){
                    one = iterator.next();
                } else {
                    break;
                }
                if (iterator.hasNext()){
                    another = iterator.next();
                } else {
                    break;
                }
                context.affected |= one.fertilityLampAffect(
                        (FertilityLampAffectContext)context)
                        && another.fertilityLampAffect(
                                (FertilityLampAffectContext)context
                );
            }
            context.endAffect = true;
        }
    }
}
