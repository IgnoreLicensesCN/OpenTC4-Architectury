package thaumcraft.common.entities.golems.typedgolems.advanced;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.AdvancedGolemEntity;
import thaumcraft.common.entities.golems.properties.GolemBasicAbilityProperties;
import thaumcraft.common.entities.golems.typedgolems.WoodGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class AdvancedWoodGolemEntity extends AdvancedGolemEntity {

    public AdvancedWoodGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ADVANCED_WOOD_GOLEM(),level);
    }

    public AdvancedWoodGolemEntity(EntityType<AdvancedWoodGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.ADVANCED_WOOD_GOLEM().getDefaultInstance();
    }


    public static AttributeSupplier.Builder createAttributes() {
        return WoodGolemEntity.createGolemAttributes().add(Attributes.MOVEMENT_SPEED, 0.35 * 1.1);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            WoodGolemEntity.ABILITY_PROPERTIES.toBuilder()
                    .attackDelay(WoodGolemEntity.ABILITY_PROPERTIES.attackDelay -2)
                    .basicUpgradeSize(WoodGolemEntity.ABILITY_PROPERTIES.basicUpgradeSize + 1).build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }

}
