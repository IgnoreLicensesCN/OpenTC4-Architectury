package thaumcraft.common.lib.world.registries;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.world.feature.GreatwoodTreeFeature;
import thaumcraft.common.lib.world.feature.SilverwoodTreeFeature;

public class ThaumcraftWorldGenConfiguredFeatures {
    public static class Features{
        public static final Feature<TreeConfiguration> SILVERWOOD_PLAYER_PLANTED_FEATURE =
                FeaturesRegistry.SUPPLIER_SILVERWOOD_PLAYER_PLANTED_FEATURE.get();
        public static final ResourceKey<Feature<TreeConfiguration>> SILVERWOOD_PLAYER_PLANTED_FEATURE_KEY =
                FeaturesRegistry.SUPPLIER_SILVERWOOD_PLAYER_PLANTED_FEATURE.getKey();
        public static final Feature<TreeConfiguration> SILVERWOOD_GENERATED_FEATURE =
                FeaturesRegistry.SUPPLIER_SILVERWOOD_GENERATED_FEATURE.get();
        public static final ResourceKey<Feature<TreeConfiguration>> SILVERWOOD_GENERATED_FEATURE_KEY =
                FeaturesRegistry.SUPPLIER_SILVERWOOD_GENERATED_FEATURE.getKey();
        public static final Feature<TreeConfiguration> GREATWOOD_PLAYER_PLANTED_FEATURE =
                FeaturesRegistry.SUPPLIER_GREATWOOD_PLAYER_PLANTED_FEATURE.get();
        public static final ResourceKey<Feature<TreeConfiguration>> GREATWOOD_PLAYER_PLANTED_FEATURE_KEY =
                FeaturesRegistry.SUPPLIER_GREATWOOD_PLAYER_PLANTED_FEATURE.getKey();
        public static final Feature<TreeConfiguration> GREATWOOD_GENERATED_FEATURE =
                FeaturesRegistry.SUPPLIER_GREATWOOD_GENERATED_FEATURE.get();
        public static final ResourceKey<Feature<TreeConfiguration>> GREATWOOD_GENERATED_FEATURE_KEY =
                FeaturesRegistry.SUPPLIER_GREATWOOD_GENERATED_FEATURE.getKey();
    }
    public static class FeaturesRegistry{

        public static final DeferredRegister<Feature<?>> WORLD_GEN_FEATURES = DeferredRegister.create(
                Thaumcraft.MOD_ID, Registries.FEATURE
        );
        public static final RegistrySupplier<Feature<TreeConfiguration>> SUPPLIER_SILVERWOOD_PLAYER_PLANTED_FEATURE
                = WORLD_GEN_FEATURES.register("silverwood_planted_feature",() -> new SilverwoodTreeFeature(true,7,5));

        public static final RegistrySupplier<Feature<TreeConfiguration>> SUPPLIER_SILVERWOOD_GENERATED_FEATURE
                = WORLD_GEN_FEATURES.register("silverwood_generated_feature",() -> new SilverwoodTreeFeature(false,8,5));

        public static final RegistrySupplier<Feature<TreeConfiguration>> SUPPLIER_GREATWOOD_PLAYER_PLANTED_FEATURE
                = WORLD_GEN_FEATURES.register("greatwood_planted_feature",() -> new GreatwoodTreeFeature(true));

        public static final RegistrySupplier<Feature<TreeConfiguration>> SUPPLIER_GREATWOOD_GENERATED_FEATURE
                = WORLD_GEN_FEATURES.register("greatwood_generated_feature",() -> new GreatwoodTreeFeature(false));

    }
    public static class Configureds{
        public static final ConfiguredFeature<TreeConfiguration, ?> SILVERWOOD_PLANTED_CONFIGURED =
                ConfiguredRegistry.SUPPLIER_SILVERWOOD_PLAYER_PLANT_CONFIGURED.get();
        public static final ResourceKey<ConfiguredFeature<TreeConfiguration, ?>> SILVERWOOD_PLANTED_CONFIGURED_KEY =
                ConfiguredRegistry.SUPPLIER_SILVERWOOD_PLAYER_PLANT_CONFIGURED.getKey();
        public static final ConfiguredFeature<TreeConfiguration, ?> SILVERWOOD_GENERATED_CONFIGURED =
                ConfiguredRegistry.SUPPLIER_SILVERWOOD_GENERATED_CONFIGURED.get();
        public static final ResourceKey<ConfiguredFeature<TreeConfiguration, ?>> SILVERWOOD_GENERATED_CONFIGURED_KEY =
                ConfiguredRegistry.SUPPLIER_SILVERWOOD_GENERATED_CONFIGURED.getKey();
        public static final ConfiguredFeature<TreeConfiguration, ?> GREATWOOD_PLANTED_CONFIGURED =
                ConfiguredRegistry.SUPPLIER_GREATWOOD_PLAYER_PLANT_CONFIGURED.get();
        public static final ResourceKey<ConfiguredFeature<TreeConfiguration, ?>> GREATWOOD_PLANTED_CONFIGURED_KEY =
                ConfiguredRegistry.SUPPLIER_GREATWOOD_PLAYER_PLANT_CONFIGURED.getKey();
        public static final ConfiguredFeature<TreeConfiguration, ?> GREATWOOD_GENERATED_CONFIGURED =
                ConfiguredRegistry.SUPPLIER_GREATWOOD_GENERATED_CONFIGURED.get();
        public static final ResourceKey<ConfiguredFeature<TreeConfiguration, ?>> GREATWOOD_GENERATED_CONFIGURED_KEY =
                ConfiguredRegistry.SUPPLIER_GREATWOOD_GENERATED_CONFIGURED.getKey();
    }

    public static class ConfiguredRegistry {
        public static final DeferredRegister<ConfiguredFeature<?, ?>> WORLD_GEN_CONFIGURED_FEATURES = DeferredRegister.create(
                Thaumcraft.MOD_ID, Registries.CONFIGURED_FEATURE
        );

        public static final RegistrySupplier<ConfiguredFeature<TreeConfiguration, ?>> SUPPLIER_SILVERWOOD_PLAYER_PLANT_CONFIGURED
                = WORLD_GEN_CONFIGURED_FEATURES.register("silverwood_planted_configured",() -> new ConfiguredFeature<>(
                        Features.SILVERWOOD_PLAYER_PLANTED_FEATURE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ThaumcraftBlocks.SILVERWOOD_LOG),
                        new StraightTrunkPlacer(7, 2, 0),
                        BlockStateProvider.simple(ThaumcraftBlocks.SILVERWOOD_LEAVES),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1)
                ).build()
        ));
        public static final RegistrySupplier<ConfiguredFeature<TreeConfiguration, ?>> SUPPLIER_SILVERWOOD_GENERATED_CONFIGURED
                = WORLD_GEN_CONFIGURED_FEATURES.register("silverwood_generated_configured",() -> new ConfiguredFeature<>(
                Features.SILVERWOOD_GENERATED_FEATURE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ThaumcraftBlocks.SILVERWOOD_LOG),
                        new StraightTrunkPlacer(7, 2, 0),
                        BlockStateProvider.simple(ThaumcraftBlocks.SILVERWOOD_LEAVES),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1)
                ).build()
        ));
        public static final RegistrySupplier<ConfiguredFeature<TreeConfiguration, ?>> SUPPLIER_GREATWOOD_PLAYER_PLANT_CONFIGURED
                = WORLD_GEN_CONFIGURED_FEATURES.register("greatwood_planted_configured",() -> new ConfiguredFeature<>(
                Features.GREATWOOD_PLAYER_PLANTED_FEATURE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ThaumcraftBlocks.GREATWOOD_LOG),
                        new StraightTrunkPlacer(7, 2, 0),
                        BlockStateProvider.simple(ThaumcraftBlocks.GREATWOOD_LEAVES),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1)
                ).build()
        ));
        public static final RegistrySupplier<ConfiguredFeature<TreeConfiguration, ?>> SUPPLIER_GREATWOOD_GENERATED_CONFIGURED
                = WORLD_GEN_CONFIGURED_FEATURES.register("greatwood_generated_configured",() -> new ConfiguredFeature<>(
                Features.GREATWOOD_GENERATED_FEATURE,
                new TreeConfiguration.TreeConfigurationBuilder(
                        BlockStateProvider.simple(ThaumcraftBlocks.GREATWOOD_LOG),
                        new StraightTrunkPlacer(7, 2, 0),
                        BlockStateProvider.simple(ThaumcraftBlocks.GREATWOOD_LEAVES),
                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1)
                ).build()
        ));
    }

    public static void init(){
        FeaturesRegistry.WORLD_GEN_FEATURES.register();
        ConfiguredRegistry.WORLD_GEN_CONFIGURED_FEATURES.register();
    }
}
