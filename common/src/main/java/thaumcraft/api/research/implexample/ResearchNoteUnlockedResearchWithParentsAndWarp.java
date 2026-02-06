package thaumcraft.api.research.implexample;

import org.jetbrains.annotations.Range;
import thaumcraft.api.research.interfaces.IResearchParentsOwner;
import thaumcraft.api.research.interfaces.IResearchWarpOwner;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.List;

public abstract class ResearchNoteUnlockedResearchWithParentsAndWarp
        extends ResearchNoteUnlockedResearchWithParents
        implements IResearchWarpOwner {
    private final int warp;
    public ResearchNoteUnlockedResearchWithParentsAndWarp(
            ResearchItemResourceLocation key,
            ResearchCategoryResourceLocation category,
            @Range(from = 1, to = 3) int complexity,
            List<ResearchItemResourceLocation> parents,
            int warp) {
        super(key, category, complexity, parents);
        this.warp = warp;
    }

    @Override
    public int getWarp() {
        return warp;
    }
}
