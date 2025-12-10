package thaumcraft.common.entities.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class EntityAlumentum extends EntityThrowable {
   public EntityAlumentum(World par1World) {
      super(par1World);
   }

   public EntityAlumentum(World par1World, EntityLivingBase par2EntityLiving) {
      super(par1World, par2EntityLiving);
   }

   public EntityAlumentum(World par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6);
   }

   protected float func_70182_d() {
      return 0.75F;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.worldObj.isRemote) {
         for(int a = 0; a < 3; ++a) {
            Thaumcraft.proxy.wispFX2(this.worldObj, this.posX + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F), this.posY + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F), this.posZ + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F), 0.3F, 5, true, true, 0.02F);
            double x2 = (this.posX + this.prevPosX) / (double)2.0F + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F);
            double y2 = (this.posY + this.prevPosY) / (double)2.0F + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F);
            double z2 = (this.posZ + this.prevPosZ) / (double)2.0F + (double)((this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F);
            Thaumcraft.proxy.wispFX2(this.worldObj, x2, y2, z2, 0.3F, 5, true, true, 0.02F);
            Thaumcraft.proxy.sparkle((float)this.posX + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.1F, (float)this.posY + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.1F, (float)this.posZ + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.1F, 6);
         }
      }

   }

   protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
      if (!this.worldObj.isRemote) {
         boolean var2 = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
         this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 1.66F, var2);
         this.setDead();
      }

   }

   public float getShadowSize() {
      return 0.1F;
   }
}
