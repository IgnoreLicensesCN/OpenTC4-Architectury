package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.ai.goals.GolemMoveToRestrictionCenterGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.lib.resourcelocations.GoalResourceLocation;

import java.util.function.Function;

import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

public interface IGolemAIProviderItem extends IGolemAbilityModifierItem {
    class DefaultGoalsAndIDs {
        public static final GoalResourceLocation AVOID_CREEPER = GoalResourceLocation.of(Thaumcraft.MOD_ID, "avoid_creeper");
        public static final Function<BaseGolemEntity, Goal> AVOID_CREEPER_GOAL =
                golem ->
                        new AvoidEntityGoal<>(
                golem,
                Creeper.class,
                9,
                1.125,
                1.25,
                probablyCreeper ->
                        probablyCreeper instanceof Creeper creeper
                                && (creeper.getSwellDir()>0 || creeper.isIgnited())
        );
        public static final GoalResourceLocation GET_HOME_IF_FAR = GoalResourceLocation.of(Thaumcraft.MOD_ID, "get_home_if_far");
        public static final Function<BaseGolemEntity,Goal> GET_HOME_IF_FAR_GOAL = golem -> new GolemMoveToRestrictionCenterGoal(
                golem,
                1,
                2,
                3
        );
    }
    default void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector){
        goalSelector.addGoalIfNotExist(0,AVOID_CREEPER, AVOID_CREEPER_GOAL,golem);
        goalSelector.addGoalIfNotExist(6,GET_HOME_IF_FAR,GET_HOME_IF_FAR_GOAL,golem);
        //this.tasks.addTask(6, new AIReturnHome(this));
        //this.tasks.addTask(7, new EntityAIWatchClosest(this, Player.class, 6.0F));
        //this.tasks.addTask(8, new EntityAILookIdle(this));
        //TODO
    }

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, BaseGolemEntity.GolemAbilitiesData abilitiesData) {
        registerGoalForGolem(golem, upgradeStack, abilitiesData.goalSelector,abilitiesData.targetSelector);
    }
}
