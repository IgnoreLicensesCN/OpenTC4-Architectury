package thaumcraft.api.research.impl.basics;

import thaumcraft.api.research.implexample.AutoUnlockedResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public final class AspectDisplayingResearch extends AutoUnlockedResearchItem {
    public AspectDisplayingResearch(){
        super(ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "aspect_displaying"));
    }
}
