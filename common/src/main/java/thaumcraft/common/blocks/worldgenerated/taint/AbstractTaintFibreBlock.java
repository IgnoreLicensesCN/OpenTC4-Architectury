package thaumcraft.common.blocks.worldgenerated.taint;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityTaintSpore;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.biomes.BiomeUtils;

import java.util.List;
import java.util.Random;

public abstract class AbstractTaintFibreBlock extends AbstractTaintBlock {
    public AbstractTaintFibreBlock(Properties properties) {
        super(properties);
    }


    @Override
    public void randomTick(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            int md = world.getBlockMetadata(x, y, z);
            BiomeUtils.taintBiomeSpread(world, x, y, z, random, this);
            if (md == 0 && isOnlyAdjacentToTaint(world, x, y, z) || world.getBiomeGenForCoords(x, z).biomeID != Config.biomeTaintID) {
                world.setBlock(x, y, z, Blocks.air);
                return;
            }

            int xx = x + random.nextInt(3) - 1;
            int yy = y + random.nextInt(5) - 3;
            int zz = z + random.nextInt(3) - 1;
            if (world.getBiomeGenForCoords(xx, zz).biomeID == Config.biomeTaintID) {
                Block bi = world.getBlock(xx, yy, zz);
                if (!spreadFibres(world, xx, yy, zz)) {
                    int adjacentTaint = BiomeUtils.getAdjacentTaint(world, xx, yy, zz);
                    Material bm = world.getBlock(xx, yy, zz).getMaterial();
                    if (adjacentTaint >= 2 && (Utils.isWoodLog(world, xx, yy, zz) || bm == Material.gourd || bm == Material.cactus)) {
                        world.setBlock(xx, yy, zz, ConfigBlocks.blockTaint, 0, 3);
                        world.addBlockEvent(xx, yy, zz, ConfigBlocks.blockTaint, 1, 0);
                    }

                    if (adjacentTaint >= 3 && bi != Blocks.air && (bm == Material.sand || bm == Material.ground || bm == Material.grass || bm == Material.clay)) {
                        world.setBlock(xx, yy, zz, ConfigBlocks.blockTaint, 1, 3);
                        world.addBlockEvent(xx, yy, zz, ConfigBlocks.blockTaint, 1, 0);
                    }

                    if (md == 3 && Config.spawnTaintSpore && random.nextInt(10) == 0 && world.isAirBlock(x, y + 1, z)) {
                        world.setBlockMetadataWithNotify(x, y, z, 4, 3);
                        EntityTaintSpore spore = new EntityTaintSpore(world);
                        spore.setLocationAndAngles((float)x + 0.5F, y + 1, (float)z + 0.5F, 0.0F, 0.0F);
                        world.spawnEntityInWorld(spore);
                    } else if (md == 4) {
                        List<Entity> targets = world.getEntitiesWithinAABB(EntityTaintSpore.class, AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1));
                        if (targets.isEmpty()) {
                            world.setBlockMetadataWithNotify(x, y, z, 3, 3);
                        }
                    }
                }
            }
        }

    }
}
