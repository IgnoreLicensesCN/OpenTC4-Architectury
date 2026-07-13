package thaumcraft.common.items.wands.componentbase;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.abstracts.wandabstraction.component.IWandComponentNameOwnerComponentItem;
import thaumcraft.common.items.abstracts.wandabstraction.component.IWandRodPropertiesOwnerComponentItem;

public abstract class ThaumcraftWandRodItem extends Item implements IWandRodPropertiesOwnerComponentItem<Aspect>, IWandComponentNameOwnerComponentItem {
    public ThaumcraftWandRodItem(Properties properties) {
        super(properties);
    }


    @Override
    @UnmodifiableView
    public CalcCacheableCentiVisList<Aspect> getCentiVisCapacity() {
        return CalcCacheableCentiVisList.emptySingleton();
    }

    @Override
    public boolean tryCastAspectClass(Class<? extends Aspect> aspClass) {
        return Aspect.class.isAssignableFrom(aspClass);
    }
}
