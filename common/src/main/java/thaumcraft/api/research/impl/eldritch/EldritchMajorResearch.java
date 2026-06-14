package thaumcraft.api.research.impl.eldritch;

import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class EldritchMajorResearch extends ResearchItem {
    public static final ResearchItemResourceLocation ID = ResearchItemResourceLocation.of(Thaumcraft.MOD_ID,"eldritch_major");
    public EldritchMajorResearch() {
        super(ID);
    }
}
