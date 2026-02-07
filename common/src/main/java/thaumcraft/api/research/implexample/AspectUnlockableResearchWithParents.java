package thaumcraft.api.research.implexample;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.interfaces.IAspectUnlockable;
import thaumcraft.api.research.interfaces.IResearchParentsOwner;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AspectUnlockableResearchWithParents extends SimpleAspectUnlockedResearch implements IAspectUnlockable, IResearchParentsOwner {

    private final List<ResearchItemResourceLocation> parents;
    public AspectUnlockableResearchWithParents(
            ResearchItemResourceLocation key,
            ResearchCategoryResourceLocation category,
            AspectList<Aspect> aspectsCost,
            List<ResearchItemResourceLocation> parents
    ) {
        super(key, category,aspectsCost);
        this.parents = parents;
    }

    @Override
    public boolean canPlayerCompleteResearchWithAspect(String playerName) {
        Set<ResearchItemResourceLocation> researched = new HashSet<>(ResearchManager.getResearchForPlayer(playerName));
        return researched.containsAll(getParents());
    }

    @Override
    public List<ResearchItemResourceLocation> getParents() {
        return parents;
    }

    @Override
    public boolean canPlayerResearch(String playerName) {
        Set<ResearchItemResourceLocation> researched = new HashSet<>(ResearchManager.getResearchForPlayer(playerName));
        return researched.containsAll(getParents());
    }
}
