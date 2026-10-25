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
import thaumcraft.common.entities.golems.typedgolems.StoneGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class AdvancedStoneGolemEntity extends AdvancedGolemEntity {

    public AdvancedStoneGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_STONE_GOLEM(),level);
    }

    public AdvancedStoneGolemEntity(EntityType<AdvancedStoneGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.ADVANCED_STONE_GOLEM().getDefaultInstance();
    }


    public static AttributeSupplier.Builder createAttributes() {
        return StoneGolemEntity.createGolemAttributes().add(Attributes.MOVEMENT_SPEED, 0.32 * 1.1);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            StoneGolemEntity.ABILITY_PROPERTIES.toBuilder()
                    .attackDelay(StoneGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(StoneGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();

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
