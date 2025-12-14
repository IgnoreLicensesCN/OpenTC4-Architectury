package thaumcraft.common.entities.monster;

import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.level.Level;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.combat.AICreeperSwell;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.util.List;

public class EntityTaintCreeper extends EntityMob implements ITaintedMob {
   private int lastActiveTime;
   private int timeSinceIgnited;
   private int fuseTime = 30;
   private int explosionRadius = 3;

   public EntityTaintCreeper(Level par1World) {
      super(par1World);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new AICreeperSwell(this));
      this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0F, 1.2));
      this.tasks.addTask(4, new AIAttackOnCollide(this, 1.0F, false));
      this.tasks.addTask(5, new EntityAIWander(this, 1.0F));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, Player.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, Player.class, 0, true));
      this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25F);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25F);
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected boolean canDespawn() {
      return false;
   }

   public int getMaxSafePointTries() {
      return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
   }

   protected void fall(float par1) {
      super.fall(par1);
      this.timeSinceIgnited = (int)((float)this.timeSinceIgnited + par1 * 1.5F);
      if (this.timeSinceIgnited > this.fuseTime - 5) {
         this.timeSinceIgnited = this.fuseTime - 5;
      }

   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      if (this.dataWatcher.getWatchableObjectByte(17) == 1) {
         par1NBTTagCompound.setBoolean("powered", true);
      }

      par1NBTTagCompound.setShort("Fuse", (short)this.fuseTime);
      par1NBTTagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.dataWatcher.updateObject(17, (byte)(par1NBTTagCompound.getBoolean("powered") ? 1 : 0));
      if (par1NBTTagCompound.hasKey("Fuse")) {
         this.fuseTime = par1NBTTagCompound.getShort("Fuse");
      }

      if (par1NBTTagCompound.hasKey("ExplosionRadius")) {
         this.explosionRadius = par1NBTTagCompound.getByte("ExplosionRadius");
      }

   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, -1);
      this.dataWatcher.addObject(17, (byte)0);
   }

   protected Item getDropItem() {
      return ConfigItems.itemResource;
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.level().rand.nextBoolean()) {
         this.entityDropItem(new ItemStack(ThaumcraftItems.TAINTED_GOO,1), this.height / 2.0F);
      } else {
         this.entityDropItem(new ItemStack(ThaumcraftItems.TAINT_TENDRIL,1), this.height / 2.0F);
      }

   }

   public void onUpdate() {
      if (this.isEntityAlive()) {
         this.lastActiveTime = this.timeSinceIgnited;
         int var1 = this.getCreeperState();
         if (var1 > 0 && this.timeSinceIgnited == 0) {
            this.level().playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
         }

         this.timeSinceIgnited += var1;
         if (this.timeSinceIgnited < 0) {
            this.timeSinceIgnited = 0;
         }

         if (this.timeSinceIgnited >= 30) {
            this.timeSinceIgnited = 30;
            if (Platform.getEnvironment() != Env.CLIENT) {
               this.level().createExplosion(this, this.posX, this.posY + (double)(this.height / 2.0F), this.posZ, 1.5F, false);
               List ents = this.level().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(6.0F, 6.0F, 6.0F));
               if (!ents.isEmpty()) {
                  for(Object ent : ents) {
                     EntityLivingBase el = (EntityLivingBase)ent;
                     if (!(el instanceof ITaintedMob) && !el.isEntityUndead()) {
                        el.addPotionEffect(new PotionEffect(ThaumcraftEffects.FLUX_TAINT, 100, 0, false));
                     }
                  }
               }

               int x = (int)this.posX;
               int y = (int)this.posY;
               int z = (int)this.posZ;

               for(int a = 0; a < 10; ++a) {
                  int xx = x + (int)((this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F);
                  int zz = z + (int)((this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F);
                  if (this.level().rand.nextBoolean() && this.level().getBiomeGenForCoords(xx, zz) != ThaumcraftWorldGenerator.biomeTaint) {
                     Utils.setBiomeAt(this.level(), xx, zz, ThaumcraftWorldGenerator.biomeTaint);
                     if (this.level().isBlockNormalCubeDefault(xx, y - 1, zz, false) && this.level().getBlock(xx, y, zz).isReplaceable(this.level(), xx, y, zz)) {
                        this.level().setBlock(xx, y, zz, ConfigBlocks.blockTaintFibres, 0, 3);
                     }
                  }
               }

               this.setDead();
            } else {
               for(int a = 0; a < Thaumcraft.proxy.particleCount(100); ++a) {
                  Thaumcraft.proxy.taintsplosionFX(this);
               }
            }
         }
      }

      super.onUpdate();
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if ((Platform.getEnvironment() == Env.CLIENT) && this.ticksExisted < 5) {
         for(int a = 0; a < Thaumcraft.proxy.particleCount(10); ++a) {
            Thaumcraft.proxy.splooshFX(this);
         }
      }

   }

   public float getCreeperFlashIntensity(float par1) {
      return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * par1) / 28.0F;
   }

   protected String getHurtSound() {
      return "mob.creeper.say";
   }

   protected String getDeathSound() {
      return "mob.creeper.death";
   }

   protected float getSoundPitch() {
      return 0.7F;
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      return true;
   }

   public int getCreeperState() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   public void setCreeperState(int par1) {
      this.dataWatcher.updateObject(16, (byte)par1);
   }
}
