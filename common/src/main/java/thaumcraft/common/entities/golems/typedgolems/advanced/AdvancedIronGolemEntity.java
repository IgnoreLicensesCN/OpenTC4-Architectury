package thaumcraft.common.entities.golems.typedgolems.advanced;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.AdvancedGolemEntity;
import thaumcraft.common.entities.golems.properties.GolemBasicAbilityProperties;
import thaumcraft.common.entities.golems.typedgolems.IronGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class AdvancedIronGolemEntity extends AdvancedGolemEntity {

    public AdvancedIronGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_IRON_GOLEM(),level);
    }

    public AdvancedIronGolemEntity(EntityType<AdvancedIronGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.ADVANCED_IRON_GOLEM().getDefaultInstance();
    }


    public static AttributeSupplier.Builder createAttributes() {
        return IronGolemEntity.createGolemAttributes().add(Attributes.MOVEMENT_SPEED, 0.31 * 1.1);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            IronGolemEntity.ABILITY_PROPERTIES.toBuilder()
                    .attackDelay(IronGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(IronGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }

    @Override
    public double getAttributeValue(Attribute attribute) {
        var result = super.getAttributeValue(attribute);
        if (this.isInWater()){
            result *= 2;
        }
        return result;
    }
}
