package thaumcraft.api.listeners.lamp.fertility.check;

import lombok.AllArgsConstructor;
import net.minecraft.world.entity.animal.Animal;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.lamp.fertility.IFertilityAffectiveEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.EntityTags.FERTILITY_LAMP_AFFECTIVE;
import static thaumcraft.common.entities.ThaumcraftEntities.EntityTags.FERTILITY_LAMP_NOT_AFFECTIVE;

public abstract class FertilityLampAffectiveChecker implements Comparable<FertilityLampAffectiveChecker> {
    public final int priority;

    public FertilityLampAffectiveChecker(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull FertilityLampAffectiveChecker o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void test(FertilityLampAffectiveContext context);

    @AllArgsConstructor
    public enum DefaultImpl {
        TAG_AVOID_CHECK(new FertilityLampAffectiveChecker(0) {
            @Override
            public void test(FertilityLampAffectiveContext context) {
                tagAvoidCheck(context);
            }
        }),
        TAG_CHECK(new FertilityLampAffectiveChecker(100) {
            @Override
            public void test(FertilityLampAffectiveContext context) {
                tagCheck(context);
            }
        }),
        COMMON_ANIMAL_CHECK(
                new FertilityLampAffectiveChecker(200) {
                    @Override
                    public void test(FertilityLampAffectiveContext context) {
                        commonAnimalCheck(context);
                    }
                }
        ),
        INSTANCE_CHECK(new FertilityLampAffectiveChecker(300) {
            @Override
            public void test(FertilityLampAffectiveContext context) {
                instanceCheck(context);
            }
        })
        ;

        public static void instanceCheck(FertilityLampAffectiveContext context) {
            if (context.entity instanceof IFertilityAffectiveEntity){
                context.recommendAffective = true;
                context.endCheck = true;
            }
        }

        public static void commonAnimalCheck(FertilityLampAffectiveContext context) {
            if (context.entity instanceof Animal){
                context.recommendAffective = true;
                context.endCheck = true;
            }
        }

        public static void tagAvoidCheck(FertilityLampAffectiveContext context) {
            if (context.entity.getType().is(FERTILITY_LAMP_NOT_AFFECTIVE)){
                context.recommendAffective = false;
                context.endCheck = true;
            }
        }

        public static void tagCheck(FertilityLampAffectiveContext context) {
            if (context.entity.getType().is(FERTILITY_LAMP_AFFECTIVE)){
                context.recommendAffective = true;
                context.endCheck = true;
            }
        }

        public final FertilityLampAffectiveChecker checker;

    }
}
