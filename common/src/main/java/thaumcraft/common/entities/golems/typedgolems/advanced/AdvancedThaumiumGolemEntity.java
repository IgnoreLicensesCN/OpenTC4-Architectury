package thaumcraft.common.entities.golems.typedgolems.advanced;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.AdvancedGolemEntity;
import thaumcraft.common.entities.golems.properties.GolemBasicAbilityProperties;
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
            ThaumiumGolemEntity.ABILITY_PROPERTIES.toBuilder()
                    .attackDelay(ThaumiumGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(ThaumiumGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource damageSource, float f) {
        if (damageSource.is(DamageTypes.MAGIC) && !Float.isNaN(f)){
            f /= 2;
        }
        return super.getDamageAfterMagicAbsorb(damageSource, f);
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
