package thaumcraft.api.expands.listeners.worldgen.node.consts;

import com.linearity.opentc4.utils.vanilla1710.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.expands.listeners.worldgen.node.listeners.NodeAspectGenerator;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.world.biomes.BiomeHandler;

import static thaumcraft.api.expands.listeners.worldgen.node.NodeGenerationManager.basicAspects;
import static thaumcraft.api.expands.listeners.worldgen.node.NodeGenerationManager.complexAspects;

public class NodeAspectGenerators {
    public static final NodeAspectGenerator DEFAULT_ASPECT_GENERATOR = new NodeAspectGenerator(0) {

        @Override
        public AspectList<Aspect>getNodeAspects(
                WorldGenLevel world,
                BlockPos pos,
                RandomSource random, boolean silverwood, boolean eerie, boolean small, AspectList<Aspect>previous,
                NodeType type, @Nullable NodeModifier modifier
                                         ) {
            
            Holder<Biome> biomeHolder = world.getBiome(pos);

            int baura = BiomeHandler.getBiomeAura(biomeHolder.value());
            if (type == NodeType.TAINTED) {

                baura = (int) ((float) baura * 1.5F);
            }

            if (silverwood || small) {
                baura /= 4;
            }

            int value = random.nextInt(baura / 2) + baura / 2;
            Aspect aspectFromBiome = BiomeHandler.getRandomBiomeTag(biomeHolder.value(), random);
            if (aspectFromBiome != null) {
                previous.addAll(aspectFromBiome, 2);
            } else {
                Aspect aa = complexAspects.get(random.nextInt(complexAspects.size()));
                previous.addAll(aa, 1);
                aa = basicAspects.get(random.nextInt(basicAspects.size()));
                previous.addAll(aa, 1);
            }

            for (int a = 0; a < 3; ++a) {
                if (random.nextBoolean()) {
                    if (random.nextInt(Config.specialNodeRarity) == 0) {
                        Aspect aa = complexAspects.get(random.nextInt(complexAspects.size()));
                        previous.mergeWithHighest(aa, 1);
                    } else {
                        Aspect aa = basicAspects.get(random.nextInt(basicAspects.size()));
                        previous.mergeWithHighest(aa, 1);
                    }
                }
            }

            if (type == NodeType.HUNGRY) {
                previous.mergeWithHighest(Aspects.HUNGER, 2);
                if (random.nextBoolean()) {
                    previous.mergeWithHighest(Aspects.GREED, 1);
                }
            } else if (type == NodeType.PURE) {
                if (random.nextBoolean()) {
                    previous.mergeWithHighest(Aspects.LIFE, 2);
                } else {
                    previous.mergeWithHighest(Aspects.ORDER, 2);
                }
            } else if (type == NodeType.DARK) {
                if (random.nextBoolean()) {
                    previous.mergeWithHighest(Aspects.DEATH, 1);
                }

                if (random.nextBoolean()) {
                    previous.mergeWithHighest(Aspects.UNDEAD, 1);
                }

                if (random.nextBoolean()) {
                    previous.mergeWithHighest(Aspects.ENTROPY, 1);
                }

                if (random.nextBoolean()) {
                    previous.mergeWithHighest(Aspects.DARKNESS, 1);
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
                                BlockState bi = world.getBlockState(pos.offset(xx,yy,zz));
                                FluidState fb = bi.getFluidState();
                                if (fb.is(Fluids.WATER) || fb.is(Fluids.FLOWING_WATER)) {
                                    ++water;
                                } else if (fb.is(Fluids.LAVA) || fb.is(Fluids.FLOWING_LAVA)) {
                                    ++lava;
                                } else if (bi.is(Blocks.STONE)) {
                                    ++stone;
                                }

                                if (BlockUtils.isFoliage(bi)) {
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
                previous.mergeWithHighest(Aspects.WATER, 1);
            }

            if (lava > 100) {
                previous.mergeWithHighest(Aspects.FIRE, 1);
                previous.mergeWithHighest(Aspects.EARTH, 1);
            }

            if (stone > 500) {
                previous.mergeWithHighest(Aspects.EARTH, 1);
            }

            if (foliage > 100) {
                previous.mergeWithHighest(Aspects.PLANT, 1);
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
                previous.mergeWithHighest(previous.getAspectsSorted()[a], (int) ((float) spread[a] / total * (float) value));
            }
            return previous;
        }
    };
}
