package thaumcraft.common.entities.monster;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

import java.util.ArrayList;

public class EntityTaintSpore extends EntityMob implements ITaintedMob, IEntityAdditionalSpawnData {
   public ArrayList swarm = new ArrayList<>();
   protected int growth = 0;
   public float displaySize = 0.0F;

   public EntityTaintSpore(World par1World) {
      super(par1World);
      this.setSporeSize(2);
   }

   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte) 1);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("Size", this.getSporeSize() - 1);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setSporeSize(par1NBTTagCompound.getInteger("Size") + 1);
   }

   public void setSporeSize(int par1) {
      this.dataWatcher.updateObject(16, (byte) par1);
      float size = Math.max(0.15F * (float)par1, 0.5F);
      this.setSize(size, size);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.experienceValue = par1;
   }

   public int getSporeSize() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0F);
   }

   public float getShadowSize() {
      return 0.0F;
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public boolean canBePushed() {
      return false;
   }

   public void moveEntity(double par1, double par3, double par5) {
      par1 = 0.0F;
      par5 = 0.0F;
      if (par3 > (double)0.0F) {
         par3 = 0.0F;
      }

      int x = MathHelper.floor_double(this.posX);
      int y = MathHelper.floor_double(this.boundingBox.minY) - 1;
      int z = MathHelper.floor_double(this.posZ);
      if (this.worldObj.getBlock(x, y, z) != ConfigBlocks.blockTaintFibres || this.worldObj.getBlockMetadata(x, y, z) != 4) {
         super.moveEntity(par1, par3, par5);
      }
   }

   protected void updateEntityActionState() {
   }

   public boolean isInRangeToRenderDist(double par1) {
      return par1 < (double)4096.0F;
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote && this.ticksExisted % 20 == 0 && this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ)).biomeID != Config.biomeTaintID) {
         this.damageEntity(DamageSource.starve, 1.0F);
      }

      this.sporeOnUpdate();
   }

   protected void sporeOnUpdate() {
      if (this.getSporeSize() < 10 && this.growth++ == 1200) {
         this.setSporeSize(this.getSporeSize() + 1);
         this.growth = 0;
      }

      if (this.worldObj.isRemote) {
         if (this.displaySize < (float)this.getSporeSize()) {
            this.displaySize += 0.02F;
         }

         for(int a = 0; a < this.swarm.size(); ++a) {
            if (this.swarm.get(a) == null || ((Entity)this.swarm.get(a)).isDead) {
               this.swarm.remove(a);
               break;
            }
         }

         if (this.swarm.size() < this.getSporeSize() / 3) {
            this.swarm.add(Thaumcraft.proxy.swarmParticleFX(this.worldObj, this, 0.1F, 10.0F, 0.0F));
         }
      }

      int x = MathHelper.floor_double(this.posX);
      int y = MathHelper.floor_double(this.boundingBox.minY) - 1;
      int z = MathHelper.floor_double(this.posZ);
      if (this.worldObj.getBlock(x, y, z) == ConfigBlocks.blockTaintFibres && this.worldObj.getBlockMetadata(x, y, z) == 4) {
         if (this.deathTime > 0) {
            this.spiderBurst();
         }
      } else {
         this.spiderBurst();
      }

   }

   public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
      this.spiderBurst();
   }

   protected void spiderBurst() {
      if (!this.worldObj.isRemote) {
         this.worldObj.playSoundAtEntity(this, "thaumcraft:gore", 1.0F, 0.9F + this.worldObj.rand.nextFloat() * 0.1F);
         int q = this.getSporeSize() / 3 + this.worldObj.rand.nextInt(this.getSporeSize() / 2 + 1);

         for(int a = 0; a < q; ++a) {
            EntityTaintSpider spiderling = new EntityTaintSpider(this.worldObj);
            spiderling.setLocationAndAngles(this.posX + (double)this.worldObj.rand.nextFloat() - (double)this.worldObj.rand.nextFloat(), this.posY + (double)this.worldObj.rand.nextFloat(), this.posZ + (double)this.worldObj.rand.nextFloat() - (double)this.worldObj.rand.nextFloat(), this.worldObj.rand.nextFloat() * 360.0F, 0.0F);
            this.worldObj.spawnEntityInWorld(spiderling);
         }

         int x = MathHelper.floor_double(this.posX);
         int y = MathHelper.floor_double(this.boundingBox.minY) - 1;
         int z = MathHelper.floor_double(this.posZ);
         if (this.worldObj.getBlock(x, y, z) == ConfigBlocks.blockTaintFibres && this.worldObj.getBlockMetadata(x, y, z) == 4) {
            this.worldObj.setBlockMetadataWithNotify(x, y, z, 3, 3);
         }

         this.setDead();
      } else {
         this.sploosh(50);
      }

   }

   protected void sploosh(int amt) {
      for(int a = 0; a < amt; ++a) {
         Thaumcraft.proxy.splooshFX(this);
      }

   }

   public void writeSpawnData(ByteBuf data) {
      data.writeFloat((float)this.getSporeSize());
   }

   public void readSpawnData(ByteBuf data) {
      try {
         this.displaySize = data.readFloat();
      } catch (Exception ignored) {
      }

   }

   protected float getSoundVolume() {
      return 0.1F;
   }

   public int getTalkInterval() {
      return 200;
   }

   protected String getLivingSound() {
      return "thaumcraft:swarm";
   }

   protected String getHurtSound() {
      return "thaumcraft:gore";
   }

   protected String getDeathSound() {
      return "thaumcraft:gore";
   }

   protected Item getDropItem() {
      return ConfigItems.itemResource;
   }

   protected void dropFewItems(boolean flag, int i) {
      if (this.worldObj.rand.nextBoolean()) {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 11), this.height / 2.0F);
      } else {
         this.entityDropItem(new ItemStack(ConfigItems.itemResource, 1, 12), this.height / 2.0F);
      }

   }
}
