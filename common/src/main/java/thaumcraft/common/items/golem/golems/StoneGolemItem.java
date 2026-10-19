package thaumcraft.common.items.golem.golems;

import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.golems.typedgolems.StoneGolemEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.STONE_GOLEM;

public class StoneGolemItem extends BaseGolemPlacerItem<StoneGolemEntity> {

    public StoneGolemItem() {
        this(new Properties().stacksTo(1));
    }
    public StoneGolemItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull EntityType<StoneGolemEntity> getEntityTypeToPlace() {
        return STONE_GOLEM();
    }
}
