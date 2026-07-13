package thaumcraft.common.items.abstracts.wandabstraction.component;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;

//for crafting
//it just means"provides aspect cost for crafting",how to use it depends on recipe
public interface ICraftingCostAspectOwnerComponentItem<Asp extends Aspect> {
    CentiVisList<Asp> getCraftingCostCentiVis();
}
