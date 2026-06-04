package thaumcraft.common.items.displayhelper;

import net.minecraft.world.item.Item;
import thaumcraft.api.aspects.Aspect;

//sorry guys i'm lazy in rendering(in some cases) so i made this--i want mojang help me rendering.
public class AspectItem extends Item {
    public final Aspect aspect;
    public AspectItem(Properties properties,Aspect aspect) {
        super(properties);
        this.aspect = aspect;
    }
    public AspectItem(Aspect aspect) {
        this(new Item.Properties(),aspect);
    }
}
