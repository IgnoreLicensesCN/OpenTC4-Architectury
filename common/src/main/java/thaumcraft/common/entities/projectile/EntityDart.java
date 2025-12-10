package thaumcraft.common.entities.projectile;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDart extends EntityArrow implements IProjectile, IEntityAdditionalSpawnData {
   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private int inTile = 0;
   private int inData = 0;
   private boolean inGround = false;
   private int ticksInGround;
   private int ticksInAir = 0;
   private double damage = 1.0F;
   private int knockbackStrength;
   private boolean first = true;

   public void writeSpawnData(ByteBuf data) {
      data.writeDouble(this.motionX);
      data.writeDouble(this.motionY);
      data.writeDouble(this.motionZ);
      data.writeFloat(this.rotationYaw);
      data.writeFloat(this.rotationPitch);
   }

   public void readSpawnData(ByteBuf data) {
      this.motionX = data.readDouble();
      this.motionY = data.readDouble();
      this.motionZ = data.readDouble();
      this.rotationYaw = data.readFloat();
      this.rotationPitch = data.readFloat();
   }

   public EntityDart(World par1World) {
      super(par1World);
      this.renderDistanceWeight = 10.0F;
      this.setSize(0.5F, 0.5F);
   }

   public EntityDart(World par1World, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving, float par4, float par5) {
      super(par1World);
      this.renderDistanceWeight = 10.0F;
      this.shootingEntity = par2EntityLiving;
      this.posY = par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight() - (double)0.1F;
      double var6 = par3EntityLiving.posX - par2EntityLiving.posX;
      double var8 = par3EntityLiving.posY + (double)par3EntityLiving.getEyeHeight() - (double)0.7F - this.posY;
      double var10 = par3EntityLiving.posZ - par2EntityLiving.posZ;
      double var12 = MathHelper.sqrt_double(var6 * var6 + var10 * var10);
      if (var12 >= 1.0E-7) {
         float var14 = (float)(Math.atan2(var10, var6) * (double)180.0F / Math.PI) - 90.0F;
         float var15 = (float)(-(Math.atan2(var8, var12) * (double)180.0F / Math.PI));
         double var16 = var6 / var12;
         double var18 = var10 / var12;
         this.setLocationAndAngles(par2EntityLiving.posX + var16 / (double)5.0F, this.posY, par2EntityLiving.posZ + var18 / (double)5.0F, var14, var15);
         this.yOffset = 0.0F;
         float var20 = (float)var12 * 0.2F;
         this.setThrowableHeading(var6, var8 + (double)var20, var10, par4, par5);
      }

   }

   public void onUpdate() {
      if (this.first && this.worldObj.isRemote) {
         this.first = false;

         for(int a = 0; a < 5; ++a) {
            this.worldObj.spawnParticle("smoke", this.posX - this.motionX / (double)1.5F, this.posY - this.motionY / (double)1.5F, this.posZ - this.motionZ / (double)1.5F, this.motionX / (double)9.0F + this.rand.nextGaussian() * 0.01, this.motionY / (double)9.0F + this.rand.nextGaussian() * 0.01, this.motionZ / (double)9.0F + this.rand.nextGaussian() * 0.01);
         }
      }

      super.onUpdate();
   }
}
