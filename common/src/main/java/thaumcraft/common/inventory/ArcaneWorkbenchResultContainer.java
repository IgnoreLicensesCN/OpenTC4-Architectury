package thaumcraft.common.inventory;

import net.minecraft.world.inventory.ResultContainer;
import thaumcraft.api.aspects.CentiVisList;

public class ArcaneWorkbenchResultContainer extends ResultContainer {
    protected CentiVisList costsAspects = new CentiVisList();
    public void setCostsAspects(CentiVisList costsAspects) {
        this.costsAspects = costsAspects;
    }
    public CentiVisList getCostsCentiVis() {
        return costsAspects;
    }
}
