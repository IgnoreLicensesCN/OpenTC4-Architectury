package thaumcraft.common.items.golem.golems.advanced;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.items.golem.golems.BaseGolemPlacerItem;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_IRON_GOLEM;

public class AdvancedIronGolemItem extends BaseGolemPlacerItem<AdvancedIronGolemEntity> {

    public AdvancedIronGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public AdvancedIronGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<AdvancedIronGolemEntity> getEntityTypeToPlace() {
        return ADVANCED_IRON_GOLEM();
    }
}
