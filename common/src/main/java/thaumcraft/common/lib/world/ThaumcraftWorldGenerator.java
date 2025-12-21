package thaumcraft.common.lib.world;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.expands.worldgen.node.NodeGenerationManager;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.biomes.BiomeGenTaint;
import thaumcraft.common.lib.world.biomes.BiomeHandler;
import thaumcraft.common.lib.world.dim.MazeHandler;
import thaumcraft.common.lib.world.dim.MazeThread;
import thaumcraft.common.tiles.AbstractNodeBlockEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class ThaumcraftWorldGenerator implements IWorldGenerator {
    public static BiomeGenBase biomeTaint;
    public static BiomeGenBase biomeEerie;
    public static BiomeGenBase biomeMagicalForest;
    public static BiomeGenBase biomeEldritchLands;
    static Collection<Aspect> c;
    static ArrayList<Aspect> basicAspects;
    static ArrayList<Aspect> complexAspects;
    public static HashMap<Integer, Integer> dimensionBlacklist;
    public static HashMap<Integer, Integer> biomeBlacklist;
    HashMap<Integer, Boolean> structureNode = new HashMap<>();

    public static int getFirstFreeBiomeSlot(int old) {
        for (int a = 0; a < BiomeGenBase.getBiomeGenArray().length; ++a) {
            if (BiomeGenBase.getBiomeGenArray()[a] == null) {
                Thaumcraft.log.warn("Biome slot {} already occupied. Using first free biome slot at {}", old, a);
                return a;
            }
        }

        return -1;
    }

    public void initialize() {
        BiomeGenTaint.blobs = new WorldGenBlockBlob(ConfigBlocks.blockTaint, 0);
        BiomeManager.addBiome(BiomeType.WARM, new BiomeManager.BiomeEntry(biomeMagicalForest, Config.biomeMagicalForestWeight));
        BiomeManager.addBiome(BiomeType.COOL, new BiomeManager.BiomeEntry(biomeMagicalForest, Config.biomeMagicalForestWeight));
        BiomeManager.addBiome(BiomeType.WARM, new BiomeManager.BiomeEntry(biomeTaint, Config.biomeTaintWeight));
        BiomeManager.addBiome(BiomeType.COOL, new BiomeManager.BiomeEntry(biomeTaint, Config.biomeTaintWeight));
    }

    public static void addDimBlacklist(int dim, int level) {
        dimensionBlacklist.put(dim, level);
    }

    public static int getDimBlacklist(int dim) {
        return dimensionBlacklist.getOrDefault(dim, -1);
    }

    public static void addBiomeBlacklist(int biome, int level) {
        biomeBlacklist.put(biome, level);
    }

    public static int getBiomeBlacklist(int biome) {
        return biomeBlacklist.getOrDefault(biome, -1);
    }

    public void generate(Random random, int chunkX, int chunkZ, Level world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        this.worldGeneration(random, chunkX, chunkZ, world, true);
    }

    public void worldGeneration(Random random, int chunkX, int chunkZ, Level world, boolean newGen) {
        if (world.dimension() == Config.dimensionOuterId) {
            MazeHandler.generateEldritch(world, random, chunkX, chunkZ);
            world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
        } else {
            switch (world.dimension()) {
                case -1:
                    this.generateNether(world, random, chunkX, chunkZ, newGen);
                case 1:
                    break;
                default:
                    this.generateSurface(world, random, chunkX, chunkZ, newGen);
            }

            if (!newGen) {
                world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
            }
        }

    }

    private boolean generateTotem(Level world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
        if (Config.genStructure
                && (world.dimension() == 0 || world.dimension() == 1)
                && newGen
                && !auraGen && random.nextInt(Config.nodeRarity * 10) == 0
        ) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            int topy = world.dimension() == -1
                    ? Utils.getFirstUncoveredY(world, x, z) - 1
                    : world.getHeight(Heightmap.Types.WORLD_SURFACE,x, z) - 1;
            if (topy > world.getActualHeight()) {
                return false;
            }

            if (world.getBlock(x, topy, z) != null && world.getBlock(x, topy, z).isLeaves(world, x, topy, z)) {
                do {
                    --topy;
                } while (world.getBlock(x, topy, z) != Blocks.grass && topy > 40);
            }

            if (world.getBlock(x, topy, z) == Blocks.snow_layer || world.getBlock(x, topy, z) == Blocks.tallgrass) {
                --topy;
            }

            if (world.getBlock(x, topy, z) == Blocks.grass || world.getBlock(x, topy, z) == Blocks.sand || world.getBlock(x, topy, z) == Blocks.dirt || world.getBlock(x, topy, z) == Blocks.stone || world.getBlock(x, topy, z) == Blocks.netherrack) {
                int count;
                for (count = 1; (world.isAirBlock(x, topy + count, z) || world.getBlock(x, topy + count, z) == Blocks.snow_layer || world.getBlock(x, topy + count, z) == Blocks.tallgrass) && count < 3; ++count) {
                }

                if (count >= 2) {
                    world.setBlock(x, topy, z, ConfigBlocks.blockCosmeticSolid, 1, 3);
                    count = 1;

                    while ((world.isAirBlock(x, topy + count, z) || world.getBlock(x, topy + count, z) == Blocks.snow_layer || world.getBlock(x, topy + count, z) == Blocks.tallgrass) && count < 5) {
                        world.setBlock(x, topy + count, z, ConfigBlocks.blockCosmeticSolid, 0, 3);
                        if (count > 1 && random.nextInt(4) == 0) {
                            world.setBlock(x, topy + count, z, ConfigBlocks.blockCosmeticSolid, 8, 3);
                            createRandomNodeAt(world, x, topy + count, z, random, false, true, false);
                            count = 5;
                            auraGen = true;
                        }

                        ++count;
                        if (count >= 5 && !auraGen) {
                            world.setBlock(x, topy + 5, z, ConfigBlocks.blockCosmeticSolid, 8, 3);
                            createRandomNodeAt(world, x, topy + 5, z, random, false, true, false);
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean generateWildNodes(Level world, Random random, int chunkX, int chunkZ, boolean auraGen, boolean newGen) {
        return NodeGenerationManager.generateWildNodes(world, random, chunkX, chunkZ, auraGen, newGen);
//        if (Config.genAura && random.nextInt(Config.nodeRarity) == 0 && !auraGen) {
//            int x = chunkX * 16 + random.nextInt(16);
//            int z = chunkZ * 16 + random.nextInt(16);
//            int q = Utils.getFirstUncoveredY(world, x, z);
//            if (q < 2) {
//                q = world.provider.getAverageGroundLevel() + random.nextInt(64) - 32 + Utils.getFirstUncoveredY(world, x, z);
//            }
//
//            if (q < 2) {
//                q = 32 + random.nextInt(64);
//            }
//
//            if (world.isAirBlock(x, q + 1, z)) {
//                ++q;
//            }
//
//            int p = random.nextInt(4);
//            Block b = world.getBlock(x, q + p, z);
//            if (world.isAirBlock(x, q + p, z) || b.isReplaceable(world, x, q + p, z)) {
//                q += p;
//            }
//
//            if (q > world.getActualHeight()) {
//                return false;
//            } else {
//                createRandomNodeAt(world, x, q, z, random, false, false, false);
//                return true;
//            }
//        }
//        else {
//            return false;
//        }
    }

    @Deprecated(forRemoval = true,since = "we have new api called same name")
    public static void createRandomNodeAt(WorldGenLevel world, int x, int y, int z, RandomSource random, boolean silverwood, boolean eerie, boolean small) {
        if (basicAspects.isEmpty()) {
            for (Aspect as : c) {
                if (as.getComponents() != null) {
                    complexAspects.add(as);
                } else {
                    basicAspects.add(as);
                }
            }
        }

        NodeType type = NodeType.NORMAL;
        if (silverwood) {
            type = NodeType.PURE;
        } else if (eerie) {
            type = NodeType.DARK;
        } else if (random.nextInt(Config.specialNodeRarity) == 0) {
            switch (random.nextInt(10)) {
                case 0:
                case 1:
                case 2:
                    type = NodeType.DARK;
                    break;
                case 3:
                case 4:
                case 5:
                    type = NodeType.UNSTABLE;
                    break;
                case 6:
                case 7:
                case 8:
                    type = NodeType.PURE;
                    break;
                case 9:
                    type = NodeType.HUNGRY;
            }
        }

        NodeModifier modifier = null;
        if (random.nextInt(Config.specialNodeRarity / 2) == 0) {
            switch (random.nextInt(3)) {
                case 0:
                    modifier = NodeModifier.BRIGHT;
                    break;
                case 1:
                    modifier = NodeModifier.PALE;
                    break;
                case 2:
                    modifier = NodeModifier.FADING;
            }
        }

        BiomeGenBase bg = world.getBiomeGenForCoords(x, z);
        int baura = BiomeHandler.getBiomeAura(bg);
        if (type != NodeType.PURE && bg.biomeID == biomeTaint.biomeID) {
            baura = (int) ((float) baura * 1.5F);
            if (random.nextBoolean()) {
                type = NodeType.TAINTED;
                baura = (int) ((float) baura * 1.5F);
            }
        }

        if (silverwood || small) {
            baura /= 4;
        }

        int value = random.nextInt(baura / 2) + baura / 2;
        Aspect ra = BiomeHandler.getRandomBiomeTag(bg.biomeID, random);
        AspectList al = new AspectList();
        if (ra != null) {
            al.addAll(ra, 2);
        } else {
            Aspect aa = complexAspects.get(random.nextInt(complexAspects.size()));
            al.addAll(aa, 1);
            aa = basicAspects.get(random.nextInt(basicAspects.size()));
            al.addAll(aa, 1);
        }

        for (int a = 0; a < 3; ++a) {
            if (random.nextBoolean()) {
                if (random.nextInt(Config.specialNodeRarity) == 0) {
                    Aspect aa = complexAspects.get(random.nextInt(complexAspects.size()));
                    al.mergeWithHighest(aa, 1);
                } else {
                    Aspect aa = basicAspects.get(random.nextInt(basicAspects.size()));
                    al.mergeWithHighest(aa, 1);
                }
            }
        }

        if (type == NodeType.HUNGRY) {
            al.mergeWithHighest(Aspect.HUNGER, 2);
            if (random.nextBoolean()) {
                al.mergeWithHighest(Aspect.GREED, 1);
            }
        } else if (type == NodeType.PURE) {
            if (random.nextBoolean()) {
                al.mergeWithHighest(Aspect.LIFE, 2);
            } else {
                al.mergeWithHighest(Aspect.ORDER, 2);
            }
        } else if (type == NodeType.DARK) {
            if (random.nextBoolean()) {
                al.mergeWithHighest(Aspect.DEATH, 1);
            }

            if (random.nextBoolean()) {
                al.mergeWithHighest(Aspect.UNDEAD, 1);
            }

            if (random.nextBoolean()) {
                al.mergeWithHighest(Aspect.ENTROPY, 1);
            }

            if (random.nextBoolean()) {
                al.mergeWithHighest(Aspect.DARKNESS, 1);
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
            al.mergeWithHighest(Aspect.WATER, 1);
        }

        if (lava > 100) {
            al.mergeWithHighest(Aspect.FIRE, 1);
            al.mergeWithHighest(Aspect.EARTH, 1);
        }

        if (stone > 500) {
            al.mergeWithHighest(Aspect.EARTH, 1);
        }

        if (foliage > 100) {
            al.mergeWithHighest(Aspect.PLANT, 1);
        }

        int[] spread = new int[al.size()];
        float total = 0.0F;

        for (int a = 0; a < spread.length; ++a) {
            if (al.getAmount(al.getAspectsSorted()[a]) == 2) {
                spread[a] = 50 + random.nextInt(25);
            } else {
                spread[a] = 25 + random.nextInt(50);
            }

            total += (float) spread[a];
        }

        for (int a = 0; a < spread.length; ++a) {
            al.mergeWithHighest(al.getAspectsSorted()[a], (int) ((float) spread[a] / total * (float) value));
        }

        createNodeAt(world, x, y, z, type, modifier, al);
    }

    public static void createNodeAt(Level world, int x, int y, int z, NodeType nt, NodeModifier nm, AspectList al) {
        if (world.isAirBlock(x, y, z)) {
            world.setBlock(x, y, z, ConfigBlocks.blockAiry, 0, 0);
        }

        BlockEntity te = world.getBlockEntity(x, y, z);
        if (te instanceof AbstractNodeBlockEntity) {
            ((AbstractNodeBlockEntity) te).setNodeType(nt);
            ((AbstractNodeBlockEntity) te).setNodeModifier(nm);
            ((AbstractNodeBlockEntity) te).setAspects(al);
        }

        world.markBlockForUpdate(x, y, z);
    }

    private void generateSurface(Level world, Random random, int chunkX, int chunkZ, boolean newGen) {
        boolean auraGen = false;
        int blacklist = getDimBlacklist(world.dimension());
        if (blacklist == -1 && Config.genTrees && !world.getWorldInfo().getTerrainType().getWorldTypeName().startsWith("flat") && (newGen || Config.regenTrees)) {
            this.generateVegetation(world, random, chunkX, chunkZ, newGen);
        }

        if (blacklist != 0 && blacklist != 2) {
            this.generateOres(world, random, chunkX, chunkZ, newGen);
        }

        if (blacklist != 0 && blacklist != 2 && Config.genAura && (newGen || Config.regenAura)) {
            ChunkPosition var7 = (new MapGenScatteredFeature()).func_151545_a(world, chunkX * 16 + 8, world.getHeight(Heightmap.Types.WORLD_SURFACE,chunkX * 16 + 8, chunkZ * 16 + 8), chunkZ * 16 + 8);
            if (var7 != null && !this.structureNode.containsKey(var7.hashCode())) {
                auraGen = true;
                this.structureNode.put(var7.hashCode(), true);
                createRandomNodeAt(world, var7.chunkPosX, world.getHeight(Heightmap.Types.WORLD_SURFACE,var7.chunkPosX, var7.chunkPosZ) + 3, var7.chunkPosZ, random, false, false, false);
            }

            auraGen = this.generateWildNodes(world, random, chunkX, chunkZ, auraGen, newGen);
        }

        if (blacklist == -1
                && Config.genStructure
                && world.dimension() == 0
                && !world.getWorldInfo().getTerrainType().getWorldTypeName().startsWith("flat")
                && (newGen || Config.regenStructure)
        ) {
            int randPosX = chunkX * 16 + random.nextInt(16);
            int randPosZ = chunkZ * 16 + random.nextInt(16);
            int randPosY = world.getHeight(Heightmap.Types.WORLD_SURFACE,randPosX, randPosZ) - 9;
            if (randPosY < world.getActualHeight()) {
                world.getChunkFromBlockCoords(MathHelper.floor_double(randPosX), MathHelper.floor_double(randPosZ));
//                if (random.nextInt(150) == 0) {
//                    if (WorldGenMound.generateStatic(world, random, randPosX, randPosY, randPosZ)) {
//                        auraGen = true;
//                        int value = random.nextInt(200) + 400;
//                        createRandomNodeAt(
//                                world,
//                                randPosX + 9, randPosY + 8, randPosZ + 9,
//                                random,
//                                false,
//                                true,
//                                false
//                        );
//                    }
//                } else
                    if (random.nextInt(66) == 0) {
                    WorldGenEldritchRing stonering = new WorldGenEldritchRing();
                    randPosY += 8;
                    int w = 11 + random.nextInt(6) * 2;
                    int h = 11 + random.nextInt(6) * 2;
                    stonering.chunkX = chunkX;
                    stonering.chunkZ = chunkZ;
                    stonering.width = w;
                    stonering.height = h;
                    if (stonering.generate(world, random, randPosX, randPosY, randPosZ)) {
                        auraGen = true;
                        createRandomNodeAt(world, randPosX, randPosY + 2, randPosZ, random, false, true, false);
                        Thread t = new Thread(new MazeThread(chunkX, chunkZ, w, h, random.nextLong()));
                        t.start();
                    }
                } else if (random.nextInt(40) == 0) {
                    randPosY += 9;
                    WorldGenerator hilltopStones = new WorldGenHilltopStones();
                    if (hilltopStones.generate(world, random, randPosX, randPosY, randPosZ)) {
                        auraGen = true;
                        createRandomNodeAt(world, randPosX, randPosY + 5, randPosZ, random, false, true, false);
                    }
                }
            }

            this.generateTotem(world, random, chunkX, chunkZ, auraGen, newGen);
        }

    }

    private void generateVegetation(Level world, Random random, int chunkX, int chunkZ, boolean newGen) {
        BiomeGenBase bgb = world.getBiomeGenForCoords(chunkX * 16 + 8, chunkZ * 16 + 8);
        if (getBiomeBlacklist(bgb.biomeID) == -1) {
            if (random.nextInt(60) == 3) {
                generateSilverwood(world, random, chunkX, chunkZ);
            }

            if (random.nextInt(25) == 7) {
                generateGreatwood(world, random, chunkX, chunkZ);
            }

            int randPosX = chunkX * 16 + random.nextInt(16);
            int randPosZ = chunkZ * 16 + random.nextInt(16);
            int randPosY = world.getHeight(Heightmap.Types.WORLD_SURFACE,randPosX, randPosZ);
            if (randPosY <= world.getActualHeight()) {
                if (world.getBiomeGenForCoords(randPosX, randPosZ).topBlock == Blocks.sand && world.getBiomeGenForCoords(randPosX, randPosZ).temperature > 1.0F && random.nextInt(30) == 0) {
                    generateFlowers(world, random, randPosX, randPosY, randPosZ, 3);
                }

            }
        }
    }

    private void generateOres(Level world, Random random, int chunkX, int chunkZ, boolean newGen) {
        BiomeGenBase bgb = world.getBiomeGenForCoords(chunkX * 16 + 8, chunkZ * 16 + 8);
        if (getBiomeBlacklist(bgb.biomeID) != 0 && getBiomeBlacklist(bgb.biomeID) != 2) {
            if (Config.genCinnibar && (newGen || Config.regenCinnibar)) {
                for (int i = 0; i < 18; ++i) {
                    int randPosX = chunkX * 16 + random.nextInt(16);
                    int randPosY = random.nextInt(world.getHeight() / 5);
                    int randPosZ = chunkZ * 16 + random.nextInt(16);
                    Block block = world.getBlock(randPosX, randPosY, randPosZ);
                    if (block != null && block.isReplaceableOreGen(world, randPosX, randPosY, randPosZ, Blocks.stone)) {
                        world.setBlock(randPosX, randPosY, randPosZ, ConfigBlocks.blockCustomOre, 0, 0);
                    }
                }
            }

            if (Config.genAmber && (newGen || Config.regenAmber)) {
                for (int i = 0; i < 20; ++i) {
                    int randPosX = chunkX * 16 + random.nextInt(16);
                    int randPosZ = chunkZ * 16 + random.nextInt(16);
                    int randPosY = world.getHeight(Heightmap.Types.WORLD_SURFACE,randPosX, randPosZ) - random.nextInt(25);
                    Block block = world.getBlock(randPosX, randPosY, randPosZ);
                    if (block != null && block.isReplaceableOreGen(world, randPosX, randPosY, randPosZ, Blocks.stone)) {
                        world.setBlock(randPosX, randPosY, randPosZ, ConfigBlocks.blockCustomOre, 7, 2);
                    }
                }
            }

            if (Config.genInfusedStone && (newGen || Config.regenInfusedStone)) {
                for (int i = 0; i < 8; ++i) {
                    int randPosX = chunkX * 16 + random.nextInt(16);
                    int randPosZ = chunkZ * 16 + random.nextInt(16);
                    int randPosY = random.nextInt(Math.max(5, world.getHeight(Heightmap.Types.WORLD_SURFACE,randPosX, randPosZ) - 5));
                    int md = random.nextInt(6) + 1;
                    if (random.nextInt(3) == 0) {
                        Aspect tag = BiomeHandler.getRandomBiomeTag(world.getBiomeGenForCoords(randPosX, randPosZ).biomeID, random);
                        if (tag == null) {
                            md = 1 + random.nextInt(6);
                        } else if (tag == Aspect.AIR) {
                            md = 1;
                        } else if (tag == Aspect.FIRE) {
                            md = 2;
                        } else if (tag == Aspect.WATER) {
                            md = 3;
                        } else if (tag == Aspect.EARTH) {
                            md = 4;
                        } else if (tag == Aspect.ORDER) {
                            md = 5;
                        } else if (tag == Aspect.ENTROPY) {
                            md = 6;
                        }
                    }

                    try {
                        (new WorldGenMinable(ConfigBlocks.blockCustomOre, md, 6, Blocks.stone)).generate(world, random, randPosX, randPosY, randPosZ);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private void generateNether(Level world, Random random, int chunkX, int chunkZ, boolean newGen) {
        boolean auraGen = false;
        if (!world.getWorldInfo().getTerrainType().getWorldTypeName().startsWith("flat") && (newGen || Config.regenStructure)) {
            this.generateTotem(world, random, chunkX, chunkZ, auraGen, newGen);
        }

        if (newGen || Config.regenAura) {
            this.generateWildNodes(world, random, chunkX, chunkZ, auraGen, newGen);
        }

    }

    public static boolean generateFlowers(Level world, Random random, int x, int y, int z, int flower) {
        WorldGenerator flowers = new WorldGenCustomFlowers(ConfigBlocks.blockCustomPlant, flower);
        return flowers.generate(world, random, x, y, z);
    }

    public static boolean generateGreatwood(Level world, Random random, int chunkX, int chunkZ) {
        int x = chunkX * 16 + random.nextInt(16);
        int z = chunkZ * 16 + random.nextInt(16);
        int y = world.getHeight(Heightmap.Types.WORLD_SURFACE,x, z);
        Biome biome = world.getBiome(new BlockPos(x, y, z)).value();
//        int bio = world.getBiomeGenForCoords(x, z).biomeID;
        if (BiomeHandler.getBiomeSupportsGreatwood(biome) > random.nextFloat()) {
            boolean t = (new WorldGenGreatwoodTrees(false)).generateInternal(world, random, x, y, z, random.nextInt(8) == 0);
            return t;
        } else {
            return false;
        }
    }

    public static boolean generateSilverwood(Level world, Random random, int chunkX, int chunkZ) {
        int x = chunkX * 16 + random.nextInt(16);
        int z = chunkZ * 16 + random.nextInt(16);
        int y = world.getHeight(Heightmap.Types.WORLD_SURFACE,x, z);
        Holder<Biome> bio = world.getBiome(new BlockPos(x, y, z));
//        BiomeGenBase bio = world.getBiomeGenForCoords(x, z);
        if ((bio.is(biomeMagicalForest)
                || bio.is(biomeTaint)
                || !bio.is(Type.MAGICAL))
                && !bio.is(Biomes.FOREST)
                && !bio.is(Biomes.BIRCH_FOREST)
                && !bio.is(Biomes.OLD_GROWTH_BIRCH_FOREST)
        ) {
            return false;
        } else {
            boolean t = (new WorldGenSilverwoodTrees(false, 7, 4)).generate(world, random, x, y, z);
            return t;
        }
    }

    static {
        c = Aspect.aspects.values();
        basicAspects = new ArrayList<>();
        complexAspects = new ArrayList<>();
        dimensionBlacklist = new HashMap<>();
        biomeBlacklist = new HashMap<>();
    }
}
