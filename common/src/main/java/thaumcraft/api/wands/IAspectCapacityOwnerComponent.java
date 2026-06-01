package thaumcraft.api.wands;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;

public interface IAspectCapacityOwnerComponent<Asp extends Aspect> {

    CentiVisList<Asp> getCentiVisCapacity();
    boolean tryCastAspectClass(Class<? extends Aspect> aspClass);
}
