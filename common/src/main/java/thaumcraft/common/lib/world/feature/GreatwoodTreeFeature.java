package thaumcraft.common.lib.world.feature;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.lib.utils.BlockUtils;

import java.util.Arrays;

//okay i'm lack ofAspectVisList algorithm so WTF is this anazor?--ignoreLicensesCN
public class GreatwoodTreeFeature extends Feature<TreeConfiguration> {
    private static final BlockState logFaceY = ThaumcraftBlocks.GREATWOOD_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
    private static final BlockState logFaceX = ThaumcraftBlocks.GREATWOOD_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
    private static final BlockState logFaceZ = ThaumcraftBlocks.GREATWOOD_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);

    private static final double heightAttenuation = 0.618;
    static final byte[] otherCoordPairs = new byte[]{2, 0, 0, 1, 2, 1};
//    private static final double branchDensity = 1.0F;
    private static final double branchSlope = 0.38;
    private static final double leafDensity = 0.9;
    private static final int trunkSize = 2;
    private static final int heightLimit = 11;
    private static final int leafDistanceLimit = 4;
    private final boolean disableSpiders;
    static class PlaceContext {

        int heightLimit = 0;
        int height;// 实际计算的主干高度
        double scaleWidth = 1.2;// 树冠扩散系数
        int[][] leafNodes;// 每个叶节点[x, y, z, yBase]的坐标
        final int[] basePos = new int[]{0, 0, 0};// 树基座坐标[x, y, z]
    }

    public GreatwoodTreeFeature(boolean disableSpiders) {
        super(TreeConfiguration.CODEC);
        this.disableSpiders = disableSpiders;
    }
    void generateLeafNodeList(PlaceContext placeContext,WorldGenLevel level, RandomSource randomSource) {

        placeContext.height = Math.min(
                (int)(placeContext.height * heightAttenuation),
                placeContext.heightLimit - 1//limit height
        );

        int numberOfLeafNodes  = (int)
                (1.382 + Math.pow(leafDensity * (double)placeContext.heightLimit / (double)13.0F, 2.0F));//oh wtf
        numberOfLeafNodes = Math.max(1, numberOfLeafNodes);


        int[][] tempLeafNodes = new int[numberOfLeafNodes  * placeContext.heightLimit][4];
        int yTop = placeContext.basePos[1] + placeContext.heightLimit - leafDistanceLimit;
        int nodeIndex = 1;
        int remainingHeight = yTop  - placeContext.basePos[1];

        final int var5 = placeContext.basePos[1] + placeContext.height;
        tempLeafNodes[0][0] = placeContext.basePos[0];
        tempLeafNodes[0][1] = yTop;
        tempLeafNodes[0][2] = placeContext.basePos[2];
        tempLeafNodes[0][3] = placeContext.basePos[1] + placeContext.height;
        --yTop ;

        while(remainingHeight >= 0) {

            float layerSize = this.layerSize(placeContext,remainingHeight);
            if (!(layerSize < 0.0F)) {
                for (int i = 0; i < numberOfLeafNodes ; ++i) {
                    // 随机极坐标生成叶节点
                    double radialDistance = placeContext.scaleWidth
                            * (double) layerSize
                            * ((double) randomSource.nextFloat() + 0.328);
                    double angle = (double) randomSource.nextFloat() * (double) 2.0F * Math.PI;

                    int leafX = MathHelper.floor_double(radialDistance * Math.sin(angle)
                            + (double) placeContext.basePos[0]
                            + 0.5F);
                    int leafZ = MathHelper.floor_double(radialDistance * Math.cos(angle)
                            + (double) placeContext.basePos[2]
                            + 0.5F);
                    int[] leafTopPos = new int[]{leafX, yTop , leafZ};
                    int[] leafBottomPos = new int[]{leafX, yTop  + leafDistanceLimit, leafZ};
                    if (this.checkBlockLine(level,leafTopPos, leafBottomPos) == -1) {
                        int[] branchStartPos = new int[]{
                                placeContext.basePos[0],
                                placeContext.basePos[1],
                                placeContext.basePos[2]
                        };
                        double horizontalDistance = Math.sqrt(
                                Math.pow(
                                        Math.abs(
                                                placeContext.basePos[0] - leafTopPos[0]),
                                        2.0F)
                                        + Math.pow(
                                                Math.abs(placeContext.basePos[2] - leafTopPos[2])
                                        , 2.0F));
                        double verticalOffset = horizontalDistance * branchSlope;
                        branchStartPos[1] = (int) Math.min(var5, leafTopPos[1] - verticalOffset);

                        if (this.checkBlockLine(level,branchStartPos, leafTopPos) == -1) {
                            tempLeafNodes[nodeIndex][0] = leafX;
                            tempLeafNodes[nodeIndex][1] = yTop ;
                            tempLeafNodes[nodeIndex][2] = leafZ;
                            tempLeafNodes[nodeIndex][3] = branchStartPos[1];
                            ++nodeIndex;
                        }
                    }
                }

            }
            --yTop ;
            --remainingHeight;
        }

        placeContext.leafNodes = Arrays.copyOf(tempLeafNodes, nodeIndex);
    }

    void genTreeLayer(WorldGenLevel level,int par1, int par2, int par3, float par4, byte par5, Block par6) {
        int var7 = (int)((double)par4 + 0.618);
        byte var8 = otherCoordPairs[par5];
        byte var9 = otherCoordPairs[par5 + 3];
        int[] var10 = new int[]{par1, par2, par3};
        int[] var11 = new int[]{0, 0, 0};
        int var12 = -var7;
        int var13 = -var7;

        for(var11[par5] = var10[par5]; var12 <= var7; ++var12) {
            var11[var8] = var10[var8] + var12;
            var13 = -var7;

            while(var13 <= var7) {
                double var15 = Math.pow((double)Math.abs(var12) + (double)0.5F, 2.0F) + Math.pow((double)Math.abs(var13) + (double)0.5F, 2.0F);
                if (!(var15 > (double) (par4 * par4))) {
                    try {
                        var11[var9] = var10[var9] + var13;
                        var setBlockPos = new BlockPos(var11[0], var11[1], var11[2]);
                        BlockState blockState = level.getBlockState(setBlockPos);
                        boolean canReplace =
                                blockState.isAir()
                                        || blockState.is(BlockTags.LEAVES)
                                        || blockState.is(BlockTags.REPLACEABLE_BY_TREES);
                        if ((blockState.isAir() || blockState.is(ThaumcraftBlocks.GREATWOOD_LEAVES)) 
                                && canReplace) {
                            level.setBlock(setBlockPos, par6.defaultBlockState(), 3);
                        }
                    } catch (Exception ignored) {
                    }

                }
                ++var13;
            }
        }

    }

    float layerSize(PlaceContext placeContext,int par1) {
        if ((double)par1 < (double)((float)placeContext.heightLimit) * 0.3) {
            return -1.618F;
        } else {
            float var2 = (float)placeContext.heightLimit / 2.0F;
            float var3 = (float)placeContext.heightLimit / 2.0F - (float)par1;
            float var4;
            if (var3 == 0.0F) {
                var4 = var2;
            } else if (Math.abs(var3) >= var2) {
                var4 = 0.0F;
            } else {
                var4 = (float)Math.sqrt(Math.pow(Math.abs(var2), 2.0F) - Math.pow(Math.abs(var3), 2.0F));
            }

            var4 *= 0.5F;
            return var4;
        }
    }

    float leafSize(int par1) {
        return par1 >= 0 && par1 < leafDistanceLimit ? (par1 != 0 && par1 != leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
    }

    void generateLeafNode(WorldGenLevel level,int par1, int par2, int par3) {
        int var4 = par2;

        for(int var5 = par2 + leafDistanceLimit; var4 < var5; ++var4) {
            float var6 = this.leafSize(var4 - par2);
            this.genTreeLayer(level,par1, var4, par3, var6, (byte)1, ThaumcraftBlocks.GREATWOOD_LEAVES);
        }

    }

    void placeBlockLine(WorldGenLevel level, int[] par1ArrayOfInteger, int[] par2ArrayOfInteger, Block par3) {
        int[] var4 = new int[]{0, 0, 0};
        byte var5 = 0;

        byte var6;
        for(var6 = 0; var5 < 3; ++var5) {
            var4[var5] = par2ArrayOfInteger[var5] - par1ArrayOfInteger[var5];
            if (Math.abs(var4[var5]) > Math.abs(var4[var6])) {
                var6 = var5;
            }
        }

        if (var4[var6] != 0) {
            byte var7 = otherCoordPairs[var6];
            byte var8 = otherCoordPairs[var6 + 3];
            byte var9;
            if (var4[var6] > 0) {
                var9 = 1;
            } else {
                var9 = -1;
            }

            double var10 = (double)var4[var7] / (double)var4[var6];
            double var12 = (double)var4[var8] / (double)var4[var6];
            int[] var14 = new int[]{0, 0, 0};
            int var15 = 0;

            for(int var16 = var4[var6] + var9; var15 != var16; var15 += var9) {
                var14[var6] = MathHelper.floor_double((double)(par1ArrayOfInteger[var6] + var15) + (double)0.5F);
                var14[var7] = MathHelper.floor_double((double)par1ArrayOfInteger[var7] + (double)var15 * var10 + (double)0.5F);
                var14[var8] = MathHelper.floor_double((double)par1ArrayOfInteger[var8] + (double)var15 * var12 + (double)0.5F);
                BlockState pickLogState = logFaceY;
                int var18 = Math.abs(var14[0] - par1ArrayOfInteger[0]);
                int var19 = Math.abs(var14[2] - par1ArrayOfInteger[2]);
                int var20 = Math.max(var18, var19);
                if (var20 > 0) {
                    if (var18 == var20) {
                        pickLogState = logFaceX;
                    } else if (var19 == var20) {
                        pickLogState = logFaceZ;
                    }
                }

                level.setBlock(new BlockPos(var14[0], var14[1], var14[2]), pickLogState, 3);
            }
        }

    }

    void generateLeaves(PlaceContext placeContext,WorldGenLevel level) {
        int var1 = 0;

        for(int var2 = placeContext.leafNodes.length; var1 < var2; ++var1) {
            int var3 = placeContext.leafNodes[var1][0];
            int var4 = placeContext.leafNodes[var1][1];
            int var5 = placeContext.leafNodes[var1][2];
            this.generateLeafNode(level,var3, var4, var5);
        }

    }

    boolean leafNodeNeedsBase(PlaceContext placeContext,int par1) {
        return (double)par1 >= (double)placeContext.heightLimit * 0.2;
    }

    void generateTrunk(PlaceContext placeContext,WorldGenLevel level) {
        int var1 = placeContext.basePos[0];
        int var2 = placeContext.basePos[1];
        int var3 = placeContext.basePos[1] + placeContext.height;
        int var4 = placeContext.basePos[2];
        int[] var5 = new int[]{var1, var2, var4};
        int[] var6 = new int[]{var1, var3, var4};
        this.placeBlockLine(level,var5, var6, ThaumcraftBlocks.GREATWOOD_LOG);
        if (trunkSize == 2) {
            var5[0]++;
            var6[0]++;
            this.placeBlockLine(level,var5, var6, ThaumcraftBlocks.GREATWOOD_LOG);
            var5[2]++;
            var6[2]++;
            this.placeBlockLine(level,var5, var6, ThaumcraftBlocks.GREATWOOD_LOG);
            var5[0] -= 1;
            var6[0] -= 1;
            this.placeBlockLine(level,var5, var6, ThaumcraftBlocks.GREATWOOD_LOG);
        }

    }

    void generateLeafNodeBases(PlaceContext placeContext,WorldGenLevel level) {
        int var1 = 0;
        int var2 = placeContext.leafNodes.length;

        for(int[] var3 = new int[]{placeContext.basePos[0], placeContext.basePos[1], placeContext.basePos[2]}; var1 < var2; ++var1) {
            int[] var4 = placeContext.leafNodes[var1];
            int[] var5 = new int[]{var4[0], var4[1], var4[2]};
            var3[1] = var4[3];
            int var6 = var3[1] - placeContext.basePos[1];
            if (this.leafNodeNeedsBase(placeContext,var6)) {
                this.placeBlockLine(level,var3, var5, ThaumcraftBlocks.GREATWOOD_LOG);
            }
        }

    }

    int checkBlockLine(BlockGetter level,int[] par1ArrayOfInteger, int[] par2ArrayOfInteger) {
        int[] var3 = new int[]{0, 0, 0};
        byte var4 = 0;

        byte var5;
        for(var5 = 0; var4 < 3; ++var4) {
            var3[var4] = par2ArrayOfInteger[var4] - par1ArrayOfInteger[var4];
            if (Math.abs(var3[var4]) > Math.abs(var3[var5])) {
                var5 = var4;
            }
        }

        if (var3[var5] == 0) {
            return -1;
        } else {
            byte var6 = otherCoordPairs[var5];
            byte var7 = otherCoordPairs[var5 + 3];
            byte var8;
            if (var3[var5] > 0) {
                var8 = 1;
            } else {
                var8 = -1;
            }

            double var9 = (double)var3[var6] / (double)var3[var5];
            double var11 = (double)var3[var7] / (double)var3[var5];
            int[] var13 = new int[]{0, 0, 0};
            int var14 = 0;

            int var15;
            for(var15 = var3[var5] + var8; var14 != var15; var14 += var8) {
                var13[var5] = par1ArrayOfInteger[var5] + var14;
                var13[var6] = MathHelper.floor_double((double)par1ArrayOfInteger[var6] + (double)var14 * var9);
                var13[var7] = MathHelper.floor_double((double)par1ArrayOfInteger[var7] + (double)var14 * var11);

                try {
                    BlockState var16 = level.getBlockState(new BlockPos(var13[0], var13[1], var13[2]));
                    if (!var16.isAir() && !var16.is(ThaumcraftBlocks.GREATWOOD_LEAVES)) {
                        break;
                    }
                } catch (Exception ignored) {
                }
            }

            return var14 == var15 ? -1 : Math.abs(var14);
        }
    }

    boolean validTreeLocation(PlaceContext placeContext,LevelReader level, int x, int z) {
        int[] var1 = new int[]{placeContext.basePos[0] + x, placeContext.basePos[1], placeContext.basePos[2] + z};
        int[] var2 = new int[]{placeContext.basePos[0] + x, placeContext.basePos[1] + placeContext.heightLimit - 1, placeContext.basePos[2] + z};

        try {
            var pos = new BlockPos(placeContext.basePos[0] + x, placeContext.basePos[1], placeContext.basePos[2] + z);
            boolean isSoil = Blocks.OAK_SAPLING.canSurvive(Blocks.OAK_SAPLING.defaultBlockState(), level, pos);
            if (!isSoil) {
                return false;
            } else {
                int var4 = this.checkBlockLine(level,var1, var2);
                if (var4 == -1) {
                    return true;
                } else if (var4 < 6) {
                    return false;
                } else {
                    placeContext.heightLimit = var4;
                    return true;
                }
            }
        } catch (Exception var8) {
            return false;
        }
    }
    
    //(Level par1World, Random par2Random, int par3, int par4, int par5, boolean spiders)
    //1. 找一个合法的 2x2 树根位置
    //2. 计算叶节点（leafNodes）
    //3. 生成叶子
    //4. 生成叶节点到主干的“枝条”
    //5. 生成主干（trunkSize=2）
    //6. 再次生成叶节点（大树冠）
    //7. 如果触发 spiders：
    //   - 放刷怪笼
    //   - 撒蜘蛛网
    //   - 放战利品箱子
    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> featurePlaceContext) {
        var worldGenLevel = featurePlaceContext.level();
        var trueLevel = worldGenLevel.getLevel();
        var random = featurePlaceContext.random();
        var pos = featurePlaceContext.origin();
        var originX = pos.getX();
        var originY = pos.getY();
        var originZ = pos.getZ();
        var placeContext = new PlaceContext();
        placeContext.basePos[0] = pos.getX();
        placeContext.basePos[1] = pos.getY();
        placeContext.basePos[2] = pos.getZ();

        boolean spiders = !disableSpiders && (random.nextInt(8) == 0);

        if (placeContext.heightLimit == 0) {
            placeContext.heightLimit = placeContext.heightLimit + random.nextInt(placeContext.heightLimit);
        }

        int x = 0;
        int z = 0;
        boolean valid = false;

        label77:
        for(int a = -1; a < 2; ++a) {
            label75:
            for(int b = -1; b < 2; ++b) {
                for(int var17 = 0; var17 < trunkSize; ++var17) {
                    for(int var18 = 0; var18 < trunkSize; ++var18) {
                        if (!this.validTreeLocation(placeContext,worldGenLevel,var17 + a, var18 + b)) {
                            continue label75;
                        }
                    }
                }

                valid = true;
                placeContext.basePos[0] += a;
                placeContext.basePos[2] += b;
                break label77;
            }
        }

        if (!valid) {
            return false;
        }
        else {
            this.generateLeafNodeList(placeContext,worldGenLevel,random);
            this.generateLeaves(placeContext,worldGenLevel);
            this.generateLeafNodeBases(placeContext,worldGenLevel);
            this.generateTrunk(placeContext,worldGenLevel);
            placeContext.scaleWidth = 1.66;
            placeContext.basePos[0] = originX;
            placeContext.basePos[1] = originY + placeContext.height;
            placeContext.basePos[2] = originZ;
            this.generateLeafNodeList(placeContext,worldGenLevel,random);
            this.generateLeaves(placeContext,worldGenLevel);
            this.generateLeafNodeBases(placeContext,worldGenLevel);
            this.generateTrunk(placeContext,worldGenLevel);
            if (spiders) {
                var spawnerBlockPos = new BlockPos(originX, originY - 1, originZ);
                var lootChestBlockPos = new BlockPos(originX, originY - 2, originZ);

                //idk what does 0 and 3 use
                worldGenLevel.setBlock(spawnerBlockPos, Blocks.SPAWNER.defaultBlockState(), 3);
                BlockEntity var14 = worldGenLevel.getBlockEntity(spawnerBlockPos);
                if (var14 instanceof SpawnerBlockEntity spawner) {
                    spawner.setEntityId(EntityType.CAVE_SPIDER,trueLevel.random);
//                    var14.func_145881_a().setEntityName("CaveSpider");

                    for(int a = 0; a < 50; ++a) {
                        int xx = originX - 7 + random.nextInt(14);
                        int yy = originY + random.nextInt(10);
                        int zz = originZ - 7 + random.nextInt(14);
                        var randomizedBlockPos = new BlockPos(xx, yy, zz);
                        if (worldGenLevel.getBlockState(randomizedBlockPos).isAir()
                                && (BlockUtils.isBlockTouching(worldGenLevel,
                                randomizedBlockPos,
                                ThaumcraftBlocks.GREATWOOD_LEAVES) 
                                || BlockUtils.isBlockTouching(
                                        worldGenLevel, randomizedBlockPos, ThaumcraftBlocks.GREATWOOD_LOG))) {
                            worldGenLevel.setBlock(randomizedBlockPos,Blocks.COBWEB.defaultBlockState(),3);
                        }
                    }

                    worldGenLevel.setBlock(lootChestBlockPos, Blocks.CHEST.defaultBlockState(), 3);
                    BlockEntity var16 = worldGenLevel.getBlockEntity(lootChestBlockPos);
                    if (var16 instanceof ChestBlockEntity chest) {
                        ResourceLocation lootTable = BuiltInLootTables.SIMPLE_DUNGEON; // 或你自己的
                        chest.setLootTable(lootTable, random.nextLong());
                    }
                }
            }

            return true;
        }
//        return this.generateInternal(var1, var2, var3, var4, var5, var2.nextInt(8) == 0);
    }
}
