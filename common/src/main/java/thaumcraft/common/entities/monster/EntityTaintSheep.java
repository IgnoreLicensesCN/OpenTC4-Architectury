package thaumcraft.common.entities.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.misc.AIConvertGrass;

import java.util.ArrayList;

public class EntityTaintSheep extends EntityMob implements IShearable, ITaintedMob {
   private int sheepTimer;
   private AIConvertGrass field_48137_c = new AIConvertGrass(this);

   public EntityTaintSheep(World par1World) {
      super(par1World);
      this.setSize(0.9F, 1.3F);
      this.getNavigator().setAvoidsWater(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, this.field_48137_c);
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityPlayer.class, 1.0F, false));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityVillager.class, 1.0F, true));
      this.tasks.addTask(6, new EntityAIWander(this, 1.0F));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25F);
   }

   protected boolean isAIEnabled() {
      return true;
   }

   protected boolean canDespawn() {
      return false;
   }

   public int getTotalArmorValue() {
      return 1;
   }

   protected void updateAITasks() {
      this.sheepTimer = this.field_48137_c.func_48396_h();
      super.updateAITasks();
   }

   public void onLivingUpdate() {
      if (this.worldObj.isRemote) {
         this.sheepTimer = Math.max(0, this.sheepTimer - 1);
      }

      super.onLivingUpdate();
      if (this.worldObj.isRemote && this.ticksExisted < 5) {
         for(int a = 0; a < Thaumcraft.proxy.particleCount(10); ++a) {
            Thaumcraft.proxy.splooshFX(this);
         }
      }

   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte) 0);
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

   public void handleHealthUpdate(byte par1) {
      if (par1 == 10) {
         this.sheepTimer = 40;
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public float func_44003_c(float par1) {
      return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? ((float)this.sheepTimer - par1) / 4.0F : -((float)(this.sheepTimer - 40) - par1) / 4.0F));
   }

   public float func_44002_d(float par1) {
      if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
         float var2 = ((float)(this.sheepTimer - 4) - par1) / 32.0F;
         return ((float)Math.PI / 5F) + 0.2199115F * MathHelper.sin(var2 * 28.7F);
      } else {
         return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch / (180F / (float)Math.PI);
      }
   }

   public boolean interact(EntityPlayer par1EntityPlayer) {
      return super.interact(par1EntityPlayer);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("Sheared", this.getSheared());
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setSheared(par1NBTTagCompound.getBoolean("Sheared"));
   }

   protected String getLivingSound() {
      return "mob.sheep.say";
   }

   protected String getHurtSound() {
      return "mob.sheep.say";
   }

   protected String getDeathSound() {
      return "mob.sheep.say";
   }

   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.playSound("mob.sheep.step", 0.15F, 1.0F);
   }

   protected float getSoundPitch() {
      return 0.7F;
   }

   public boolean getSheared() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 16) != 0;
   }

   public void setSheared(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 16));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -17));
      }

   }

   public boolean isShearable(ItemStack item, IBlockAccess world, int X, int Y, int Z) {
      return !this.getSheared();
   }

   public ArrayList onSheared(ItemStack item, IBlockAccess world, int X, int Y, int Z, int fortune) {
      ArrayList<ItemStack> ret = new ArrayList<>();
      this.setSheared(true);
      int i = 1 + this.rand.nextInt(3);

      for(int j = 0; j < i; ++j) {
         ret.add(new ItemStack(Blocks.wool, 1, 10));
      }

      return ret;
   }
}
