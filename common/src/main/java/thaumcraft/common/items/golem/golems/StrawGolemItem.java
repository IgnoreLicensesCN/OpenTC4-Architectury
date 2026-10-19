package thaumcraft.common.items.golem.golems;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.StrawGolemEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.STRAW_GOLEM;

public class StrawGolemItem extends BaseGolemPlacerItem<StrawGolemEntity> {

    public StrawGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public StrawGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<StrawGolemEntity> getEntityTypeToPlace() {
        return STRAW_GOLEM();
    }
}
