package thaumcraft.api.research.implexample;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Range;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IResearchNoteCreatableResearch;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public abstract class ResearchNoteUnlockedResearch extends ResearchItem implements IResearchNoteCreatableResearch {

    private final @Range(from = 1, to = 3) int complexity;

    public ResearchNoteUnlockedResearch(
            ResearchItemResourceLocation key,
            ResearchCategoryResourceLocation category,
            @Range(from = 1, to = 3) int complexity
    ) {
        super(key, category);
        this.complexity = complexity;
    }

    @Override
    public boolean canPlayerCreateResearchNote(Player player) {
        return true;
    }

    @Override
    public @Range(from = 1, to = 3) int getComplexity() {
        return complexity;
    }
}
