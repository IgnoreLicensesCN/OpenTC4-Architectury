package thaumcraft.common.entities.golems.typedgolems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class StoneGolemEntity extends BaseGolemEntity {

    public StoneGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.STONE_GOLEM(),level);
    }

    public StoneGolemEntity(EntityType<StoneGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public ItemStack getCollapseToBasicStack() {
        return ThaumcraftItemInstances.STONE_GOLEM().getDefaultInstance();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return BaseGolemEntity.createGolemAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.MOVEMENT_SPEED, 0.32);
    }

    public static final GolemBasicAbilityProperties ABILITY_PROPERTIES =
            new GolemBasicAbilityProperties.Builder()
                    .basicUpgradeSize(1)
                    .carryPower(16)
                    .regenDelay(100)
                    .strength(3)
                    .build();

    @Override
    public GolemBasicAbilityProperties getBasicAbilityProperties() {
        return ABILITY_PROPERTIES;
    }
}
