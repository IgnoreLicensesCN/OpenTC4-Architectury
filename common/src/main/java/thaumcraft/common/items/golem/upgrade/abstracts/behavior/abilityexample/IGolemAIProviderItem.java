package thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.ai.goals.golem.*;
import thaumcraft.common.entities.ai.goals.golem.miscjobs.*;
import thaumcraft.common.entities.ai.goals.golem.targeting.GolemHurtByTargetGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.essentiainteraction.GolemFillInEssentiaGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.essentiainteraction.GolemGatherEssentiaGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.essentiainteraction.GolemGotoFillInEssentiaGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.*;
import thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.gotogoal.container.GolemGotoContainerForEmptyGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.gotogoal.container.GolemGotoContainerForFillingGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.gotogoal.pickup.GolemPickupItemGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.throwgoal.GolemThrowItemStackHomeIfNoContainerGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.throwgoal.GolemThrowItemStackToMarkedContainerGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.liquidinteraction.GolemFillInLiquidGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.liquidinteraction.GolemGetLiquidGoal;
import thaumcraft.common.entities.ai.goals.golem.transportresources.liquidinteraction.GolemGotoGetLiquidGoal;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemAbilitiesData;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationKey;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemAbilityModifierItem;
import thaumcraft.common.lib.resourcelocations.GoalResourceLocation;

import java.util.Set;
import java.util.function.Function;

import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.*;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.TARGET_SELECTOR_PLAYER_CONFIG_KEY;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

public interface IGolemAIProviderItem extends IGolemAbilityModifierItem {
    class DefaultGoalsAndIDs {
        public interface GolemGoalProvider extends Function<BaseGolemEntity, Goal>{}
        public static final GoalResourceLocation AVOID_CREEPER = GoalResourceLocation.of(Thaumcraft.MOD_ID, "avoid_creeper");
        public static final GolemGoalProvider AVOID_CREEPER_GOAL =
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
        public static final GolemGoalProvider GET_HOME_IF_FAR_GOAL = golem -> new GolemMoveToRestrictionCenterGoal(
                golem,
                1,
                2,
                3
        );
        public static final GoalResourceLocation WATCH_CLOSEST_PLAYER = GoalResourceLocation.of(Thaumcraft.MOD_ID,"watch_closest_player");
        public static final GolemGoalProvider WATCH_CLOSEST_PLAYER_GOAL_PROVIDER = golem -> new LookAtPlayerGoal(golem, Player.class, 6);
        public static final GoalResourceLocation IDLE = GoalResourceLocation.of(Thaumcraft.MOD_ID, "idle");
        public static final GolemGoalProvider IDLE_GOAL_PROVIDER = RandomLookAroundGoal::new;
        public static final GoalResourceLocation PLACE_ITEM_STACK_HOME = GoalResourceLocation.of(Thaumcraft.MOD_ID, "place_item_stack_home");
        public static final GolemGoalProvider PLACE_ITEM_STACK_HOME_DEFAULT_GOAL_PROVIDER = GolemPlaceItemStackToHomeGoal::new;
        public static final GoalResourceLocation DROP_ITEM_STACK_HOME_IF_NO_CONTAINER = GoalResourceLocation.of(Thaumcraft.MOD_ID,"drop_item_stack_home_if_no_container");
        public static final GolemGoalProvider DROP_ITEM_STACK_HOME_IF_NO_CONTAINER_GOAL_PROVIDER = GolemThrowItemStackHomeIfNoContainerGoal::new;
        public static final GoalResourceLocation TAKE_ITEM_STACK_FROM_CONTAINER = GoalResourceLocation.of(Thaumcraft.MOD_ID,"take_item_stack_from_container");
        public static final GolemGoalProvider TAKE_ITEM_STACK_FROM_CONTAINER_GOAL_PROVIDER = GolemTakeItemStackFromContainerGoal::new;
        public static final GoalResourceLocation GOTO_CONTAINER_FOR_FILLING = GoalResourceLocation.of(Thaumcraft.MOD_ID,"goto_container_for_filling");
        public static final GolemGoalProvider GOTO_CONTAINER_FOR_FILLING_GOAL_PROVIDER = GolemGotoContainerForFillingGoal::new;
        public static final GoalResourceLocation EMPTY_CORE_PLACE_ITEM_STACK_HOME = GoalResourceLocation.of(Thaumcraft.MOD_ID,"empty_core_place_item_stack_home");
        public static final GolemGoalProvider EMPTY_CORE_PLACE_ITEM_STACK_HOME_GOAL_PROVIDER = GolemEmptyCorePlaceItemStackToHomeGoal::new;
        public static final GoalResourceLocation PLACE_ITEM_STACK_TO_MARKED_CONTAINER = GoalResourceLocation.of(Thaumcraft.MOD_ID,"place_item_stack_to_marked_container");
        public static final GolemGoalProvider PLACE_ITEM_STACK_TO_MARKED_CONTAINER_GOAL_PROVIDER = GolemPlaceItemStackToMarkedContainerGoal::new;
        public static final GoalResourceLocation THROW_ITEM_STACK_TO_MARKED_CONTAINER = GoalResourceLocation.of(Thaumcraft.MOD_ID,"throw_item_stack_to_marked_container");
        public static final GolemGoalProvider THROW_ITEM_STACK_TO_MARKED_CONTAINER_GOAL_PROVIDER = GolemThrowItemStackToMarkedContainerGoal::new;
        public static final GoalResourceLocation GOTO_CONTAINER_FOR_EMPTY = GoalResourceLocation.of(Thaumcraft.MOD_ID,"goto_container_for_empty");
        public static final GolemGoalProvider GOTO_CONTAINER_FOR_EMPTY_GOAL_PROVIDER = GolemGotoContainerForEmptyGoal::new;
        public static final GoalResourceLocation TAKE_ITEM_STACK_FROM_HOME = GoalResourceLocation.of(Thaumcraft.MOD_ID,"take_item_stack_from_home");
        public static final GolemGoalProvider TAKE_ITEM_STACK_FROM_HOME_GOAL_PROVIDER = GolemTakeItemStackFromHomeGoal::new;
        public static final GoalResourceLocation PICKUP_ITEM = GoalResourceLocation.of(Thaumcraft.MOD_ID,"pickup_item");
        public static final GolemGoalProvider PICKUP_ITEM_GOAL_PROVIDER = GolemPickupItemGoal::new;
        public static final GoalResourceLocation HARVEST_CROP = GoalResourceLocation.of(Thaumcraft.MOD_ID,"harvest_crop");
        public static final GolemGoalProvider HARVEST_CROP_GOAL_PROVIDER = GolemHarvestCropsGoal::new;
        public static final GoalResourceLocation GOLEM_HURT_BY_TARGET = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_hurt_by_target");
        public static final GolemGoalProvider GOLEM_HURT_BY_TARGET_GOAL_PROVIDER = GolemHurtByTargetGoal::new;
        public static final GoalResourceLocation ATTACK_NEAREST_ATTACKABLE = GoalResourceLocation.of(Thaumcraft.MOD_ID,"attack_nearest_attackable");
        public static final GolemGoalProvider ATTACK_NEAREST_ATTACKABLE_GOAL_PROVIDER = (golem) -> new NearestAttackableTargetGoal<>(golem, LivingEntity.class,true,living -> golemCanSelectTargetWithDefaultConfig(golem,living));
        public static final GoalResourceLocation GOLEM_MELEE_ATTACK = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_melee_attack");
        public static final GolemGoalProvider GOLEM_MELEE_ATTACK_GOAL_PROVIDER = GolemMeleeAttackGoal::new;
        public static final GoalResourceLocation GOLEM_FILL_IN_LIQUID = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_fill_in_liquid");
        public static final GolemGoalProvider GOLEM_FILL_IN_LIQUID_GOAL_PROVIDER = GolemFillInLiquidGoal::new;
        public static final GoalResourceLocation GOLEM_GET_LIQUID = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_get_liquid");
        public static final GolemGoalProvider GOLEM_GET_LIQUID_GOAL_PROVIDER = GolemGetLiquidGoal::new;
        public static final GoalResourceLocation GOLEM_GOTO_GET_LIQUID = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_goto_goto_get_liquid");
        public static final GolemGoalProvider GOLEM_GOTO_GET_LIQUID_GOAL_PROVIDER = GolemGotoGetLiquidGoal::new;
        public static final GoalResourceLocation GOLEM_FILL_IN_ESSENTIA = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_fill_in_essentia");
        public static final GolemGoalProvider GOLEM_FILL_IN_ESSENTIA_GOAL_PROVIDER = GolemFillInEssentiaGoal::new;
        public static final GoalResourceLocation GOLEM_GATHER_ESSENTIA = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_gather_essentia");
        public static final GolemGoalProvider GOLEM_GATHER_ESSENTIA_GOAL_PROVIDER = GolemGatherEssentiaGoal::new;
        public static final GoalResourceLocation GOLEM_GOTO_FILL_IN_ESSENTIA = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_goto_gather_essentia");
        public static final GolemGoalProvider GOLEM_GOTO_FILL_IN_ESSENTIA_GOAL_PROVIDER = GolemGotoFillInEssentiaGoal::new;
        public static final GoalResourceLocation GOLEM_HARVEST_LOG = GoalResourceLocation.of(Thaumcraft.MOD_ID,"golem_harvest_log");
        public static final GolemGoalProvider GOLEM_HARVEST_LOG_GOAL_PROVIDER = GolemHarvestLogsGoal::new;


        public static void registerDefaultTargetConfig(Set<GolemConfigurationKey<?>> configurationProviders){
            configurationProviders.add(TARGET_SELECTOR_HOSTILE_CONFIG_KEY);
            configurationProviders.add(TARGET_SELECTOR_ANIMAL_CONFIG_KEY);
            configurationProviders.add(TARGET_SELECTOR_PLAYER_CONFIG_KEY);
            configurationProviders.add(TARGET_SELECTOR_CREEPER_CONFIG_KEY);
        }
        public static boolean golemCanSelectTargetWithDefaultConfig(BaseGolemEntity golem, LivingEntity maybeTarget) {
            boolean hostileFlag = golem.configurationProviders.contains(TARGET_SELECTOR_HOSTILE_CONFIG_KEY)
                    ? TARGET_SELECTOR_HOSTILE_CONFIG_KEY.provider.getBooleanFrom(golem) : TARGET_SELECTOR_HOSTILE_CONFIG_KEY.provider.defaultValue;
            boolean creeperFlag = golem.configurationProviders.contains(TARGET_SELECTOR_CREEPER_CONFIG_KEY)
                    ? TARGET_SELECTOR_CREEPER_CONFIG_KEY.provider.getBooleanFrom(golem) : TARGET_SELECTOR_CREEPER_CONFIG_KEY.provider.defaultValue;
            if (maybeTarget instanceof Enemy){
                if (maybeTarget instanceof Creeper){
                    return creeperFlag;
                }
                return hostileFlag;
            }
            boolean animalFlag = golem.configurationProviders.contains(TARGET_SELECTOR_ANIMAL_CONFIG_KEY)
                    ? TARGET_SELECTOR_ANIMAL_CONFIG_KEY.provider.getBooleanFrom(golem) : TARGET_SELECTOR_ANIMAL_CONFIG_KEY.provider.defaultValue;
            if (maybeTarget instanceof Animal){
                return animalFlag;
            }
            boolean playerFlag = golem.configurationProviders.contains(TARGET_SELECTOR_PLAYER_CONFIG_KEY)
                    ? TARGET_SELECTOR_PLAYER_CONFIG_KEY.provider.getBooleanFrom(golem) : TARGET_SELECTOR_PLAYER_CONFIG_KEY.provider.defaultValue;
            if (maybeTarget instanceof Player player){
                var owner = golem.getOwner();
                if (owner == null || !player.isAlliedTo(owner)){
                    return playerFlag;
                }
            }
            return false;
        }
    }

    default void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        goalSelector.addGoalIfNotExist(-1, AVOID_CREEPER, AVOID_CREEPER_GOAL, golem);
        goalSelector.addGoalIfNotExist(6, GET_HOME_IF_FAR, GET_HOME_IF_FAR_GOAL, golem);
        goalSelector.addGoalIfNotExist(7, WATCH_CLOSEST_PLAYER, WATCH_CLOSEST_PLAYER_GOAL_PROVIDER, golem);
        goalSelector.addGoalIfNotExist(8, IDLE, IDLE_GOAL_PROVIDER, golem);
    }

    @Override
    default void modifyGolemAbility(BaseGolemEntity golem, ItemStack upgradeStack, GolemAbilitiesData abilitiesData) {
        registerGoalForGolem(golem, upgradeStack, abilitiesData.goalSelector, abilitiesData.targetSelector);
    }
}
