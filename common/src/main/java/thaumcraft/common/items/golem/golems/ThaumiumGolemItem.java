package thaumcraft.common.items.golem.golems;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.ThaumiumGolemEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.THAUMIUM_GOLEM;

public class ThaumiumGolemItem extends BaseGolemPlacerItem<ThaumiumGolemEntity> {

    public ThaumiumGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public ThaumiumGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<ThaumiumGolemEntity> getEntityTypeToPlace() {
        return THAUMIUM_GOLEM();
    }
}
