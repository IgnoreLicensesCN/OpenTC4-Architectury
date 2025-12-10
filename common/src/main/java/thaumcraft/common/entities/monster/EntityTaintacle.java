package thaumcraft.common.entities.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.damagesource.DamageSourceThaumcraft;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.util.List;

public class EntityTaintacle extends EntityMob implements ITaintedMob {
   public float flailIntensity = 1.0F;

   public EntityTaintacle(World par1World) {
      super(par1World);
      this.setSize(0.66F, 3.0F);
      this.experienceValue = 10;
   }

   public boolean getCanSpawnHere() {
      int var1 = MathHelper.floor_double(this.boundingBox.minY);
      int var2 = MathHelper.floor_double(this.posX);
      int var3 = MathHelper.floor_double(this.posZ);
      this.worldObj.getBlockLightValue(var2, var1, var3);
      byte var5 = 7;
      List ents = this.worldObj.getEntitiesWithinAABB(EntityTaintacle.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(24.0F, 8.0F, 24.0F));
      boolean onTaint = (this.worldObj.getBlock(var2, var1, var3) == ConfigBlocks.blockTaintFibres && this.worldObj.getBlockMetadata(var2, var1, var3) == 0 || this.worldObj.getBlock(var2, var1, var3) == ConfigBlocks.blockTaint && this.worldObj.getBlockMetadata(var2, var1, var3) == 1) && this.worldObj.getBiomeGenForCoords(var2, var3).biomeID == Config.biomeTaintID;
      return ents.isEmpty() && onTaint && super.getCanSpawnHere();
   }

   public float getShadowSize() {
      return 0.25F;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0F);
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public boolean canBePushed() {
      return true;
   }

   protected Entity findPlayerToAttack() {
      Entity entity = null;
      List ents = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(this.height * 6.0F, this.height * 3.0F, this.height * 6.0F));
      if (!ents.isEmpty()) {
         double distance = Double.MAX_VALUE;

         for(Object ent : ents) {
            if (ent != null) {
               EntityLivingBase el = (EntityLivingBase)ent;
               double d = el.getDistanceSqToEntity(this);
               if (!(el instanceof ITaintedMob) && d < distance) {
                  distance = d;
                  entity = el;
               }
            }
         }
      }

      return entity;
   }

   public void moveEntity(double par1, double par3, double par5) {
      par1 = 0.0F;
      par5 = 0.0F;
      if (par3 > (double)0.0F) {
         par3 = 0.0F;
      }

      super.moveEntity(par1, par3, par5);
   }

   protected void updateEntityActionState() {
      if (this.entityToAttack != null) {
         this.faceEntity(this.entityToAttack, 5.0F);
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote && this.ticksExisted % 20 == 0 && this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ)).biomeID != Config.biomeTaintID) {
         this.damageEntity(DamageSource.starve, 1.0F);
      }

      if (this.worldObj.isRemote) {
         if ((float)this.ticksExisted > this.height * 10.0F && (this.hurtTime > 0 || this.attackTime > 0 || this.entityToAttack != null && this.entityToAttack.getDistanceToEntity(this) < this.height)) {
            if (this.flailIntensity < 3.0F) {
               this.flailIntensity += 0.2F;
            }
         } else if (this.flailIntensity > 1.0F) {
            this.flailIntensity -= 0.2F;
         }

         if ((float)this.ticksExisted < this.height * 10.0F && this.onGround) {
            Thaumcraft.proxy.tentacleAriseFX(this);
         }
      }

      if (this.entityToAttack == null) {
         this.entityToAttack = this.findPlayerToAttack();
      } else if (this.entityToAttack.isEntityAlive() && this.getAgitationState()) {
         float f1 = this.entityToAttack.getDistanceToEntity(this);
         if (!this.worldObj.isRemote && this.canEntityBeSeen(this.entityToAttack)) {
            this.attackEntity(this.entityToAttack, f1);
         }
      } else {
         this.entityToAttack = null;
      }

   }

   protected void attackEntity(Entity entity, float par2) {
      if (this.attackTime <= 0) {
         if (par2 <= this.height && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            this.attackEntityAsMob(entity);
            this.playSound("thaumcraft:tentacle", this.getSoundVolume(), this.getSoundPitch());
         } else if (par2 > this.height && entity.onGround && !(this instanceof EntityTaintacleSmall)) {
            this.spawnTentacles(entity);
         }
      }

   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      float i = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      if (this.isPotionActive(Potion.damageBoost)) {
         i += (float)(3 << this.getActivePotionEffect(Potion.damageBoost).getAmplifier());
      }

      if (this.isPotionActive(Potion.weakness)) {
         i -= (float)(2 << this.getActivePotionEffect(Potion.weakness).getAmplifier());
      }

      int j = 0;
      if (par1Entity instanceof EntityLivingBase) {
         i += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)par1Entity);
         j += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)par1Entity);
      }

      boolean flag = par1Entity.attackEntityFrom(DamageSourceThaumcraft.causeTentacleDamage(this), i);
      if (flag) {
         if (j > 0) {
            par1Entity.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)j * 0.5F, 0.1, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)j * 0.5F);
            this.motionX *= 0.6;
            this.motionZ *= 0.6;
         }

         int k = EnchantmentHelper.getFireAspectModifier(this);
         if (k > 0) {
            par1Entity.setFire(k * 4);
         }

         if (par1Entity instanceof EntityLivingBase) {
            EnchantmentHelper.func_151384_a((EntityLivingBase)par1Entity, this);
         }

         EnchantmentHelper.func_151385_b(this, par1Entity);
      }

      return flag;
   }

   protected void spawnTentacles(Entity entity) {
      int i = MathHelper.floor_double(entity.posX);
      int j = MathHelper.floor_double(entity.boundingBox.minY);
      int k = MathHelper.floor_double(entity.posZ);
      if (this.worldObj.getBiomeGenForCoords(i, k).biomeID == Config.biomeEldritchID || this.worldObj.getBiomeGenForCoords(i, k).biomeID == Config.biomeTaintID && (this.worldObj.getBlock(i, j, k).getMaterial() == Config.taintMaterial || this.worldObj.getBlock(i, j, k).getMaterial() == Config.taintMaterial || this.worldObj.getBlock(i, j - 1, k).getMaterial() == Config.taintMaterial)) {
         this.attackTime = 40 + this.worldObj.rand.nextInt(20);
         EntityTaintacleSmall taintlet = new EntityTaintacleSmall(this.worldObj);
         taintlet.setLocationAndAngles(entity.posX + (double)this.worldObj.rand.nextFloat() - (double)this.worldObj.rand.nextFloat(), entity.posY, entity.posZ + (double)this.worldObj.rand.nextFloat() - (double)this.worldObj.rand.nextFloat(), 0.0F, 0.0F);
         this.worldObj.spawnEntityInWorld(taintlet);
         this.playSound("thaumcraft:tentacle", this.getSoundVolume(), this.getSoundPitch());
         if (this.worldObj.getBiomeGenForCoords(i, k).biomeID == Config.biomeEldritchID && this.worldObj.isAirBlock(i, j, k) && BlockUtils.isAdjacentToSolidBlock(this.worldObj, i, j, k)) {
            Utils.setBiomeAt(this.worldObj, i, k, ThaumcraftWorldGenerator.biomeTaint);
            this.worldObj.setBlock(i, j, k, ConfigBlocks.blockTaintFibres, this.worldObj.rand.nextInt(4) == 0 ? 1 : 0, 3);
         }
      }

   }

   public boolean attackEntityFrom(DamageSource ds, float par2) {
      if (!(this instanceof EntityTaintacleSmall) && ds.getEntity() != null && this.getDistanceToEntity(ds.getEntity()) > 16.0F && !this.worldObj.isRemote) {
         this.spawnTentacles(ds.getEntity());
      }

      return super.attackEntityFrom(ds, par2);
   }

   public boolean getAgitationState() {
      return this.entityToAttack != null && this.entityToAttack.getDistanceSqToEntity(this) < (double)(this.height * 7.0F * this.height * 7.0F);
   }

   public void faceEntity(Entity par1Entity, float par2) {
      double d0 = par1Entity.posX - this.posX;
      double d1 = par1Entity.posZ - this.posZ;
      float f2 = (float)(Math.atan2(d1, d0) * (double)180.0F / Math.PI) - 90.0F;
      this.rotationYaw = this.updateRotation(this.rotationYaw, f2, par2);
   }

   protected float updateRotation(float par1, float par2, float par3) {
      float f3 = MathHelper.wrapAngleTo180_float(par2 - par1);
      if (f3 > par3) {
         f3 = par3;
      }

      if (f3 < -par3) {
         f3 = -par3;
      }

      return par1 + f3;
   }

   public int getTalkInterval() {
      return 200;
   }

   protected String getLivingSound() {
      return "thaumcraft:roots";
   }

   protected float getSoundPitch() {
      return 1.3F - this.height / 10.0F;
   }

   protected float getSoundVolume() {
      return this.height / 8.0F;
   }

   protected String getHurtSound() {
      return "thaumcraft:tentacle";
   }

   protected String getDeathSound() {
      return "thaumcraft:tentacle";
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.worldObj.rand.nextBoolean()) {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 11), this.height / 2.0F);
      } else {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 12), this.height / 2.0F);
      }

      super.dropFewItems(flag, i);
   }
}
