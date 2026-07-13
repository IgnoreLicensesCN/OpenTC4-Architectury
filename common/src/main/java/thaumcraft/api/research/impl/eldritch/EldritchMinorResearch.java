package thaumcraft.api.research.impl.eldritch;

import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class EldritchMinorResearch extends ResearchItem {
    public static final ResearchItemResourceLocation ID = ResearchItemResourceLocation.of(Thaumcraft.MOD_ID,"eldritch_minor");
    public EldritchMinorResearch() {
        super(ID);
    }
}
