package thaumcraft.common.entities.golems.typedgolems.advanced;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.AdvancedGolemEntity;
import thaumcraft.common.entities.golems.GolemBasicAbilityProperties;
import thaumcraft.common.entities.golems.typedgolems.StrawGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class AdvancedStrawGolemEntity extends AdvancedGolemEntity {

    public AdvancedStrawGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_STRAW_GOLEM(),level);
    }

    public AdvancedStrawGolemEntity(EntityType<AdvancedStrawGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.ADVANCED_STRAW_GOLEM().getDefaultInstance();
    }


    public static AttributeSupplier.Builder createAttributes() {
        return StrawGolemEntity.createGolemAttributes().add(Attributes.MOVEMENT_SPEED, 0.38 * 1.1);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            StrawGolemEntity.ABILITY_PROPERTIES.toBuilder()
                    .attackDelay(StrawGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(StrawGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }

}
