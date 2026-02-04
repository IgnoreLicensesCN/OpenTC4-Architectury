package thaumcraft.common.items.displayhelper;

import net.minecraft.world.item.Item;
import thaumcraft.api.aspects.Aspect;

public class AspectItem extends Item {
    public final Aspect aspect;
    public AspectItem(Properties properties,Aspect aspect) {
        super(properties);
        this.aspect = aspect;
    }
    public AspectItem(Aspect aspect) {
        super(new Item.Properties());
        this.aspect = aspect;
    }
}
