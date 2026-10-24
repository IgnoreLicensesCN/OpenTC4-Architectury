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

public class ClayGolemEntity extends BaseGolemEntity {

    public ClayGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.CLAY_GOLEM(),level);
    }

    public ClayGolemEntity(EntityType<ClayGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.CLAY_GOLEM().getDefaultInstance();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BaseGolemEntity.createGolemAttributes()
                .add(Attributes.MAX_HEALTH, 25)
                .add(Attributes.ARMOR, 9)
                .add(Attributes.MOVEMENT_SPEED, 0.33);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            new GolemBasicAbilityProperties.Builder()
                    .basicUpgradeSize(1)
                    .carryPower(8)
                    .regenDelay(100)
                    .strength(2)
                    .build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }
}
