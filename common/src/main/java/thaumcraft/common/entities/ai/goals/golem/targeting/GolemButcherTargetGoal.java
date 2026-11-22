package thaumcraft.common.entities.ai.goals.golem.targeting;

import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import com.linearity.opentc4.utils.collectionlike.parameterizedtypechaos.EntityCategoryToEntitiesMap;
import com.linearity.opentc4.utils.entity.category.EntityCategoryByClass;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils.DoNotReallyPutInList;
import thaumcraft.common.entities.ai.goals.golem.AbstractGolemGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;

import java.util.Comparator;
import java.util.Iterator;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.LIVING_TEST;
import static thaumcraft.common.entities.ThaumcraftEntities.EntityTags.BUTCHER_GOLEM_WONT_KILL;

public class GolemButcherTargetGoal extends AbstractGolemGoal {
    public GolemButcherTargetGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected int getRange() {
        return golem.getHomeRange();
    }
    protected int getVerticalRange() {
        return 4;
    }

    protected EntityCategoryToEntitiesMap entitiesMapCache = new EntityCategoryToEntitiesMap();
    protected ButcherTargetChecker.ButcherTargetingContext context = new ButcherTargetChecker.ButcherTargetingContext(golem,entitiesMapCache);
    @Override
    public boolean canUse() {
        try {
            var currentTarget = golem.getTarget();
            if (currentTarget == null || currentTarget.isRemoved()){
                golem.setTarget(null);
            }
            var level = golem.level();
            context.reset();
            level.getEntities(
                    LIVING_TEST, AABB.ofSize(golem.position(),getRange(), getVerticalRange(),getRange()),
                    e -> ButcherTargetChecker.checked(e,context),
                    DoNotReallyPutInList.getInstance()
            );
            golem.setTarget(findBestTarget(golem,context.entityCategoryToEntitiesMap));
            return golem.getTarget() != null;
        }finally {
            entitiesMapCache.clear();
        }
    }

    protected Comparator<Entity> entityComparator = (e1,e2) -> Double.compare(e1.distanceToSqr(golem),e2.distanceTo(golem));
    protected @Nullable LivingEntity findBestTarget(BaseGolemEntity golem, EntityCategoryToEntitiesMap entitiesMap) {
        LivingEntity result = null;
        double minDistanceSqr = Double.POSITIVE_INFINITY;
        for (Iterator<EntityCategoryToEntitiesMap.UnifiedEntry<? extends Entity>> it = entitiesMap.entrySetIterator(); it.hasNext(); ) {
            var entry = it.next();
            var category = entry.getKey();
            var entities = entry.getValue();
            if (entities.size() > 2){
                var closestEntityCurrent = entities.stream().min(entityComparator).get();
                var distanceSqrCurrent = closestEntityCurrent.distanceToSqr(golem);
                if (minDistanceSqr > distanceSqrCurrent && closestEntityCurrent instanceof LivingEntity livingEntity){
                    result = livingEntity;
                }
            }
        }
        return result;
    }

    @AllArgsConstructor
    public static abstract class ButcherTargetChecker implements Comparable<ButcherTargetChecker> {
        public static final ListenerManager<ButcherTargetChecker> BUTCHER_TARGET_CHECKERS = new ListenerManager<>();
        public final int weight;

        @Override
        public int compareTo(@NotNull GolemButcherTargetGoal.ButcherTargetChecker o) {
            return Integer.compare(weight, o.weight);
        }
        public abstract void checkTargeting(LivingEntity entity,ButcherTargetingContext targetingContext);//true if do not continue check
        public static boolean checked(LivingEntity entity,ButcherTargetingContext targetingContext) {
            boolean result = false;
            for (var checker : BUTCHER_TARGET_CHECKERS.getListeners()){
                checker.checkTargeting(entity,targetingContext);
                result = result || targetingContext.added;
                if (targetingContext.endCheck){
                    break;
                }
            }
            return result;
        }
        @RequiredArgsConstructor
        public static class ButcherTargetingContext implements GolemHelper.Resettable {
            public final BaseGolemEntity golem;
            public final EntityCategoryToEntitiesMap entityCategoryToEntitiesMap;
            public boolean added = false;
            public boolean endCheck = false;

            @Override
            public void reset() {
                added = false;
                endCheck = false;
            }
        }
        @AllArgsConstructor
        public enum DefaultImpl {
            AVOID(
                    new ButcherTargetChecker(-100) {
                        @Override
                        public void checkTargeting(LivingEntity entity, ButcherTargetingContext targetingContext) {
                            tagAvoid(entity, targetingContext);
                        }
                    }
            ),
            ANIMAL(
                    new ButcherTargetChecker(0) {
                        @Override
                        public void checkTargeting(LivingEntity entity, ButcherTargetingContext targetingContext) {
                            animalCheck(entity, targetingContext);
                        }
                    }
            ),
            ;

            private static void tagAvoid(LivingEntity entity, ButcherTargetingContext targetingContext) {
                if (entity.getType().is(BUTCHER_GOLEM_WONT_KILL)){
                    targetingContext.endCheck = true;
                }
            }

            private static void animalCheck(LivingEntity entity, ButcherTargetingContext targetingContext) {
                if (entity instanceof Animal animal && !animal.isBaby() && !(animal instanceof Enemy)){
                    targetingContext.entityCategoryToEntitiesMap.add(
                            EntityCategoryByClass.of((Class<Animal>) animal.getClass()),
                            animal
                    );
                    targetingContext.added = true;
                }
            }

            public final ButcherTargetChecker checker;
        }
    }
}
