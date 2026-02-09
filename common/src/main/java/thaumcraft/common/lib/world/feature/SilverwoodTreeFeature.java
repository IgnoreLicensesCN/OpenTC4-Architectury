package thaumcraft.common.lib.world.feature;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.world.WorldGenCustomFlowers;

import static thaumcraft.api.listeners.worldgen.node.NodeGenerationManager.createRandomNodeAt;

//note that TreeConfiguration isnt used.
public class SilverwoodTreeFeature extends Feature<TreeConfiguration> {

    private final int minHeight;
    private final int randomHeight;
    private final boolean worldGen;

    public SilverwoodTreeFeature(boolean doBlockNotify,int minHeight, int randomHeight) {
        super(TreeConfiguration.CODEC);
        this.worldGen = !doBlockNotify;
        this.minHeight = minHeight;
        this.randomHeight = randomHeight;
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> context) {
        var worldGenLevel = context.level();
        var pos = context.origin();
        var posBelow = pos.below();
        var knotState = ThaumcraftBlocks.SILVERWOOD_KNOT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        var leaveState = ThaumcraftBlocks.SILVERWOOD_LEAVES.defaultBlockState();
        var logFaceY = ThaumcraftBlocks.SILVERWOOD_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        var logFaceX = ThaumcraftBlocks.SILVERWOOD_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
        var logFaceZ = ThaumcraftBlocks.SILVERWOOD_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);


        var x = pos.getX();
        var y = pos.getY();
        var z = pos.getZ();
        var random = worldGenLevel.getRandom();
        int height = random.nextInt(this.randomHeight) + this.minHeight;
        boolean flag = true;
        if (y >= 1 && y + height + 1 <= 256) {
            for(int i1 = y; i1 <= y + 1 + height; ++i1) {
                byte spread = 1;
                if (i1 == y) {
                    spread = 0;
                }

                if (i1 >= y + 1 + height - 2) {
                    spread = 3;
                }

                for(int j1 = x - spread; j1 <= x + spread && flag; ++j1) {
                    for(int k1 = z - spread; k1 <= z + spread && flag; ++k1) {
                        if (i1 >= 0 && i1 < 256) {
                            var blockState = worldGenLevel.getBlockState(new BlockPos(j1, i1, k1));
                            var isLeaves = blockState.is(BlockTags.LEAVES);
                            if (!blockState.isAir() && !isLeaves && !blockState.canBeReplaced() && i1 > y) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {

                BlockState soilState = worldGenLevel.getBlockState(posBelow);

                boolean isSoil = Blocks.OAK_SAPLING.canSurvive(Blocks.OAK_SAPLING.defaultBlockState(), worldGenLevel, pos);

                if (isSoil && y < 256 - height - 1) {
//                    soilState.neighborChanged(worldGenLevel, posBelow, Blocks.OAK_SAPLING, pos, false);
                    int start = y + height - 5;
                    int end = y + height + 3 + random.nextInt(3);

                    for (int k2 = start; k2 <= end; ++k2) {
                        int cty = MathHelper.clamp_int(k2, y + height - 3, y + height);

                        for (int xx = x - 5; xx <= x + 5; ++xx) {
                            for (int zz = z - 5; zz <= z + 5; ++zz) {
                                double d3 = xx - x;
                                double d4 = k2 - cty;
                                double d5 = zz - z;
                                double dist = d3 * d3 + d4 * d4 + d5 * d5;

                                boolean canBeReplacedByLeaves = worldGenLevel.getBlockState(new BlockPos(xx, k2, zz))
                                        .is(BlockTags.REPLACEABLE);

                                if (dist < (double) (10 + random.nextInt(8))
                                        && canBeReplacedByLeaves
                                ) {
                                    worldGenLevel.setBlock(pos, leaveState, 3);
                                }
                            }
                        }
                    }

                    int chance = (int) ((double) height * (double) 1.5F);
                    boolean lastblock = false;

                    int heightOffset;
                    for (heightOffset = 0; heightOffset < height; ++heightOffset) {
                        var canBeLogPos = new BlockPos(x, y + heightOffset, z);
                        var stateBeforeLog = worldGenLevel.getBlockState(canBeLogPos);
                        if (stateBeforeLog.isAir()
                                || stateBeforeLog.is(BlockTags.LEAVES)
                                || stateBeforeLog.canBeReplaced()
                        ) {
                            if (heightOffset > 0 && !lastblock && random.nextInt(chance) == 0) {
                                worldGenLevel.setBlock(canBeLogPos, knotState, 3);
                                createRandomNodeAt(
                                        (WorldGenLevel) worldGenLevel,
                                        canBeLogPos,
                                        random,
                                        true, false, false);
                                chance += height;
                                lastblock = true;
                            } else {
                                worldGenLevel.setBlock(canBeLogPos, logFaceY, 3);
                                lastblock = false;
                            }

                            worldGenLevel.setBlock(canBeLogPos.east(), logFaceY, 3);
                            worldGenLevel.setBlock(canBeLogPos.west(), logFaceY, 3);
                            worldGenLevel.setBlock(canBeLogPos.north(), logFaceY, 3);
                            worldGenLevel.setBlock(canBeLogPos.south(), logFaceY, 3);
                        }
                    }


                    worldGenLevel.setBlock(new BlockPos(x, y + heightOffset, z), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x - 1, y, z - 1), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x - 1, y, z + 1), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x + 1, y, z - 1), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x + 1, y, z + 1), logFaceY, 3);
                    if (random.nextInt(3) != 0) {
                        worldGenLevel.setBlock(new BlockPos(x - 1, y + 1, z - 1), logFaceY, 3);
                    }

                    if (random.nextInt(3) != 0) {
                        worldGenLevel.setBlock(new BlockPos(x + 1, y + 1, z + 1), logFaceY, 3);
                    }

                    if (random.nextInt(3) != 0) {
                        worldGenLevel.setBlock(new BlockPos(x - 1, y + 1, z + 1), logFaceY, 3);
                    }

                    if (random.nextInt(3) != 0) {
                        worldGenLevel.setBlock(new BlockPos(x + 1, y + 1, z - 1), logFaceY, 3);
                    }

                    worldGenLevel.setBlock(new BlockPos(x - 2, y, z), logFaceX, 3);
                    worldGenLevel.setBlock(new BlockPos(x + 2, y, z), logFaceX, 3);
                    worldGenLevel.setBlock(new BlockPos(x, y, z - 2), logFaceZ, 3);
                    worldGenLevel.setBlock(new BlockPos(x, y, z + 2), logFaceZ, 3);
                    worldGenLevel.setBlock(new BlockPos(x - 2, y - 1, z), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x + 2, y - 1, z), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x, y - 1, z - 2), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x, y - 1, z + 2), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x - 1, y + (height - 4), z - 1), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x + 1, y + (height - 4), z + 1), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x - 1, y + (height - 4), z + 1), logFaceY, 3);
                    worldGenLevel.setBlock(new BlockPos(x + 1, y + (height - 4), z - 1), logFaceY, 3);
                    if (random.nextInt(3) == 0) {
                        worldGenLevel.setBlock(new BlockPos(x - 1, y + (height - 5), z - 1), logFaceY, 3);
                    }

                    if (random.nextInt(3) == 0) {
                        worldGenLevel.setBlock(new BlockPos(x + 1, y + (height - 5), z + 1), logFaceY, 3);
                    }

                    if (random.nextInt(3) == 0) {
                        worldGenLevel.setBlock(new BlockPos(x - 1, y + (height - 5), z + 1), logFaceY, 3);
                    }

                    if (random.nextInt(3) == 0) {
                        worldGenLevel.setBlock(new BlockPos(x + 1, y + (height - 5), z - 1), logFaceY, 3);
                    }

                    worldGenLevel.setBlock(new BlockPos(x - 2, y + (height - 4), z), logFaceX, 3);
                    worldGenLevel.setBlock(new BlockPos(x + 2, y + (height - 4), z), logFaceX, 3);
                    worldGenLevel.setBlock(new BlockPos(x, y + (height - 4), z - 2), logFaceZ, 3);
                    worldGenLevel.setBlock(new BlockPos(x, y + (height - 4), z + 2), logFaceZ, 3);
                    if (this.worldGen) {
                        WorldGenerator flowers = new WorldGenCustomFlowers(ConfigBlocks.blockCustomPlant, 2);
                        flowers.generate(worldGenLevel, random, x, y, z);
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
