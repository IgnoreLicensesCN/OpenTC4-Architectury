package thaumcraft.common.entities.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.ThaumcraftEntities;

public class RedGolemOrbEntity extends GolemOrbEntity {
    public RedGolemOrbEntity(EntityType<? extends RedGolemOrbEntity> entityType, LivingEntity shooter, LivingEntity target, Level level){
        super(entityType, shooter,target, level);
        this.target = target;
    }
    public RedGolemOrbEntity(LivingEntity shooter, LivingEntity target, Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.RED_GOLEM_ORB(),shooter,target, level);
    }

    public RedGolemOrbEntity(EntityType<? extends RedGolemOrbEntity> entityType, Level level) {
        super(entityType, level);
    }
    public RedGolemOrbEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.RED_GOLEM_ORB(), level);
    }

    @Override
    protected float getDamageMultiplier() {
        return 1;
    }

    @Override
    protected int getMaxTickCount() {
        return 240;
    }
}
