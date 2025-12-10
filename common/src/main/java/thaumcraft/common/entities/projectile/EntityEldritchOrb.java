package thaumcraft.common.entities.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class EntityEldritchOrb extends EntityThrowable {
   public EntityEldritchOrb(Level par1World) {
      super(par1World);
   }

   public EntityEldritchOrb(Level par1World, EntityLivingBase par2EntityLiving) {
      super(par1World, par2EntityLiving);
   }

   protected float getGravityVelocity() {
      return 0.0F;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.ticksExisted > 100) {
         this.setDead();
      }

   }

   public void handleHealthUpdate(byte b) {
      if (b == 16) {
         if ((Platform.getEnvironment() == Env.CLIENT)) {
            for(int a = 0; a < 30; ++a) {
               float fx = (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F;
               float fy = (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F;
               float fz = (this.level().rand.nextFloat() - this.level().rand.nextFloat()) * 0.3F;
               Thaumcraft.proxy.wispFX3(this.level(), this.posX + (double)fx, this.posY + (double)fy, this.posZ + (double)fz, this.posX + (double)(fx * 8.0F), this.posY + (double)(fy * 8.0F), this.posZ + (double)(fz * 8.0F), 0.3F, 5, true, 0.02F);
            }
         }
      } else {
         super.handleHealthUpdate(b);
      }

   }

   protected void onImpact(HitResult mop) {
      if (Platform.getEnvironment() != Env.CLIENT && this.getThrower() != null) {
         List list = this.level().getEntitiesWithinAABBExcludingEntity(this.getThrower(), this.boundingBox.expand(2.0F, 2.0F, 2.0F));

          for (Object o : list) {
              Entity entity1 = (Entity) o;
              if (entity1 instanceof EntityLivingBase && !((EntityLivingBase) entity1).isEntityUndead()) {
                  entity1.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), (float) this.getThrower().getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue() * 0.666F);

                  try {
                      ((EntityLivingBase) entity1).addPotionEffect(new PotionEffect(Potion.weakness.id, 160, 0));
                  } catch (Exception ignored) {
                  }
              }
          }

         this.level().playSoundAtEntity(this, "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);
         this.ticksExisted = 100;
         this.level().setEntityState(this, (byte)16);
      }

   }

   public float getShadowSize() {
      return 0.1F;
   }
}
