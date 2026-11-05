package thaumcraft.common.items.golem.upgrade.abstracts.behavior.configruable;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationKey;
import thaumcraft.common.entities.golems.configuration.GolemConfigurationProvider;

import java.util.Set;

public interface IGolemConfigurationProviderFillerItem extends BaseGolemEntity.IGolemUpgradeItem {
    void fillGolemConfigurationProviders(BaseGolemEntity golem, ItemStack upgradeStack, Set<GolemConfigurationKey<?>> configurationProviders);
    CompoundTagAccessorImpl CONFIGURATION_ACCESSOR = new CompoundTagAccessorImpl("golem_configurations");
    default void readAdditionalConfig(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag){
        var wrappedTag = CONFIGURATION_ACCESSOR.readFromCompoundTag(tag);
        for (var providerKey:golem.configurationProviders){
            GolemConfigurationProvider<Object> castedProvider = (GolemConfigurationProvider<Object>) providerKey.provider;
            castedProvider.setTo(golem,castedProvider.serializeFrom(wrappedTag));
        }
    }
    default void writeAdditionalConfig(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag){
        var wrappedTag = new CompoundTag();
        for (var providerKey:golem.configurationProviders){
            GolemConfigurationProvider<Object> castedProvider = (GolemConfigurationProvider<Object>) providerKey.provider;
            castedProvider.deserializeTo(wrappedTag,castedProvider.getFrom(golem));
        }
        CONFIGURATION_ACCESSOR.writeToCompoundTag(tag,wrappedTag);
    };
}
