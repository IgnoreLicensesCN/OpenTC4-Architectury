package thaumcraft.api.research.render;

import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;

//sometimes we want research appear in many places like:
//infusion in TC4's own category,and after it unlocked,
//i want it appear in mine,like ThaumicBasics?
//behavior here
public record ShownInfoInResearchCategory(
        ResearchCategoryResourceLocation category,
        int column, int row, ShownIconBackground background, ShownIconForeground foreground
) {

    //
//        /**
//         * This indicates if the research should use a circular icon border. Usually used for "passive" research
//         * that doesn't have recipes and grants passive effects, or that unlock automatically.
//         */
//        private boolean isRound;


//        /**
//         * Special research has a spiky border. Used for important research milestones.
//         */
//        private boolean isSpecial;
}
