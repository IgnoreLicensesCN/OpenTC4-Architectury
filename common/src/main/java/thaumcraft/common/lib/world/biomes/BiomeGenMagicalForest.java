package thaumcraft.common.lib.world.biomes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockFlower;
import net.minecraft.world.entity.monster.EntityEnderman;
import net.minecraft.world.entity.monster.EntityWitch;
import net.minecraft.world.entity.passive.EntityHorse;
import net.minecraft.world.entity.passive.EntityWolf;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.*;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.WorldGenBigMagicTree;
import thaumcraft.common.lib.world.WorldGenGreatwoodTrees;
import thaumcraft.common.lib.world.WorldGenManaPods;
import thaumcraft.common.lib.world.WorldGenSilverwoodTrees;

import java.util.Random;

public class BiomeGenMagicalForest extends BiomeGenBase {
   protected WorldGenBigMagicTree bigTree = new WorldGenBigMagicTree(false);
   private static final WorldGenBlockBlob blobs;

   public BiomeGenMagicalForest(int par1) {
      super(par1);
      this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 2, 1, 3));
      this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 2, 1, 3));
      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 3, 1, 1));
      this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 3, 1, 1));
      if (Config.spawnPech) {
         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityPech.class, 10, 1, 2));
      }

      if (Config.spawnWisp) {
         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWisp.class, 10, 1, 2));
      }

      this.theBiomeDecorator.treesPerChunk = 2;
      this.theBiomeDecorator.flowersPerChunk = 10;
      this.theBiomeDecorator.grassPerChunk = 12;
      this.theBiomeDecorator.waterlilyPerChunk = 6;
      this.theBiomeDecorator.mushroomsPerChunk = 6;
      this.setTemperatureRainfall(0.7F, 0.6F);
      this.setHeight(new BiomeGenBase.Height(0.2F, 0.2F));
      this.setBiomeName("Magical Forest");
      this.setColor(Config.blueBiome ? 6728396 : 6747307);
      this.flowers.clear();

      for(int x = 0; x < BlockFlower.field_149859_a.length; ++x) {
         this.addFlower(Blocks.red_flower, x, 10);
      }

   }

   public WorldGenAbstractTree func_150567_a(Random par1Random) {
      return par1Random.nextInt(14) == 0 ? new WorldGenSilverwoodTrees(false, 8, 5) : (par1Random.nextInt(10) == 0 ? new WorldGenGreatwoodTrees(false) : this.bigTree);
   }

   public WorldGenerator getRandomWorldGenForGrass(Random par1Random) {
      return par1Random.nextInt(4) == 0 ? new WorldGenTallGrass(Blocks.tallgrass, 2) : new WorldGenTallGrass(Blocks.tallgrass, 1);
   }

   @SideOnly(Side.CLIENT)
   public int getBiomeGrassColor(int x, int y, int z) {
      return Config.blueBiome ? 6728396 : 5635969;
   }

   @SideOnly(Side.CLIENT)
   public int getBiomeFoliageColor(int x, int y, int z) {
      return Config.blueBiome ? 7851246 : 6750149;
   }

   public int getWaterColorMultiplier() {
      return 30702;
   }

   public void decorate(Level world, Random random, int x, int z) {
      int k = random.nextInt(3);

      for(int l = 0; l < k; ++l) {
         int i1 = x + random.nextInt(16) + 8;
         int j1 = z + random.nextInt(16) + 8;
         int k1 = world.getHeight(Heightmap.Types.WORLD_SURFACE,i1, j1);
         blobs.generate(world, random, i1, k1, j1);
      }

      for(int var16 = 0; var16 < 4; ++var16) {
         for(int var18 = 0; var18 < 4; ++var18) {
            int i1 = x + var16 * 4 + 1 + 8 + random.nextInt(3);
            int j1 = z + var18 * 4 + 1 + 8 + random.nextInt(3);
            int k1 = world.getHeight(Heightmap.Types.WORLD_SURFACE,i1, j1);
            if (random.nextInt(40) == 0) {
               WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
               worldgenbigmushroom.generate(world, random, i1, k1, j1);
            }
         }
      }

      super.decorate(world, random, x, z);
      WorldGenManaPods worldgenpods = new WorldGenManaPods();

      for(int var17 = 0; var17 < 10; ++var17) {
         int var19 = x + random.nextInt(16) + 8;
         byte b0 = 64;
         int i1 = z + random.nextInt(16) + 8;
         worldgenpods.generate(world, random, var19, b0, i1);
      }

      for(int a = 0; a < 8; ++a) {
         int xx = x + random.nextInt(16);
         int zz = z + random.nextInt(16);

         int yy;
         for(yy = world.getHeight(Heightmap.Types.WORLD_SURFACE,xx, zz); yy > 50 && world.getBlock(xx, yy, zz) != Blocks.grass; --yy) {
         }

         Block l1 = world.getBlock(xx, yy, zz);
         if (l1 == Blocks.grass && world.getBlock(xx, yy + 1, zz).isReplaceable(world, xx, yy + 1, zz) && this.isBlockAdjacentToWood(world, xx, yy + 1, zz)) {
            world.setBlock(xx, yy + 1, zz, ConfigBlocks.blockCustomPlant, 5, 2);
         }
      }

   }

   private boolean isBlockAdjacentToWood(IBlockAccess world, int x, int y, int z) {
      int count = 0;

      for(int xx = -1; xx <= 1; ++xx) {
         for(int yy = -1; yy <= 1; ++yy) {
            for(int zz = -1; zz <= 1; ++zz) {
               if ((xx != 0 || yy != 0 || zz != 0) && Utils.isWoodLog(world, xx + x, yy + y, zz + z)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public BiomeGenBase createMutation() {
      return null;
   }

   static {
      blobs = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);
   }
}
