package thaumcraft.api.research.implexample;

import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.ResearchCategoryResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class AutoUnlockedResearchItem extends ResearchItem {
    public AutoUnlockedResearchItem(ResearchItemResourceLocation key, ResearchCategoryResourceLocation category) {
        super(key, category);
    }

    @Override
    public boolean isPlayerCompletedResearch(String playerName) {
        return true;
    }
}
