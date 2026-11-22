package thaumcraft.common.entities.golems.configuration;

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
    //maybe other mods want this?
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
                        new GolemConfigurationProviderBooleanImpl(
                                "match_nbt_config"
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
                        new GolemConfigurationProviderBooleanImpl(
                                "match_if_contain_same_tag_config"
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> PRECISE_OR_ANY_AMOUNT_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"precise_or_any_amount_config"),
                        new GolemConfigurationProviderBooleanImpl(
                                "precise_or_any_amount_config")
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> TARGET_SELECTOR_HOSTILE_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"target_selector_hostile"),
                        new GolemConfigurationProviderBooleanImpl(
                                "target_selector_hostile",
                                true
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> TARGET_SELECTOR_ANIMAL_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"target_selector_animal"),
                        new GolemConfigurationProviderBooleanImpl(
                                "target_selector_animal"
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> TARGET_SELECTOR_ANIMAL_DEFAULT_TRUE_CONFIG_KEY /*not same as the one you meet before*/ =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"target_selector_animal_default_true"),
                        new GolemConfigurationProviderBooleanImpl(
                                "target_selector_animal",
                                true
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> TARGET_SELECTOR_PLAYER_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"target_selector_player"),
                        new GolemConfigurationProviderBooleanImpl(
                                "target_selector_player"
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> TARGET_SELECTOR_CREEPER_CONFIG_KEY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"target_selector_creeper"),
                        new GolemConfigurationProviderBooleanImpl(
                                "target_selector_creeper"
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> USE_CORE_BLOCK_OR_EMPTY =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"use_core_block_or_empty"),
                        new GolemConfigurationProviderBooleanImpl(
                                "use_core_block_or_empty"
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> USE_CORE_DO_SNEAKING =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"use_core_do_sneaking"),
                        new GolemConfigurationProviderBooleanImpl(
                                "use_core_do_sneaking"
                        )
                );
        public static final GolemConfigurationKey<GolemConfigurationProviderBooleanImpl> USE_CORE_LEFT_CLICK =
                new GolemConfigurationKey<>(
                        GolemConfigurationResourceLocation.of(Thaumcraft.MOD_ID,"use_core_left_click"),
                        new GolemConfigurationProviderBooleanImpl(
                                "use_core_left_click"
                        )
                );
    }
}
