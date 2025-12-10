package thaumcraft.common.entities.projectile;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.HitResult.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;

public class EntityGolemOrb extends EntityThrowable implements IEntityAdditionalSpawnData {
   int targetID = 0;
   EntityLivingBase target;
   public boolean red = false;

   public EntityGolemOrb(Level par1World) {
      super(par1World);
   }

   public EntityGolemOrb(Level par1World, EntityLivingBase par2EntityLiving, EntityLivingBase t, boolean r) {
      super(par1World, par2EntityLiving);
      this.target = t;
      this.red = r;
   }

   protected float getGravityVelocity() {
      return 0.0F;
   }

   public void writeSpawnData(ByteBuf data) {
      int id = -1;
      if (this.target != null) {
         id = this.target.getEntityId();
      }

      data.writeInt(id);
      data.writeBoolean(this.red);
   }

   public void readSpawnData(ByteBuf data) {
      int id = data.readInt();

      try {
         if (id >= 0) {
            this.target = (EntityLivingBase)this.level().getEntityByID(id);
         }
      } catch (Exception ignored) {
      }

      this.red = data.readBoolean();
   }

   protected void onImpact(HitResult mop) {
      if (Platform.getEnvironment() != Env.CLIENT && this.getThrower() != null && mop.typeOfHit == MovingObjectType.ENTITY) {
         mop.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), (float)this.getThrower().getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue() * (this.red ? 1.0F : 0.6F));
      }

      this.level().playSoundEffect(this.posX, this.posY, this.posZ, "thaumcraft:shock", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
      Thaumcraft.proxy.burst(this.level(), this.posX, this.posY, this.posZ, 1.0F);
      this.setDead();
   }

   public float getShadowSize() {
      return 0.1F;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.ticksExisted > (this.red ? 240 : 160)) {
         this.setDead();
      }

      if (this.target != null) {
         double d = this.getDistanceSqToEntity(this.target);
         double dx = this.target.posX - this.posX;
         double dy = this.target.boundingBox.minY + (double)this.target.height * 0.6 - this.posY;
         double dz = this.target.posZ - this.posZ;
         double d13 = 0.2;
         dx /= d;
         dy /= d;
         dz /= d;
         this.motionX += dx * d13;
         this.motionY += dy * d13;
         this.motionZ += dz * d13;
         this.motionX = MathHelper.clamp_float((float)this.motionX, -0.25F, 0.25F);
         this.motionY = MathHelper.clamp_float((float)this.motionY, -0.25F, 0.25F);
         this.motionZ = MathHelper.clamp_float((float)this.motionZ, -0.25F, 0.25F);
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
               this.level().playSoundAtEntity(this, "thaumcraft:zap", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
