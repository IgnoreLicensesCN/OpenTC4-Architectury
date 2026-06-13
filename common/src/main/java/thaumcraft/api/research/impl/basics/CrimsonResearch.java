package thaumcraft.api.research.impl.basics;

import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public final class CrimsonResearch extends ResearchItem {
    public CrimsonResearch(){
        super(ResearchItemResourceLocation.of(Thaumcraft.MOD_ID, "crimson"));
    }
}
