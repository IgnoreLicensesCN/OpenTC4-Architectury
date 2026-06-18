package thaumcraft.api.research.impl.eldritch;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearchWithParentsAndWarp;
import thaumcraft.api.research.interfaces.*;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.List;

public class FocusPrimalResearch
        extends ResearchNoteUnlockedResearchWithParentsAndWarp
        implements
        IThemedAspectOwner,
        IResearchNoteCopyable,
        IStringBasedSingleResearchClueOwner {
    public static final ResearchItemResourceLocation ID = ResearchItemResourceLocation.of(Thaumcraft.MOD_ID,"primal_focus");
    public FocusPrimalResearch() {
        super(ID,
                2,
                List.of(EldritchMinorResearch.ID),
                2
        );
    }

    public static final ClueResourceLocation CLUE = ID.convertToClueResLoc();
    @Override
    public ClueResourceLocation getNeededClue() {
        return CLUE;
    }

    @Override
    public boolean canPlayerResearch(Player player) {
        return super.canPlayerResearch(player) || livingHasClue(player);
    }

    private static final AspectList<Aspect> givenAspects = UnmodifiableAspectList.of(
            Aspects.AIR,6,
            Aspects.EARTH,6,
            Aspects.WATER,6,
            Aspects.FIRE,6,
            Aspects.ORDER,6,
            Aspects.ENTROPY,6,
            Aspects.MAGIC,6
    );
    @Override
    public AspectList<Aspect> getResearchGivenAspects() {
        return givenAspects;
    }

    @Override
    public @NotNull("null->empty") Aspect getResearchThemedAspect() {
        return Aspects.MAGIC;
    }

    @Override
    public boolean canPlayerCopyResearch(Player player) {
        return true;
    }

    @Override
    public @UnmodifiableView AspectList<Aspect> getCopyResearchBaseAspects() {
        return givenAspects;
    }
}
