package thaumcraft.common.entities.projectile;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEmber extends EntityThrowable implements IEntityAdditionalSpawnData {
   public int duration = 20;
   public int firey = 0;
   public float damage = 1.0F;

   public EntityEmber(World par1World) {
      super(par1World);
   }

   public EntityEmber(World par1World, EntityLivingBase par2EntityLiving, float scatter) {
      super(par1World, par2EntityLiving);
      this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), scatter);
   }

   protected float getGravityVelocity() {
      return 0.0F;
   }

   protected float func_70182_d() {
      return 1.0F;
   }

   public void onUpdate() {
      if (this.ticksExisted > this.duration) {
         this.setDead();
      }

      if (this.duration <= 20) {
         this.motionX *= 0.95;
         this.motionY *= 0.95;
         this.motionZ *= 0.95;
      } else {
         this.motionX *= 0.975;
         this.motionY *= 0.975;
         this.motionZ *= 0.975;
      }

      if (this.onGround) {
         this.motionX *= 0.66;
         this.motionY *= 0.66;
         this.motionZ *= 0.66;
      }

      super.onUpdate();
   }

   public void writeSpawnData(ByteBuf data) {
      data.writeByte(this.duration);
   }

   public void readSpawnData(ByteBuf data) {
      this.duration = data.readByte();
   }

   protected void onImpact(MovingObjectPosition mop) {
      if (!this.worldObj.isRemote) {
         if (mop.entityHit != null) {
            if (!mop.entityHit.isImmuneToFire() && mop.entityHit.attackEntityFrom((new EntityDamageSourceIndirect("fireball", this, this.getThrower())).setFireDamage(), this.damage)) {
               mop.entityHit.setFire(3 + this.firey);
            }
         } else if (this.rand.nextFloat() < 0.025F * (float)this.firey) {
            int i = mop.blockX;
            int j = mop.blockY;
            int k = mop.blockZ;
            switch (mop.sideHit) {
               case 0:
                  --j;
                  break;
               case 1:
                  ++j;
                  break;
               case 2:
                  --k;
                  break;
               case 3:
                  ++k;
                  break;
               case 4:
                  --i;
                  break;
               case 5:
                  ++i;
            }

            if (this.worldObj.isAirBlock(i, j, k)) {
               this.worldObj.setBlock(i, j, k, Blocks.fire);
            }
         }
      }

      this.setDead();
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setFloat("damage", this.damage);
      par1NBTTagCompound.setInteger("firey", this.firey);
      par1NBTTagCompound.setInteger("duration", this.duration);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.damage = par1NBTTagCompound.getFloat("damage");
      this.firey = par1NBTTagCompound.getInteger("firey");
      this.duration = par1NBTTagCompound.getInteger("duration");
   }

   public boolean canBeCollidedWith() {
       return super.canBeCollidedWith();
   }

   public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
      return false;
   }
}
