package thaumcraft.common.lib.world.treegrower;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.world.ThaumcraftWorldGenConfiguredFeatures;
import thaumcraft.common.lib.world.feature.SilverwoodTreeFeature;

import java.util.Random;

public class SilverwoodTreeGrower extends AbstractTreeGrower {
    private final SilverwoodTreeFeature treeFeature = new SilverwoodTreeFeature(true,7,5);
    public SilverwoodTreeGrower() {}

    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean bl) {

        return (ResourceKey<ConfiguredFeature<?,?>>)(Object)ThaumcraftWorldGenConfiguredFeatures.Configureds.SILVERWOOD_PLANTED_CONFIGURED_KEY;
    }
}
