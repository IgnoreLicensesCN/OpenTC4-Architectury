package thaumcraft.common.entities.golems.configuration;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.BooleanTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResourceLocationTagAccessor;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.golems.configuration.providerimpl.GolemConfigurationProviderImpl;
import thaumcraft.common.entities.golems.configuration.providerimpl.basic.GolemConfigurationProviderBooleanImpl;
import thaumcraft.common.lib.resourcelocations.GolemConfigurationResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GolemConfigurationKey<ProviderClass extends GolemConfigurationProvider<?>>{
    private static final Map<GolemConfigurationResourceLocation,GolemConfigurationKey<?>> configurationID2Key = new HashMap<>();
    public static final Map<GolemConfigurationResourceLocation,GolemConfigurationKey<?>> CONFIGURATION_ID_TO_KEY_VIEW = Collections.unmodifiableMap(configurationID2Key);
    public final GolemConfigurationResourceLocation key;
    public final ProviderClass provider;

    public GolemConfigurationKey(GolemConfigurationResourceLocation key, ProviderClass provider) {
        this.key = key;
        this.provider = provider;
        configurationID2Key.put(key, this);
    }

    public static class Default{
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> MATCH_NBT_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"amount_config"),
                        new GolemConfigurationProviderBooleanImpl(new BooleanTagAccessor("match_nbt_config")
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderImpl<ResourceLocation>> MATCH_TAG_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"match_tag_config"),
                        new GolemConfigurationProviderImpl<>(
                                new ResourceLocationTagAccessor("match_tag_config"),
                                () -> ResourceLocation.tryBuild("","empty")
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> MATCH_IF_CONTAIN_SAME_TAG_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"match_if_contain_same_tag_config"),
                        new GolemConfigurationProviderBooleanImpl(new BooleanTagAccessor("match_if_contain_same_tag_config"))
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> PRECISE_OR_ANY_AMOUNT_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"precise_or_any_amount_config"),
                        new GolemConfigurationProviderBooleanImpl(new BooleanTagAccessor("precise_or_any_amount_config"))
                );
    }
}
