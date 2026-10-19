package thaumcraft.common.items.golem.golems.advanced;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.items.golem.golems.BaseGolemPlacerItem;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_STONE_GOLEM;

public class AdvancedStoneGolemItem extends BaseGolemPlacerItem<AdvancedStoneGolemEntity> {

    public AdvancedStoneGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public AdvancedStoneGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<AdvancedStoneGolemEntity> getEntityTypeToPlace() {
        return ADVANCED_STONE_GOLEM();
    }
}
