package thaumcraft.api.wands;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;

//for crafting
public interface ICraftingCostAspectOwnerComponent<Asp extends Aspect> {
    CentiVisList<Asp> getCraftingCostCentiVis();
}
