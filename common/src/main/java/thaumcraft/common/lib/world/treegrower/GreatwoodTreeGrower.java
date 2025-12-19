package thaumcraft.common.lib.world.treegrower;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.world.ThaumcraftWorldGenConfiguredFeatures;

public class GreatwoodTreeGrower extends AbstractTreeGrower {
    public GreatwoodTreeGrower() {}

    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean bl) {

        return //TODO
    }
}
