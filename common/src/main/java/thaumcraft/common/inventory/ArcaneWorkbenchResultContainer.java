package thaumcraft.common.inventory;

import net.minecraft.world.inventory.ResultContainer;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.LinkedHashCentiVisList;

public class ArcaneWorkbenchResultContainer extends ResultContainer {
    protected CentiVisList<Aspect> costsAspects = new LinkedHashCentiVisList<>();
    public void setCostsAspects(CentiVisList<Aspect> costsAspects) {
        this.costsAspects = costsAspects;
    }
    public CentiVisList<Aspect> getCostsCentiVis() {
        return costsAspects;
    }
}
