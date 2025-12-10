package thaumcraft.common.entities.projectile;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;

public class EntityFrostShard extends EntityThrowable implements IEntityAdditionalSpawnData {
   public double bounce = 0.5F;
   public int bounceLimit = 3;
   public boolean fragile = false;

   public EntityFrostShard(World par1World) {
      super(par1World);
   }

   public EntityFrostShard(World par1World, EntityLivingBase par2EntityLiving, float scatter) {
      super(par1World, par2EntityLiving);
      this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), scatter);
   }

   protected float getGravityVelocity() {
      return this.fragile ? 0.015F : 0.05F;
   }

   public void writeSpawnData(ByteBuf data) {
      data.writeDouble(this.bounce);
      data.writeInt(this.bounceLimit);
      data.writeBoolean(this.fragile);
   }

   public void readSpawnData(ByteBuf data) {
      this.bounce = data.readDouble();
      this.bounceLimit = data.readInt();
      this.fragile = data.readBoolean();
   }

   protected void onImpact(MovingObjectPosition mop) {
      if (mop.entityHit != null) {
         int ox = MathHelper.floor_double(this.posX) - MathHelper.floor_double(mop.entityHit.posX);
         int oy = MathHelper.floor_double(this.posY) - MathHelper.floor_double(mop.entityHit.posY);
         int oz = MathHelper.floor_double(this.posZ) - MathHelper.floor_double(mop.entityHit.posZ);
         if (oz != 0) {
            this.motionZ *= -1.0F;
         }

         if (ox != 0) {
            this.motionX *= -1.0F;
         }

         if (oy != 0) {
            this.motionY *= -0.9;
         }

         this.motionX *= 0.66;
         this.motionY *= 0.66;
         this.motionZ *= 0.66;

         for(int a = 0; (float)a < this.getDamage(); ++a) {
            this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(ConfigBlocks.blockCustomOre) + "_15", this.posX, this.posY, this.posZ, (double)4.0F * ((double)this.rand.nextFloat() - (double)0.5F), 0.5F, ((double)this.rand.nextFloat() - (double)0.5F) * (double)4.0F);
         }
      } else if (mop.typeOfHit == MovingObjectType.BLOCK) {
         ForgeDirection dir = ForgeDirection.getOrientation(mop.sideHit);
         if (dir.offsetZ != 0) {
            this.motionZ *= -1.0F;
         }

         if (dir.offsetX != 0) {
            this.motionX *= -1.0F;
         }

         if (dir.offsetY != 0) {
            this.motionY *= -0.9;
         }

         Block bhit = this.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);

         try {
            this.playSound(bhit.stepSound.getBreakSound(), 0.3F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
         } catch (Exception ignored) {
         }

         for(int a = 0; (float)a < this.getDamage(); ++a) {
            this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(bhit) + "_" + this.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ), this.posX, this.posY, this.posZ, (double)4.0F * ((double)this.rand.nextFloat() - (double)0.5F), 0.5F, ((double)this.rand.nextFloat() - (double)0.5F) * (double)4.0F);
         }
      }

      this.motionX *= this.bounce;
      this.motionY *= this.bounce;
      this.motionZ *= this.bounce;
      float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
      this.posX -= this.motionX / (double)var20 * (double)0.05F;
      this.posY -= this.motionY / (double)var20 * (double)0.05F;
      this.posZ -= this.motionZ / (double)var20 * (double)0.05F;
      this.setBeenAttacked();
      if (!this.worldObj.isRemote && mop.entityHit != null) {
         double mx = mop.entityHit.motionX;
         double my = mop.entityHit.motionY;
         double mz = mop.entityHit.motionZ;
         mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.getDamage());
         if (mop.entityHit instanceof EntityLivingBase && this.getFrosty() > 0) {
            ((EntityLivingBase)mop.entityHit).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, this.getFrosty() - 1));
         }

         if (this.fragile) {
            mop.entityHit.hurtResistantTime = 0;
            this.setDead();
            this.playSound(Blocks.ice.stepSound.getBreakSound(), 0.3F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            mop.entityHit.motionX = mx + (mop.entityHit.motionX - mx) / (double)10.0F;
            mop.entityHit.motionY = my + (mop.entityHit.motionY - my) / (double)10.0F;
            mop.entityHit.motionZ = mz + (mop.entityHit.motionZ - mz) / (double)10.0F;
         }
      }

      if (this.bounceLimit-- <= 0) {
         this.setDead();
         this.playSound(Blocks.ice.stepSound.getBreakSound(), 0.3F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

         for(int a = 0; (float)a < 8.0F * this.getDamage(); ++a) {
            this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(ConfigBlocks.blockCustomOre) + "_15", this.posX, this.posY, this.posZ, (double)4.0F * ((double)this.rand.nextFloat() - (double)0.5F), 0.5F, ((double)this.rand.nextFloat() - (double)0.5F) * (double)4.0F);
         }
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if (this.worldObj.isRemote && this.getFrosty() > 0) {
         float s = this.getDamage() / 10.0F;

         for(int a = 0; a < this.getFrosty(); ++a) {
            Thaumcraft.proxy.sparkle((float)this.posX - s + this.rand.nextFloat() * s * 2.0F, (float)this.posY - s + this.rand.nextFloat() * s * 2.0F, (float)this.posZ - s + this.rand.nextFloat() * s * 2.0F, 0.4F, 6, 0.005F);
         }
      }

      float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * (double)180.0F / Math.PI);

      for(this.rotationPitch = (float)(Math.atan2(this.motionY, var20) * (double)180.0F / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
      }

      while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setFloat("damage", this.getDamage());
      par1NBTTagCompound.setBoolean("fragile", this.fragile);
      par1NBTTagCompound.setInteger("frost", this.getFrosty());
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setDamage(par1NBTTagCompound.getFloat("damage"));
      this.fragile = par1NBTTagCompound.getBoolean("fragile");
      this.setFrosty(par1NBTTagCompound.getInteger("frost"));
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, 0.0F);
      this.dataWatcher.addObject(17, (byte) 0);
   }

   public void setDamage(float par1) {
      this.dataWatcher.updateObject(16, par1);
      this.setSize(0.15F + par1 * 0.15F, 0.15F + par1 * 0.15F);
   }

   public float getDamage() {
      return this.dataWatcher.getWatchableObjectFloat(16);
   }

   public void setFrosty(int frosty) {
      this.dataWatcher.updateObject(17, (byte)frosty);
   }

   public int getFrosty() {
      return this.dataWatcher.getWatchableObjectByte(17);
   }
}
