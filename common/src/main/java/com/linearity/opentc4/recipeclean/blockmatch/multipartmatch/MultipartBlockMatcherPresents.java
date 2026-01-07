package com.linearity.opentc4.recipeclean.blockmatch.multipartmatch;

import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.IMultipartComponentBlock;

import java.util.Objects;

public class MultipartBlockMatcherPresents {
    public static class InfernalFurnaceMatcherImpls {
        public static final IMultipartFormedBlockMatcher CORNER_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_CORNER);
        public static final IMultipartFormedBlockMatcher SIDE_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_SIDE);
        public static final IMultipartFormedBlockMatcher BAR_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_BAR);
        public static final IMultipartFormedBlockMatcher LAVA_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_LAVA);
        public static final IMultipartFormedBlockMatcher BOTTOM_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_BOTTOM);
        public static final IMultipartFormedBlockMatcher X_AXIS_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_X_AXIS);
        public static final IMultipartFormedBlockMatcher Y_AXIS_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_Y_AXIS);
        public static final IMultipartFormedBlockMatcher Z_AXIS_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_Z_AXIS);

    }
    public static final IMultipartFormedBlockMatcher[][][] INFERNAL_FURNACE_MATCHERS_FORMED = {
            //yxz
            {
                    {InfernalFurnaceMatcherImpls.CORNER_MATCHER,InfernalFurnaceMatcherImpls.Z_AXIS_MATCHER,InfernalFurnaceMatcherImpls.CORNER_MATCHER},
                    {InfernalFurnaceMatcherImpls.X_AXIS_MATCHER,InfernalFurnaceMatcherImpls.BOTTOM_MATCHER,InfernalFurnaceMatcherImpls.X_AXIS_MATCHER},
                    {InfernalFurnaceMatcherImpls.CORNER_MATCHER,InfernalFurnaceMatcherImpls.Z_AXIS_MATCHER,InfernalFurnaceMatcherImpls.CORNER_MATCHER},
            },
            {
                    {InfernalFurnaceMatcherImpls.Y_AXIS_MATCHER,InfernalFurnaceMatcherImpls.SIDE_MATCHER,InfernalFurnaceMatcherImpls.Y_AXIS_MATCHER},
                    {InfernalFurnaceMatcherImpls.SIDE_MATCHER,InfernalFurnaceMatcherImpls.LAVA_MATCHER,InfernalFurnaceMatcherImpls.SIDE_MATCHER},
                    {InfernalFurnaceMatcherImpls.Y_AXIS_MATCHER,InfernalFurnaceMatcherImpls.BAR_MATCHER,InfernalFurnaceMatcherImpls.Y_AXIS_MATCHER},//x=2,y=1,z=1
            },
            {

                    {InfernalFurnaceMatcherImpls.CORNER_MATCHER,InfernalFurnaceMatcherImpls.Z_AXIS_MATCHER,InfernalFurnaceMatcherImpls.CORNER_MATCHER},//y=2,x=0
                    {InfernalFurnaceMatcherImpls.X_AXIS_MATCHER,null,InfernalFurnaceMatcherImpls.X_AXIS_MATCHER},
                    {InfernalFurnaceMatcherImpls.CORNER_MATCHER,InfernalFurnaceMatcherImpls.Z_AXIS_MATCHER,InfernalFurnaceMatcherImpls.CORNER_MATCHER},
            },
    };


    public static IMultipartFormedBlockMatcher generateSimpleMatcher(IMultipartComponentBlock componentBlock) {
        return (atLevel, state, pos, relatedPos, matchInfo) ->
        {
            var block = state.getBlock();
            if (block != componentBlock){
                return false;
            }
            if (!Objects.equals(relatedPos,componentBlock.findSelfPosRelatedInMultipart(atLevel,state,pos))){
                return false;
            }
            if (!Objects.equals(matchInfo,componentBlock.getMatchInfo(atLevel,state,pos))){
                return false;
            }
            return true;
        };

    }
}
