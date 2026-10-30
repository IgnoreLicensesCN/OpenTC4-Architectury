package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.ai.goals.golem.GolemMoveToRestrictionCenterGoal;
import thaumcraft.common.entities.ai.goals.golem.GolemPlaceItemStackToHomeGoal;
import thaumcraft.common.entities.ai.goals.golem.GolemTakeItemStackFromContainerGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;
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
                                                && (creeper.getSwellDir() > 0 || creeper.isIgnited())
                        );
        public static final GoalResourceLocation GET_HOME_IF_FAR = GoalResourceLocation.of(Thaumcraft.MOD_ID, "get_home_if_far");
        public static final Function<BaseGolemEntity, Goal> GET_HOME_IF_FAR_GOAL = golem -> new GolemMoveToRestrictionCenterGoal(
                golem,
                1,
                2,
                3
        );
        public static final GoalResourceLocation WATCH_CLOSEST_PLAYER = GoalResourceLocation.of("watch_closest_player");
        public static final Function<BaseGolemEntity, Goal> WATCH_CLOSEST_PLAYER_GOAL_PROVIDER = golem -> new LookAtPlayerGoal(golem, Player.class, 6);
        public static final GoalResourceLocation IDLE = GoalResourceLocation.of(Thaumcraft.MOD_ID, "idle");
        public static final Function<BaseGolemEntity, Goal> IDLE_GOAL_PROVIDER = RandomLookAroundGoal::new;
        public static final GoalResourceLocation PLACE_ITEM_STACK_HOME = GoalResourceLocation.of(Thaumcraft.MOD_ID, "place_item_stack_home");
        public static final Function<BaseGolemEntity, Goal> PLACE_ITEM_STACK_HOME_DEFAULT_GOAL_PROVIDER = GolemPlaceItemStackToHomeGoal::new;
        public static final GoalResourceLocation DROP_ITEM_STACK_HOME_IF_NO_CONTAINER = GoalResourceLocation.of(Thaumcraft.MOD_ID,"drop_item_stack_home_if_no_container");
        public static final Function<BaseGolemEntity, Goal> DROP_ITEM_STACK_HOME_IF_NO_CONTAINER_GOAL_PROVIDER = GolemPlaceItemStackToHomeGoal::new;
        public static final GoalResourceLocation TAKE_ITEM_STACK_FROM_CONTAINER = GoalResourceLocation.of("take_item_stack_from_container");
        public static final Function<BaseGolemEntity,Goal> TAKE_ITEM_STACK_FROM_CONTAINER_GOAL_PROVIDER = GolemTakeItemStackFromContainerGoal::new;
    }

    default void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        goalSelector.addGoalIfNotExist(0, AVOID_CREEPER, AVOID_CREEPER_GOAL, golem);
        goalSelector.addGoalIfNotExist(6, GET_HOME_IF_FAR, GET_HOME_IF_FAR_GOAL, golem);
        goalSelector.addGoalIfNotExist(7, WATCH_CLOSEST_PLAYER, WATCH_CLOSEST_PLAYER_GOAL_PROVIDER, golem);
        goalSelector.addGoalIfNotExist(8, IDLE, IDLE_GOAL_PROVIDER, golem);
    }

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, GolemAbilitiesData abilitiesData) {
        registerGoalForGolem(golem, upgradeStack, abilitiesData.goalSelector, abilitiesData.targetSelector);
    }
}
