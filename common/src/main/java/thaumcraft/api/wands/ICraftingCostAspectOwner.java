package thaumcraft.api.wands;

import thaumcraft.api.aspects.Aspect;

import java.util.Map;

//for crafting
public interface ICraftingCostAspectOwner {
    Map<Aspect, Integer> getCraftingCostCentiVis();
}
