package thaumcraft.common.entities.monster.boss;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;

import net.minecraft.util.Vec3;
import net.minecraft.world.level.Level;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockLoot;
import thaumcraft.common.entities.ai.combat.AIAttackOnCollide;
import thaumcraft.common.entities.ai.combat.AILongRangeAttack;
import thaumcraft.common.entities.monster.mods.ChampionModifier;
import thaumcraft.common.entities.projectile.EntityGolemOrb;
import thaumcraft.common.lib.utils.EntityUtils;

public class EntityEldritchGolem extends EntityThaumcraftBoss implements IEldritchMob, IRangedAttackMob {
   int beamCharge = 0;
   boolean chargingBeam = false;
   int arcing = 0;
   int ax = 0;
   int ay = 0;
   int az = 0;
   private int attackTimer;

   public EntityEldritchGolem(World p_i1745_1_) {
      super(p_i1745_1_);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(3, new AIAttackOnCollide(this, EntityLivingBase.class, 1.1, false));
      this.tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 0.8));
      this.tasks.addTask(7, new EntityAIWander(this, 0.8));
      this.tasks.addTask(8, new EntityAIWatchClosest(this, Player.class, 8.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, Player.class, 0, true));
      this.setSize(1.75F, 3.5F);
      this.isImmuneToFire = true;
   }

   public void generateName() {
      int t = (int)this.getEntityAttribute(EntityUtils.CHAMPION_MOD).getAttributeValue();
      if (t >= 0) {
         this.setCustomNameTag(String.format(Component.translatable("entity.Thaumcraft.EldritchGolem.name"),
                 ChampionModifier.mods[t].getModNameLocalized()));
      }

   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(12, (byte)0);
   }

   public boolean isHeadless() {
      return this.getDataWatcher().getWatchableObjectByte(12) == 1;
   }

   public void setHeadless(boolean par1) {
      this.dataWatcher.updateObject(12, (byte) (par1 ? 1 : 0));
   }

   public void writeEntityToNBT(NBTTagCompound nbt) {
      super.writeEntityToNBT(nbt);
      nbt.setBoolean("headless", this.isHeadless());
   }

   public void readEntityFromNBT(NBTTagCompound nbt) {
      super.readEntityFromNBT(nbt);
      this.setHeadless(nbt.getBoolean("headless"));
      if (this.isHeadless()) {
         this.makeHeadless();
      }

   }

   public float getEyeHeight() {
      return this.isHeadless() ? 3.33F : 3.0F;
   }

   public int getTotalArmorValue() {
      return super.getTotalArmorValue() + 6;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(10.0F);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(250.0F);
   }

   protected String getHurtSound() {
      return "mob.irongolem.hit";
   }

   protected String getDeathSound() {
      return "mob.irongolem.death";
   }

   protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
      this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
      this.spawnTimer = 100;
      return super.onSpawnWithEgg(p_110161_1_);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.attackTimer > 0) {
         --this.attackTimer;
      }

      if (this.motionX * this.motionX + this.motionZ * this.motionZ > (double)2.5000003E-7F && this.rand.nextInt(5) == 0) {
         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
         int k = MathHelper.floor_double(this.posZ);
         Block block = this.level().getBlock(i, j, k);
         if (block.getMaterial() != Material.air) {
            this.level().spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.level().getBlockMetadata(i, j, k), this.posX + ((double)this.rand.nextFloat() - (double)0.5F) * (double)this.width, this.boundingBox.minY + 0.1, this.posZ + ((double)this.rand.nextFloat() - (double)0.5F) * (double)this.width, (double)4.0F * ((double)this.rand.nextFloat() - (double)0.5F), 0.5F, ((double)this.rand.nextFloat() - (double)0.5F) * (double)4.0F);
         }

         if (Platform.getEnvironment() != Env.CLIENT && block instanceof BlockLoot) {
            this.level().func_147480_a(i, j, k, true);
         }
      }

      if (Platform.getEnvironment() != Env.CLIENT) {
         int i = MathHelper.floor_double(this.posX + this.motionX);
         int j = MathHelper.floor_double(this.boundingBox.minY);
         int k = MathHelper.floor_double(this.posZ + this.motionZ);
         Block block = this.level().getBlock(i, j, k);
         float h = block.getBlockHardness(this.level(), i, j, k);
         if (h >= 0.0F && h <= 0.15F) {
            this.level().func_147480_a(i, j, k, true);
         }
      }

   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      if (Platform.getEnvironment() != Env.CLIENT && damage > this.getHealth() && !this.isHeadless()) {
         this.setHeadless(true);
         this.spawnTimer = 100;
         double xx = MathHelper.cos(this.rotationYaw % 360.0F / 180.0F * (float)Math.PI) * 0.75F;
         double zz = MathHelper.sin(this.rotationYaw % 360.0F / 180.0F * (float)Math.PI) * 0.75F;
         this.level().createExplosion(this, this.posX + xx, this.posY + (double)this.getEyeHeight(), this.posZ + zz, 2.0F, false);
         this.makeHeadless();
         return false;
      } else {
         return super.attackEntityFrom(source, damage);
      }
   }

   void makeHeadless() {
      this.tasks.addTask(2, new AILongRangeAttack(this, 3.0F, 1.0F, 5, 5, 24.0F));
   }

   public boolean attackEntityAsMob(Entity target) {
      if (this.attackTimer > 0) {
         return false;
      } else {
         this.attackTimer = 10;
         this.level().setEntityState(this, (byte)4);
         boolean flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue() * 0.75F);
         if (flag) {
            target.motionY += 0.2000000059604645;
            if (this.isHeadless()) {
               target.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * 1.5F, 0.1, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * 1.5F);
            }
         }

         return flag;
      }
   }

   public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) {
      if (this.canEntityBeSeen(entitylivingbase) && !this.chargingBeam && this.beamCharge > 0) {
         this.beamCharge -= 15 + this.rand.nextInt(5);
         this.getLookHelper().setLookPosition(entitylivingbase.posX, entitylivingbase.boundingBox.minY + (double)(entitylivingbase.height / 2.0F), entitylivingbase.posZ, 30.0F, 30.0F);
         Vec3 v = this.getLook(1.0F);
         EntityGolemOrb blast = new EntityGolemOrb(this.level(), this, entitylivingbase, false);
         blast.posX += v.xCoord;
         blast.posZ += v.zCoord;
         blast.setPosition(blast.posX, blast.posY, blast.posZ);
         double d0 = entitylivingbase.posX + entitylivingbase.motionX - this.posX;
         double d1 = entitylivingbase.posY - this.posY - (double)(entitylivingbase.height / 2.0F);
         double d2 = entitylivingbase.posZ + entitylivingbase.motionZ - this.posZ;
         blast.setThrowableHeading(d0, d1, d2, 0.66F, 5.0F);
         this.playSound("thaumcraft:egattack", 1.0F, 1.0F + this.rand.nextFloat() * 0.1F);
         this.level().spawnEntityInWorld(blast);
      }

   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte p_70103_1_) {
      if (p_70103_1_ == 4) {
         this.attackTimer = 10;
         this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
      } else if (p_70103_1_ == 18) {
         this.spawnTimer = 150;
      } else if (p_70103_1_ == 19) {
         if (this.arcing == 0) {
            float radius = 2.0F + this.rand.nextFloat() * 2.0F;
            double radians = Math.toRadians(this.rand.nextInt(360));
            double deltaX = (double)radius * Math.cos(radians);
            double deltaZ = (double)radius * Math.sin(radians);
            int bx = MathHelper.floor_double(this.posX + deltaX);
            int by = MathHelper.floor_double(this.posY);
            int bz = MathHelper.floor_double(this.posZ + deltaZ);

            for(int c = 0; c < 5 && this.level().isAirBlock(bx, by, bz); --by) {
               ++c;
            }

            if (this.level().isAirBlock(bx, by + 1, bz) && !this.level().isAirBlock(bx, by, bz)) {
               this.ax = bx;
               this.ay = by;
               this.az = bz;
               this.arcing = 8 + this.rand.nextInt(5);
               this.level().playSound(this.posX, this.posY, this.posZ, "thaumcraft:jacobs", 0.8F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F, false);
            }
         }
      } else {
         super.handleHealthUpdate(p_70103_1_);
      }

   }

   public void onUpdate() {
      if (this.getSpawnTimer() == 150) {
         this.level().setEntityState(this, (byte)18);
      }

      if (this.getSpawnTimer() > 0) {
         this.heal(2.0F);
      }

      super.onUpdate();
      if ((Platform.getEnvironment() == Env.CLIENT)) {
         if (this.isHeadless()) {
            this.rotationPitch = 0.0F;
            float f1 = MathHelper.cos(-this.renderYawOffset * ((float)Math.PI / 180F) - (float)Math.PI);
            float f2 = MathHelper.sin(-this.renderYawOffset * ((float)Math.PI / 180F) - (float)Math.PI);
            float f3 = -MathHelper.cos(-this.rotationPitch * ((float)Math.PI / 180F));
            float f4 = MathHelper.sin(-this.rotationPitch * ((float)Math.PI / 180F));
            Vec3 v = Vec3.createVectorHelper(f2 * f3, f4, f1 * f3);
            if (this.rand.nextInt(20) == 0) {
               float a = (this.rand.nextFloat() - this.rand.nextFloat()) / 2.0F;
               float b = (this.rand.nextFloat() - this.rand.nextFloat()) / 2.0F;
               ClientFXUtils.spark((float)(this.posX + v.xCoord + (double)a), (float)this.posY + this.getEyeHeight() - 0.25F, (float)(this.posZ + v.zCoord + (double)b), 0.3F, 0.65F + this.rand.nextFloat() * 0.1F, 1.0F, 1.0F, 0.8F);
            }

            Thaumcraft.proxy.drawVentParticles(this.level(), (double)((float)this.posX) + v.xCoord * 0.66, (float)this.posY + this.getEyeHeight() - 0.75F, (double)((float)this.posZ) + v.zCoord * 0.66, 0.0F, 0.001, 0.0F, 5592405, 4.0F);
            if (this.arcing > 0) {
               Thaumcraft.proxy.arcLightning(this.level(), this.posX, this.posY + (double)(this.height / 2.0F), this.posZ, (double)this.ax + (double)0.5F, this.ay + 1, (double)this.az + (double)0.5F, 0.65F + this.rand.nextFloat() * 0.1F, 1.0F, 1.0F, 1.0F - (float)this.arcing / 10.0F);
               --this.arcing;
            }
         }
      } else {
         if (this.isHeadless() && this.beamCharge <= 0) {
            this.chargingBeam = true;
         }

         if (this.isHeadless() && this.chargingBeam) {
            ++this.beamCharge;
            this.level().setEntityState(this, (byte)19);
            if (this.beamCharge == 150) {
               this.chargingBeam = false;
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   protected void dropRareDrop(int fortune) {
      super.dropRareDrop(fortune);
   }
}
