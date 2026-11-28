package thaumcraft.common.lib.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;
import thaumcraft.common.Thaumcraft;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class ThaumcraftWorlds {
    public static final ResourceKey<Level> OUTER = ResourceKey.create(Registries.DIMENSION, Objects.requireNonNull(ResourceLocation.tryBuild(Thaumcraft.MOD_ID, "outer")));
    public static final ResourceKey<LevelStem> OUTER_STEM = ResourceKey.create(Registries.LEVEL_STEM, Objects.requireNonNull(ResourceLocation.tryBuild(Thaumcraft.MOD_ID, "outer")));

    private static boolean inited = false;
    public static void init() {
        if (inited) {
            return;
        }
        inited = true;


    }
}
