package thaumcraft.common.lib.world.biomes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import thaumcraft.common.lib.world.HolderCache;

public class ThaumcraftBiomeLookups {

    private static final HolderCache<Biome> holderCache = HolderCache.of(Registries.BIOME);

    public static Holder<Biome> biomeHolderForLevel(Level level, ResourceKey<Biome> biomeKey) {
        return holderCache.getHolder(level, biomeKey);
    }
}
