package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;

import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.GOLEM_MELEE_ATTACK_GOAL_PROVIDER;

public class ButcherGolemCoreItem extends AbstractGolemCoreItemWithGui{
    public ButcherGolemCoreItem() {
        this(new Properties());
    }
    public ButcherGolemCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoalIfNotExist(2, GOLEM_MELEE_ATTACK, GOLEM_MELEE_ATTACK_GOAL_PROVIDER, golem);
        targetSelector.addGoalIfNotExist(1, BUTCHER_TARGET,BUTCHER_TARGET_GOAL_PROVIDER, golem);
    }
}
