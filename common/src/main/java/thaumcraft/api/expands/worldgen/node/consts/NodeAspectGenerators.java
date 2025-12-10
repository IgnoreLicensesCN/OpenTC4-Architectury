package thaumcraft.api.expands.worldgen.node.consts;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.expands.worldgen.node.listeners.NodeAspectGenerator;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.world.biomes.BiomeHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static thaumcraft.api.expands.worldgen.node.NodeGenerationManager.basicAspects;
import static thaumcraft.api.expands.worldgen.node.NodeGenerationManager.complexAspects;

public class NodeAspectGenerators {
    public static final NodeAspectGenerator DEFAULT_ASPECT_GENERATOR = new NodeAspectGenerator(0) {
        @Override
        @ParametersAreNonnullByDefault
        public AspectList getNodeAspects(World world,
                                         int x, int y, int z,
                                         Random random, boolean silverwood, boolean eerie, boolean small, AspectList previous,
                                         NodeType type,@Nullable NodeModifier modifier
                                         ) {
            BiomeGenBase bg = world.getBiomeGenForCoords(x, z);
            int baura = BiomeHandler.getBiomeAura(bg);
            if (type == NodeType.TAINTED) {

                baura = (int) ((float) baura * 1.5F);
            }

            if (silverwood || small) {
                baura /= 4;
            }

            int value = random.nextInt(baura / 2) + baura / 2;
            Aspect aspectFromBiome = BiomeHandler.getRandomBiomeTag(bg.biomeID, random);
            if (aspectFromBiome != null) {
                previous.add(aspectFromBiome, 2);
            } else {
                Aspect aa = complexAspects.get(random.nextInt(complexAspects.size()));
                previous.add(aa, 1);
                aa = basicAspects.get(random.nextInt(basicAspects.size()));
                previous.add(aa, 1);
            }

            for (int a = 0; a < 3; ++a) {
                if (random.nextBoolean()) {
                    if (random.nextInt(Config.specialNodeRarity) == 0) {
                        Aspect aa = complexAspects.get(random.nextInt(complexAspects.size()));
                        previous.merge(aa, 1);
                    } else {
                        Aspect aa = basicAspects.get(random.nextInt(basicAspects.size()));
                        previous.merge(aa, 1);
                    }
                }
            }

            if (type == NodeType.HUNGRY) {
                previous.merge(Aspect.HUNGER, 2);
                if (random.nextBoolean()) {
                    previous.merge(Aspect.GREED, 1);
                }
            } else if (type == NodeType.PURE) {
                if (random.nextBoolean()) {
                    previous.merge(Aspect.LIFE, 2);
                } else {
                    previous.merge(Aspect.ORDER, 2);
                }
            } else if (type == NodeType.DARK) {
                if (random.nextBoolean()) {
                    previous.merge(Aspect.DEATH, 1);
                }

                if (random.nextBoolean()) {
                    previous.merge(Aspect.UNDEAD, 1);
                }

                if (random.nextBoolean()) {
                    previous.merge(Aspect.ENTROPY, 1);
                }

                if (random.nextBoolean()) {
                    previous.merge(Aspect.DARKNESS, 1);
                }
            }

            int water = 0;
            int lava = 0;
            int stone = 0;
            int foliage = 0;

            try {
                for (int xx = -5; xx <= 5; ++xx) {
                    for (int yy = -5; yy <= 5; ++yy) {
                        for (int zz = -5; zz <= 5; ++zz) {
                            try {
                                Block bi = world.getBlock(x + xx, y + yy, z + zz);
                                if (bi.getMaterial() == Material.water) {
                                    ++water;
                                } else if (bi.getMaterial() == Material.lava) {
                                    ++lava;
                                } else if (bi == Blocks.stone) {
                                    ++stone;
                                }

                                if (bi.isFoliage(world, x + xx, y + yy, z + zz)) {
                                    ++foliage;
                                }
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            }

            if (water > 100) {
                previous.merge(Aspect.WATER, 1);
            }

            if (lava > 100) {
                previous.merge(Aspect.FIRE, 1);
                previous.merge(Aspect.EARTH, 1);
            }

            if (stone > 500) {
                previous.merge(Aspect.EARTH, 1);
            }

            if (foliage > 100) {
                previous.merge(Aspect.PLANT, 1);
            }

            int[] spread = new int[previous.size()];
            float total = 0.0F;

            for (int a = 0; a < spread.length; ++a) {
                if (previous.getAmount(previous.getAspectsSorted()[a]) == 2) {
                    spread[a] = 50 + random.nextInt(25);
                } else {
                    spread[a] = 25 + random.nextInt(50);
                }

                total += (float) spread[a];
            }

            for (int a = 0; a < spread.length; ++a) {
                previous.merge(previous.getAspectsSorted()[a], (int) ((float) spread[a] / total * (float) value));
            }
            return previous;
        }
    };
}
