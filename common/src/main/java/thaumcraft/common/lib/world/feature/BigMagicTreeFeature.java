package thaumcraft.common.lib.world.feature;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class BigMagicTreeFeature extends Feature<TreeConfiguration> {
    private static final BlockState logFaceY = Blocks.OAK_LOG.defaultBlockState().setValue(
            RotatedPillarBlock.AXIS, Direction.Axis.Y);
    private static final BlockState logFaceX = Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
    private static final BlockState logFaceZ = Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);


    public static class PlaceContext{
        int[] basePos = new int[]{0, 0, 0};
        int heightLimit;
        int height;
        double scaleWidth = 1.0F;
        double leafDensity = 1.0F;
        int trunkSize = 1;
        int heightLimitLimit = 12;
        int leafDistanceLimit = 3;
        int[][] leafNodes;
    }
    public BigMagicTreeFeature() {
        super(TreeConfiguration.CODEC);
    }
    static final byte[] otherCoordPairs = new byte[]{2, 0, 0, 1, 2, 1};
    private static final double heightAttenuation = 0.6618;
//    double branchDensity = 1.0F;
    private static  final double branchSlope = 0.381;

    void generateLeafNodeList(PlaceContext placeContext, WorldGenLevel level, RandomSource random) {
        placeContext.height = (int)((double)placeContext.heightLimit * heightAttenuation);
        if (placeContext.height >= placeContext.heightLimit) {
            placeContext.height = placeContext.heightLimit - 1;
        }

        int i = (int)(1.382 + Math.pow(placeContext.leafDensity * (double)placeContext.heightLimit / (double)13.0F, 2.0F));
        if (i < 1) {
            i = 1;
        }

        int[][] aint = new int[i * placeContext.heightLimit][4];
        int j = placeContext.basePos[1] + placeContext.heightLimit - placeContext.leafDistanceLimit;
        int k = 1;
        int l = placeContext.basePos[1] + placeContext.height;
        int i1 = j - placeContext.basePos[1];
        aint[0][0] = placeContext.basePos[0];
        aint[0][1] = j;
        aint[0][2] = placeContext.basePos[2];
        aint[0][3] = l;
        --j;

        while(i1 >= 0) {
            int j1 = 0;
            float f = this.layerSize(placeContext,i1);
            if (!(f < 0.0F)) {
                for (double d0 = 0.5F; j1 < i; ++j1) {
                    double d1 = placeContext.scaleWidth * (double) f * ((double) random.nextFloat() + 0.328);
                    double d2 = (double) random.nextFloat() * (double) 2.0F * Math.PI;
                    int k1 = MathHelper.floor_double(d1 * Math.sin(d2) + (double) placeContext.basePos[0] + d0);
                    int l1 = MathHelper.floor_double(d1 * Math.cos(d2) + (double) placeContext.basePos[2] + d0);
                    int[] aint1 = new int[]{k1, j, l1};
                    int[] aint2 = new int[]{k1, j + placeContext.leafDistanceLimit, l1};
                    if (this.checkBlockLine(level,aint1, aint2) == -1) {
                        int[] aint3 = new int[]{placeContext.basePos[0], placeContext.basePos[1], placeContext.basePos[2]};
                        double d3 = Math.sqrt(Math.pow(Math.abs(placeContext.basePos[0] - aint1[0]), 2.0F) + Math.pow(Math.abs(placeContext.basePos[2] - aint1[2]), 2.0F));
                        double d4 = d3 * this.branchSlope;
                        if ((double) aint1[1] - d4 > (double) l) {
                            aint3[1] = l;
                        } else {
                            aint3[1] = (int) ((double) aint1[1] - d4);
                        }

                        if (this.checkBlockLine(level,aint3, aint1) == -1) {
                            aint[k][0] = k1;
                            aint[k][1] = j;
                            aint[k][2] = l1;
                            aint[k][3] = aint3[1];
                            ++k;
                        }
                    }
                }

            }
            --j;
            --i1;
        }

        placeContext.leafNodes = new int[k][4];
        System.arraycopy(aint, 0, placeContext.leafNodes, 0, k);
    }

    void genTreeLayer(WorldGenLevel level,int par1, int par2, int par3, float par4, byte par5, Block par6) {
        int i1 = (int)((double)par4 + 0.618);
        byte b1 = otherCoordPairs[par5];
        byte b2 = otherCoordPairs[par5 + 3];
        int[] aint = new int[]{par1, par2, par3};
        int[] aint1 = new int[]{0, 0, 0};
        int j1 = -i1;
        int k1 = -i1;

        for(aint1[par5] = aint[par5]; j1 <= i1; ++j1) {
            aint1[b1] = aint[b1] + j1;
            k1 = -i1;

            while(k1 <= i1) {
                double d0 = Math.pow((double)Math.abs(j1) + (double)0.5F, 2.0F) + Math.pow((double)Math.abs(k1) + (double)0.5F, 2.0F);
                if (d0 > (double)(par4 * par4)) {
                    ++k1;
                } else {
                    aint1[b2] = aint[b2] + k1;

                    try {
                        var pos = new BlockPos(aint1[0], aint1[1], aint1[2]);
                        BlockState l1 = level.getBlockState(pos);
                        if (l1.isAir() || l1.is(BlockTags.LEAVES)) {
                            level.setBlock(pos,par6.defaultBlockState(),3);
                        }
                        ++k1;
                    } catch (Exception ignored) {
                    }
                }
            }
        }

    }

    float layerSize(PlaceContext placeContext,int par1) {
        if ((double)par1 < (double)((float)placeContext.heightLimit) * 0.3) {
            return -1.618F;
        } else {
            float f = (float)placeContext.heightLimit / 2.0F;
            float f1 = (float)placeContext.heightLimit / 2.0F - (float)par1;
            float f2;
            if (f1 == 0.0F) {
                f2 = f;
            } else if (Math.abs(f1) >= f) {
                f2 = 0.0F;
            } else {
                f2 = (float)Math.sqrt(Math.pow(Math.abs(f), 2.0F) - Math.pow(Math.abs(f1), 2.0F));
            }

            f2 *= 0.5F;
            return f2;
        }
    }

    float leafSize(PlaceContext placeContext,int par1) {
        return par1 >= 0 && par1 < placeContext.leafDistanceLimit ? (par1 != 0 && par1 != placeContext.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
    }

    void generateLeafNode(PlaceContext placeContext,WorldGenLevel level,int par1, int par2, int par3) {
        int l = par2;

        for(int i1 = par2 + placeContext.leafDistanceLimit; l < i1; ++l) {
            float f = this.leafSize(placeContext,l - par2);
            this.genTreeLayer(level,par1, l, par3, f, (byte)1, Blocks.OAK_LEAVES);
        }

    }

    void placeBlockLine(WorldGenLevel level,int[] par1ArrayOfInteger, int[] par2ArrayOfInteger, Block par3) {
        int[] aint2 = new int[]{0, 0, 0};
        byte b0 = 0;

        byte b1;
        for(b1 = 0; b0 < 3; ++b0) {
            aint2[b0] = par2ArrayOfInteger[b0] - par1ArrayOfInteger[b0];
            if (Math.abs(aint2[b0]) > Math.abs(aint2[b1])) {
                b1 = b0;
            }
        }

        if (aint2[b1] != 0) {
            byte b2 = otherCoordPairs[b1];
            byte b3 = otherCoordPairs[b1 + 3];
            byte b4;
            if (aint2[b1] > 0) {
                b4 = 1;
            } else {
                b4 = -1;
            }

            double d0 = (double)aint2[b2] / (double)aint2[b1];
            double d1 = (double)aint2[b3] / (double)aint2[b1];
            int[] aint3 = new int[]{0, 0, 0};
            int j = 0;

            for(int k = aint2[b1] + b4; j != k; j += b4) {
                aint3[b1] = MathHelper.floor_double((double)(par1ArrayOfInteger[b1] + j) + (double)0.5F);
                aint3[b2] = MathHelper.floor_double((double)par1ArrayOfInteger[b2] + (double)j * d0 + (double)0.5F);
                aint3[b3] = MathHelper.floor_double((double)par1ArrayOfInteger[b3] + (double)j * d1 + (double)0.5F);
                var b5 = logFaceY;
                int l = Math.abs(aint3[0] - par1ArrayOfInteger[0]);
                int i1 = Math.abs(aint3[2] - par1ArrayOfInteger[2]);
                int j1 = Math.max(l, i1);
                if (j1 > 0) {
                    if (l == j1) {
                        b5 = logFaceX;
                    } else if (i1 == j1) {
                        b5 = logFaceZ;
                    }
                }

                level.setBlock(new BlockPos(aint3[0], aint3[1], aint3[2]), b5, 3);
            }
        }

    }

    void generateLeaves(PlaceContext placeContext,WorldGenLevel level) {
        int i = 0;

        try {
            for(int j = placeContext.leafNodes.length; i < j; ++i) {
                int k = placeContext.leafNodes[i][0];
                int l = placeContext.leafNodes[i][1];
                int i1 = placeContext.leafNodes[i][2];
                this.generateLeafNode(placeContext,level,k, l, i1);
            }
        } catch (Exception ignored) {
        }

    }

    boolean leafNodeNeedsBase(PlaceContext placeContext,int par1) {
        return (double)par1 >= (double)placeContext.heightLimit * 0.2;
    }

    void generateTrunk(PlaceContext placeContext,WorldGenLevel level) {
        int i = placeContext.basePos[0];
        int j = placeContext.basePos[1];
        int k = placeContext.basePos[1] + placeContext.height;
        int l = placeContext.basePos[2];
        int[] aint = new int[]{i, j, l};
        int[] aint1 = new int[]{i, k, l};
        this.placeBlockLine(level,aint, aint1, Blocks.OAK_LOG);
        if (placeContext.trunkSize == 2) {
            int var10002 = aint[0]++;
            var10002 = aint1[0]++;
            this.placeBlockLine(level,aint, aint1, Blocks.OAK_LOG);
            var10002 = aint[2]++;
            var10002 = aint1[2]++;
            this.placeBlockLine(level,aint, aint1, Blocks.OAK_LOG);
            aint[0] -= 1;
            aint1[0] -= 1;
            this.placeBlockLine(level,aint, aint1, Blocks.OAK_LOG);
        }

    }

    void generateLeafNodeBases(PlaceContext placeContext,WorldGenLevel level) {
        int i = 0;
        int j = placeContext.leafNodes.length;

        for(int[] aint = new int[]{placeContext.basePos[0], placeContext.basePos[1], placeContext.basePos[2]}; i < j; ++i) {
            int[] aint1 = placeContext.leafNodes[i];
            int[] aint2 = new int[]{aint1[0], aint1[1], aint1[2]};
            aint[1] = aint1[3];
            int k = aint[1] - placeContext.basePos[1];
            if (this.leafNodeNeedsBase(placeContext,k)) {
                this.placeBlockLine(level,aint, aint2, Blocks.OAK_LOG);
            }
        }

    }

    int checkBlockLine(WorldGenLevel level,int[] par1ArrayOfInteger, int[] par2ArrayOfInteger) {
        int[] aint2 = new int[]{0, 0, 0};
        byte b0 = 0;

        byte b1;
        for(b1 = 0; b0 < 3; ++b0) {
            aint2[b0] = par2ArrayOfInteger[b0] - par1ArrayOfInteger[b0];
            if (Math.abs(aint2[b0]) > Math.abs(aint2[b1])) {
                b1 = b0;
            }
        }

        if (aint2[b1] == 0) {
            return -1;
        } else {
            byte b2 = otherCoordPairs[b1];
            byte b3 = otherCoordPairs[b1 + 3];
            byte b4;
            if (aint2[b1] > 0) {
                b4 = 1;
            } else {
                b4 = -1;
            }

            double d0 = (double)aint2[b2] / (double)aint2[b1];
            double d1 = (double)aint2[b3] / (double)aint2[b1];
            int[] aint3 = new int[]{0, 0, 0};
            int i = 0;
            int j = 0;

            try {
                for(j = aint2[b1] + b4; i != j; i += b4) {
                    aint3[b1] = par1ArrayOfInteger[b1] + i;
                    aint3[b2] = MathHelper.floor_double((double)par1ArrayOfInteger[b2] + (double)i * d0);
                    aint3[b3] = MathHelper.floor_double((double)par1ArrayOfInteger[b3] + (double)i * d1);
                    BlockState k = level.getBlockState(new BlockPos(aint3[0], aint3[1], aint3[2]));
                    if (!k.isAir() && !k.is(BlockTags.LEAVES)) {
                        break;
                    }
                }
            } catch (Exception ignored) {
            }

            return i == j ? -1 : Math.abs(i);
        }
    }

    boolean validTreeLocation(PlaceContext placeContext,WorldGenLevel level) {
        int[] aint = new int[]{placeContext.basePos[0], placeContext.basePos[1], placeContext.basePos[2]};
        int[] aint1 = new int[]{placeContext.basePos[0], placeContext.basePos[1] + placeContext.heightLimit - 1, placeContext.basePos[2]};
//        Block soil = level.getBlock(placeContext.basePos[0], placeContext.basePos[1] - 1, placeContext.basePos[2]);
//        boolean isValidSoil = soil != null && soil.canSustainPlant(level, placeContext.basePos[0], placeContext.basePos[1] - 1, placeContext.basePos[2], Direction.UP, (BlockSapling)Blocks.sapling);
        boolean isValidSoil = Blocks.OAK_SAPLING.canSurvive(Blocks.OAK_SAPLING.defaultBlockState(), level, new BlockPos(placeContext.basePos[0], placeContext.basePos[1], placeContext.basePos[2]));
        if (!isValidSoil) {
            return false;
        } else {
            int j = this.checkBlockLine(level,aint, aint1);
            if (j == -1) {
                return true;
            } else if (j < 6) {
                return false;
            } else {
                placeContext.heightLimit = j;
                return true;
            }
        }
    }

    public void setScale(PlaceContext placeContext,double par1, double par3, double par5) {
        placeContext.heightLimitLimit = (int)(par1 * (double)12.0F);
        if (par1 > (double)0.5F) {
            placeContext.leafDistanceLimit = 3;
        }

        placeContext.scaleWidth = par3;
        placeContext.leafDensity = par5 * 0.8;
    }
    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> featurePlaceContext) {
        var worldGenLevel = featurePlaceContext.level();
        var random = featurePlaceContext.random();
        var basePos = featurePlaceContext.origin();
        var placeContext = new PlaceContext();
        placeContext.basePos[0] = basePos.getX();
        placeContext.basePos[1] = basePos.getY();
        placeContext.basePos[2] = basePos.getZ();
        if (placeContext.heightLimit == 0) {
            placeContext.heightLimit = 11 + random.nextInt(placeContext.heightLimitLimit);
        }

        if (!this.validTreeLocation(placeContext,worldGenLevel)) {
            return false;
        } else {
            this.generateLeafNodeList(placeContext,worldGenLevel,random);
            this.generateLeaves(placeContext,worldGenLevel);
            this.generateTrunk(placeContext,worldGenLevel);
            this.generateLeafNodeBases(placeContext,worldGenLevel);
            return true;
        }
    }
}
