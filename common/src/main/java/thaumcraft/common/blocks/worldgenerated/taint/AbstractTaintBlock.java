package thaumcraft.common.blocks.worldgenerated.taint;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thaumcraft.common.blocks.BlockTaintFibres;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.liquid.FluxGooBlock;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityTaintSporeSwarmer;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.world.biomes.BiomeUtils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;

import java.util.List;

public abstract class AbstractTaintBlock extends Block implements ITaintMaterial{
    public AbstractTaintBlock(Properties properties) {
        super(properties.randomTicks());

    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        super.randomTick(blockState, serverLevel, blockPos, random);
        if (Platform.getEnvironment() != Env.CLIENT) {
            BiomeUtils.taintBiomeSpread(serverLevel, blockPos, random, this);
            if (md == 0) {
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

            var spreadFibresPos = blockPos.offset(random.nextInt(3) - 1,random.nextInt(3) - 1,random.nextInt(3) - 1);
            if (serverLevel.getBiome(spreadFibresPos).is(ThaumcraftBiomeIDs.TAINT_ID)) {
                if (BlockTaintFibres.spreadFibres(serverLevel, spreadFibresPos)) {
                }

                if (md == 0) {
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
            } else if (md == 0 && random.nextInt(20) == 0) {
                world.setBlock(x, y, z, ConfigBlocks.blockFluxGoo, ((FluxGooBlock)ConfigBlocks.blockFluxGoo).getQuanta(), 3);
            } else if (md == 1 && random.nextInt(10) == 0) {
                world.setBlock(x, y, z, Blocks.dirt, 0, 3);
            }
        }
    }

    public static boolean spreadFibres(ServerLevel world, BlockPos blockPos) {
        var blockState = world.getBlockState(blockPos);
        var liquidState = world.getFluidState(blockPos);
        Block block = world.getBlockState(blockPos).getBlock();
        if (BlockUtils.isAdjacentToSolidBlock(world, blockPos)
                && !isOnlyAdjacentToTaint(world, blockPos)
                && !liquidState.isEmpty()
                && (blockState.isAir()
                || blockState.canBeReplaced()
                || blockState.is(BlockTags.FLOWERS)
                || blockState.is(BlockTags.LEAVES)
        )
        ) {
            if (world.getRandom().nextInt(10) == 0
                    && world.getBlockState(blockPos.above()).isAir()
                    && world.getBlockState(blockPos.below()).isFaceSturdy(world,blockPos.below(),Direction.UP)
            ) {
                BlockState state = ThaumcraftBlocks.TAINT_FIBRE.defaultBlockState();
                if (world.getRandom().nextInt(10) < 9) {
                    world.setBlock(blockPos, ConfigBlocks.blockTaintFibres, 1, 3);
                } else if (world.getRandom().nextInt(12) < 10) {
                    world.setBlock(blockPos, ConfigBlocks.blockTaintFibres, 2, 3);
                } else {
                    world.setBlock(blockPos, ConfigBlocks.blockTaintFibres, 3, 3);
                }
            } else {
                world.setBlock(blockPos, ConfigBlocks.blockTaintFibres, 0, 3);
            }

            world.blockEvent(blockPos, ConfigBlocks.blockTaintFibres, 1, 0);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isOnlyAdjacentToTaint(Level world, BlockPos pos) {
        for(int a = 0; a < 6; ++a) {
            Direction d = Direction.values()[a];
            var consideringPos = pos.relative(d);
            var bState = world.getBlockState(consideringPos);
            var block = bState.getBlock();
            if (!bState.isAir() && !(block instanceof ITaintMaterial)) {//taintMaterial -> ITaintMaterial
                return false;
            }
        }

        return true;
    }

}
