package thaumcraft.common.entities.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.utils.EntityUtils;
import thaumcraft.common.lib.utils.Utils;

public class EntityFireBat extends EntityMob {
   private ChunkCoordinates currentFlightTarget;
   public EntityPlayer owner = null;
   public int damBonus = 0;

   public EntityFireBat(World par1World) {
      super(par1World);
      this.setSize(0.5F, 0.9F);
      this.setIsBatHanging(true);
      this.isImmuneToFire = true;
   }

   public void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte) 0);
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   protected float getSoundVolume() {
      return 0.1F;
   }

   protected float getSoundPitch() {
      return super.getSoundPitch() * 0.95F;
   }

   protected String getLivingSound() {
      return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
   }

   protected String getHurtSound() {
      return "mob.bat.hurt";
   }

   protected String getDeathSound() {
      return "mob.bat.death";
   }

   public boolean canBePushed() {
      return false;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.getIsDevil() ? (double)15.0F : (double)5.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(this.getIsSummoned() ? (double)((this.getIsDevil() ? 3 : 2) + this.damBonus) : (double)1.0F);
   }

   public boolean getIsBatHanging() {
      return Utils.getBit(this.dataWatcher.getWatchableObjectByte(16), 0);
   }

   public void setIsBatHanging(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)Utils.setBit(var2, 0));
      } else {
         this.dataWatcher.updateObject(16, (byte)Utils.clearBit(var2, 0));
      }

   }

   public boolean getIsSummoned() {
      return Utils.getBit(this.dataWatcher.getWatchableObjectByte(16), 1);
   }

   public void setIsSummoned(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)Utils.setBit(var2, 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)Utils.clearBit(var2, 1));
      }

      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(par1 ? (double)((this.getIsDevil() ? 3 : 2) + this.damBonus) : (double)1.0F);
   }

   public boolean getIsExplosive() {
      return Utils.getBit(this.dataWatcher.getWatchableObjectByte(16), 2);
   }

   public void setIsExplosive(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)Utils.setBit(var2, 2));
      } else {
         this.dataWatcher.updateObject(16, (byte)Utils.clearBit(var2, 2));
      }

   }

   public boolean getIsDevil() {
      return Utils.getBit(this.dataWatcher.getWatchableObjectByte(16), 3);
   }

   public void setIsDevil(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)Utils.setBit(var2, 3));
      } else {
         this.dataWatcher.updateObject(16, (byte)Utils.clearBit(var2, 3));
      }

      if (par1) {
         this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(this.getIsSummoned() ? (double)((par1 ? 3 : 2) + this.damBonus) : (double)1.0F);
      }

   }

   public boolean getIsVampire() {
      return Utils.getBit(this.dataWatcher.getWatchableObjectByte(16), 4);
   }

   public void setIsVampire(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)Utils.setBit(var2, 4));
      } else {
         this.dataWatcher.updateObject(16, (byte)Utils.clearBit(var2, 4));
      }

   }

   protected boolean isAIEnabled() {
       return super.isAIEnabled();
   }

   public void onLivingUpdate() {
      if (this.isWet()) {
         this.attackEntityFrom(DamageSource.drown, 1.0F);
      }

      super.onLivingUpdate();
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.worldObj.isRemote && this.getIsExplosive()) {
         Thaumcraft.proxy.drawGenericParticles(this.worldObj, this.prevPosX + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F), this.prevPosY + (double)(this.height / 2.0F) + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F), this.prevPosZ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.8F, false, 151, 9, 1, 7 + this.rand.nextInt(5), 0, 1.0F + this.rand.nextFloat() * 0.5F);
      }

      if (this.getIsBatHanging()) {
         this.motionX = this.motionY = this.motionZ = 0.0F;
         this.posY = (double)MathHelper.floor_double(this.posY) + (double)1.0F - (double)this.height;
      } else {
         this.motionY *= 0.6F;
      }

      if (this.worldObj.isRemote && !this.getIsVampire()) {
         this.worldObj.spawnParticle("smoke", this.prevPosX + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), this.prevPosY + (double)(this.height / 2.0F) + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), this.prevPosZ + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), 0.0F, 0.0F, 0.0F);
         this.worldObj.spawnParticle("flame", this.prevPosX + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), this.prevPosY + (double)(this.height / 2.0F) + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), this.prevPosZ + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F), 0.0F, 0.0F, 0.0F);
      }

   }

   protected void updateEntityActionState() {
      super.updateEntityActionState();
      if (this.getIsBatHanging()) {
         if (!this.worldObj.isBlockNormalCubeDefault(MathHelper.floor_double(this.posX), (int)this.posY + 1, MathHelper.floor_double(this.posZ), false)) {
            this.setIsBatHanging(false);
            this.worldObj.playAuxSFXAtEntity(null, 1015, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
         } else {
            if (this.rand.nextInt(200) == 0) {
               this.rotationYawHead = (float)this.rand.nextInt(360);
            }

            if (this.worldObj.getClosestPlayerToEntity(this, 4.0F) != null) {
               this.setIsBatHanging(false);
               this.worldObj.playAuxSFXAtEntity(null, 1015, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            }
         }
      } else {
         if (this.entityToAttack == null) {
            if (this.getIsSummoned()) {
               this.attackEntityFrom(DamageSource.generic, 2.0F);
            }

            if (this.currentFlightTarget != null && (!this.worldObj.isAirBlock(this.currentFlightTarget.posX, this.currentFlightTarget.posY, this.currentFlightTarget.posZ) || this.currentFlightTarget.posY < 1)) {
               this.currentFlightTarget = null;
            }

            if (this.currentFlightTarget == null || this.rand.nextInt(30) == 0 || this.currentFlightTarget.getDistanceSquared((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0F) {
               this.currentFlightTarget = new ChunkCoordinates((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }

            double var1 = (double)this.currentFlightTarget.posX + (double)0.5F - this.posX;
            double var3 = (double)this.currentFlightTarget.posY + 0.1 - this.posY;
            double var5 = (double)this.currentFlightTarget.posZ + (double)0.5F - this.posZ;
            this.motionX += (Math.signum(var1) * (double)0.5F - this.motionX) * (double)0.1F;
            this.motionY += (Math.signum(var3) * (double)0.7F - this.motionY) * (double)0.1F;
            this.motionZ += (Math.signum(var5) * (double)0.5F - this.motionZ) * (double)0.1F;
            float var7 = (float)(Math.atan2(this.motionZ, this.motionX) * (double)180.0F / Math.PI) - 90.0F;
            float var8 = MathHelper.wrapAngleTo180_float(var7 - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += var8;
            if (this.rand.nextInt(100) == 0 && this.worldObj.isBlockNormalCubeDefault(MathHelper.floor_double(this.posX), (int)this.posY + 1, MathHelper.floor_double(this.posZ), false)) {
               this.setIsBatHanging(true);
            }
         } else if (this.entityToAttack != null) {
            double var1 = this.entityToAttack.posX - this.posX;
            double var3 = this.entityToAttack.posY + (double)(this.entityToAttack.getEyeHeight() * 0.66F) - this.posY;
            double var5 = this.entityToAttack.posZ - this.posZ;
            this.motionX += (Math.signum(var1) * (double)0.5F - this.motionX) * (double)0.1F;
            this.motionY += (Math.signum(var3) * (double)0.7F - this.motionY) * (double)0.1F;
            this.motionZ += (Math.signum(var5) * (double)0.5F - this.motionZ) * (double)0.1F;
            float var7 = (float)(Math.atan2(this.motionZ, this.motionX) * (double)180.0F / Math.PI) - 90.0F;
            float var8 = MathHelper.wrapAngleTo180_float(var7 - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += var8;
         }

         if (this.entityToAttack instanceof EntityPlayer && ((EntityPlayer)this.entityToAttack).capabilities.disableDamage) {
            this.entityToAttack = null;
         }
      }

   }

   protected void updateAITasks() {
      super.updateAITasks();
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void fall(float par1) {
   }

   protected void updateFallState(double par1, boolean par3) {
   }

   public boolean doesEntityNotTriggerPressurePlate() {
      return true;
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if (!this.isEntityInvulnerable() && !par1DamageSource.isFireDamage() && !par1DamageSource.isExplosion()) {
         if (!this.worldObj.isRemote && this.getIsBatHanging()) {
            this.setIsBatHanging(false);
         }

         return super.attackEntityFrom(par1DamageSource, par2);
      } else {
         return false;
      }
   }

   protected void attackEntity(Entity par1Entity, float par2) {
      if (this.attackTime <= 0 && par2 < Math.max(2.5F, par1Entity.width * 1.1F) && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
         if (this.getIsSummoned()) {
            EntityUtils.setRecentlyHit((EntityLivingBase)par1Entity, 100);
         }

         if (this.getIsVampire()) {
            if (this.owner != null && !this.owner.isPotionActive(Potion.regeneration.id)) {
               this.owner.addPotionEffect(new PotionEffect(Potion.regeneration.id, 26, 1));
            }

            this.heal(1.0F);
         }

         this.attackTime = 20;
         if ((this.getIsExplosive() || this.worldObj.rand.nextInt(10) == 0) && !this.worldObj.isRemote && !this.getIsDevil()) {
            par1Entity.hurtResistantTime = 0;
            this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.5F + (this.getIsExplosive() ? (float)this.damBonus * 0.33F : 0.0F), false, false);
            this.setDead();
         } else if (!this.getIsVampire() && !this.worldObj.rand.nextBoolean()) {
            par1Entity.setFire(this.getIsSummoned() ? 4 : 2);
         } else {
            double mx = par1Entity.motionX;
            double my = par1Entity.motionY;
            double mz = par1Entity.motionZ;
            this.attackEntityAsMob(par1Entity);
            par1Entity.isAirBorne = false;
            par1Entity.motionX = mx;
            par1Entity.motionY = my;
            par1Entity.motionZ = mz;
         }

         this.worldObj.playSoundAtEntity(this, "mob.bat.hurt", 0.5F, 0.9F + this.worldObj.rand.nextFloat() * 0.2F);
      }

   }

   protected Entity findPlayerToAttack() {
      double var1 = 12.0F;
      return this.getIsSummoned() ? null : this.worldObj.getClosestVulnerablePlayerToEntity(this, var1);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.dataWatcher.updateObject(16, par1NBTTagCompound.getByte("BatFlags"));
      this.damBonus = par1NBTTagCompound.getByte("damBonus");
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
      par1NBTTagCompound.setByte("damBonus", (byte)this.damBonus);
   }

   public boolean getCanSpawnHere() {
      int var1 = MathHelper.floor_double(this.boundingBox.minY);
      int var2 = MathHelper.floor_double(this.posX);
      int var3 = MathHelper.floor_double(this.posZ);
      int var4 = this.worldObj.getBlockLightValue(var2, var1, var3);
      byte var5 = 7;
      return var4 <= this.rand.nextInt(var5) && super.getCanSpawnHere();
   }

   protected Item getDropItem() {
      return !this.getIsSummoned() ? Items.gunpowder : Item.getItemById(0);
   }

   protected boolean isValidLightLevel() {
      return true;
   }
}
