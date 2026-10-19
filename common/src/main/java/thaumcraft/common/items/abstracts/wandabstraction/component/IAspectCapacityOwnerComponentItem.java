package thaumcraft.common.items.abstracts.wandabstraction.component;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;

public interface IAspectCapacityOwnerComponentItem<Asp extends Aspect> {

    @UnmodifiableView
    CalcCacheableCentiVisList<Asp> getCentiVisCapacity(ItemStack stack);
    boolean tryCastAspectClass(Class<? extends Aspect> aspClass);
}
