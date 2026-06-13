package thaumcraft.api.research.impl.basics;

import thaumcraft.api.research.implexample.AutoUnlockedResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public final class ResearchBasicResearch extends AutoUnlockedResearchItem {
    public ResearchBasicResearch(){
        super(ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "research_basic"));
    }
}
