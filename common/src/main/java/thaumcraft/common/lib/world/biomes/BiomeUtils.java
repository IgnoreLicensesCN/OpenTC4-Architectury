package thaumcraft.common.lib.world.biomes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import thaumcraft.common.blocks.worldgenerated.taint.AbstractTaintBlock;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.utils.Utils;

import static com.linearity.opentc4.Consts.TAINT_SPREAD_DOWN_DISTANCE;
import static com.linearity.opentc4.Consts.TAINT_SPREAD_UP_DISTANCE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.TAINTED_MATERIAL_BLOCK;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.FIBROUS_TAINT;

public class BiomeUtils {

    public static void taintBiomeSpread(ServerLevel world, BlockPos basePos, RandomSource rand) {
       if (Config.taintSpreadRate > 0) {
          var pos = basePos.offset(rand.nextInt(3) - 1,0,rand.nextInt(3) - 1);
          if (!world.getBiome(pos).is(ThaumcraftBiomeIDs.TAINT_ID)&& rand.nextInt(Config.taintSpreadRate * 5) == 0 && getTaintedBlocksNear(world, basePos) >= 2) {
              var biomeToSet = ThaumcraftBiomeLookups.biomeHolderForLevel(world,ThaumcraftBiomeIDs.TAINT_KEY);
             for (int yOffset = TAINT_SPREAD_DOWN_DISTANCE; yOffset < TAINT_SPREAD_UP_DISTANCE; yOffset++) {
                var afffectPos = pos.above(yOffset);
                Utils.setBiomeAt(world, pos.above(yOffset), biomeToSet);
                world.blockEvent(afffectPos,world.getBlockState(afffectPos).getBlock(),1,0);
             }
          }
       }
    }

    public static int getTaintedBlocksNear(BlockGetter world, BlockPos pos) {
       int count = 0;

       for (var dir:Direction.values()){
           if (world.getBlockState(pos.relative(dir)).is(TAINTED_MATERIAL_BLOCK)) {
               ++count;
           }
       }
       return count;
    }

    public static void setPosTaint(ServerLevel world, BlockPos pos, Holder<Biome> taintHolder) {
        for (int yOffset = TAINT_SPREAD_DOWN_DISTANCE; yOffset < TAINT_SPREAD_UP_DISTANCE; yOffset++) {
            var afffectPos = pos.above(yOffset);
            Utils.setBiomeAt(world, pos.above(yOffset), taintHolder);
            world.blockEvent(afffectPos,world.getBlockState(afffectPos).getBlock(),1,0);
        }
    }

    public static void setPosTaintAndSetTaintSource(ServerLevel serverLevel, BlockPos pickPos) {
        var holderTaint = ThaumcraftBiomeLookups.biomeHolderForLevel(serverLevel,ThaumcraftBiomeIDs.TAINT_KEY);
        setPosTaint(serverLevel, pickPos,holderTaint);
        var fibreLocation = pickPos.below();
        if (!serverLevel.getBlockState(fibreLocation).isAir()){
            serverLevel.setBlockAndUpdate(fibreLocation,FIBROUS_TAINT().defaultBlockState());
        }
    }

    public static void setPosTaintAndSetTaintSourceIfNotTaint(ServerLevel serverLevel, BlockPos pickPos) {
        var biome = serverLevel.getBiome(pickPos);
        if (!biome.is(ThaumcraftBiomeIDs.TAINT_ID)) {
            setPosTaintAndSetTaintSource(serverLevel, pickPos);
        }
    }
}
