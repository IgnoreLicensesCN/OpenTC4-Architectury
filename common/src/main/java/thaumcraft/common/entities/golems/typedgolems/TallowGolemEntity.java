
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

public class TallowGolemEntity extends BaseGolemEntity {

    public TallowGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TALLOW_GOLEM(),level);
    }

    public TallowGolemEntity(EntityType<TallowGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.TALLOW_GOLEM().getDefaultInstance();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BaseGolemEntity.createGolemAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ARMOR, 9)
                .add(Attributes.MOVEMENT_SPEED, 0.33);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            new GolemBasicAbilityProperties.Builder()
                    .basicUpgradeSize(2)
                    .carryPower(8)
                    .regenDelay(75)
                    .strength(2)
                    .build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }
}
