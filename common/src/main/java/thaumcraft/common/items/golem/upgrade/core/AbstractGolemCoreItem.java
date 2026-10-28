package thaumcraft.common.items.golem.upgrade.core;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.world.item.Item;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemCoreItem;

@UtilityLikeAbstraction
public abstract class AbstractGolemCoreItem extends Item implements IGolemCoreItem {
    public AbstractGolemCoreItem() {
        this(new Properties());
    }
    public AbstractGolemCoreItem(Properties properties) {
        super(properties);
    }
}
