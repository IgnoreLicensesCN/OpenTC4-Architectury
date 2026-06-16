package thaumcraft.api.wands;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import thaumcraft.api.aspects.Aspect;

public interface IAspectCapacityOwnerComponent<Asp extends Aspect> {

    CalcCacheableCentiVisList<Asp> getCentiVisCapacity();
    boolean tryCastAspectClass(Class<? extends Aspect> aspClass);
}
