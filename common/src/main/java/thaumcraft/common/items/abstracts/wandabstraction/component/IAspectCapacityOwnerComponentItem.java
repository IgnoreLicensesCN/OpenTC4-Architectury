package thaumcraft.common.items.abstracts.wandabstraction.component;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import thaumcraft.api.aspects.Aspect;

public interface IAspectCapacityOwnerComponentItem<Asp extends Aspect> {

    CalcCacheableCentiVisList<Asp> getCentiVisCapacity();
    boolean tryCastAspectClass(Class<? extends Aspect> aspClass);
}
