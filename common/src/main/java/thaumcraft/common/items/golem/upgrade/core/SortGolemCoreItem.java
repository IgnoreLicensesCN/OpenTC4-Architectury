package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;

import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

//ItemGolemCore:0
public class SortGolemCoreItem
        extends
        AbstractGolemCoreItemWithGui
{
    public SortGolemCoreItem() {
        super();
    }
    public SortGolemCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoal(0,PLACE_ITEM_STACK_HOME,SORTING_CORE_PLACE_ITEM_STACK_HOME_GOAL_PROVIDER.apply(golem));
        goalSelector.addGoal(1,PLACE_ITEM_STACK_TO_MARKED_CONTAINER, SORTING_CORE_PLACE_ITEM_STACK_TO_MARKED_CONTAINER_GOAL_PROVIDER.apply(golem));
        goalSelector.addGoalIfNotExist(3,SORTING_CORE_GOTO_MARKED_CONTAINER,SORTING_CORE_GOTO_MARKED_CONTAINER_GOAL_PROVIDER,golem);
        goalSelector.addGoal(4, TAKE_ITEM_STACK_FROM_HOME, SORTING_CORE_TAKE_ITEM_STACK_FROM_HOME_GOAL_PROVIDER.apply(golem));
        //AIHomeTake
    }
}
