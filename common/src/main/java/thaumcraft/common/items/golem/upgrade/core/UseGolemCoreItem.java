package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationKey;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.configruable.IGolemConfigurationProviderFillerItem;

import java.util.Set;

import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.*;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

//toggle 0: block/empty space
public class UseGolemCoreItem
        extends
        AbstractGolemCoreItemWithGui
        implements
        IGolemConfigurationProviderFillerItem
{
    public UseGolemCoreItem() {
        this(new Properties());
    }
    public UseGolemCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoal(1, PLACE_ITEM_STACK_HOME,GOLEM_USE_CORE_PLACE_ITEM_STACK_HOME_GOAL_PROVIDER.apply(golem));
        goalSelector.addGoalIfNotExist(2,GOLEM_USE,GOLEM_USE_GOAL_PROVIDER,golem);
        goalSelector.addGoal(3, TAKE_ITEM_STACK_FROM_HOME, USE_CORE_TAKE_ITEM_STACK_FROM_HOME_GOAL_PROVIDER.apply(golem));
    }

    @Override
    public void fillGolemConfigurationProviders(BaseGolemEntity golem, ItemStack upgradeStack, Set<GolemConfigurationKey<?>> configurationProviders) {
        configurationProviders.add(USE_CORE_BLOCK_OR_EMPTY);
        configurationProviders.add(USE_CORE_DO_SNEAKING);
        configurationProviders.add(USE_CORE_LEFT_CLICK);
    }
}
