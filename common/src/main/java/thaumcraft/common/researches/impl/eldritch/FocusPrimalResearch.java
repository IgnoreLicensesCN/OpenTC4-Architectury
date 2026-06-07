package thaumcraft.common.researches.impl.eldritch;

import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearchWithParents;
import thaumcraft.api.research.interfaces.*;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class FocusPrimalResearch
        extends ResearchNoteUnlockedResearchWithParents
        implements IThemedAspectOwner,
        IRenderableResearch,
        IResearchNoteCopyable,
        IStringBasedSingleResearchClueOwner {


    public static final ClueResourceLocation CLUE = ClueResourceLocation.of(Thaumcraft.MOD_ID,"focus_primal");
    @Override
    public ClueResourceLocation getNeededClue() {
        return CLUE;
    }
}
