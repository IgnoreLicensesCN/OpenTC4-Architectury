package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.IGolemAdditionalDataProviderItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters.IGolemItemStackMatcherModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners.IGolemUpdateItemInHandListenerItem;

import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

//ItemGolemCore:0
public class FillGolemCoreItem
        extends
        AbstractGolemCoreItemWithGui
        implements
        IGolemUpdateItemInHandListenerItem,
        IGolemItemStackMatcherModifierItem,//TODO:Impl NBT and tag matching
        IGolemAdditionalDataProviderItem//also TODO:Impl NBT and tag matching
{
    public FillGolemCoreItem() {
        super();
    }
    public FillGolemCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoalIfNotExist(0,PLACE_ITEM_STACK_HOME, PLACE_ITEM_STACK_HOME_DEFAULT_GOAL_PROVIDER,golem);
        goalSelector.addGoalIfNotExist(1,DROP_ITEM_STACK_HOME_IF_NO_CONTAINER,DROP_ITEM_STACK_HOME_IF_NO_CONTAINER_GOAL_PROVIDER,golem);
        goalSelector.addGoalIfNotExist(2,TAKE_ITEM_STACK_FROM_CONTAINER,TAKE_ITEM_STACK_FROM_CONTAINER_GOAL_PROVIDER,golem);
    }
}
