package thaumcraft.api.wands;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;

public interface IAspectCapacityOwner<Asp extends Aspect> {

    CentiVisList<Asp> getCentiVisCapacity();
    boolean tryCastAspectClass(Class<? extends Aspect> aspClass);
}
