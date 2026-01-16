package thaumcraft.common.blocks.worldgenerated.taint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityTaintSporeSwarmer;

import java.util.List;

public class CrustedTaintBlock extends AbstractTaintBlock {
    public CrustedTaintBlock(Properties properties) {
        super(properties);
    }
    public CrustedTaintBlock() {
        super();
    }

    @Override
    public void onBlockOutOfTaintBiome(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (random.nextInt(20) == 0) {
            world.setBlock(blockPos, ThaumcraftBlocks.FLUX_GOO.defaultBlockState(), 3);
        }
    }

    @Override
    public void beforeSpreading(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (this.tryToFall(world, x, y, z, x, y, z)) {
            return;
        }

        if (world.isAirBlock(x, y + 1, z)) {
            boolean doIt = true;
            Direction dir = Direction.getOrientation(2 + random.nextInt(4));

            for(int a = 0; a < 4; ++a) {
                if (!world.isAirBlock(x + dir.offsetX, y - a, z + dir.offsetZ)) {
                    doIt = false;
                    break;
                }

                if (world.getBlock(x, y - a, z) != this) {
                    doIt = false;
                    break;
                }
            }

            if (doIt && this.tryToFall(world, x, y, z, x + dir.offsetX, y, z + dir.offsetZ)) {
                return;
            }
        }
    }

    @Override
    public void afterSpread(BlockState blockState, ServerLevel world, BlockPos blockPos, RandomSource random) {
        if (Config.spawnTaintSpore && world.isAirBlock(x, y + 1, z) && random.nextInt(200) == 0) {
            List<Entity> targets = world.getEntitiesWithinAABB(
                    EntityTaintSporeSwarmer.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(16.0F, 16.0F, 16.0F));
            if (targets.isEmpty()) {
                world.setBlockToAir(x, y, z);
                EntityTaintSporeSwarmer spore = new EntityTaintSporeSwarmer(world);
                spore.setLocationAndAngles((float)x + 0.5F, y, (float)z + 0.5F, 0.0F, 0.0F);
                world.spawnEntityInWorld(spore);
                world.playSoundAtEntity(spore, "thaumcraft:roots", 0.1F, 0.9F + world.getRandom().nextFloat() * 0.2F);
            }
        } else {
            boolean doIt = world.getBlock(x, y + 1, z) == this;
            if (doIt) {
                for(int a = 2; a < 6; ++a) {
                    Direction dir = Direction.getOrientation(a);
                    if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != this) {
                        doIt = false;
                        break;
                    }
                }
            }

            if (doIt) {
                world.setBlock(x, y, z, ConfigBlocks.blockFluxGoo, ((FluxGooBlock)ConfigBlocks.blockFluxGoo).getQuanta(), 3);
            }
        }
    }
}
