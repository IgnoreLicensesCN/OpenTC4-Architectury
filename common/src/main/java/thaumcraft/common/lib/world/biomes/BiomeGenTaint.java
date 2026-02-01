package thaumcraft.common.lib.world.biomes;

import com.linearity.colorannotation.annotation.RGBColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityTaintacle;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;
import thaumcraft.common.lib.world.WorldGenBigMagicTree;

import java.util.Random;

public class BiomeGenTaint extends BiomeGenBase {
   public static WorldGenBlockBlob blobs = null;
   protected WorldGenBigMagicTree bigTree = new WorldGenBigMagicTree(false);

   public static final  @RGBColor int TAINT_COLOR = 0x6d4189;
   public BiomeGenTaint(int par1) {
      super(par1);
      this.theBiomeDecorator.treesPerChunk = 2;
      this.theBiomeDecorator.flowersPerChunk = -999;
      this.theBiomeDecorator.grassPerChunk = 2;
      this.theBiomeDecorator.reedsPerChunk = -999;
      this.setBiomeName("Tainted Land");
      this.setColor(TAINT_COLOR);
      this.spawnableCreatureList.clear();
      this.spawnableMonsterList.clear();
      this.spawnableWaterCreatureList.clear();
      if (Config.spawnTaintacle) {
         this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityTaintacle.class, 1, 1, 1));
      }

   }

   public void decorate(Level world, Random random, int x, int z) {
      super.decorate(world, random, x, z);
      this.decorateSpecial(world, random, x, z);
   }

   public WorldGenAbstractTree func_150567_a(Random par1Random) {
      return par1Random.nextInt(8) == 0 ? this.bigTree : super.func_150567_a(par1Random);
   }

   public void decorateSpecial(Level world, Random random, int x, int z) {
      int k = random.nextInt(3);

      for(int l = 0; l < k; ++l) {
         int i1 = x + random.nextInt(16) + 8;
         int j1 = z + random.nextInt(16) + 8;
         int k1 = world.getHeight(Heightmap.Types.WORLD_SURFACE,i1, j1);
         blobs.generate(world, random, i1, k1, j1);
      }

      for(int a = 0; a < 10; ++a) {
         int xx = x + random.nextInt(16);
         int zz = z + random.nextInt(16);
         int yy = world.getHeight(Heightmap.Types.WORLD_SURFACE,xx, zz) - 1;
         Block l1 = world.getBlock(xx, yy, zz);
         if (l1 != Blocks.air) {
            if (l1 == Blocks.grass) {
               world.setBlock(xx, yy + 1, zz, ConfigBlocks.blockTaintFibres, 0, 2);
            } else if (l1.isReplaceable(world, xx, yy, zz) && world.getBlock(xx, yy - 1, zz) == Blocks.grass) {
               world.setBlock(xx, yy, zz, ConfigBlocks.blockTaintFibres, 0, 2);
            }
         }
      }

      for(int a = 0; a < 8; ++a) {
         int xx = x + random.nextInt(16);
         int zz = z + random.nextInt(16);
         int yy = Utils.getFirstUncoveredBlockHeight(world, xx, zz) + 1;
         if (world.getBiomeGenForCoords(xx, zz) != this) {
            Utils.setBiomeAt(world, xx, zz, ThaumcraftWorldGenerator.biomeTaint);
         }

         if (world.isAirBlock(xx, yy, zz) && BlockUtils.isAdjacentToSolidBlock(world, xx, yy, zz)) {
            world.setBlock(xx, yy, zz, ConfigBlocks.blockTaintFibres, 0, 2);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public int getBiomeGrassColor(int x, int y, int z) {
      return 7160201;
   }

   @SideOnly(Side.CLIENT)
   public int getBiomeFoliageColor(int x, int y, int z) {
      return 8154503;
   }

   public int getSkyColorByTemp(float par1) {
      return 8144127;
   }

   public int getWaterColorMultiplier() {
      return 13373832;
   }

   public BiomeGenBase createMutation() {
      return null;
   }
}
