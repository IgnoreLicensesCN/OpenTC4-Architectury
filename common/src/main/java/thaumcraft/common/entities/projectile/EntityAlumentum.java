package thaumcraft.common.entities.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;

public class EntityAlumentum extends EntityThrowable {
   public EntityAlumentum(Level par1World) {
      super(par1World);
   }

   public EntityAlumentum(Level par1World, EntityLivingBase par2EntityLiving) {
      super(par1World, par2EntityLiving);
   }

   public EntityAlumentum(Level par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6);
   }

   protected float func_70182_d() {
      return 0.75F;
   }

   public void onUpdate() {
      super.onUpdate();
      if ((Platform.getEnvironment() == Env.CLIENT)) {
         for(int a = 0; a < 3; ++a) {
            Thaumcraft.proxy.wispFX2(this.level(), this.posX + (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F), this.posY + (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F), this.posZ + (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F), 0.3F, 5, true, true, 0.02F);
            double x2 = (this.posX + this.prevPosX) / (double)2.0F + (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F);
            double y2 = (this.posY + this.prevPosY) / (double)2.0F + (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F);
            double z2 = (this.posZ + this.prevPosZ) / (double)2.0F + (double)((this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F);
            Thaumcraft.proxy.wispFX2(this.level(), x2, y2, z2, 0.3F, 5, true, true, 0.02F);
            ClientFXUtils.sparkle((float)this.posX + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.1F, (float)this.posY + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.1F, (float)this.posZ + (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.1F, 6);
         }
      }

   }

   protected void onImpact(HitResult par1HitResult) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         boolean var2 = this.level().getGameRules().getGameRuleBooleanValue("mobGriefing");
         this.level().createExplosion(null, this.posX, this.posY, this.posZ, 1.66F, var2);
         this.setDead();
      }

   }

   public float getShadowSize() {
      return 0.1F;
   }
}
