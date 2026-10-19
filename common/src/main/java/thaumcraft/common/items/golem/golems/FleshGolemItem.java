package thaumcraft.common.items.golem.golems;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.FleshGolemEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.FLESH_GOLEM;

public class FleshGolemItem extends BaseGolemPlacerItem<FleshGolemEntity> {

    public FleshGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public FleshGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<FleshGolemEntity> getEntityTypeToPlace() {
        return FLESH_GOLEM();
    }
}
