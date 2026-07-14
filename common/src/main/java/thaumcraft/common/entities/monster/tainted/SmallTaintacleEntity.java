package thaumcraft.common.entities.monster.tainted;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallTaintacleEntity extends TaintacleEntity {
    public SmallTaintacleEntity(Level level) {
        super(level);
    }

    public SmallTaintacleEntity(EntityType<? extends TaintacleEntity> entityType, Level level) {
        super(entityType, level);
    }
    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8).add(Attributes.ATTACK_DAMAGE,2);
    }

    protected int lifetime = 200;

    @Override
    public void tick() {
        super.tick();
        if (lifetime-- <= 0) {
            hurt(level().damageSources().magic(),10);
        }
    }

    @Override
    protected @Nullable EntityType<?> getSmallTaintacleType() {
        return null;
    }
}
