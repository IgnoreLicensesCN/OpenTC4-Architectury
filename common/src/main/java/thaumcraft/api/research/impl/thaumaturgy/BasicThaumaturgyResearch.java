package thaumcraft.api.research.impl.thaumaturgy;

import thaumcraft.api.research.implexample.AutoUnlockedResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class BasicThaumaturgyResearch extends AutoUnlockedResearchItem {

    public static final ResearchItemResourceLocation ID = ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "basic_thaumaturgy");

    public BasicThaumaturgyResearch(){
        super(ID);
    }
}
