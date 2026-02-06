package thaumcraft.common.researches.impl;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.implexample.AutoUnlockedResearchItem;
import thaumcraft.api.research.interfaces.IRenderableResearch;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.api.research.render.impls.ShownIconsBackground;
import thaumcraft.api.research.render.impls.ShownIconsForeground;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.ThaumcraftResearchCategories;

import java.util.List;

public final class Research extends AutoUnlockedResearchItem implements IRenderableResearch {
    private static final ShownInfoInResearchCategory shownInfo = new ShownInfoInResearchCategory(
            ThaumcraftResearchCategories.BASICS.categoryKey,
                            2, 0,
            ShownIconsBackground.ROUND,
            ShownIconsForeground.RESEARCH_FOREGROUND_ICON
            );
    public Research(){
        super(
                new ResearchItemResourceLocation(Thaumcraft.MOD_ID, "research"),
                ThaumcraftResearchCategories.BASICS.categoryKey);
        ResearchCategory.getResearchCategory(shownInfo.category()).addResearch(this, shownInfo);
    }

    @Override
    public ShownInfoInResearchCategory getShownInfo(@NotNull ResearchCategory category) {
        return shownInfo;
    }

    @Override
    public List<ResearchPage> getPages(@NotNull ResearchCategory category, @Nullable Player player) {
        return List.of(
                new ResearchPage("tc.research_page.RESEARCH.1"), new ResearchPage("tc.research_page.RESEARCH.2"),
                new ResearchPage((IRecipe) recipes.get("Thaumometer")),
                new ResearchPage("tc.research_page.RESEARCH.3"),
                new ResearchPage("tc.research_page.RESEARCH.4"), new ResearchPage((IRecipe) recipes.get("Scribe1")),
                new ResearchPage((IRecipe) recipes.get("Scribe2")),
                new ResearchPage((IRecipe) recipes.get("Scribe3")),
                new ResearchPage("tc.research_page.RESEARCH.5"), new ResearchPage("tc.research_page.RESEARCH.6"),
                new ResearchPage("tc.research_page.RESEARCH.7"), new ResearchPage("tc.research_page.RESEARCH.8"),
                new ResearchPage("tc.research_page.RESEARCH.9"), new ResearchPage("tc.research_page.RESEARCH.10"),
                new ResearchPage("tc.research_page.RESEARCH.11"), new ResearchPage("tc.research_page.RESEARCH.12"));
    }
}
