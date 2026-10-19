package thaumcraft.common.items.golem.golems.advanced;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.advanced.AdvancedClayGolemEntity;
import thaumcraft.common.items.golem.golems.BaseGolemPlacerItem;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_CLAY_GOLEM;

public class AdvancedClayGolemItem extends BaseGolemPlacerItem<AdvancedClayGolemEntity> {

    public AdvancedClayGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public AdvancedClayGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<AdvancedClayGolemEntity> getEntityTypeToPlace() {
        return ADVANCED_CLAY_GOLEM();
    }
}
