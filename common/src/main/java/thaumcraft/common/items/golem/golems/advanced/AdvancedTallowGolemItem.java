package thaumcraft.common.items.golem.golems.advanced;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.advanced.AdvancedTallowGolemEntity;
import thaumcraft.common.items.golem.golems.BaseGolemPlacerItem;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_TALLOW_GOLEM;

public class AdvancedTallowGolemItem extends BaseGolemPlacerItem<AdvancedTallowGolemEntity> {

    public AdvancedTallowGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public AdvancedTallowGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<AdvancedTallowGolemEntity> getEntityTypeToPlace() {
        return ADVANCED_TALLOW_GOLEM();
    }
}
