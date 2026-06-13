package thaumcraft.api.research.impl.basics;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearchWithParents;
import thaumcraft.api.research.interfaces.IResearchNoteCopyable;
import thaumcraft.api.research.interfaces.IThemedAspectOwner;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.api.research.client.ThaumcraftResearchCategories;
import thaumcraft.api.research.ThaumcraftResearches;

import java.util.List;

public class ResearchExpertiseResearch
        extends ResearchNoteUnlockedResearchWithParents
        implements IThemedAspectOwner , IResearchNoteCopyable {
    public ResearchExpertiseResearch(){
        super(
                ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "research_expertise"),
                ThaumcraftResearchCategories.BASICS.categoryKey,
                1, List.of(ThaumcraftResearches.RESEARCH_BASIC.key)
        );
    }

    private final AspectList<Aspect> aspects = UnmodifiableAspectList.of(
            Aspects.MIND, 3,
            Aspects.SENSES, 3,
            Aspects.ORDER, 3
    );
    private final Aspect themedAspect = Aspects.MIND;
    @Override
    public @NotNull Aspect getResearchThemedAspect() {
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
