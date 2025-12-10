package thaumcraft.common.entities.monster;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.combat.AILongRangeAttack;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;
import thaumcraft.common.items.ItemWispEssence;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXSonic;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;

import java.util.List;

public class EntityEldritchGuardian extends EntityMob implements IRangedAttackMob, IEldritchMob {
   public float armLiftL = 0.0F;
   public float armLiftR = 0.0F;
   boolean lastBlast = false;

   public EntityEldritchGuardian(World p_i1745_1_) {
      super(p_i1745_1_);
      this.getNavigator().setBreakDoors(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new AILongRangeAttack(this, 8.0F, 1.0F, 20, 40, 24.0F));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityLivingBase.class, 1.0F, false));
      this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.8));
      this.tasks.addTask(7, new EntityAIWander(this, 1.0F));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCultist.class, 0, true));
      this.setSize(0.8F, 2.25F);
      this.experienceValue = 20;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0F);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0F);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.28);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(12, (byte)0);
      this.getDataWatcher().addObject(13, (byte)0);
      this.getDataWatcher().addObject(14, (byte)0);
   }

   public int getTotalArmorValue() {
      return 4;
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public boolean canPickUpLoot() {
      return false;
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      if (source.isMagicDamage()) {
         damage /= 2.0F;
      }

      return super.attackEntityFrom(source, damage);
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.worldObj.isRemote) {
         if (this.armLiftL > 0.0F) {
            this.armLiftL -= 0.05F;
         }

         if (this.armLiftR > 0.0F) {
            this.armLiftR -= 0.05F;
         }

         float x = (float)(this.posX + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F));
         float z = (float)(this.posZ + (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F));
         Thaumcraft.proxy.wispFXEG(this.worldObj, x, (float)(this.posY + 0.22 * (double)this.height), z, this);
      } else if (this.worldObj.provider.dimensionId != Config.dimensionOuterId && (this.ticksExisted == 0 || this.ticksExisted % 100 == 0) && this.worldObj.difficultySetting != EnumDifficulty.EASY) {
         double d6 = this.worldObj.difficultySetting == EnumDifficulty.HARD ? (double)576.0F : (double)256.0F;

         for(int i = 0; i < this.worldObj.playerEntities.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer)this.worldObj.playerEntities.get(i);
            if (entityplayer1.isEntityAlive()) {
               double d5 = entityplayer1.getDistanceSq(this.posX, this.posY, this.posZ);
               if (d5 < d6) {
                  PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((short)2), (EntityPlayerMP)entityplayer1);
               }
            }
         }
      }

   }

   public boolean attackEntityAsMob(Entity p_70652_1_) {
      boolean flag = super.attackEntityAsMob(p_70652_1_);
      if (flag) {
         int i = this.worldObj.difficultySetting.getDifficultyId();
         if (this.getHeldItem() == null && this.isBurning() && this.rand.nextFloat() < (float)i * 0.3F) {
            p_70652_1_.setFire(2 * i);
         }
      }

      return flag;
   }

   protected String getLivingSound() {
      return "thaumcraft:egidle";
   }

   protected String getDeathSound() {
      return "thaumcraft:egdeath";
   }

   public int getTalkInterval() {
      return 500;
   }

   protected Item getDropItem() {
       return super.getDropItem();
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.rand.nextBoolean()) {
         ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
         new AspectList();
         ((ItemWispEssence)ess.getItem()).setAspects(ess, (new AspectList()).add(Aspect.UNDEAD, 2));
         this.entityDropItem(ess, 1.0F);
      }

      if (this.rand.nextBoolean()) {
         ItemStack ess = new ItemStack(ConfigItems.itemWispEssence);
         new AspectList();
         ((ItemWispEssence)ess.getItem()).setAspects(ess, (new AspectList()).add(Aspect.ELDRITCH, 2));
         this.entityDropItem(ess, 1.0F);
      }

      super.dropFewItems(flag, i);
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   protected void dropRareDrop(int p_70600_1_) {
      this.dropItem(ConfigItems.itemEldritchObject, 1);
   }

   public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
      super.writeEntityToNBT(p_70014_1_);
      if (this.getHomePosition() != null && this.func_110174_bM() > 0.0F) {
         p_70014_1_.setInteger("HomeD", (int)this.func_110174_bM());
         p_70014_1_.setInteger("HomeX", this.getHomePosition().posX);
         p_70014_1_.setInteger("HomeY", this.getHomePosition().posY);
         p_70014_1_.setInteger("HomeZ", this.getHomePosition().posZ);
      }

   }

   public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
      super.readEntityFromNBT(p_70037_1_);
      if (p_70037_1_.hasKey("HomeD")) {
         this.setHomeArea(p_70037_1_.getInteger("HomeX"), p_70037_1_.getInteger("HomeY"), p_70037_1_.getInteger("HomeZ"), p_70037_1_.getInteger("HomeD"));
      }

   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
      IEntityLivingData p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);
      float f = this.worldObj.func_147462_b(this.posX, this.posY, this.posZ);
      if (this.worldObj.provider.dimensionId == Config.dimensionOuterId) {
         int bh = (int)this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() / 2;
         this.setAbsorptionAmount(this.getAbsorptionAmount() + (float)bh);
      }

      return p_110161_1_1;
   }

   protected void updateAITasks() {
      super.updateAITasks();
      if (this.worldObj.provider.dimensionId == Config.dimensionOuterId && this.hurtResistantTime <= 0 && this.ticksExisted % 25 == 0) {
         int bh = (int)this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() / 2;
         if (this.getAbsorptionAmount() < (float)bh) {
            this.setAbsorptionAmount(this.getAbsorptionAmount() + 1.0F);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte p_70103_1_) {
      if (p_70103_1_ == 15) {
         this.armLiftL = 0.5F;
      } else if (p_70103_1_ == 16) {
         this.armLiftR = 0.5F;
      } else if (p_70103_1_ == 17) {
         this.armLiftL = 0.9F;
         this.armLiftR = 0.9F;
      } else {
         super.handleHealthUpdate(p_70103_1_);
      }

   }

   protected boolean canDespawn() {
      return !this.hasHome();
   }

   public float getEyeHeight() {
      return 2.1F;
   }

   public boolean getCanSpawnHere() {
      List ents = this.worldObj.getEntitiesWithinAABB(EntityEldritchGuardian.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX + (double)1.0F, this.posY + (double)1.0F, this.posZ + (double)1.0F).expand(32.0F, 16.0F, 32.0F));
      return ents.isEmpty() && super.getCanSpawnHere();
   }

   protected boolean isValidLightLevel() {
      return true;
   }

   protected float getSoundVolume() {
      return 1.5F;
   }

   public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) {
      if (this.rand.nextFloat() > 0.1F) {
         EntityEldritchOrb blast = new EntityEldritchOrb(this.worldObj, this);
         this.lastBlast = !this.lastBlast;
         this.worldObj.setEntityState(this, (byte)(this.lastBlast ? 16 : 15));
         int rr = this.lastBlast ? 90 : 180;
         double xx = MathHelper.cos((this.rotationYaw + (float)rr) % 360.0F / 180.0F * (float)Math.PI) * 0.5F;
         double yy = 0.057777777 * (double)this.height;
         double zz = MathHelper.sin((this.rotationYaw + (float)rr) % 360.0F / 180.0F * (float)Math.PI) * 0.5F;
         blast.setPosition(blast.posX - xx, blast.posY - yy, blast.posZ - zz);
         double d0 = entitylivingbase.posX + entitylivingbase.motionX - this.posX;
         double d1 = entitylivingbase.posY - this.posY - (double)(entitylivingbase.height / 2.0F);
         double d2 = entitylivingbase.posZ + entitylivingbase.motionZ - this.posZ;
         blast.setThrowableHeading(d0, d1, d2, 1.0F, 2.0F);
         this.playSound("thaumcraft:egattack", 2.0F, 1.0F + this.rand.nextFloat() * 0.1F);
         this.worldObj.spawnEntityInWorld(blast);
      } else if (this.canEntityBeSeen(entitylivingbase)) {
         PacketHandler.INSTANCE.sendToAllAround(new PacketFXSonic(this.getEntityId()), new NetworkRegistry.TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 32.0F));

         try {
            entitylivingbase.addPotionEffect(new PotionEffect(Potion.wither.id, 400, 0));
         } catch (Exception ignored) {
         }

         if (entitylivingbase instanceof EntityPlayer) {
            Thaumcraft.addWarpToPlayer((EntityPlayer)entitylivingbase, 1 + this.worldObj.rand.nextInt(3), true);
         }

         this.playSound("thaumcraft:egscreech", 3.0F, 1.0F + this.rand.nextFloat() * 0.1F);
      }

   }

   public boolean isOnSameTeam(EntityLivingBase el) {
      return el instanceof IEldritchMob;
   }
}
