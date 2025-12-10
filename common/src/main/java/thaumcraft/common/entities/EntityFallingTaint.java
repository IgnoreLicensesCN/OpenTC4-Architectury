package thaumcraft.common.entities;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockTaint;
import thaumcraft.common.config.ConfigBlocks;

public class EntityFallingTaint extends Entity implements IEntityAdditionalSpawnData {
   public Block block;
   public int metadata;
   public int oldX;
   public int oldY;
   public int oldZ;
   public int fallTime = 0;
   private int fallHurtMax = 40;
   private float fallHurtAmount = 2.0F;

   public EntityFallingTaint(World par1World) {
      super(par1World);
   }

   public EntityFallingTaint(World par1World, double par2, double par4, double par6, Block par8, int par9, int ox, int oy, int oz) {
      super(par1World);
      this.block = par8;
      this.metadata = par9;
      this.preventEntitySpawning = true;
      this.setSize(0.98F, 0.98F);
      this.yOffset = this.height / 2.0F;
      this.setPosition(par2, par4, par6);
      this.motionX = 0.0F;
      this.motionY = 0.0F;
      this.motionZ = 0.0F;
      this.prevPosX = par2;
      this.prevPosY = par4;
      this.prevPosZ = par6;
      this.oldX = ox;
      this.oldY = oy;
      this.oldZ = oz;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void entityInit() {
   }

   public void writeSpawnData(ByteBuf data) {
      data.writeInt(Block.getIdFromBlock(this.block));
      data.writeByte(this.metadata);
   }

   public void readSpawnData(ByteBuf data) {
      try {
         this.block = Block.getBlockById(data.readInt());
         this.metadata = data.readByte();
      } catch (Exception ignored) {
      }

   }

   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   public void onUpdate() {
      if (this.block != null && this.block != Blocks.air) {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         ++this.fallTime;
         this.motionY -= 0.04F;
         this.moveEntity(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.98F;
         this.motionY *= 0.98F;
         this.motionZ *= 0.98F;
         if (!this.worldObj.isRemote) {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY);
            int k = MathHelper.floor_double(this.posZ);
            if (this.fallTime == 1) {
               if (this.worldObj.getBlock(this.oldX, this.oldY, this.oldZ) != this.block) {
                  this.setDead();
                  return;
               }

               this.worldObj.setBlockToAir(this.oldX, this.oldY, this.oldZ);
            }

            if (!this.onGround && (this.worldObj.getBlock(i, j - 1, k) != ConfigBlocks.blockFluxGoo || this.worldObj.getBlockMetadata(i, j - 1, k) < 4)) {
               if (this.fallTime > 100 && !this.worldObj.isRemote && (j < 1 || j > 256) || this.fallTime > 600) {
                  this.setDead();
               }
            } else {
               this.motionX *= 0.7F;
               this.motionZ *= 0.7F;
               this.motionY *= -0.5F;
               if (this.worldObj.getBlock(i, j, k) != Blocks.piston && this.worldObj.getBlock(i, j, k) != Blocks.piston_extension && this.worldObj.getBlock(i, j, k) != Blocks.piston_head) {
                  this.worldObj.playSoundAtEntity(this, "thaumcraft:gore", 0.5F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
                  this.setDead();
                  if (this.canPlace(i, j, k) && !BlockTaint.canFallBelow(this.worldObj, i, j - 1, k) && this.worldObj.setBlock(i, j, k, this.block, this.metadata, 3) && this.block instanceof BlockTaint) {
                     ((BlockTaint)this.block).onFinishFalling(this.worldObj, i, j, k, this.metadata);
                  }
               }
            }
         } else if (this.onGround || this.fallTime == 1) {
            for(int j = 0; j < 10; ++j) {
               Thaumcraft.proxy.taintLandFX(this);
            }
         }
      } else {
         this.setDead();
      }

   }

   private boolean canPlace(int i, int j, int k) {
      return this.worldObj.getBlock(i, j, k) == ConfigBlocks.blockTaintFibres || this.worldObj.getBlock(i, j, k) == ConfigBlocks.blockFluxGoo || this.worldObj.canPlaceEntityOnSide(this.block, i, j, k, true, 1, null, null);
   }

   protected void fall(float par1) {
   }

   protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setInteger("TileID", Block.getIdFromBlock(this.block));
      par1NBTTagCompound.setByte("Data", (byte)this.metadata);
      par1NBTTagCompound.setByte("Time", (byte)this.fallTime);
      par1NBTTagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
      par1NBTTagCompound.setInteger("FallHurtMax", this.fallHurtMax);
      par1NBTTagCompound.setInteger("OldX", this.oldX);
      par1NBTTagCompound.setInteger("OldY", this.oldY);
      par1NBTTagCompound.setInteger("OldZ", this.oldZ);
   }

   protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      if (par1NBTTagCompound.hasKey("TileID")) {
         this.block = Block.getBlockById(par1NBTTagCompound.getInteger("TileID"));
      }

      this.metadata = par1NBTTagCompound.getByte("Data") & 255;
      this.fallTime = par1NBTTagCompound.getByte("Time") & 255;
      this.oldX = par1NBTTagCompound.getInteger("OldX");
      this.oldY = par1NBTTagCompound.getInteger("OldY");
      this.oldZ = par1NBTTagCompound.getInteger("OldZ");
      if (par1NBTTagCompound.hasKey("HurtEntities")) {
         this.fallHurtAmount = par1NBTTagCompound.getFloat("FallHurtAmount");
         this.fallHurtMax = par1NBTTagCompound.getInteger("FallHurtMax");
      }

      if (this.block == null) {
         this.block = Blocks.sand;
      }

   }

   public void addEntityCrashInfo(CrashReportCategory par1CrashReportCategory) {
      super.addEntityCrashInfo(par1CrashReportCategory);
      par1CrashReportCategory.addCrashSection("Immitating block ID", Block.getIdFromBlock(this.block));
      par1CrashReportCategory.addCrashSection("Immitating block data", this.metadata);
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   @SideOnly(Side.CLIENT)
   public World getWorld() {
      return this.worldObj;
   }

   @SideOnly(Side.CLIENT)
   public boolean canRenderOnFire() {
      return false;
   }
}
