package thaumcraft.api.research.impl.basics;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearchWithParentsAndWarp;
import thaumcraft.api.research.interfaces.IResearchNoteCopyable;
import thaumcraft.api.research.interfaces.IThemedAspectOwner;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.api.research.client.ThaumcraftResearchCategories;

import java.util.List;

import static thaumcraft.api.research.ThaumcraftResearches.RESEARCH_EXPERTISE;

public class ResearchMasteryResearch
        extends ResearchNoteUnlockedResearchWithParentsAndWarp
    implements IThemedAspectOwner, IResearchNoteCopyable
{
    public ResearchMasteryResearch() {
        super(ResearchItemResourceLocation.of(Thaumcraft.MOD_ID,"research_mastery"),
                ThaumcraftResearchCategories.BASICS.categoryKey,
                2,
                List.of(RESEARCH_EXPERTISE.key),
                1);
    }
    private final AspectList<Aspect> aspects =
            UnmodifiableAspectList.of(
                    Aspects.MIND, 6,
                    Aspects.ORDER, 3,
                    Aspects.SENSES, 3,
                    Aspects.MAGIC, 3
            );
    private final Aspect themedAspect = Aspects.MIND;

    @Override
    public @NotNull("null->empty") Aspect getResearchThemedAspect() {
        return themedAspect;
    }

    @Override
    public AspectList<Aspect> getResearchGivenAspects() {
        return aspects;
    }

    @Override
    public boolean canPlayerCopyResearch(Player player) {
        return canPlayerResearch(player);
    }

    @Override
    public @UnmodifiableView AspectList<Aspect> getCopyResearchBaseAspects() {
        return aspects;
    }
}
