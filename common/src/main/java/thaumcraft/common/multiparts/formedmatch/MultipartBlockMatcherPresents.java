package thaumcraft.common.multiparts.formedmatch;

import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.IMultipartComponentBlock;

import java.util.Objects;

public class MultipartBlockMatcherPresents {
    public static class InfernalFurnaceMatcherImpls {
        public static final IFormedBlockMatcher CORNER_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_CORNER);
        public static final IFormedBlockMatcher SIDE_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_SIDE);
        public static final IFormedBlockMatcher BAR_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_BAR);
        public static final IFormedBlockMatcher LAVA_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_LAVA);
        public static final IFormedBlockMatcher BOTTOM_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_BOTTOM);
        public static final IFormedBlockMatcher X_AXIS_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_X_AXIS);
        public static final IFormedBlockMatcher Y_AXIS_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_Y_AXIS);
        public static final IFormedBlockMatcher Z_AXIS_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_Z_AXIS);
    }

    public static final IFormedBlockMatcher[][][] INFERNAL_FURNACE_FORMED_MATCHER = {
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
    public static class AdvancedAlchemicalFurnaceMatcherImpls {
        public static final IFormedBlockMatcher BOTTOM_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.INFERNAL_FURNACE_BOTTOM);
        public static final IFormedBlockMatcher CORNER_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_BASE_CORNER);
        public static final IFormedBlockMatcher ALEMBIC_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_ALEMBIC);
        public static final IFormedBlockMatcher NOZZLE_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_NOZZLE);
        public static final IFormedBlockMatcher FENCE_MATCHER = generateSimpleMatcher(ThaumcraftBlocks.ADVANCED_ALCHEMICAL_FURNACE_UPPER_FENCE);
    }
    public static final IFormedBlockMatcher[][][] ADVANCED_ALCHEMICAL_FURNACE_FORMED_MATCHER = {
            {
                    {AdvancedAlchemicalFurnaceMatcherImpls.CORNER_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.NOZZLE_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.CORNER_MATCHER},
                    {AdvancedAlchemicalFurnaceMatcherImpls.NOZZLE_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.BOTTOM_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.NOZZLE_MATCHER},
                    {AdvancedAlchemicalFurnaceMatcherImpls.CORNER_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.NOZZLE_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.CORNER_MATCHER},
            },
            {
                    {AdvancedAlchemicalFurnaceMatcherImpls.ALEMBIC_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.FENCE_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.ALEMBIC_MATCHER},
                    {AdvancedAlchemicalFurnaceMatcherImpls.FENCE_MATCHER,null,AdvancedAlchemicalFurnaceMatcherImpls.FENCE_MATCHER},
                    {AdvancedAlchemicalFurnaceMatcherImpls.ALEMBIC_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.FENCE_MATCHER,AdvancedAlchemicalFurnaceMatcherImpls.ALEMBIC_MATCHER},
            },
    };


    public static IFormedBlockMatcher generateSimpleMatcher(IMultipartComponentBlock componentBlock) {
        return (  atLevel,
                  state,
                  posInWorld,
                  transformBasePosRelatedInMultipart,
                  selfRelatedPosInMultipart,
                  multipartMatchInfo) -> {
            var block = state.getBlock();
            if (block != componentBlock){
                return false;
            }
            if (!Objects.equals(selfRelatedPosInMultipart,componentBlock.findSelfPosRelatedInMultipart(atLevel,state,posInWorld))){
                return false;
            }
            if (!Objects.equals(multipartMatchInfo,componentBlock.getMatchInfo(atLevel,state,posInWorld))){
                return false;
            }
            return true;
        };
    }
}
