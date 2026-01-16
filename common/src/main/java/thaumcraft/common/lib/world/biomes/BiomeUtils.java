package thaumcraft.common.lib.world.biomes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import thaumcraft.common.blocks.worldgenerated.taint.AbstractTaintBlock;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.Utils;

import java.util.Random;

import static com.linearity.opentc4.Consts.TAINT_SPREAD_UP_DISTANCE;

public class BiomeUtils {
    public static void taintBiomeSpread(ServerLevel world, int x, int y, int z, RandomSource rand, Block block) {
       taintBiomeSpread(world,new BlockPos(x,y,z),rand,block);
    }

    public static void taintBiomeSpread(ServerLevel world, BlockPos basePos, RandomSource rand, Block block) {
       if (Config.taintSpreadRate > 0) {
          var pos = basePos.offset(rand.nextInt(3) - 1,0,rand.nextInt(3) - 1);
          if (!world.getBiome(pos).is(ThaumcraftBiomeIDs.TAINT_ID)&& rand.nextInt(Config.taintSpreadRate * 5) == 0 && getAdjacentTaint(world, basePos) >= 2) {
             for (int yOffset = 0; yOffset < TAINT_SPREAD_UP_DISTANCE; yOffset++) {
                var afffectPos = pos.above(yOffset);
                var biomeToSet = world.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(
                        ResourceKey.create(Registries.BIOME,ThaumcraftBiomeIDs.TAINT_ID));
                Utils.setBiomeAt(world, pos.above(yOffset), biomeToSet);
                world.blockEvent(afffectPos,world.getBlockState(afffectPos).getBlock(),1,0);
             }
          }
       }
    }

    public static int getAdjacentTaint(BlockGetter world, int x, int y, int z) {
       return getAdjacentTaint(world,new BlockPos(x, y, z));
    }

    public static int getAdjacentTaint(BlockGetter world, BlockPos pos) {
       int count = 0;

       for(int a = 0; a < 6; ++a) {
          Direction d = Direction.values()[a];
          Block b = world.getBlockState(pos.offset(d.getStepX(),d.getStepY(),d.getStepZ())).getBlock();
          if (b instanceof AbstractTaintBlock) {
             ++count;
          }
       }
       return count;
    }
}
