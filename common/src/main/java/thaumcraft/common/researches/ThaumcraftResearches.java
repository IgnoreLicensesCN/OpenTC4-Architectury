package thaumcraft.common.researches;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.expands.UnmodifiableAspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.ThaumcraftShownResearchItem;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.api.research.render.impls.ShownIconsForeground;
import thaumcraft.api.research.render.impls.ShownIconsBackground;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;


public class ThaumcraftResearches {
    public static final ResearchItem RESEARCH = new ThaumcraftShownResearchItem(
            new ResearchItemResourceLocation(Thaumcraft.MOD_ID, "research"),
            ThaumcraftResearchCategories.BASICS.categoryKey,
            new AspectList<>(), 0
    )
            .addShownInfo(
                    new ShownInfoInResearchCategory(
                            ThaumcraftResearchCategories.BASICS.categoryKey,
                            2, 0,
                            ShownIconsBackground.ROUND,
                            ShownIconsForeground.RESEARCH_FOREGROUND_ICON
                    )
            )
            .setPages(
                    new ResearchPage("tc.research_page.RESEARCH.1"), new ResearchPage("tc.research_page.RESEARCH.2"),
                    new ResearchPage((IRecipe) recipes.get("Thaumometer")),
                    new ResearchPage("tc.research_page.RESEARCH.3"),
                    new ResearchPage("tc.research_page.RESEARCH.4"), new ResearchPage((IRecipe) recipes.get("Scribe1")),
                    new ResearchPage((IRecipe) recipes.get("Scribe2")),
                    new ResearchPage((IRecipe) recipes.get("Scribe3")),
                    new ResearchPage("tc.research_page.RESEARCH.5"), new ResearchPage("tc.research_page.RESEARCH.6"),
                    new ResearchPage("tc.research_page.RESEARCH.7"), new ResearchPage("tc.research_page.RESEARCH.8"),
                    new ResearchPage("tc.research_page.RESEARCH.9"), new ResearchPage("tc.research_page.RESEARCH.10"),
                    new ResearchPage("tc.research_page.RESEARCH.11"), new ResearchPage("tc.research_page.RESEARCH.12")
            )
            .setAutoUnlock()
            .setStub();

    public static final ResearchItem RESEARCH_EXPERTISE = new ThaumcraftShownResearchItem(
            new ResearchItemResourceLocation(Thaumcraft.MOD_ID, "researcher1"),
            ThaumcraftResearchCategories.BASICS.categoryKey,
            UnmodifiableAspectList.of(
                    Aspects.MIND, Aspects.MIND, Aspects.MIND,
                    Aspects.SENSES, Aspects.SENSES, Aspects.SENSES,
                    Aspects.ORDER, Aspects.ORDER, Aspects.ORDER
            ),
            1
    )
            .addShownInfo(new ShownInfoInResearchCategory(
                    ThaumcraftResearchCategories.BASICS.categoryKey,
                    4,
                    1,
                    ShownIconsBackground.ROUND,
                    ShownIconsForeground.RESEARCH_EXPERTISE_FOREGROUND_ICON
            ))
            .setPages(new ResearchPage("tc.research_page.RESEARCHER1.1"))
            .setParents(RESEARCH.key);

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
                    ShownIconsBackground.ROUND,
                    ShownIconsForeground.RESEARCH_MASTERY_FOREGROUND_ICON

            ))
            .setPages(new ResearchPage("tc.research_page.RESEARCHER2.1"))
            .setRound()
            .setSpecial()
            .setParents(RESEARCH_EXPERTISE.key)
            .setWarp(1);
}
