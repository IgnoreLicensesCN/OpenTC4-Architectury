package thaumcraft.common.entities.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import tc4tweak.ConfigurationHandler;
import thaumcraft.codechicken.lib.math.MathHelper;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.EntityUtils;

public class EntityShockOrb extends EntityThrowable {
   public int area = 4;
   public int damage = 5;

   public EntityShockOrb(World par1World) {
      super(par1World);
   }

   public EntityShockOrb(World par1World, EntityLivingBase par2EntityLiving) {
      super(par1World, par2EntityLiving);
   }

   protected float getGravityVelocity() {
      return 0.05F;
   }

   public static boolean canEarthShockHurt(Entity entity) {
      switch (ConfigurationHandler.INSTANCE.getEarthShockHarmMode()) {
         case OnlyLiving:
            return entity instanceof EntityLivingBase;
         case ExceptItemXp:
            return !(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb);
         case AllEntity:
         default:
            return true;
      }
   }
   protected void onImpact(MovingObjectPosition mop) {
      if (!this.worldObj.isRemote) {
         for(Entity e : EntityUtils.getEntitiesInRange(this.worldObj, this.posX, this.posY, this.posZ, this, Entity.class, this.area)) {

            if (EntityUtils.canEntityBeSeen(this, e)
                    && canEarthShockHurt(e)
            ) {
               e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), (float)this.damage);
            }
         }

         for(int a = 0; a < 20; ++a) {
            int xx = MathHelper.floor_double(this.posX) + this.rand.nextInt(this.area) - this.rand.nextInt(this.area);
            int yy = MathHelper.floor_double(this.posY) + this.area;

            int zz  = MathHelper.floor_double(this.posZ)
                    + this.rand.nextInt(this.area)
                    - this.rand.nextInt(this.area);
            while (
                    this.worldObj.isAirBlock(xx, yy, zz)
                    && (yy > MathHelper.floor_double(this.posY) - this.area)
            ) {
               yy -= 1;
            }

            if (this.worldObj.isAirBlock(xx, yy + 1, zz) && !this.worldObj.isAirBlock(xx, yy, zz) && this.worldObj.getBlock(xx, yy + 1, zz) != ConfigBlocks.blockAiry && EntityUtils.canEntityBeSeen(this, (double)xx + (double)0.5F, (double)yy + (double)1.5F, (double)zz + (double)0.5F)) {
               this.worldObj.setBlock(xx, yy + 1, zz, ConfigBlocks.blockAiry, 10, 3);
            }
         }
      }

      Thaumcraft.proxy.burst(this.worldObj, this.posX, this.posY, this.posZ, 3.0F);
      this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "thaumcraft:shock", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
      this.setDead();
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.ticksExisted > 500) {
         this.setDead();
      }

   }

   public float getShadowSize() {
      return 0.1F;
   }

   public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
      if (this.isEntityInvulnerable()) {
         return false;
      } else {
         this.setBeenAttacked();
         if (p_70097_1_.getEntity() != null) {
            Vec3 vec3 = p_70097_1_.getEntity().getLookVec();
            if (vec3 != null) {
               this.motionX = vec3.xCoord;
               this.motionY = vec3.yCoord;
               this.motionZ = vec3.zCoord;
               this.motionX *= 0.9;
               this.motionY *= 0.9;
               this.motionZ *= 0.9;
               this.worldObj.playSoundAtEntity(this, "thaumcraft:zap", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            }

            return true;
         } else {
            return false;
         }
      }
   }
}
