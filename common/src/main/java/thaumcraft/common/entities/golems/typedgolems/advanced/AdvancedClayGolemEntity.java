package thaumcraft.common.entities.golems.typedgolems.advanced;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.AdvancedGolemEntity;
import thaumcraft.common.entities.golems.GolemBasicAbilityProperties;
import thaumcraft.common.entities.golems.typedgolems.ClayGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class AdvancedClayGolemEntity extends AdvancedGolemEntity {

    public AdvancedClayGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_CLAY_GOLEM(),level);
    }

    public AdvancedClayGolemEntity(EntityType<AdvancedClayGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.ADVANCED_CLAY_GOLEM().getDefaultInstance();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ClayGolemEntity.createGolemAttributes().add(Attributes.MOVEMENT_SPEED, 0.33 * 1.1);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            ClayGolemEntity.ABILITY_PROPERTIES.toBuilder()
                    .attackDelay(ClayGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(ClayGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();
    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }

}
