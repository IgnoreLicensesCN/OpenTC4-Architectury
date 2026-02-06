package thaumcraft.api.research.implexample;

import org.jetbrains.annotations.Range;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IResearchNoteCreatable;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public abstract class ResearchNoteUnlockedResearch extends ResearchItem implements IResearchNoteCreatable {

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
    public boolean canPlayerCreateResearchNote(String playerName) {
        return true;
    }

    @Override
    public @Range(from = 1, to = 3) int getComplexity() {
        return complexity;
    }
}
