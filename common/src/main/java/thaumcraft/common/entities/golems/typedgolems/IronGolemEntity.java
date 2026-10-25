
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

public class IronGolemEntity extends BaseGolemEntity {

    public IronGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.IRON_GOLEM(),level);
    }

    public IronGolemEntity(EntityType<IronGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.IRON_GOLEM().getDefaultInstance();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BaseGolemEntity.createGolemAttributes()
                .add(Attributes.MAX_HEALTH, 35)
                .add(Attributes.ARMOR, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.31);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            GolemBasicAbilityProperties.builder()
                    .basicUpgradeSize(1)
                    .carryPower(32)
                    .regenDelay(125)
                    .strength(4)
                    .build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }
}
