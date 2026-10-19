package thaumcraft.common.items.golem.golems.advanced;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.items.golem.golems.BaseGolemPlacerItem;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_STRAW_GOLEM;

public class AdvancedStrawGolemItem extends BaseGolemPlacerItem<AdvancedStrawGolemEntity> {

    public AdvancedStrawGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public AdvancedStrawGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<AdvancedStrawGolemEntity> getEntityTypeToPlace() {
        return ADVANCED_STRAW_GOLEM();
    }
}
