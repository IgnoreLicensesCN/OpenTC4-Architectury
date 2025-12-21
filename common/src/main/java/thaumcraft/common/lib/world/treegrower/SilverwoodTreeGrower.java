package thaumcraft.common.lib.world.treegrower;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.world.registries.ThaumcraftWorldGenConfiguredFeatures;

//sapling
public class SilverwoodTreeGrower extends AbstractTreeGrower {
    public SilverwoodTreeGrower() {}

    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean bl) {

        return (ResourceKey<ConfiguredFeature<?,?>>)(Object)ThaumcraftWorldGenConfiguredFeatures.Configureds.SILVERWOOD_PLANTED_CONFIGURED_KEY;
    }
}
