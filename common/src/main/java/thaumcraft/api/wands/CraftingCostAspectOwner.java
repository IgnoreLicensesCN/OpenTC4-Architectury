package thaumcraft.api.wands;

import thaumcraft.api.aspects.Aspect;

import java.util.Map;

//for crafting
public interface CraftingCostAspectOwner {
    Map<Aspect, Integer> getCraftingCostAspect();
}
