package thaumcraft.common.entities.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.ThaumcraftWorldGenerator;

import java.util.List;

public class EntityBottleTaint extends EntityThrowable {
   public EntityBottleTaint(World p_i1788_1_) {
      super(p_i1788_1_);
   }

   public EntityBottleTaint(World p_i1790_1_, EntityLivingBase p_i1790_2) {
      super(p_i1790_1_, p_i1790_2);
   }

   protected float getGravityVelocity() {
      return 0.05F;
   }

   protected float func_70182_d() {
      return 0.5F;
   }

   protected float func_70183_g() {
      return -20.0F;
   }

   protected void onImpact(HitResult p_70184_1_) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         List ents = this.level().getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ).expand(5.0F, 5.0F, 5.0F));
         if (!ents.isEmpty()) {
            for(Object ent : ents) {
               EntityLivingBase el = (EntityLivingBase)ent;
               if (!(el instanceof ITaintedMob) && !el.isEntityUndead()) {
                  el.addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 100, 0, false));
               }
            }
         }

         int x = (int)this.posX;
         int y = (int)this.posY;
         int z = (int)this.posZ;

         for(int a = 0; a < 10; ++a) {
            int xx = x + (int)((this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F);
            int zz = z + (int)((this.rand.nextFloat() - this.rand.nextFloat()) * 5.0F);
            if (this.level().rand.nextBoolean() && this.level().getBiomeGenForCoords(xx, zz) != ThaumcraftWorldGenerator.biomeTaint) {
               Utils.setBiomeAt(this.level(), xx, zz, ThaumcraftWorldGenerator.biomeTaint);
               if (this.level().isBlockNormalCubeDefault(xx, y - 1, zz, false) && this.level().getBlock(xx, y, zz).isReplaceable(this.level(), xx, y, zz)) {
                  this.level().setBlock(xx, y, zz, ConfigBlocks.blockTaintFibres, 0, 3);
               }
            }
         }

         this.setDead();
      } else {
         for(int a = 0; a < Thaumcraft.proxy.particleCount(100); ++a) {
            Thaumcraft.proxy.taintsplosionFX(this);
         }

         Thaumcraft.proxy.bottleTaintBreak(this.level(), this.posX, this.posY, this.posZ);
      }

   }
}
