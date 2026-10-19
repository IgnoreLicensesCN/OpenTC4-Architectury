package thaumcraft.common.entities.golems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public abstract class AdvancedGolemEntity extends BaseGolemEntity {
    public AdvancedGolemEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    public int getHomeRange() {
        int range = super.getHomeRange();
        range += (int) Math.max(range * 0.2F, 2.0F);
        return range;
    }

    @Override
    public int getAttackDelay() {
        return Math.max(super.getAttackDelay() -2, 1);
    }
}
