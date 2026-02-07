package thaumcraft.common.researches.impl;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.expands.UnmodifiableAspectList;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearchWithParents;
import thaumcraft.api.research.interfaces.IRenderableResearch;
import thaumcraft.api.research.interfaces.IResearchNoteCopyable;
import thaumcraft.api.research.interfaces.IThemedAspectOwner;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.api.research.render.impls.ShownIconsBackground;
import thaumcraft.api.research.render.impls.ShownIconsForeground;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.ThaumcraftResearchCategories;
import thaumcraft.common.researches.ThaumcraftResearches;

import java.util.List;

public class ResearchExpertise
        extends ResearchNoteUnlockedResearchWithParents
        implements IThemedAspectOwner, IRenderableResearch , IResearchNoteCopyable {
    private static final ShownInfoInResearchCategory shownInfo = new ShownInfoInResearchCategory(
            ThaumcraftResearchCategories.BASICS.categoryKey,
            4,
            1,
            ShownIconsBackground.ROUND,
            ShownIconsForeground.RESEARCH_EXPERTISE_FOREGROUND_ICON
    );
    public ResearchExpertise(){
        super(
                ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "researcher1"),
                ThaumcraftResearchCategories.BASICS.categoryKey,
                1, List.of(ThaumcraftResearches.RESEARCH.key)
        );
        ResearchCategory.getResearchCategory(shownInfo.category()).addResearchAndShownInfo(this,shownInfo);
    }

    private final AspectList<Aspect> aspects = UnmodifiableAspectList.of(
            Aspects.MIND, Aspects.MIND, Aspects.MIND,
            Aspects.SENSES, Aspects.SENSES, Aspects.SENSES,
            Aspects.ORDER, Aspects.ORDER, Aspects.ORDER
    );
    private final Aspect themedAspect = IThemedAspectOwner.getResearchThemedAspectViaList(aspects);
    @Override
    public @NotNull Aspect getResearchThemedAspect() {
        return themedAspect;
    }

    @Override
    public ShownInfoInResearchCategory getShownInfo(@NotNull ResearchCategory category) {
        return shownInfo;
    }

    private final List<ResearchPage> pages = List.of(new ResearchPage("tc.research_page.RESEARCHER1.1"));
    @Override
    public List<ResearchPage> getPages(@NotNull ResearchCategory category, @Nullable Player player) {
        return pages;
    }

    @Override
    public AspectList<Aspect> getResearchGivenAspects() {
        return aspects;
    }


    @Override
    public boolean canPlayerCopyResearch(String playerName) {
        return canPlayerResearch(playerName);
    }

    @Override
    public @UnmodifiableView AspectList<Aspect> getCopyResearchBaseAspects() {
        return aspects;
    }

}
