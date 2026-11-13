package thaumcraft.common.items.golem.upgrade.primal;

import lombok.AllArgsConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationKey;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.configruable.IGolemConfigurationProviderFillerItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters.IGolemItemStackMatcherModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners.IGolemDamagedListenerItem;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.ITEM_ENTITY_TEST;
import static thaumcraft.common.entities.golems.GolemHelper.getConsideringTagsForItem;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.*;

//GolemUpgrade:5
public class EntropyGolemUpgradeItem extends GolemBasicUpgradeItem
        implements TravelingTrunkEntity.ITravelingTrunkUpgradeItem,
        IGolemDamagedListenerItem,
        IGolemConfigurationProviderFillerItem,
        IGolemItemStackMatcherModifierItem {
    public EntropyGolemUpgradeItem(Properties properties) {
        super(properties);
    }

    public EntropyGolemUpgradeItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("golem_upgrade.entropy.desc"));
    }

    @Override
    public void travelingTrunkUpgrade$tick(TravelingTrunkEntity trunk, ItemStack upgradeStack) {
        var level = trunk.level();
        if (!trunk.isDeadOrDying() && !(trunk.getHealth() <= 0.0F)) {
            if (!level.isClientSide()) {
                //pick
                level.getEntities(
                        ITEM_ENTITY_TEST,
                        AABB.ofSize(
                                trunk.position(),
                                0.5F,
                                0.5F,
                                0.5F
                        ), itemEntity -> !itemEntity.hasPickUpDelay()
                ).forEach(itemEntity -> {
                    var stack = itemEntity.getItem();
                    ItemStack outstack = InventoryUtils.placeItemStackIntoInventory(stack, trunk.inventory, true);
                    if (outstack.isEmpty() || outstack.getCount() != stack.getCount()) {
                        trunk.playSound(SoundEvents.GENERIC_EAT, 0.5F, 0.5F + trunk.getRandom().nextFloat() * 0.5F);
                        level.broadcastEntityEvent(trunk, (byte) 17);
                        if (!outstack.isEmpty()) {
                            itemEntity.setItem(outstack);
                        } else {
                            itemEntity.discard();
                        }
                    }
                });
            }

            //drag in
            level.getEntities(
                    ITEM_ENTITY_TEST,
                    AABB.ofSize(
                            trunk.position(),
                            3,
                            3,
                            3
                    ), itemEntity -> !itemEntity.hasPickUpDelay()
            ).forEach(itemEntity -> {
                var pos = itemEntity.position();
                var trunkPos = trunk.position();
                var vecToItem = pos.subtract(trunkPos);
                var length = vecToItem.length();
                if (length > 0.01){
                    itemEntity.addDeltaMovement(vecToItem.scale(-0.075 / length));
                }
            });

        }
    }

    @Override
    public void onDamaged(BaseGolemEntity golem, ItemStack upgradeStack, DamageSource damageSource, float damage) {
        var causingEntity = damageSource.getEntity();
        if (causingEntity != golem && causingEntity != null) {
            causingEntity.hurt(golem.damageSources().thorns(golem), 1 + golem.getRandom().nextInt(2));
            causingEntity.playSound(SoundEvents.THORNS_HIT,0.5F,1);
        }
    }

    @Override
    public void fillGolemConfigurationProviders(BaseGolemEntity golem, ItemStack upgradeStack, Set<GolemConfigurationKey<?>> configurationProviders) {
        configurationProviders.add(MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY);
        configurationProviders.add(MATCH_NBT_CONFIG_KEY);
        configurationProviders.add(MATCH_TAG_CONFIG_KEY);
    }

    @Override
    public BaseGolemEntity.GolemItemStackMatcher modifyItemStackMatching(BaseGolemEntity golem, ItemStack upgradeStack) {
        return (g,a,b) -> getItemStackHaveSameTag(g,a,b) || getItemStackItemStackForNBTAndTagConfig(g,a,b);
    }

    public static boolean getItemStackItemStackForNBTAndTagConfig(BaseGolemEntity golem,ItemStack a,ItemStack b) {
        var matchNBT = golem.configurationProviders.contains(MATCH_NBT_CONFIG_KEY)?MATCH_NBT_CONFIG_KEY.provider:null;
        if (matchNBT != null && matchNBT.getBooleanFrom(golem)) {
            return BaseGolemEntity.GolemItemStackMatcher.isSameItemSameTags.test(golem,a,b);
        }
        var matchTag = golem.configurationProviders.contains(MATCH_TAG_CONFIG_KEY)?MATCH_TAG_CONFIG_KEY.provider:null;
        if (matchTag != null){
            var tagMatching = matchTag.getFrom(golem);
            if (!tagMatching.getNamespace().isEmpty() && !tagMatching.getPath().isEmpty()) {
                return TagBiItemMatcher.INSTANCES.computeIfAbsent(tagMatching, TagBiItemMatcher::new).test(golem,a,b);
            }
        }
        return a.getItem() == b.getItem();
    }

    public static boolean getItemStackHaveSameTag(BaseGolemEntity golem,ItemStack a,ItemStack b) {
        var matchHaveSameTag = golem.configurationProviders.contains(MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY) ? MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY.provider:null;
        if (matchHaveSameTag != null && matchHaveSameTag.getBooleanFrom(golem)) {
            var tags = getConsideringTagsForItem(a.getItem());
            var tagsForConsidering = getConsideringTagsForItem(b.getItem());
            return !Collections.disjoint(tags, tagsForConsidering);
        }
        return false;
    }


    @AllArgsConstructor
    public static class TagBiItemMatcher implements BaseGolemEntity.GolemItemStackMatcher {
        public static final Map<ResourceLocation, TagBiItemMatcher> INSTANCES = new ConcurrentHashMap<>();
        public final TagKey<Item> key;
        public TagBiItemMatcher(ResourceLocation resLoc) {
            this.key = TagKey.create(Registries.ITEM, resLoc);
        }
        public boolean test(BaseGolemEntity golem,ItemStack stackA, ItemStack stackB) {
            return stackA.is(key) && stackB.is(key);
        }
    }
}
