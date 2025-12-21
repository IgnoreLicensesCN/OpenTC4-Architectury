package thaumcraft.api.wands;

import thaumcraft.api.aspects.Aspect;

import java.util.Map;

public interface AspectCapacityOwner {

    Map<Aspect,Integer> getAspectCapacity();
}
