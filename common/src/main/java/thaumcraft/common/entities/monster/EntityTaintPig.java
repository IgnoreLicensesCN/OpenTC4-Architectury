package thaumcraft.common.entities.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
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

public class EntityTaintPig extends EntityMob implements ITaintedMob {
   public EntityTaintPig(World par1World) {
      super(par1World);
      this.setSize(0.9F, 0.9F);
      this.getNavigator().setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new AIAttackOnCollide(this, EntityPlayer.class, 1.0F, false));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityVillager.class, 1.0F, true));
      this.tasks.addTask(5, new EntityAIWander(this, 1.0F));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
      this.tasks.addTask(8, new AIAttackOnCollide(this, EntityAnimal.class, 1.0F, false));
      this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
      this.targetTasks.addTask(8, new EntityAINearestAttackableTarget(this, EntityAnimal.class, 0, false));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.275);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.worldObj.isRemote && this.ticksExisted < 5) {
         for(int a = 0; a < Thaumcraft.proxy.particleCount(10); ++a) {
            Thaumcraft.proxy.splooshFX(this);
         }
      }

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

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   protected String getLivingSound() {
      return "mob.pig.say";
   }

   protected String getHurtSound() {
      return "mob.pig.say";
   }

   protected String getDeathSound() {
      return "mob.pig.death";
   }

   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.playSound("mob.pig.step", 0.15F, 1.0F);
   }

   protected float getSoundPitch() {
      return 0.7F;
   }

   public boolean interact(EntityPlayer par1EntityPlayer) {
      return super.interact(par1EntityPlayer);
   }

   protected Item getDropItem() {
      return ConfigItems.itemResource;
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.worldObj.rand.nextInt(3) == 0) {
         if (this.worldObj.rand.nextBoolean()) {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 11), this.height / 2.0F);
         } else {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 12), this.height / 2.0F);
         }
      }

   }
}
