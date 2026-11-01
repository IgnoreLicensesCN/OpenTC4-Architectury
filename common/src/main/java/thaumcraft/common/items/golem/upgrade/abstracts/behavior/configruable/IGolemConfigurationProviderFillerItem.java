package thaumcraft.common.items.golem.upgrade.abstracts.behavior.configruable;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProviderMap;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.IGolemAdditionalDataProviderItem;

public interface IGolemConfigurationProviderFillerItem extends BaseGolemEntity.IGolemUpgradeItem, IGolemAdditionalDataProviderItem {
    void fillGolemConfigurationProviders(BaseGolemEntity golem, ItemStack upgradeStack, GolemConfigurationProviderMap configurationProviders);
    CompoundTagAccessorImpl CONFIGURATION_ACCESSOR = new CompoundTagAccessorImpl("golem_configurations");
    @Override
    default void readAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag){
        var wrappedTag = CONFIGURATION_ACCESSOR.readFromCompoundTag(tag);
        for (var provider:golem.configurationProviders.values()){
            GolemConfigurationProvider<Object> castedProvider = (GolemConfigurationProvider<Object>) provider;
            castedProvider.setTo(golem,castedProvider.serializeFrom(wrappedTag));
        }
    }

    @Override
    default void writeAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag){
        var wrappedTag = new CompoundTag();
        for (var provider:golem.configurationProviders.values()){
            GolemConfigurationProvider<Object> castedProvider = (GolemConfigurationProvider<Object>) provider;
            castedProvider.deserializeTo(wrappedTag,castedProvider.getFrom(golem));
        }
        CONFIGURATION_ACCESSOR.writeToCompoundTag(tag,wrappedTag);
    };
}
