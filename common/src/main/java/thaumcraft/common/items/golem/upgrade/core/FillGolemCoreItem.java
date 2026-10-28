package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;

import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.PLACE_ITEM_STACK_HOME;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.PLACE_ITEM_STACK_HOME_DEFAULT_GOAL;

//ItemGolemCore:0
public class FillGolemCoreItem extends AbstractGolemCoreItemWithGui {
    public FillGolemCoreItem() {
        super();
    }
    public FillGolemCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoalIfNotExist(0,PLACE_ITEM_STACK_HOME,PLACE_ITEM_STACK_HOME_DEFAULT_GOAL,golem);
    }
}
