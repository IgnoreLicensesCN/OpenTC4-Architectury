package thaumcraft.common.lib.world.biomes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import thaumcraft.common.Thaumcraft;

public class ThaumcraftBiomeTags {
    public static final TagKey<Biome> MANA_BEAN_SURVIVES = TagKey.create(Registries.BIOME, new ResourceLocation(
            Thaumcraft.MOD_ID,"mana_bean_survives"));
    public static void init(){}
}
