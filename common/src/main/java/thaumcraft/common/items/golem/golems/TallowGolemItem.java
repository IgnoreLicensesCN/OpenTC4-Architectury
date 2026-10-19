package thaumcraft.common.items.golem.golems;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.TallowGolemEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.TALLOW_GOLEM;

public class TallowGolemItem extends BaseGolemPlacerItem<TallowGolemEntity> {

    public TallowGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public TallowGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<TallowGolemEntity> getEntityTypeToPlace() {
        return TALLOW_GOLEM();
    }
}
