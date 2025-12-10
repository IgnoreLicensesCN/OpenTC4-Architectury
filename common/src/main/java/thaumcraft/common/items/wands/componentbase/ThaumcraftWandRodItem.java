package thaumcraft.common.items.wands;

import com.linearity.opentc4.datautils.SimplePair;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandComponentNameOwner;
import thaumcraft.api.wands.WandRodPropertiesOwner;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
