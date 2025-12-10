package thaumcraft.common.entities.monster;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.combat.AICultistHurtByTarget;
import thaumcraft.common.entities.ai.combat.AILongRangeAttack;
import thaumcraft.common.entities.ai.misc.AIAltarFocus;
import thaumcraft.common.entities.projectile.EntityGolemOrb;

public class EntityCultistCleric extends EntityCultist implements IRangedAttackMob, IEntityAdditionalSpawnData {
   public EntityCultistCleric(World p_i1745_1_) {
      super(p_i1745_1_);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new AIAltarFocus(this));
      this.tasks.addTask(2, new AILongRangeAttack(this, 2.0F, 1.0F, 20, 40, 24.0F));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityLivingBase.class, 1.0F, false));
      this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
      this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
      this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 0.8));
      this.tasks.addTask(7, new EntityAIWander(this, 0.8));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, Player.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new AICultistHurtByTarget(this, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, Player.class, 0, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0F);
   }

   protected void addRandomArmor() {
      this.setCurrentItemOrArmor(4, new ItemStack(ConfigItems.itemHelmetCultistRobe));
      this.setCurrentItemOrArmor(3, new ItemStack(ConfigItems.itemChestCultistRobe));
      this.setCurrentItemOrArmor(2, new ItemStack(ConfigItems.itemLegsCultistRobe));
      if (this.rand.nextFloat() < (this.level().difficultySetting == Difficulty.HARD ? 0.3F : 0.1F)) {
         this.setCurrentItemOrArmor(1, new ItemStack(ConfigItems.itemBootsCultist));
      }

   }

   public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) {
      double d0 = entitylivingbase.posX - this.posX;
      double d1 = entitylivingbase.boundingBox.minY + (double)(entitylivingbase.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
      double d2 = entitylivingbase.posZ - this.posZ;
      this.swingItem();
      if (this.rand.nextFloat() > 0.66F) {
         EntityGolemOrb blast = new EntityGolemOrb(this.level(), this, entitylivingbase, true);
         blast.posX += blast.motionX / (double)2.0F;
         blast.posZ += blast.motionZ / (double)2.0F;
         blast.setPosition(blast.posX, blast.posY, blast.posZ);
         blast.setThrowableHeading(d0, d1 + (double)2.0F, d2, 0.66F, 3.0F);
         this.playSound("thaumcraft:egattack", 1.0F, 1.0F + this.rand.nextFloat() * 0.1F);
         this.level().spawnEntityInWorld(blast);
      } else {
         float f1 = MathHelper.sqrt_float(f) * 0.5F;
         this.level().playAuxSFXAtEntity(null, 1009, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

         for(int i = 0; i < 3; ++i) {
            EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.level(), this, d0 + this.rand.nextGaussian() * (double)f1, d1, d2 + this.rand.nextGaussian() * (double)f1);
            entitysmallfireball.posY = this.posY + (double)(this.height / 2.0F) + (double)0.5F;
            this.level().spawnEntityInWorld(entitysmallfireball);
         }
      }

   }

   protected boolean canDespawn() {
      return !this.getIsRitualist();
   }

   public void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte) 0);
   }

   public boolean getIsRitualist() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void setIsRitualist(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -2));
      }

   }

   public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
      if (this.isEntityInvulnerable()) {
         return false;
      } else {
         this.setIsRitualist(false);
         return super.attackEntityFrom(p_70097_1_, p_70097_2_);
      }
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.dataWatcher.updateObject(16, par1NBTTagCompound.getByte("Flags"));
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("Flags", this.dataWatcher.getWatchableObjectByte(16));
   }

   public void writeSpawnData(ByteBuf data) {
      data.writeInt(this.getHomePosition().posX);
      data.writeInt(this.getHomePosition().posY);
      data.writeInt(this.getHomePosition().posZ);
   }

   public void readSpawnData(ByteBuf data) {
      this.setHomeArea(data.readInt(), data.readInt(), data.readInt(), 8);
   }

   public void onUpdate() {
      super.onUpdate();
      if ((Platform.getEnvironment() == Env.CLIENT) && this.getIsRitualist()) {
         double d0 = (double)this.getHomePosition().posX + (double)0.5F - this.posX;
         double d1 = (double)this.getHomePosition().posY + (double)1.5F - (this.posY + (double)this.getEyeHeight());
         double d2 = (double)this.getHomePosition().posZ + (double)0.5F - this.posZ;
         double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
         float f = (float)(Math.atan2(d2, d0) * (double)180.0F / Math.PI) - 90.0F;
         float f1 = (float)(-(Math.atan2(d1, d3) * (double)180.0F / Math.PI));
         this.rotationPitch = this.updateRotation(this.rotationPitch, f1, 10.0F);
         this.rotationYawHead = this.updateRotation(this.rotationYawHead, f, (float)this.getVerticalFaceSpeed());
      }

   }

   private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_) {
      float f3 = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);
      if (f3 > p_75652_3_) {
         f3 = p_75652_3_;
      }

      if (f3 < -p_75652_3_) {
         f3 = -p_75652_3_;
      }

      return p_75652_1_ + f3;
   }

   protected String getLivingSound() {
      return "thaumcraft:chant";
   }

   public int getTalkInterval() {
      return 500;
   }
}
