package thaumcraft.common.items.golem.golems;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.IronGolemEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.IRON_GOLEM;

public class IronGolemItem extends BaseGolemPlacerItem<IronGolemEntity> {

    public IronGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public IronGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<IronGolemEntity> getEntityTypeToPlace() {
        return IRON_GOLEM();
    }
}
