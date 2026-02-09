package thaumcraft.common.researches.impl;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearchWithParentsAndWarp;
import thaumcraft.api.research.interfaces.IRenderableResearch;
import thaumcraft.api.research.interfaces.IResearchNoteCopyable;
import thaumcraft.api.research.interfaces.IThemedAspectOwner;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.api.research.render.impls.ShownIconsBackground;
import thaumcraft.api.research.render.impls.ShownIconsForeground;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.ThaumcraftResearchCategories;

import java.util.List;

import static thaumcraft.common.researches.ThaumcraftResearches.RESEARCH_EXPERTISE;

public class ResearchMastery
        extends ResearchNoteUnlockedResearchWithParentsAndWarp
    implements IThemedAspectOwner, IRenderableResearch, IResearchNoteCopyable
{
    private static final ShownInfoInResearchCategory shownInfo = new ShownInfoInResearchCategory(
            ThaumcraftResearchCategories.BASICS.categoryKey,
            3, 3,
            ShownIconsBackground.ROUND_AND_SPECIAL,
            ShownIconsForeground.RESEARCH_MASTERY_FOREGROUND_ICON

    );
    public ResearchMastery() {
        super(ResearchItemResourceLocation.of(Thaumcraft.MOD_ID,"researcher2"),
                ThaumcraftResearchCategories.BASICS.categoryKey,
                2,
                List.of(RESEARCH_EXPERTISE.key),
                1);
        ResearchCategory.getResearchCategory(shownInfo.category()).addResearchAndShownInfo(this,shownInfo);
    }
    private final AspectList<Aspect> aspects =
            new UnmodifiableAspectList<>(new AspectList<>().addAll(Aspects.MIND, 6)
            .addAll(Aspects.ORDER, 3)
            .addAll(Aspects.SENSES, 3)
            .addAll(Aspects.MAGIC, 3));
    private final Aspect themedAspect = IThemedAspectOwner.getResearchThemedAspectViaList(aspects);

    @Override
    public @NotNull("null->empty") Aspect getResearchThemedAspect() {
        return themedAspect;
    }

    @Override
    public AspectList<Aspect> getResearchGivenAspects() {
        return aspects;
    }

    @Override
    public ShownInfoInResearchCategory getShownInfo(@NotNull ResearchCategory category) {
        if (category.categoryKey.equals(shownInfo.category())) {
            return shownInfo;
        }
        return null;
    }

    private final List<ResearchPage> pages = List.of(new ResearchPage("tc.research_page.RESEARCHER2.1"));

    @Override
    public List<ResearchPage> getPages(@NotNull ResearchCategory category, @Nullable Player player) {
        return pages;
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
