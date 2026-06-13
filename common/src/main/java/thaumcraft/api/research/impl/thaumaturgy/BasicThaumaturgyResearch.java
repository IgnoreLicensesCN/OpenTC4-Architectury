package thaumcraft.api.research.impl.thaumaturgy;

import thaumcraft.api.research.implexample.AutoUnlockedResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class BasicThaumaturgyResearch extends AutoUnlockedResearchItem {
    public BasicThaumaturgyResearch(){
        super(ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "basic_thaumaturgy"));
    }
}
