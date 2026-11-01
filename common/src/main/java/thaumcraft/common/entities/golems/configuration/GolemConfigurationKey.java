package thaumcraft.common.entities.golems.configuration;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.BooleanTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResourceLocationTagAccessor;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.golems.configuration.providerimpl.GolemConfigurationProviderImpl;
import thaumcraft.common.entities.golems.configuration.providerimpl.basic.GolemConfigurationProviderBooleanImpl;
import thaumcraft.common.lib.resourcelocations.GolemConfigurationResourceLocation;

public class GolemConfigurationKey<ProviderClass extends GolemConfigurationProvider<?>>{
    public final GolemConfigurationResourceLocation key;
    public final ProviderClass provider;

    public GolemConfigurationKey(GolemConfigurationResourceLocation key, ProviderClass provider) {
        this.key = key;
        this.provider = provider;
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
                                () -> ResourceLocation.tryBuild(Thaumcraft.MOD_ID,"empty")
                        )
                );
    }
}
