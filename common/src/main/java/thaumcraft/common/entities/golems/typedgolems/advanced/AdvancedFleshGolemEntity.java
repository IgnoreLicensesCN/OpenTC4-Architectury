package thaumcraft.common.entities.golems.typedgolems.advanced;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.AdvancedGolemEntity;
import thaumcraft.common.entities.golems.typedgolems.FleshGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class AdvancedFleshGolemEntity extends AdvancedGolemEntity {

    public AdvancedFleshGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_FLESH_GOLEM(),level);
    }

    public AdvancedFleshGolemEntity(EntityType<AdvancedFleshGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.ADVANCED_FLESH_GOLEM().getDefaultInstance();
    }


    public static AttributeSupplier.Builder createAttributes() {
        return FleshGolemEntity.createGolemAttributes().add(Attributes.MOVEMENT_SPEED, 0.35 * 1.1);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            GolemBasicAbilityProperties.Builder
                    .copy(FleshGolemEntity.ABILITY_PROPERTIES)
                    .attackDelay(FleshGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(FleshGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }

}
