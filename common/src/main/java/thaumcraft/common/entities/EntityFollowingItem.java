package thaumcraft.common.entities;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class EntityFollowingItem extends EntitySpecialItem implements IEntityAdditionalSpawnData {
   double targetX;
   double targetY;
   double targetZ;
   int type;
   public Entity target;
   int age;
   public double gravity;

   public EntityFollowingItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
      super(par1World);
      this.targetX = 0.0F;
      this.targetY = 0.0F;
      this.targetZ = 0.0F;
      this.type = 3;
      this.target = null;
      this.age = 20;
      this.gravity = 0.04F;
      this.setSize(0.25F, 0.25F);
      this.yOffset = this.height / 2.0F;
      this.setPosition(par2, par4, par6);
      this.setEntityItemStack(par8ItemStack);
      this.rotationYaw = (float)(Math.random() * (double)360.0F);
   }

   public EntityFollowingItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack, Entity target, int t) {
      this(par1World, par2, par4, par6, par8ItemStack);
      this.target = target;
      this.targetX = target.posX;
      this.targetY = target.boundingBox.minY + (double)(target.height / 2.0F);
      this.targetZ = target.posZ;
      this.type = t;
      this.noClip = true;
   }

   public EntityFollowingItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack, double tx, double ty, double tz) {
      this(par1World, par2, par4, par6, par8ItemStack);
      this.targetX = tx;
      this.targetY = ty;
      this.targetZ = tz;
   }

   public EntityFollowingItem(World par1World) {
      super(par1World);
      this.targetX = 0.0F;
      this.targetY = 0.0F;
      this.targetZ = 0.0F;
      this.type = 3;
      this.target = null;
      this.age = 20;
      this.gravity = 0.04F;
      this.setSize(0.25F, 0.25F);
      this.yOffset = this.height / 2.0F;
   }

   public void onUpdate() {
      if (this.target != null) {
         this.targetX = this.target.posX;
         this.targetY = this.target.boundingBox.minY + (double)(this.target.height / 2.0F);
         this.targetZ = this.target.posZ;
      }

      if (this.targetX == (double)0.0F && this.targetY == (double)0.0F && this.targetZ == (double)0.0F) {
         this.motionY -= this.gravity;
      } else {
         float xd = (float)(this.targetX - this.posX);
         float yd = (float)(this.targetY - this.posY);
         float zd = (float)(this.targetZ - this.posZ);
         if (this.age > 1) {
            --this.age;
         }

         double distance = MathHelper.sqrt_float(xd * xd + yd * yd + zd * zd);
         if (distance > (double)0.5F) {
            distance *= this.age;
            this.motionX = (double)xd / distance;
            this.motionY = (double)yd / distance;
            this.motionZ = (double)zd / distance;
         } else {
            this.motionX *= 0.1F;
            this.motionY *= 0.1F;
            this.motionZ *= 0.1F;
            this.targetX = 0.0F;
            this.targetY = 0.0F;
            this.targetZ = 0.0F;
            this.target = null;
            this.noClip = false;
         }

         if (this.worldObj.isRemote) {
            if (this.type != 10) {
               Thaumcraft.proxy.sparkle((float)this.prevPosX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125F, (float)this.prevPosY + this.yOffset + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125F, (float)this.prevPosZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125F, this.type);
            } else {
               Thaumcraft.proxy.crucibleBubble(this.worldObj, (float)this.prevPosX + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125F, (float)this.prevPosY + this.yOffset + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125F, (float)this.prevPosZ + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.125F, 0.33F, 0.33F, 1.0F);
            }
         }
      }

      super.onUpdate();
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setShort("type", (short)this.type);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.type = par1NBTTagCompound.getShort("type");
   }

   public void writeSpawnData(ByteBuf data) {
      if (this.target != null) {
         data.writeInt(this.target == null ? -1 : this.target.getEntityId());
         data.writeDouble(this.targetX);
         data.writeDouble(this.targetY);
         data.writeDouble(this.targetZ);
         data.writeByte(this.type);
      }

   }

   public void readSpawnData(ByteBuf data) {
      try {
         int ent = data.readInt();
         if (ent > -1) {
            this.target = this.worldObj.getEntityByID(ent);
         }

         this.targetX = data.readDouble();
         this.targetY = data.readDouble();
         this.targetZ = data.readDouble();
         this.type = data.readByte();
      } catch (Exception ignored) {
      }

   }
}
