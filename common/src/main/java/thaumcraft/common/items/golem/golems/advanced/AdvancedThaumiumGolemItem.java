package thaumcraft.common.items.golem.golems.advanced;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.items.golem.golems.BaseGolemPlacerItem;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_THAUMIUM_GOLEM;

public class AdvancedThaumiumGolemItem extends BaseGolemPlacerItem<AdvancedThaumiumGolemEntity> {

    public AdvancedThaumiumGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public AdvancedThaumiumGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<AdvancedThaumiumGolemEntity> getEntityTypeToPlace() {
        return ADVANCED_THAUMIUM_GOLEM();
    }
}
