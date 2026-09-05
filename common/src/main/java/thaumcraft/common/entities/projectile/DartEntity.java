package thaumcraft.common.entities.projectile;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.entities.ThaumcraftEntities;

public class DartEntity extends AbstractArrow {
    public DartEntity(LivingEntity livingEntity,LivingEntity victim, Level level,float par4, float par5){
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.DART(),livingEntity,victim,level,par4,par5);
    }
    public DartEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.DART(), level);
    }
    public DartEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public DartEntity(EntityType<? extends AbstractArrow> entityType, double d, double e, double f, Level level) {
        super(entityType, d, e, f, level);
    }

    public DartEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity shooter,LivingEntity victim, Level level,float par4, float par5){
        super(entityType, shooter, level);
//        this.renderDistanceWeight = 10.0F;
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeHeight() - (double)0.1F, shooter.getZ());
        double var6 = victim.getX() - shooter.getX();
        double var8 = victim.getY() + (double)victim.getEyeHeight() - (double)0.7F - this.getY();
        double var10 = victim.getZ() - shooter.getZ();
        double var12 = MathHelper.sqrt_double(var6 * var6 + var10 * var10);
        if (var12 >= 1.0E-7) {
            float var14 = (float)(Math.atan2(var10, var6) * (double)180.0F / Math.PI) - 90.0F;
            float var15 = (float)(-(Math.atan2(var8, var12) * (double)180.0F / Math.PI));
            double var16 = var6 / var12;
            double var18 = var10 / var12;
            this.setPos(shooter.getX() + var16 / (double)5.0F,
                    this.getY(), shooter.getZ() + var18 / (double)5.0F);
            this.setXRot(var14);
            this.setYRot(var15);
            float var20 = (float)var12 * 0.2F;
            this.shoot(var6, var8 + (double)var20, var10, par4, par5);
        }
    }
    public DartEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity livingEntity, Level level) {
        super(entityType, livingEntity, level);
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }


    private boolean first = true;

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && first) {
            first = false;
            var motion = getDeltaMovement();
            for(int a = 0; a < 5; ++a) {
                this.level().addParticle(
                        ParticleTypes.SMOKE,
                        this.getX() - motion.x / (double)1.5F,
                        this.getY() - motion.y / (double)1.5F,
                        this.getZ() - motion.z / (double)1.5F,
                        motion.x / (double)9.0F + random.nextGaussian() * 0.01,
                        motion.y / (double)9.0F + random.nextGaussian() * 0.01,
                        motion.z / (double)9.0F + random.nextGaussian() * 0.01
                );
            }
        }
    }
}
