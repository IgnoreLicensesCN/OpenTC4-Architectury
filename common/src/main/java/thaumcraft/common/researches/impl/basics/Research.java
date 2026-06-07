package thaumcraft.common.researches.impl.basics;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.research.client.ResearchCategory;
import thaumcraft.api.research.client.ResearchPage;
import thaumcraft.api.research.implexample.AutoUnlockedResearchItem;
import thaumcraft.api.research.interfaces.IRenderableResearch;
import thaumcraft.api.research.client.render.ShownInfoInResearchCategory;
import thaumcraft.api.research.client.render.impls.ShownIconsBackground;
import thaumcraft.api.research.client.render.impls.ShownIconsForeground;
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
                ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "research"),
                ThaumcraftResearchCategories.BASICS.categoryKey);
        ResearchCategory.getResearchCategory(shownInfo.category()).addResearchAndShownInfo(this);
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
