package thaumcraft.common.lib.world;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.gen.feature.WorldGenerator;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileManaPod;

import java.util.Random;

public class WorldGenManaPods extends WorldGenerator {
   public boolean generate(Level par1World, Random par2Random, int x, int y, int z) {
      int l = x;

      for(int i1 = z; y < Math.min(128, par1World.getHeight(Heightmap.Types.WORLD_SURFACE,x, z)); ++y) {
         if (par1World.isAirBlock(x, y, z) && par1World.isAirBlock(x, y - 1, z)) {
            if (ConfigBlocks.blockManaPod.canPlaceBlockOnSide(par1World, x, y, z, 0)) {
               par1World.setBlock(x, y, z, ConfigBlocks.blockManaPod, 2 + par2Random.nextInt(5), 2);
               BlockEntity tile = par1World.getBlockEntity(x, y, z);
               if (tile instanceof TileManaPod) {
                  ((TileManaPod)tile).checkGrowth();
               }
               break;
            }
         } else {
            x = l + par2Random.nextInt(4) - par2Random.nextInt(4);
            z = i1 + par2Random.nextInt(4) - par2Random.nextInt(4);
         }
      }

      return true;
   }
}
