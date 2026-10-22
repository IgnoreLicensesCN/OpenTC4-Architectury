package thaumcraft.common.entities.golems.typedgolems.advanced;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.AdvancedGolemEntity;
import thaumcraft.common.entities.golems.typedgolems.ThaumiumGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class AdvancedThaumiumGolemEntity extends AdvancedGolemEntity {

    public AdvancedThaumiumGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_THAUMIUM_GOLEM(),level);
    }

    public AdvancedThaumiumGolemEntity(EntityType<AdvancedThaumiumGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.ADVANCED_THAUMIUM_GOLEM().getDefaultInstance();
    }


    public static AttributeSupplier.Builder createAttributes() {
        return ThaumiumGolemEntity.createGolemAttributes().add(Attributes.MOVEMENT_SPEED, 0.32 * 1.1);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            GolemBasicAbilityProperties.Builder
                    .copy(ThaumiumGolemEntity.ABILITY_PROPERTIES)
                    .attackDelay(ThaumiumGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(ThaumiumGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }

}
