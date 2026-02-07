package thaumcraft.api.wands;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;

import java.util.Map;

//for crafting
public interface ICraftingCostAspectOwner<Asp extends Aspect> {
    CentiVisList<Asp> getCraftingCostCentiVis();
}
