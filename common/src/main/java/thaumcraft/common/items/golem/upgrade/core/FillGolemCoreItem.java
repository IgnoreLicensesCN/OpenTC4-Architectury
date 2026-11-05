package thaumcraft.common.items.golem.upgrade.core;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.BooleanTagAccessor;
import lombok.AllArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemGoalSelector;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationKey;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;
import thaumcraft.common.entities.golems.configuration.providerimpl.basic.GolemConfigurationProviderBooleanImpl;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.configruable.IGolemConfigurationProviderFillerItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters.IGolemItemStackMatcherModifierItem;
import thaumcraft.common.lib.resourcelocations.GolemConfigurationResourceLocation;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_NBT_CONFIG_KEY;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.MATCH_TAG_CONFIG_KEY;
import static thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemAIProviderItem.DefaultGoalsAndIDs.*;
//toggle 0:Precise amount/Any amount
//(false/true)

//ItemGolemCore:0
public class FillGolemCoreItem
        extends
        AbstractGolemCoreItemWithGui
        implements
        IGolemItemStackMatcherModifierItem,
        IGolemConfigurationProviderFillerItem//TODO:Impl NBT and tag matching and other options
{
    public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> AMOUNT_CONFIG_KEY =
            new GolemConfigurationKey<>(
                    GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"amount_config"),
                    new GolemConfigurationProviderBooleanImpl(new BooleanTagAccessor("amount_config"))
            );
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
        goalSelector.addGoalIfNotExist(3,GOTO_CONTAINER_FOR_FILLING,GOTO_CONTAINER_FOR_FILLING_GOAL_PROVIDER,golem);
    }

    @Override
    public void fillGolemConfigurationProviders(BaseGolemEntity golem, ItemStack upgradeStack, Set<GolemConfigurationKey<?>> configurationProviders) {
        configurationProviders.add(AMOUNT_CONFIG_KEY);
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
                return TagBiItemMatcher.INSTANCES.computeIfAbsent(tagMatching,TagBiItemMatcher::new);
            }
        }
        return (a,b) -> a.getItem() == b.getItem();
    }

    @AllArgsConstructor
    public static class TagBiItemMatcher implements BiPredicate<ItemStack,ItemStack> {
        public static final Map<ResourceLocation, TagBiItemMatcher> INSTANCES = new ConcurrentHashMap<>();
        public final TagKey<Item> key;
        public TagBiItemMatcher(ResourceLocation resLoc) {
            this.key = TagKey.create(Registries.ITEM, resLoc);
        }
        public boolean test(ItemStack stackA, ItemStack stackB) {
            return stackA.is(key) && stackB.is(key);
        }
    }
}
