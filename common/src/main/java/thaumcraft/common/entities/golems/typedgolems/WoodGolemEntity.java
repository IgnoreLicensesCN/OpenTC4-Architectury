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

public class WoodGolemEntity extends BaseGolemEntity {

    public WoodGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.WOOD_GOLEM(),level);
    }

    public WoodGolemEntity(EntityType<WoodGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.WOOD_GOLEM().getDefaultInstance();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BaseGolemEntity.createGolemAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ARMOR, 6)
                .add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            GolemBasicAbilityProperties.builder()
                    .basicUpgradeSize(1)
                    .carryPower(4)
                    .regenDelay(75)
                    .strength(1)
                    .build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }
}
