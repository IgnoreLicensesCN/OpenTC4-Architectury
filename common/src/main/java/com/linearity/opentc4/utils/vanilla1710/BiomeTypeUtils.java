package com.linearity.opentc4.utils.vanilla1710;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BiomeTypeUtils {
    public static boolean hasTreeType(BiomeGenerationSettings generationSettings, String treeName) {
        for (HolderSet<PlacedFeature> featureStage : generationSettings.features()) {
            ;
            for (Holder<PlacedFeature> placedFeatureHolder : featureStage.stream().toList()) {
                PlacedFeature placedFeature = placedFeatureHolder.value();
                for (ConfiguredFeature<?, ?> configuredFeature : placedFeature.getFeatures().toList()) {
                    if (configuredFeature.config() instanceof TreeConfiguration treeConfig) {

                        if (treeConfig.trunkProvider instanceof SimpleStateProvider simpleStateProvider) {
                            if(simpleStateProvider.getState(null,null).is(Blocks.ACACIA_LOG)
                                    && (Objects.equals(treeName, "acacia"))
                            ) {
                                return true;
                            }
                            if(simpleStateProvider.getState(null,null).is(Blocks.SPRUCE_LOG)
                                    && (Objects.equals(treeName, "spruce"))
                            ) {
                                return true;
                            }
                            if(simpleStateProvider.getState(null,null).is(Blocks.OAK_LOG)
                                    && (Objects.equals(treeName, "oak"))
                            ) {
                                return true;
                            }
                            if(simpleStateProvider.getState(null,null).is(Blocks.JUNGLE_LOG)
                                    && (Objects.equals(treeName, "jungle"))
                            ) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
