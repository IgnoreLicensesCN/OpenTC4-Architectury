package thaumcraft.common.entities.monster;

import net.minecraft.world.level.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;

public class EntityEldritchCrab extends EntityMob {
   public EntityEldritchCrab(Level par1World) {
      super(par1World);
      this.setSize(0.8F, 0.6F);
      this.experienceValue = 6;
      this.getNavigator().setBreakDoors(true);
      this.getNavigator().setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.63F));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityLivingBase.class, 1.0F, false));
      this.tasks.addTask(7, new EntityAIWander(this, 0.8));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, Player.class, 0, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCultist.class, 0, true));
   }

   public double getYOffset() {
      return this.isRiding() ? (double)0.5F : (double)0.0F;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.hasHelm() ? 0.275 : 0.3);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(22, (byte) 0);
   }

   public boolean canPickUpLoot() {
      return false;
   }

   public int getTotalArmorValue() {
      return this.hasHelm() ? 5 : 0;
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData livingData) {
      if (this.level().difficultySetting == Difficulty.HARD) {
         this.setHelm(true);
      } else {
         this.setHelm(this.rand.nextFloat() < 0.33F);
      }

      if (livingData == null) {
         livingData = new EntitySpider.GroupData();
         if (this.level().difficultySetting == Difficulty.HARD && this.level().rand.nextFloat() < 0.1F * this.level().func_147462_b(this.posX, this.posY, this.posZ)) {
            ((EntitySpider.GroupData)livingData).func_111104_a(this.level().rand);
         }
      }

      if (livingData instanceof EntitySpider.GroupData) {
         int i = ((EntitySpider.GroupData)livingData).field_111105_a;
         if (i > 0 && Potion.potionTypes[i] != null) {
            this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
         }
      }

      return super.onSpawnWithEgg(livingData);
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public boolean hasHelm() {
      return (this.dataWatcher.getWatchableObjectByte(22) & 1) != 0;
   }

   public void setHelm(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(22);
      if (par1) {
         this.dataWatcher.updateObject(22, (byte)(var2 | 1));
      } else {
         this.dataWatcher.updateObject(22, (byte)(var2 & -2));
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if (this.ticksExisted < 20) {
         this.fallDistance = 0.0F;
      }

      if (this.ridingEntity == null && this.getAITarget() != null && this.getAITarget().riddenByEntity == null && !this.onGround && !this.hasHelm() && !this.getAITarget().isDead && this.posY - this.getAITarget().posY >= (double)(this.getAITarget().height / 2.0F) && this.getDistanceSqToEntity(this.getAITarget()) < (double)4.0F) {
         this.mountEntity(this.getAITarget());
      }

      if (Platform.getEnvironment() != Env.CLIENT && this.ridingEntity != null && this.attackTime <= 0) {
         this.attackTime = 10 + this.rand.nextInt(10);
         this.attackEntityAsMob(this.ridingEntity);
         if (this.ridingEntity != null && (double)this.rand.nextFloat() < 0.2) {
            this.dismountEntity(this.ridingEntity);
         }
      }

   }

   protected Item getDropItem() {
       return super.getDropItem();
   }

   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
      super.dropFewItems(p_70628_1_, p_70628_2_);
      if (p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0)) {
         this.dropItem(Items.ender_pearl, 1);
      }

   }

   public boolean attackEntityAsMob(Entity p_70652_1_) {
      if (super.attackEntityAsMob(p_70652_1_)) {
         this.playSound("thaumcraft:crabclaw", 1.0F, 0.9F + this.level().rand.nextFloat() * 0.2F);
         return true;
      } else {
         return false;
      }
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      boolean b = super.attackEntityFrom(source, damage);
      if (this.hasHelm() && this.getHealth() / this.getMaxHealth() <= 0.5F) {
         this.setHelm(false);
         this.renderBrokenItemStack(new ItemStack(ConfigItems.itemChestCultistPlate));
         this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
      }

      return b;
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.dataWatcher.updateObject(22, par1NBTTagCompound.getByte("Flags"));
      if (!this.hasHelm()) {
         this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
      }

   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("Flags", this.dataWatcher.getWatchableObjectByte(22));
   }

   public int getTalkInterval() {
      return 160;
   }

   protected String getLivingSound() {
      return "thaumcraft:crabtalk";
   }

   protected String getHurtSound() {
       return super.getHurtSound();
   }

   protected String getDeathSound() {
      return "thaumcraft:crabdeath";
   }

   protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
      this.playSound("mob.spider.step", 0.15F, 1.0F);
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   public boolean isPotionApplicable(PotionEffect p_70687_1_) {
      return p_70687_1_.getPotionID() != Potion.poison.id && super.isPotionApplicable(p_70687_1_);
   }

   public boolean isOnSameTeam(EntityLivingBase el) {
      return el instanceof EntityEldritchCrab;
   }
}
