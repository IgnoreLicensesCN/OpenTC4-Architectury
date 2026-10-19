
package thaumcraft.common.items.golem.golems.advanced;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.advanced.AdvancedFleshGolemEntity;
import thaumcraft.common.items.golem.golems.BaseGolemPlacerItem;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_FLESH_GOLEM;

public class AdvancedFleshGolemItem extends BaseGolemPlacerItem<AdvancedFleshGolemEntity> {

    public AdvancedFleshGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public AdvancedFleshGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<AdvancedFleshGolemEntity> getEntityTypeToPlace() {
        return ADVANCED_FLESH_GOLEM();
    }
}
