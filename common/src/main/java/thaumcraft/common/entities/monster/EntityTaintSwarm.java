package thaumcraft.common.entities.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.utils.EntityUtils;

public class EntityTaintSwarm extends EntityMob implements ITaintedMob {
   private ChunkCoordinates currentFlightTarget;
   public int damBonus = 0;
   public ArrayList swarm = new ArrayList<>();

   public EntityTaintSwarm(World par1World) {
      super(par1World);
      this.setSize(2.0F, 2.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte) 0);
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   protected boolean canDespawn() {
       return super.canDespawn();
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   protected float getSoundVolume() {
      return 0.1F;
   }

   protected String getLivingSound() {
      return "";
   }

   protected String getHurtSound() {
      return "thaumcraft:swarmattack";
   }

   protected String getDeathSound() {
      return "thaumcraft:swarmattack";
   }

   public boolean canBePushed() {
      return false;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2 + this.damBonus);
   }

   public boolean getIsSummoned() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
   }

   public void setIsSummoned(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 2));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -4));
      }

   }

   protected boolean isAIEnabled() {
       return super.isAIEnabled();
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
   }

   public void onUpdate() {
      super.onUpdate();
      this.motionY *= 0.6F;
      if (this.worldObj.isRemote) {
         for(int a = 0; a < this.swarm.size(); ++a) {
            if (this.swarm.get(a) == null || ((Entity)this.swarm.get(a)).isDead) {
               this.swarm.remove(a);
               break;
            }
         }

         if (this.swarm.size() < Math.max(Thaumcraft.proxy.particleCount(25), 10)) {
            this.swarm.add(Thaumcraft.proxy.swarmParticleFX(this.worldObj, this, 0.22F, 15.0F, 0.08F));
         }
      }

   }

   protected void updateEntityActionState() {
      super.updateEntityActionState();
      if (this.entityToAttack == null) {
         if (this.getIsSummoned()) {
            this.attackEntityFrom(DamageSource.generic, 5.0F);
         }

         if (this.currentFlightTarget != null && (!this.worldObj.isAirBlock(this.currentFlightTarget.posX, this.currentFlightTarget.posY, this.currentFlightTarget.posZ) || this.currentFlightTarget.posY < 1 || this.currentFlightTarget.posY > this.worldObj.getHeightValue(this.currentFlightTarget.posX, this.currentFlightTarget.posZ) + 8 || this.worldObj.getBiomeGenForCoords(this.currentFlightTarget.posX, this.currentFlightTarget.posZ).biomeID != Config.biomeTaintID)) {
            this.currentFlightTarget = null;
         }

         if (this.currentFlightTarget == null || this.rand.nextInt(30) == 0 || this.currentFlightTarget.getDistanceSquared((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0F) {
            this.currentFlightTarget = new ChunkCoordinates((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
         }

         double var1 = (double)this.currentFlightTarget.posX + (double)0.5F - this.posX;
         double var3 = (double)this.currentFlightTarget.posY + 0.1 - this.posY;
         double var5 = (double)this.currentFlightTarget.posZ + (double)0.5F - this.posZ;
         this.motionX += (Math.signum(var1) * (double)0.5F - this.motionX) * 0.015000000014901161;
         this.motionY += (Math.signum(var3) * (double)0.7F - this.motionY) * (double)0.1F;
         this.motionZ += (Math.signum(var5) * (double)0.5F - this.motionZ) * 0.015000000014901161;
         float var7 = (float)(Math.atan2(this.motionZ, this.motionX) * (double)180.0F / Math.PI) - 90.0F;
         float var8 = MathHelper.wrapAngleTo180_float(var7 - this.rotationYaw);
         this.moveForward = 0.1F;
         this.rotationYaw += var8;
      } else if (this.entityToAttack != null) {
         double var1 = this.entityToAttack.posX - this.posX;
         double var3 = this.entityToAttack.posY + (double)this.entityToAttack.getEyeHeight() - this.posY;
         double var5 = this.entityToAttack.posZ - this.posZ;
         this.motionX += (Math.signum(var1) * (double)0.5F - this.motionX) * 0.025000000149011613;
         this.motionY += (Math.signum(var3) * (double)0.7F - this.motionY) * (double)0.1F;
         this.motionZ += (Math.signum(var5) * (double)0.5F - this.motionZ) * 0.02500000001490116;
         float var7 = (float)(Math.atan2(this.motionZ, this.motionX) * (double)180.0F / Math.PI) - 90.0F;
         float var8 = MathHelper.wrapAngleTo180_float(var7 - this.rotationYaw);
         this.moveForward = 0.1F;
         this.rotationYaw += var8;
      }

      if (this.entityToAttack instanceof EntityPlayer && ((EntityPlayer)this.entityToAttack).capabilities.disableDamage) {
         this.entityToAttack = null;
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
      return !this.isEntityInvulnerable() && super.attackEntityFrom(par1DamageSource, par2);
   }

   protected void attackEntity(Entity par1Entity, float par2) {
      if (this.attackTime <= 0 && par2 < 3.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
         if (this.getIsSummoned()) {
            EntityUtils.setRecentlyHit((EntityLivingBase)par1Entity, 100);
         }

         this.attackTime = 10 + this.rand.nextInt(5);
         double mx = par1Entity.motionX;
         double my = par1Entity.motionY;
         double mz = par1Entity.motionZ;
         if (this.attackEntityAsMob(par1Entity) && !this.worldObj.isRemote && par1Entity instanceof EntityLivingBase) {
            ((EntityLivingBase)par1Entity).addPotionEffect(new PotionEffect(Potion.weakness.id, 100, 0));
         }

         par1Entity.isAirBorne = false;
         par1Entity.motionX = mx;
         par1Entity.motionY = my;
         par1Entity.motionZ = mz;
         this.worldObj.playSoundAtEntity(this, "thaumcraft:swarmattack", 0.3F, 0.9F + this.worldObj.rand.nextFloat() * 0.2F);
      }

   }

   protected Entity findPlayerToAttack() {
      double var1 = 12.0F;
      return this.getIsSummoned() ? null : this.worldObj.getClosestVulnerablePlayerToEntity(this, var1);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.dataWatcher.updateObject(16, par1NBTTagCompound.getByte("Flags"));
      this.damBonus = par1NBTTagCompound.getByte("damBonus");
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("Flags", this.dataWatcher.getWatchableObjectByte(16));
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

   protected boolean isValidLightLevel() {
      return true;
   }

   protected Item getDropItem() {
      return ConfigItems.itemResource;
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.worldObj.rand.nextBoolean()) {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 11), this.height / 2.0F);
      }

   }
}
