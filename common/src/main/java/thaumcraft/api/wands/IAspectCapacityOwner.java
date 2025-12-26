package thaumcraft.api.wands;

import thaumcraft.api.aspects.Aspect;

import java.util.Map;

public interface IAspectCapacityOwner {

    Map<Aspect,Integer> getAspectCapacity();
}
