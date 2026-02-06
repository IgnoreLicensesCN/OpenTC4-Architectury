package thaumcraft.api.research.implexample;

import org.jetbrains.annotations.Range;
import thaumcraft.api.research.interfaces.IResearchParentsOwner;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ResearchNoteUnlockedResearchWithParents
        extends ResearchNoteUnlockedResearch
        implements IResearchParentsOwner {
    List<ResearchItemResourceLocation> parents;
    public ResearchNoteUnlockedResearchWithParents(
            ResearchItemResourceLocation key,
            ResearchCategoryResourceLocation category,
            @Range(from = 1, to = 3) int complexity,
            List<ResearchItemResourceLocation> parents
            ) {
        super(key, category, complexity);
        this.parents = parents;
    }

    @Override
    public boolean canPlayerCreateResearchNote(String playerName) {
        Set<ResearchItemResourceLocation> researched = new HashSet<>(ResearchManager.getResearchForPlayer(playerName));
        return researched.containsAll(getParents());
    }

    @Override
    public List<ResearchItemResourceLocation> getParents() {
        return parents;
    }
}
