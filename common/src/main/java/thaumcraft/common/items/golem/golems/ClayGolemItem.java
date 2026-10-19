package thaumcraft.common.items.golem.golems;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.ClayGolemEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.CLAY_GOLEM;

public class ClayGolemItem extends BaseGolemPlacerItem<ClayGolemEntity> {

    public ClayGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public ClayGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<ClayGolemEntity> getEntityTypeToPlace() {
        return CLAY_GOLEM();
    }
}
