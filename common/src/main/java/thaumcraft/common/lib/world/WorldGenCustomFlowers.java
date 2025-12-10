package thaumcraft.common.lib.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenCustomFlowers extends WorldGenerator {
   private Block plantBlock;
   private int plantBlockMeta;

   public WorldGenCustomFlowers(Block bi, int md) {
      this.plantBlock = bi;
      this.plantBlockMeta = md;
   }

   public boolean generate(World world, Random par2Random, int par3, int par4, int par5) {
      for(int var6 = 0; var6 < 18; ++var6) {
         int var7 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
         int var8 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
         int var9 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
         if (world.isAirBlock(var7, var8, var9) && (world.getBlock(var7, var8 - 1, var9) == Blocks.grass || world.getBlock(var7, var8 - 1, var9) == Blocks.sand)) {
            world.setBlock(var7, var8, var9, this.plantBlock, this.plantBlockMeta, 3);
         }
      }

      return true;
   }
}
