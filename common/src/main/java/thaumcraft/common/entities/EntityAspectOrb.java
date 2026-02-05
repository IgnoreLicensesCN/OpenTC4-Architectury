package thaumcraft.common.entities;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.wandtypes.WandCastingItem;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.List;

public class EntityAspectOrb extends Entity implements IEntityAdditionalSpawnData {
   public int orbAge = 0;
   public int orbMaxAge = 150;
   public int orbCooldown;
   private int orbHealth = 5;
   private Aspect aspect;
   private int aspectValue;
   private Player closestPlayer;

   public boolean isInRangeToRenderDist(double par1) {
      double d1 = 0.5F;
      d1 *= (double)64.0F * this.renderDistanceWeight;
      return par1 < d1 * d1;
   }

   public EntityAspectOrb(Level par1World, double par2, double par4, double par6, Aspect aspect, int par8) {
      super(par1World);
      this.setSize(0.125F, 0.125F);
      this.yOffset = this.height / 2.0F;
      this.setPosition(par2, par4, par6);
      this.rotationYaw = (float)(Math.random() * (double)360.0F);
      this.motionX = (float)(Math.random() * (double)0.2F - (double)0.1F) * 2.0F;
      this.motionY = (float)(Math.random() * 0.2) * 2.0F;
      this.motionZ = (float)(Math.random() * (double)0.2F - (double)0.1F) * 2.0F;
      this.aspectValue = par8;
      this.setAspect(aspect);
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public EntityAspectOrb(Level par1World) {
      super(par1World);
      this.setSize(0.125F, 0.125F);
      this.yOffset = this.height / 2.0F;
   }

   protected void entityInit() {
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      float f1 = 0.5F;
      int i = super.getBrightnessForRender(par1);
      int j = i & 255;
      int k = i >> 16 & 255;
      j += (int)(f1 * 15.0F * 16.0F);
      if (j > 240) {
         j = 240;
      }

      return j | k << 16;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.orbCooldown > 0) {
         --this.orbCooldown;
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= 0.03F;
      if (this.level().getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial() == Material.lava) {
         this.motionY = 0.2F;
         this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
         this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
         this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
      }

      this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / (double)2.0F, this.posZ);
      double d0 = 8.0F;
      if (this.ticksExisted % 5 == 0 && this.closestPlayer == null) {
         List<Entity> targets = this.level().getEntitiesWithinAABB(Player.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(d0, d0, d0));
         if (!targets.isEmpty()) {
            double distance = Double.MAX_VALUE;

            for(Entity t : targets) {
               double d = t.getDistanceSqToEntity(this);
               if (d < distance && InventoryUtils.isWandInHotbarWithRoom(this.getAspect(), this.aspectValue, (Player)t) >= 0) {
                  distance = d;
                  this.closestPlayer = (Player)t;
               }
            }
         }
      }

      if (this.closestPlayer != null) {
         double d1 = (this.closestPlayer.posX - this.posX) / d0;
         double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / d0;
         double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
         double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
         double d5 = (double)1.0F - d4;
         if (d5 > (double)0.0F) {
            d5 *= d5;
            this.motionX += d1 / d4 * d5 * 0.1;
            this.motionY += d2 / d4 * d5 * 0.1;
            this.motionZ += d3 / d4 * d5 * 0.1;
         }
      }

      this.moveEntity(this.motionX, this.motionY, this.motionZ);
      float f = 0.98F;
      if (this.onGround) {
         f = 0.58800006F;
         Block i = this.level().getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
         if (!i.isAir(this.level(), MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ))) {
            f = i.slipperiness * 0.98F;
         }
      }

      this.motionX *= f;
      this.motionY *= 0.98F;
      this.motionZ *= f;
      if (this.onGround) {
         this.motionY *= -0.9F;
      }

      ++this.orbAge;
      if (this.orbAge >= this.orbMaxAge) {
         this.setDead();
      }

   }

   public boolean handleWaterMovement() {
      return this.level().handleMaterialAcceleration(this.boundingBox, Material.water, this);
   }

   protected void dealFireDamage(int par1) {
      this.attackEntityFrom(DamageSource.inFire, (float)par1);
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if (this.isEntityInvulnerable()) {
         return false;
      } else {
         this.setBeenAttacked();
         this.orbHealth = (int)((float)this.orbHealth - par2);
         if (this.orbHealth <= 0) {
            this.setDead();
         }

         return false;
      }
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setShort("Health", (byte)this.orbHealth);
      par1NBTTagCompound.setShort("Age", (short)this.orbAge);
      par1NBTTagCompound.setShort("Value", (short)this.aspectValue);
      par1NBTTagCompound.setString("Aspect", this.getAspect().getAspectKey());
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.orbHealth = par1NBTTagCompound.getShort("Health") & 255;
      this.orbAge = par1NBTTagCompound.getShort("Age");
      this.aspectValue = par1NBTTagCompound.getShort("Value");
      this.setAspect(Aspect.getAspect(par1NBTTagCompound.getString("Aspect")));
   }

   public void onCollideWithPlayer(Player par1Player) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         int slot = InventoryUtils.isWandInHotbarWithRoom(this.getAspect(), this.aspectValue, par1Player);
         if (this.orbCooldown == 0 && par1Player.xpCooldown == 0 && this.getAspect().isPrimal() && slot >= 0) {
            WandCastingItem wand = (WandCastingItem)par1Player.inventory.mainInventory[slot].getItem();
            wand.addCentiVis(par1Player.inventory.mainInventory[slot], this.getAspect(), this.aspectValue, true);
            par1Player.xpCooldown = 2;
            this.playSound("random.orb", 0.1F, 0.5F * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.8F));
            this.setDead();
         }
      }

   }

   public void writeSpawnData(ByteBuf data) {
      if (this.getAspect() != null) {
         data.writeShort(this.getAspect().getAspectKey().length());

         for(char c : this.getAspect().getAspectKey().toCharArray()) {
            data.writeChar(c);
         }
      }

   }

   public void readSpawnData(ByteBuf data) {
      try {
         int l = data.readShort();
         StringBuilder s = new StringBuilder();

         for(int var4 = 0; var4 < l; ++var4) {
            s.append(data.readChar());
         }

         this.setAspect(Aspect.getAspect(s.toString()));
      } catch (Exception ignored) {
      }

   }

   public int getAspectValue() {
      return this.aspectValue;
   }

   public boolean canAttackWithItem() {
      return false;
   }

   public Aspect getAspect() {
      return this.aspect;
   }

   public void setAspect(Aspect aspect) {
      this.aspect = aspect;
   }
}
