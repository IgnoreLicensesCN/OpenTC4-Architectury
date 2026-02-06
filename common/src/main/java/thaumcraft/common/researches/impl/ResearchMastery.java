package thaumcraft.common.researches.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.expands.UnmodifiableAspectList;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearchWithParentsAndWarp;
import thaumcraft.api.research.interfaces.IThemedAspectOwner;
import thaumcraft.api.research.render.ShownInfoInResearchCategory;
import thaumcraft.api.research.render.impls.ShownIconsBackground;
import thaumcraft.api.research.render.impls.ShownIconsForeground;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.ThaumcraftResearchCategories;

import java.util.List;

import static thaumcraft.common.researches.ThaumcraftResearches.RESEARCH_EXPERTISE;

public class ResearchMastery
        extends ResearchNoteUnlockedResearchWithParentsAndWarp
    implements IThemedAspectOwner
{
    private static final ShownInfoInResearchCategory shownInfo = new ShownInfoInResearchCategory(
            ThaumcraftResearchCategories.BASICS.categoryKey,
            3, 3,
            ShownIconsBackground.ROUND_AND_SPECIAL,
            ShownIconsForeground.RESEARCH_MASTERY_FOREGROUND_ICON

    );
    public ResearchMastery() {
        super(new ResearchItemResourceLocation(Thaumcraft.MOD_ID,"researcher2"),
                ThaumcraftResearchCategories.BASICS.categoryKey,
                2,
                List.of(RESEARCH_EXPERTISE.key),
                1);
        ResearchCategory.getResearchCategory(shownInfo.category()).addResearch(this,shownInfo);
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
}
