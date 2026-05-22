package thaumcraft.common.items.wands.componentbase;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.aspects.UnmodifiableCentiVisList;
import thaumcraft.api.wands.IWandComponentNameOwnerItem;
import thaumcraft.api.wands.IWandRodPropertiesOwnerComponent;

public abstract class ThaumcraftWandRodItem extends Item implements IWandRodPropertiesOwnerComponent<Aspect>, IWandComponentNameOwnerItem {
    public ThaumcraftWandRodItem(Properties properties) {
        super(properties);
    }


    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCentiVisCapacity() {
        return UnmodifiableCentiVisList.EMPTY;
    }

    @Override
    public boolean tryCastAspectClass(Class<? extends Aspect> aspClass) {
        return Aspect.class.isAssignableFrom(aspClass);
    }
}
