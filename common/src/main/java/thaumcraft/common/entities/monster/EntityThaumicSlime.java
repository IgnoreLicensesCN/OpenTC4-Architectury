package thaumcraft.common.entities.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.level.Level;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

import java.util.List;

public class EntityThaumicSlime extends Mob implements IMob, ITaintedMob {
   private static final float[] spawnChances = new float[]{1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
   public float squishAmount;
   public float squishFactor;
   public float prevSquishFactor;
   private int slimeJumpDelay = 0;
   int launched = 10;
   int spitCounter = 100;

   public EntityThaumicSlime(Level par1World) {
      super(par1World);
      int i = 1 << this.rand.nextInt(3);
      this.yOffset = 0.0F;
      this.slimeJumpDelay = this.rand.nextInt(20) + 10;
      this.setSlimeSize(i);
   }

   public EntityThaumicSlime(Level par1World, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
      super(par1World);
      this.setSlimeSize(1);
      this.posY = (par2EntityLiving.boundingBox.minY + par2EntityLiving.boundingBox.maxY) / (double)2.0F;
      double var6 = par3EntityLiving.posX - par2EntityLiving.posX;
      double var8 = par3EntityLiving.boundingBox.minY + (double)(par3EntityLiving.height / 3.0F) - this.posY;
      double var10 = par3EntityLiving.posZ - par2EntityLiving.posZ;
      double var12 = MathHelper.sqrt_double(var6 * var6 + var10 * var10);
      if (var12 >= 1.0E-7) {
         float var14 = (float)(Math.atan2(var10, var6) * (double)180.0F / Math.PI) - 90.0F;
         float var15 = (float)(-(Math.atan2(var8, var12) * (double)180.0F / Math.PI));
         double var16 = var6 / var12;
         double var18 = var10 / var12;
         this.setLocationAndAngles(par2EntityLiving.posX + var16, this.posY, par2EntityLiving.posZ + var18, var14, var15);
         this.yOffset = 0.0F;
         float var20 = (float)var12 * 0.2F;
         this.setThrowableHeading(var6, var8 + (double)var20, var10, 1.5F, 1.0F);
      }

   }

   public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
      float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
      par1 /= var9;
      par3 /= var9;
      par5 /= var9;
      par1 += this.rand.nextGaussian() * (double)0.0075F * (double)par8;
      par3 += this.rand.nextGaussian() * (double)0.0075F * (double)par8;
      par5 += this.rand.nextGaussian() * (double)0.0075F * (double)par8;
      par1 *= par7;
      par3 *= par7;
      par5 *= par7;
      this.motionX = par1;
      this.motionY = par3;
      this.motionZ = par5;
      float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
      this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * (double)180.0F / Math.PI);
      this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, var10) * (double)180.0F / Math.PI);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte) 1);
   }

   public void setSlimeSize(int par1) {
      this.dataWatcher.updateObject(16, (byte) par1);
      float ss = (float)Math.sqrt(par1);
      this.setSize(0.25F * ss + 0.25F, 0.25F * ss + 0.25F);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(par1);
      this.setHealth(this.getMaxHealth());
      this.experienceValue = (int)ss;
   }

   protected int getAttackStrength() {
      return this.getSlimeSize();
   }

   public int getSlimeSize() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("Size", this.getSlimeSize() - 1);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setSlimeSize(par1NBTTagCompound.getInteger("Size") + 1);
   }

   protected String getSlimeParticle() {
      return "slime";
   }

   protected String getJumpSound() {
      return "mob.slime." + (this.getSlimeSize() > 3 ? "big" : "small");
   }

   public void onUpdate() {
      if (Platform.getEnvironment() != Env.CLIENT && this.level().difficultySetting.getDifficultyId() == 0 && this.getSlimeSize() > 0) {
         this.isDead = true;
      }

      this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
      this.prevSquishFactor = this.squishFactor;
      boolean flag = this.onGround;
      super.onUpdate();
      int i = (int)Math.sqrt(this.getSlimeSize());
      if (this.launched > 0) {
         --this.launched;
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            for(int j = 0; j < i * (this.launched + 1); ++j) {
               Thaumcraft.proxy.slimeJumpFX(this, i);
            }
         }
      }

      if (this.onGround && !flag) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            for(int j = 0; j < i * 8; ++j) {
               Thaumcraft.proxy.slimeJumpFX(this, i);
            }
         }

         if (this.makesSoundOnLand()) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         }

         this.squishAmount = -0.5F;
      } else if (!this.onGround && flag) {
         this.squishAmount = 1.0F;
      }

      this.alterSquishAmount();
      if ((Platform.getEnvironment() == Env.CLIENT)) {
         float ff = (float)Math.sqrt(this.getSlimeSize());
         this.setSize(0.6F * ff, 0.6F * ff);
      }

   }

   protected EntityThaumicSlime getClosestMergableSlime() {
      EntityThaumicSlime closest = null;
      double distance = Double.MAX_VALUE;
      List ents = this.level().getEntitiesWithinAABB(EntityThaumicSlime.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(16.0F, 8.0F, 16.0F));
      if (ents != null && !ents.isEmpty()) {
         for(Object s : ents) {
            EntityThaumicSlime slime = (EntityThaumicSlime)s;
            if (slime.getEntityId() != this.getEntityId() && slime.ticksExisted > 100 && slime.getSlimeSize() < 100 && this.getDistanceSqToEntity(slime) < distance) {
               closest = slime;
            }

            distance = this.getDistanceSqToEntity(slime);
         }
      }

      return closest;
   }

   protected void updateEntityActionState() {
      this.despawnEntity();
      Player Player = this.level().getClosestVulnerablePlayerToEntity(this, 16.0F);
      if (Player != null) {
         if (this.spitCounter > 0) {
            --this.spitCounter;
         }

         this.faceEntity(Player, 10.0F, 20.0F);
         if (this.getDistanceToEntity(Player) > 4.0F && this.spitCounter <= 0 && this.getSlimeSize() > 3) {
            this.spitCounter = 101;
            if (Platform.getEnvironment() != Env.CLIENT) {
               EntityThaumicSlime flyslime = new EntityThaumicSlime(this.level(), this, Player);
               this.level().spawnEntityInWorld(flyslime);
            }

            this.level().playSoundAtEntity(this, "thaumcraft:gore", 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            this.setSlimeSize(this.getSlimeSize() - 1);
         }
      } else {
         EntityThaumicSlime slime = this.getClosestMergableSlime();
         if (slime != null) {
            this.faceEntity(slime, 10.0F, 20.0F);
            if (this.getDistanceToEntity(slime) < this.width + slime.width) {
               slime.setSlimeSize(Math.min(100, slime.getSlimeSize() + this.getSlimeSize()));
               this.setDead();
            }
         }
      }

      if (this.onGround && this.slimeJumpDelay-- <= 0) {
         this.slimeJumpDelay = this.getJumpDelay();
         if (Player != null) {
            this.slimeJumpDelay /= 3;
         }

         this.isJumping = true;
         if (this.makesSoundOnJump()) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
         }

         this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
         this.moveForward = (float)(Math.sqrt(this.getSlimeSize()));
      } else {
         this.isJumping = false;
         if (this.onGround) {
            this.moveStrafing = this.moveForward = 0.0F;
         }
      }

   }

   protected void alterSquishAmount() {
      this.squishAmount *= 0.6F;
   }

   protected int getJumpDelay() {
      return this.rand.nextInt(16) + 8;
   }

   protected EntityThaumicSlime createInstance() {
      return new EntityThaumicSlime(this.level());
   }

   public void setDead() {
      int i = (int)Math.sqrt(this.getSlimeSize());
      if (Platform.getEnvironment() != Env.CLIENT && i > 1 && this.getHealth() <= 0.0F) {
         for(int k = 0; k < i; ++k) {
            float f = ((float)(k % 2) - 0.5F) * (float)i / 4.0F;
            float f1 = ((float)(k / 2) - 0.5F) * (float)i / 4.0F;
            EntityThaumicSlime entityslime = this.createInstance();
            entityslime.setSlimeSize(1);
            entityslime.setLocationAndAngles(this.posX + (double)f, this.posY + (double)0.5F, this.posZ + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
            this.level().spawnEntityInWorld(entityslime);
         }
      }

      super.setDead();
   }

   public void onCollideWithPlayer(Player par1Player) {
      if (this.canDamagePlayer()) {
         int i = (int)Math.max(1.0F, Math.sqrt(this.getSlimeSize()));
         if (this.launched > 0 && i == 2) {
            i = 3;
         }

         if (this.canEntityBeSeen(par1Player) && this.getDistanceSqToEntity(par1Player) < 0.8 * (double)i * 0.8 * (double)i && par1Player.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttackStrength())) {
            this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }
      }

   }

   protected boolean canDamagePlayer() {
      return this.getSlimeSize() > 0;
   }

   protected String getHurtSound() {
      return "mob.slime." + (this.getSlimeSize() > 3 ? "big" : "small");
   }

   protected String getDeathSound() {
      return "mob.slime." + (this.getSlimeSize() > 3 ? "big" : "small");
   }

   protected Item getDropItem() {
      return this.getSlimeSize() < 3 ? ConfigItems.itemResource : Item.getItemById(0);
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.getSlimeSize() < 3 && this.rand.nextInt(3) == 0) {
         this.entityDropItem(new ItemStack(ThaumcraftItems.TAINTED_GOO,1), this.height / 2.0F);
      }

   }

   protected float getSoundVolume() {
      return 0.1F * (float)Math.sqrt(this.getSlimeSize());
   }

   public int getVerticalFaceSpeed() {
      return 0;
   }

   protected boolean makesSoundOnJump() {
      return this.getSlimeSize() > 3;
   }

   protected boolean makesSoundOnLand() {
      return this.getSlimeSize() > 5;
   }
}
