package thaumcraft.api.research.implexample;

import com.linearity.opentc4.OpenTC4;
import org.jetbrains.annotations.Range;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IResearchParentsOwner;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.List;

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
        return researchedAllParents(playerName);
    }

    @Override
    public List<ResearchItemResourceLocation> getParents() {
        return parents;
    }


    @Override
    public boolean canPlayerResearch(String playerName) {
        return researchedAllParents(playerName);
    }

    protected boolean researchedAllParents(String playerName) {
        for (ResearchItemResourceLocation researchKey : getParents()) {
            var researchParent = ResearchItem.getResearch(researchKey);
            if (researchParent == null) {
                OpenTC4.LOGGER.error("Research not found: {}",researchKey);
                return false;
            }
            if (!researchParent.isPlayerCompletedResearch(playerName)) {
                return false;
            }
        }
        return true;
    }
}
