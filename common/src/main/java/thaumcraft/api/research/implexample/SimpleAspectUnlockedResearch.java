package thaumcraft.api.research.implexample;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.interfaces.IAspectUnlockable;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class SimpleAspectUnlockedResearch extends ResearchItem implements IAspectUnlockable {
    private final AspectList<Aspect> aspects;

    public SimpleAspectUnlockedResearch(
            ResearchItemResourceLocation key,
            ResearchCategoryResourceLocation category,
            AspectList<Aspect> aspectsCost
    ) {
        super(key, category);
        this.aspects = new UnmodifiableAspectList<>(aspectsCost);
    }

    @Override
    public boolean canPlayerCompleteResearchWithAspect(String playerName) {
        return true;
    }

    @Override
    public AspectList<Aspect> getAspectCost() {
        return aspects;
    }

    @Override
    public boolean canPlayerResearch(String playerName) {
        return true;
    }
}
