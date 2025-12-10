package thaumcraft.common.entities.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;

public class EntityTaintChicken extends EntityMob implements ITaintedMob {
   public boolean field_753_a = false;
   public float field_752_b = 0.0F;
   public float destPos = 0.0F;
   public float field_757_d;
   public float field_756_e;
   public float field_755_h = 1.0F;

   public EntityTaintChicken(World par1World) {
      super(par1World);
      this.setSize(0.5F, 0.8F);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new AIAttackOnCollide(this, EntityPlayer.class, 1.0F, false));
      this.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.3F));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityVillager.class, 1.0F, true));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityAnimal.class, 1.0F, true));
      this.tasks.addTask(3, new EntityAIWander(this, 1.0F));
      this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(5, new EntityAILookIdle(this));
      this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityAnimal.class, 0, false));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4);
   }

   protected boolean canDespawn() {
      return false;
   }

   public boolean isAIEnabled() {
      return true;
   }

   public int getTotalArmorValue() {
      return 2;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      this.field_756_e = this.field_752_b;
      this.field_757_d = this.destPos;
      this.destPos = (float)((double)this.destPos + (double)(this.onGround ? -1 : 4) * 0.3);
      if (this.destPos < 0.0F) {
         this.destPos = 0.0F;
      }

      if (this.destPos > 1.0F) {
         this.destPos = 1.0F;
      }

      if (!this.onGround && this.field_755_h < 1.0F) {
         this.field_755_h = 1.0F;
      }

      this.field_755_h = (float)((double)this.field_755_h * 0.9);
      if (!this.onGround && this.motionY < (double)0.0F) {
         this.motionY *= 0.9;
      }

      this.field_752_b += this.field_755_h * 2.0F;
      if (this.worldObj.isRemote && this.ticksExisted < 5) {
         for(int a = 0; a < Thaumcraft.proxy.particleCount(10); ++a) {
            Thaumcraft.proxy.splooshFX(this);
         }
      }

   }

   protected void fall(float par1) {
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   protected String getLivingSound() {
      return "mob.chicken.say";
   }

   protected String getHurtSound() {
      return "mob.chicken.hurt";
   }

   protected String getDeathSound() {
      return "mob.chicken.hurt";
   }

   protected float getSoundPitch() {
      return 0.7F;
   }

   protected Item getDropItem() {
      return ConfigItems.itemResource;
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.worldObj.rand.nextInt(4) == 0) {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 11), this.height / 2.0F);
      } else {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 12), this.height / 2.0F);
      }

   }
}
