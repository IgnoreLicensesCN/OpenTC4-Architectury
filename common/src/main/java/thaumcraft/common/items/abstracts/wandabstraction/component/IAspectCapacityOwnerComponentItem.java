package thaumcraft.common.items.abstracts.wandabstraction.component;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

public interface IAspectCapacityOwnerComponentItem<Asp extends Aspect> {

    CalcCacheableCentiVisList<Asp> getCentiVisCapacity(ItemStack stack);
    boolean tryCastAspectClass(Class<? extends Aspect> aspClass);
}
