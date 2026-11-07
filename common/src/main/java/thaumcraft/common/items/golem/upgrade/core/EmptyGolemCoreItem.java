package thaumcraft.common.items.golem.upgrade.core;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationKey;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.configruable.IGolemConfigurationProviderFillerItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters.IGolemItemStackMatcherModifierItem;
import thaumcraft.common.items.golem.upgrade.core.FillGolemCoreItem.TagBiItemMatcher;

import java.util.Set;
import java.util.function.BiPredicate;

import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_NBT_CONFIG_KEY;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_TAG_CONFIG_KEY;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;

//ItemGolemCore:0
public class EmptyGolemCoreItem
        extends
        AbstractGolemCoreItemWithGui
        implements
        IGolemItemStackMatcherModifierItem,
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
        goalSelector.addGoalIfNotExist(0,EMPTY_CORE_PLACE_ITEM_STACK_HOME,EMPTY_CORE_PLACE_ITEM_STACK_HOME_GOAL_PROVIDER,golem);
        goalSelector.addGoalIfNotExist(1,PLACE_ITEM_STACK_TO_MARKED_CONTAINER,PLACE_ITEM_STACK_TO_MARKED_CONTAINER_GOAL_PROVIDER,golem);
        //AIEmptyDrop
        //AIEmptyGoto
        //AIHomeTake
    }

    @Override
    public void fillGolemConfigurationProviders(BaseGolemEntity golem, ItemStack upgradeStack, Set<GolemConfigurationKey<?>> configurationProviders) {
        configurationProviders.add(MATCH_NBT_CONFIG_KEY);
        configurationProviders.add(MATCH_TAG_CONFIG_KEY);
    }



    @Override
    public BiPredicate<ItemStack, ItemStack> modifyItemStackMatching(BaseGolemEntity golem, ItemStack upgradeStack) {
        var matchNBT = golem.configurationProviders.contains(MATCH_NBT_CONFIG_KEY)?MATCH_NBT_CONFIG_KEY.provider:null;
        if (matchNBT != null && matchNBT.getBooleanFrom(golem)) {
            return ItemStack::isSameItemSameTags;
        }
        var matchTag = golem.configurationProviders.contains(MATCH_TAG_CONFIG_KEY)?MATCH_TAG_CONFIG_KEY.provider:null;
        if (matchTag != null){
            var tagMatching = matchTag.getFrom(golem);
            if (!tagMatching.getNamespace().isEmpty() && !tagMatching.getPath().isEmpty()) {
                return TagBiItemMatcher.INSTANCES.computeIfAbsent(tagMatching, TagBiItemMatcher::new);
            }
        }
        return (a,b) -> a.getItem() == b.getItem();
    }
}
