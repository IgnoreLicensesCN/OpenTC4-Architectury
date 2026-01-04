package thaumcraft.common.lib.world.registries;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.world.feature.GreatwoodTreeFeature;
import thaumcraft.common.lib.world.feature.SilverwoodTreeFeature;

public class ThaumcraftWorldGenConfiguredFeatures {
    public static class Features {
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

    public static class FeaturesRegistry {

        public static final DeferredRegister<Feature<?>> WORLD_GEN_FEATURES = DeferredRegister.create(
                Thaumcraft.MOD_ID, Registries.FEATURE
        );
        public static final RegistrySupplier<Feature<TreeConfiguration>> SUPPLIER_SILVERWOOD_PLAYER_PLANTED_FEATURE
                = WORLD_GEN_FEATURES.register(
                "silverwood_planted_feature", () -> new SilverwoodTreeFeature(true, 7, 5));

        public static final RegistrySupplier<Feature<TreeConfiguration>> SUPPLIER_SILVERWOOD_GENERATED_FEATURE
                = WORLD_GEN_FEATURES.register(
                "silverwood_generated_feature", () -> new SilverwoodTreeFeature(false, 8, 5));

        public static final RegistrySupplier<Feature<TreeConfiguration>> SUPPLIER_GREATWOOD_PLAYER_PLANTED_FEATURE
                = WORLD_GEN_FEATURES.register("greatwood_planted_feature", () -> new GreatwoodTreeFeature(true));

        public static final RegistrySupplier<Feature<TreeConfiguration>> SUPPLIER_GREATWOOD_GENERATED_FEATURE
                = WORLD_GEN_FEATURES.register("greatwood_generated_feature", () -> new GreatwoodTreeFeature(false));


        static {
            WORLD_GEN_FEATURES.register();
        }

    }

    public static class Configureds {
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
        public static final ConfiguredFeature<RandomPatchConfiguration,?> CINDER_PEARL_PATCH_CONFIGURED =
                ConfiguredRegistry.SUPPLIER_CINDER_PEARL_GENERATED_CONFIGURED.get();
        public static final ResourceKey<ConfiguredFeature<RandomPatchConfiguration,?>> CINDER_PEARL_PATCH_CONFIGURED_KEY =
                ConfiguredRegistry.SUPPLIER_CINDER_PEARL_GENERATED_CONFIGURED.getKey();

    }

    public static class ConfiguredRegistry {
        public static final DeferredRegister<ConfiguredFeature<?, ?>> WORLD_GEN_CONFIGURED_FEATURES = DeferredRegister.create(
                Thaumcraft.MOD_ID, Registries.CONFIGURED_FEATURE
        );

        public static final RegistrySupplier<ConfiguredFeature<TreeConfiguration, ?>> SUPPLIER_SILVERWOOD_PLAYER_PLANT_CONFIGURED
                = WORLD_GEN_CONFIGURED_FEATURES.register(
                "silverwood_planted_configured", () -> new ConfiguredFeature<>(
                        Features.SILVERWOOD_PLAYER_PLANTED_FEATURE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(ThaumcraftBlocks.SILVERWOOD_LOG),
                                new StraightTrunkPlacer(7, 2, 0),
                                BlockStateProvider.simple(ThaumcraftBlocks.SILVERWOOD_LEAVES),
                                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                                new TwoLayersFeatureSize(1, 0, 1)
                        ).build()
                )
        );
        public static final RegistrySupplier<ConfiguredFeature<TreeConfiguration, ?>> SUPPLIER_SILVERWOOD_GENERATED_CONFIGURED
                = WORLD_GEN_CONFIGURED_FEATURES.register(
                "silverwood_generated_configured", () -> new ConfiguredFeature<>(
                        Features.SILVERWOOD_GENERATED_FEATURE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(ThaumcraftBlocks.SILVERWOOD_LOG),
                                new StraightTrunkPlacer(7, 2, 0),
                                BlockStateProvider.simple(ThaumcraftBlocks.SILVERWOOD_LEAVES),
                                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                                new TwoLayersFeatureSize(1, 0, 1)
                        ).build()
                )
        );
        public static final RegistrySupplier<ConfiguredFeature<TreeConfiguration, ?>> SUPPLIER_GREATWOOD_PLAYER_PLANT_CONFIGURED
                = WORLD_GEN_CONFIGURED_FEATURES.register(
                "greatwood_planted_configured", () -> new ConfiguredFeature<>(
                        Features.GREATWOOD_PLAYER_PLANTED_FEATURE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(ThaumcraftBlocks.GREATWOOD_LOG),
                                new StraightTrunkPlacer(7, 2, 0),
                                BlockStateProvider.simple(ThaumcraftBlocks.GREATWOOD_LEAVES),
                                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                                new TwoLayersFeatureSize(1, 0, 1)
                        ).build()
                )
        );
        public static final RegistrySupplier<ConfiguredFeature<TreeConfiguration, ?>> SUPPLIER_GREATWOOD_GENERATED_CONFIGURED
                = WORLD_GEN_CONFIGURED_FEATURES.register(
                "greatwood_generated_configured", () -> new ConfiguredFeature<>(
                        Features.GREATWOOD_GENERATED_FEATURE,
                        new TreeConfiguration.TreeConfigurationBuilder(
                                BlockStateProvider.simple(ThaumcraftBlocks.GREATWOOD_LOG),
                                new StraightTrunkPlacer(7, 2, 0),
                                BlockStateProvider.simple(ThaumcraftBlocks.GREATWOOD_LEAVES),
                                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                                new TwoLayersFeatureSize(1, 0, 1)
                        ).build()
                )
        );
        public static final RegistrySupplier<ConfiguredFeature<RandomPatchConfiguration, ?>> SUPPLIER_CINDER_PEARL_GENERATED_CONFIGURED =
                WORLD_GEN_CONFIGURED_FEATURES.register(
                        "cinder_pearl_patch", () ->
                                new ConfiguredFeature<>(
                                        Feature.RANDOM_PATCH,
                                        new RandomPatchConfiguration(
                                                32, // tries（尝试次数，dead bush 是 32）
                                                7,
                                                3,
                                                PlacementUtils.filtered(
                                                        Feature.SIMPLE_BLOCK,
                                                        new SimpleBlockConfiguration(
                                                                BlockStateProvider.simple(
                                                                        ThaumcraftBlocks.CINDER_PEARL
                                                                )
                                                        ),
                                                        new BlockPredicate() {
                                                            @Override
                                                            public @NotNull BlockPredicateType<?> type() {
                                                                return BlockPredicateType.MATCHING_BLOCKS;
                                                            }

                                                            @Override
                                                            public boolean test(WorldGenLevel level, BlockPos pos) {
                                                                var blockState = level.getBlockState(pos);
                                                                var blockStateDown = level.getBlockState(pos.below());
                                                                return blockState.isAir()
                                                                        && (
                                                                        blockStateDown.is(Blocks.SAND)
                                                                                || blockStateDown.is(Blocks.RED_SAND)
                                                                                || blockStateDown.is(Blocks.TERRACOTTA)
                                                                )
                                                                        ;
                                                            }
                                                        }
                                                )
                                        )
                                )
                );

        static {
            WORLD_GEN_CONFIGURED_FEATURES.register();
        }
    }

    public static class PlacedFeatureRegistry {
        public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
                DeferredRegister.create(Thaumcraft.MOD_ID,Registries.PLACED_FEATURE);
        public static final ResourceKey<PlacedFeature> CINDER_PEARL_PLACED_KEY =
                ResourceKey.create(
                        Registries.PLACED_FEATURE,
                        new ResourceLocation(Thaumcraft.MOD_ID, "cinder_pearl_placed")
                );

        static {
            PLACED_FEATURES.register();
        }
    }

    public static class BiomeModifiers {
        public static void init(){
            BiomeModifications.addProperties(
                    context -> {
                        var key = context.getKey();
                        return key.isPresent() && (
                                key.get().equals(Biomes.DESERT.location()) ||
                                        key.get().equals(Biomes.BADLANDS.location())
                        );
                    },
                    (context, properties) -> properties.getGenerationProperties()
                            .addFeature(
                                    GenerationStep.Decoration.VEGETAL_DECORATION,
                                    PlacedFeatureRegistry.CINDER_PEARL_PLACED_KEY
                            )
            );
        }
    }
    public static void init() {
        BiomeModifiers.init();
    }
}
