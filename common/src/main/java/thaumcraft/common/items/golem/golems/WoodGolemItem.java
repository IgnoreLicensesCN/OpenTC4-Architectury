package thaumcraft.common.items.golem.golems;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.WoodGolemEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.WOOD_GOLEM;

public class WoodGolemItem extends BaseGolemPlacerItem<WoodGolemEntity> {

    public WoodGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public WoodGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<WoodGolemEntity> getEntityTypeToPlace() {
        return WOOD_GOLEM();
    }
}
