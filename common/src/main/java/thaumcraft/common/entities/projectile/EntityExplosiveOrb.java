package thaumcraft.common.entities.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.Vec3;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;

public class EntityExplosiveOrb extends EntityThrowable {
   public float strength = 1.0F;
   public boolean onFire = false;

   public EntityExplosiveOrb(Level par1World) {
      super(par1World);
   }

   public EntityExplosiveOrb(Level par1World, EntityLivingBase par2EntityLiving) {
      super(par1World, par2EntityLiving);
   }

   protected float getGravityVelocity() {
      return 0.01F;
   }

   protected void onImpact(HitResult mop) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (mop.entityHit != null) {
            mop.entityHit.attackEntityFrom(causeFireballDamage(this, this.getThrower()), this.strength * 1.5F);
         }

         this.level().newExplosion(null, this.posX, this.posY, this.posZ, this.strength, this.onFire, false);
         this.setDead();
      }

      this.setDead();
   }

   public static DamageSource causeFireballDamage(EntityExplosiveOrb p_76362_0_, Entity p_76362_1_) {
      return p_76362_1_ == null ? (new EntityDamageSourceIndirect("onFire", p_76362_0_, p_76362_0_)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", p_76362_0_, p_76362_1_)).setFireDamage().setProjectile();
   }

   public float getShadowSize() {
      return 0.1F;
   }

   public void onUpdate() {
      super.onUpdate();
      if ((Platform.getEnvironment() == Env.CLIENT)) {
         Thaumcraft.proxy.drawGenericParticles(this.level(), this.prevPosX + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F), this.prevPosY + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F), this.prevPosZ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.8F, false, 151, 9, 1, 7 + this.rand.nextInt(5), 0, 2.0F + this.rand.nextFloat());
      }

      if (this.ticksExisted > 500) {
         this.setDead();
      }

   }

   public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
      if (this.isEntityInvulnerable()) {
         return false;
      } else {
         this.setBeenAttacked();
         if (p_70097_1_.getEntity() != null) {
            Vec3 vec3 = p_70097_1_.getEntity().getLookVec();
            if (vec3 != null) {
               this.motionX = vec3.xCoord;
               this.motionY = vec3.yCoord;
               this.motionZ = vec3.zCoord;
               this.motionX *= 0.9;
               this.motionY *= 0.9;
               this.motionZ *= 0.9;
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
