package thaumcraft.common.entities.golems.typedgolems.advanced;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.AdvancedGolemEntity;
import thaumcraft.common.entities.golems.typedgolems.TallowGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class AdvancedTallowGolemEntity extends AdvancedGolemEntity {

    public AdvancedTallowGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_TALLOW_GOLEM(),level);
    }

    public AdvancedTallowGolemEntity(EntityType<AdvancedTallowGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.ADVANCED_TALLOW_GOLEM().getDefaultInstance();
    }


    public static AttributeSupplier.Builder createAttributes() {
        return TallowGolemEntity.createGolemAttributes().add(Attributes.MOVEMENT_SPEED, 0.33 * 1.1);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            GolemBasicAbilityProperties.Builder
                    .copy(TallowGolemEntity.ABILITY_PROPERTIES)
                    .attackDelay(TallowGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(TallowGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }

}
