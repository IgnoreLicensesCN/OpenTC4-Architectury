package thaumcraft.api.research.impl.eldritch;

import net.minecraft.world.entity.player.Player;
import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearchWithParents;
import thaumcraft.api.research.interfaces.*;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;

public class FocusPrimalResearch
        extends ResearchNoteUnlockedResearchWithParents
        implements IThemedAspectOwner,
        IResearchNoteCopyable,
        IStringBasedSingleResearchClueOwner {


    public static final ClueResourceLocation CLUE = ClueResourceLocation.of(Thaumcraft.MOD_ID,"focus_primal");
    @Override
    public ClueResourceLocation getNeededClue() {
        return CLUE;
    }

    @Override
    public boolean canPlayerCreateResearchNote(Player player) {
        return super.canPlayerCreateResearchNote(player) || playerHasClue(player);
    }
}
