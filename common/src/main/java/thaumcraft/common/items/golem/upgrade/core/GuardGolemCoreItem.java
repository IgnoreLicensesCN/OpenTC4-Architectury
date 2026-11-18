package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationKey;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemBasicUpgradeItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.configruable.IGolemConfigurationProviderFillerItem;

import java.util.Set;

import static thaumcraft.common.items.ThaumcraftItemInstances.ORDER_GOLEM_UPGRADE;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

public class GuardGolemCoreItem
        extends AbstractGolemCoreItemWithGui
        implements
        IGolemConfigurationProviderFillerItem {
    public GuardGolemCoreItem() {
        super();
    }

    public GuardGolemCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void registerGoalForGolem(BaseGolemEntity golem, ItemStack upgradeStack, GolemGoalSelector goalSelector, GolemGoalSelector targetSelector) {
        super.registerGoalForGolem(golem, upgradeStack, goalSelector, targetSelector);
        goalSelector.addGoalIfNotExist(3, GOLEM_MELEE_ATTACK, GOLEM_MELEE_ATTACK_GOAL_PROVIDER, golem);
        targetSelector.addGoalIfNotExist(1, GOLEM_HURT_BY_TARGET, GOLEM_HURT_BY_TARGET_GOAL_PROVIDER, golem);
        targetSelector.addGoalIfNotExist(2, ATTACK_NEAREST_ATTACKABLE, ATTACK_NEAREST_ATTACKABLE_GOAL_PROVIDER, golem);
    }

    @Override
    public void fillGolemConfigurationProviders(BaseGolemEntity golem, ItemStack upgradeStack, Set<GolemConfigurationKey<?>> configurationProviders) {
        if (golem.golemUpgradesCacheView.get(IGolemBasicUpgradeItem.class)
                .stream()
                .anyMatch(pair -> pair.getUpgradeStack().getItem() == ORDER_GOLEM_UPGRADE())
        ) {
            registerDefaultTargetConfig(configurationProviders);
        }
    }


}
