package thaumcraft.common.entities.golems.typedgolems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.properties.GolemBasicAbilityProperties;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class FleshGolemEntity extends BaseGolemEntity {

    public FleshGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.FLESH_GOLEM(),level);
    }

    public FleshGolemEntity(EntityType<FleshGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.FLESH_GOLEM().getDefaultInstance();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BaseGolemEntity.createGolemAttributes()
                .add(Attributes.MAX_HEALTH, 15)
                .add(Attributes.ARMOR, 6)
                .add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            new GolemBasicAbilityProperties.Builder()
                    .basicUpgradeSize(2)
                    .carryPower(4)
                    .regenDelay(40)
                    .strength(1)
                    .build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }
}
