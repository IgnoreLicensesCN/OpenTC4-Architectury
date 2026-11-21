package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;

import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.GOLEM_HARVEST_LOG;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.GOLEM_HARVEST_LOG_GOAL_PROVIDER;

public class HarvestLogGolemCoreItem extends AbstractGolemCoreItem {
    public HarvestLogGolemCoreItem() {
        this(new Properties());
    }
    public HarvestLogGolemCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoalIfNotExist(1,GOLEM_HARVEST_LOG,GOLEM_HARVEST_LOG_GOAL_PROVIDER,golem);
    }
}
