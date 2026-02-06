package thaumcraft.common.researches;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.expands.UnmodifiableAspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.ThaumcraftShownResearchItem;
import thaumcraft.api.research.implexample.AutoUnlockedResearchItem;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.api.research.render.impls.ShownIconsForeground;
import thaumcraft.api.research.render.impls.ShownIconsBackground;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.impl.Research;
import thaumcraft.common.researches.impl.ResearchExpertise;


public class ThaumcraftResearches {
    public static final ResearchItem RESEARCH = new Research();
    public static final ResearchItem RESEARCH_EXPERTISE = new ResearchExpertise();

    public static final ResearchItem RESEARCH_MASTERY = (new ThaumcraftShownResearchItem(
            new ResearchItemResourceLocation(Thaumcraft.MOD_ID,"researcher2"),
            ThaumcraftResearchCategories.BASICS.categoryKey,
            new AspectList<>().addAll(Aspects.MIND, 6)
            .addAll(Aspects.ORDER, 3)
            .addAll(Aspects.SENSES, 3)
            .addAll(Aspects.MAGIC, 3), 2
    ))
            .addShownInfo(new ShownInfoInResearchCategory(
                    ThaumcraftResearchCategories.BASICS.categoryKey,
                     3, 3,
                    ShownIconsBackground.ROUND_AND_SPECIAL,
                    ShownIconsForeground.RESEARCH_MASTERY_FOREGROUND_ICON

            ))
            .setPages(new ResearchPage("tc.research_page.RESEARCHER2.1"))
            .setParents(RESEARCH_EXPERTISE.key)
            .setWarp(1);
}
