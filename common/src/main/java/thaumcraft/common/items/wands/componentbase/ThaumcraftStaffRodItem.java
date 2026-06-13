package thaumcraft.common.items.wands.componentbase;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.wands.*;

public class ThaumcraftStaffRodItem extends Item implements IWandRodPropertiesOwnerComponent<Aspect>, IImmutableAspectCapacityOwnerComponent<Aspect>, WorkAsStaffRod {
    public ThaumcraftStaffRodItem(Properties properties) {
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
