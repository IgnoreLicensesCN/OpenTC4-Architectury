package thaumcraft.common.items.wands.componentbase;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandComponentNameOwner;
import thaumcraft.api.wands.WandRodPropertiesOwner;

import java.util.Collections;
import java.util.Map;

public abstract class ThaumcraftWandRodItem extends Item implements WandRodPropertiesOwner, WandComponentNameOwner {
    public ThaumcraftWandRodItem(Properties properties) {
        super(properties);
    }

    @Override
    @UnmodifiableView
    public Map<Aspect,Integer> getAspectCapacity() {
        return Collections.emptyMap();
    }
}
