package thaumcraft.api.wands;

import com.linearity.opentc4.datautils.SimplePair;
import thaumcraft.api.aspects.Aspect;

import java.util.List;
import java.util.Map;

public interface AspectCapacityOwner {

    Map<Aspect,Integer> getAspectCapacity();
}
