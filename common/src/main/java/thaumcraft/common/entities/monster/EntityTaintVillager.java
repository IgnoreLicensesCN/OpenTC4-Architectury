package thaumcraft.common.entities.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;

public class EntityTaintVillager extends EntityMob implements ITaintedMob {
   private int randomTickDivider;
   private boolean isMatingFlag;
   private boolean isPlayingFlag;
   Village villageObj;

   public EntityTaintVillager(World par1World) {
      this(par1World, 0);
   }

   public EntityTaintVillager(World par1World, int par2) {
      super(par1World);
      this.randomTickDivider = 0;
      this.isMatingFlag = false;
      this.isPlayingFlag = false;
      this.villageObj = null;
      this.getNavigator().setBreakDoors(true);
      this.getNavigator().setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIMoveIndoors(this));
      this.tasks.addTask(2, new AIAttackOnCollide(this, EntityPlayer.class, 1.0F, false));
      this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
      this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
      this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0F));
      this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0F, false));
      this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
      this.tasks.addTask(9, new EntityAIWander(this, 1.0F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
      this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.worldObj.isRemote && this.ticksExisted < 5) {
         for(int a = 0; a < Thaumcraft.proxy.particleCount(10); ++a) {
            Thaumcraft.proxy.splooshFX(this);
         }
      }

   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      if (--this.randomTickDivider <= 0) {
         this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
         this.randomTickDivider = 70 + this.rand.nextInt(50);
         this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);
         if (this.villageObj == null) {
            this.detachHome();
         } else {
            ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
            this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int)((float)this.villageObj.getVillageRadius() * 0.6F));
         }
      }

      super.updateAITick();
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, 0);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   protected boolean canDespawn() {
      return false;
   }

   protected Item getDropItem() {
      return ConfigItems.itemResource;
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.worldObj.rand.nextInt(2) == 0) {
         if (this.worldObj.rand.nextBoolean()) {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 11), this.height / 2.0F);
         } else {
            this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 12), this.height / 2.0F);
         }
      }

      if (this.worldObj.rand.nextInt(13) < 1 + i) {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 18), 1.5F);
      }

   }

   protected String getLivingSound() {
      return "mob.villager.idle";
   }

   protected String getHurtSound() {
      return "mob.villager.hit";
   }

   protected String getDeathSound() {
      return "mob.villager.death";
   }

   protected float getSoundPitch() {
      return 0.7F;
   }

   public void setRevengeTarget(EntityLivingBase par1EntityLiving) {
      super.setRevengeTarget(par1EntityLiving);
      if (this.villageObj != null && par1EntityLiving != null) {
         this.villageObj.addOrRenewAgressor(par1EntityLiving);
      }

   }
}
