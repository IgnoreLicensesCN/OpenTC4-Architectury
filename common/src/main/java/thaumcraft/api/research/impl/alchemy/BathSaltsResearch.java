package thaumcraft.api.research.impl.alchemy;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.research.implexample.ResearchNoteUnlockedResearch;
import thaumcraft.api.research.interfaces.IStringBasedSingleResearchClueOwner;
import thaumcraft.api.research.interfaces.IThemedAspectOwner;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class BathSaltsResearch
        extends ResearchNoteUnlockedResearch
        implements IStringBasedSingleResearchClueOwner, IThemedAspectOwner {
    public static final ResearchItemResourceLocation ID = ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "bath_salts");
    public BathSaltsResearch() {
        super(ID,2);
    }
    public static final ClueResourceLocation CLUE = ID.convertToClueResLoc();

    @Override
    public ClueResourceLocation getNeededClue() {
        return CLUE;
    }
    public static final @Unmodifiable AspectList<Aspect> givenAspects = UnmodifiableAspectList.of(
            Aspects.MIND, 3,
            Aspects.AURA, 3,
            Aspects.ORDER, 3,
            Aspects.HEAL, 3
    );

    @Override
    public AspectList<Aspect> getResearchGivenAspects() {
        return givenAspects;
    }

    @Override
    public @NotNull("null->empty") Aspect getResearchThemedAspect() {
        return Aspects.HEAL;
    }

    @Override
    public boolean canPlayerResearch(Player player) {
        return playerHasClue(player);
    }
}
