package thaumcraft.common.items.golem.golems.advanced;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.advanced.AdvancedWoodGolemEntity;
import thaumcraft.common.items.golem.golems.BaseGolemPlacerItem;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_WOOD_GOLEM;

public class AdvancedWoodGolemItem extends BaseGolemPlacerItem<AdvancedWoodGolemEntity> {

    public AdvancedWoodGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public AdvancedWoodGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<AdvancedWoodGolemEntity> getEntityTypeToPlace() {
        return ADVANCED_WOOD_GOLEM();
    }
}
