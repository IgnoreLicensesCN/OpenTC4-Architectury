package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.configruable.IGolemConfigurationProviderFillerItem;

import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

//ItemGolemCore:0
public class EmptyGolemCoreItem
        extends
        AbstractGolemCoreItemWithGui
        implements
        IGolemConfigurationProviderFillerItem
{
    public EmptyGolemCoreItem() {
        super();
    }
    public EmptyGolemCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoal(0,PLACE_ITEM_STACK_HOME,EMPTY_CORE_PLACE_ITEM_STACK_HOME_GOAL_PROVIDER.apply(golem));
        goalSelector.addGoalIfNotExist(1,PLACE_ITEM_STACK_TO_MARKED_CONTAINER,PLACE_ITEM_STACK_TO_MARKED_CONTAINER_GOAL_PROVIDER,golem);
        goalSelector.addGoalIfNotExist(2,THROW_ITEM_STACK_TO_MARKED_CONTAINER,THROW_ITEM_STACK_TO_MARKED_CONTAINER_GOAL_PROVIDER,golem);
        goalSelector.addGoalIfNotExist(3,GOTO_CONTAINER_FOR_EMPTY,GOTO_CONTAINER_FOR_EMPTY_GOAL_PROVIDER,golem);
        goalSelector.addGoal(4, TAKE_ITEM_STACK_FROM_HOME, EMPTY_CORE_TAKE_ITEM_STACK_FROM_HOME_GOAL_PROVIDER.apply(golem));
        //AIHomeTake
    }
}
