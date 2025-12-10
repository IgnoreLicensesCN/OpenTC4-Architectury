package thaumcraft.common.entities.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.world.level.Level;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

public class EntityTaintSporeSwarmer extends EntityTaintSpore {
   int spawnCounter = 500;

   public EntityTaintSporeSwarmer(Level par1World) {
      super(par1World);
      this.setSporeSize(10);
   }

   public void setSporeSize(int par1) {
      this.dataWatcher.updateObject(16, (byte) par1);
      this.setSize(1.0F, 1.0F);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.experienceValue = par1;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(75.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0F);
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if ((Platform.getEnvironment() == Env.CLIENT)) {
         this.sploosh(10);
      }

      return super.attackEntityFrom(par1DamageSource, par2);
   }

   protected void sporeOnUpdate() {
      this.func_145771_j(this.posX, this.posY, this.posZ);
      if (this.spawnCounter > 0) {
         --this.spawnCounter;
      }

      if (this.spawnCounter <= 0 && this.level().getClosestVulnerablePlayerToEntity(this, 16.0F) != null) {
         this.spawnCounter = 500;
         this.swarmBurst(1);
      }

      if ((Platform.getEnvironment() == Env.CLIENT)) {
         for(int a = 0; a < this.swarm.size(); ++a) {
            if (this.swarm.get(a) == null || ((Entity)this.swarm.get(a)).isDead) {
               this.swarm.remove(a);
               break;
            }
         }

         if (this.swarm.size() < (500 - this.spawnCounter) / 25) {
            this.swarm.add(Thaumcraft.proxy.swarmParticleFX(this.level(), this, 0.1F, 10.0F, 0.0F));
         }
      }

      if (this.deathTime == 1) {
         this.swarmBurst(1);
      }

   }

   public void onCollideWithPlayer(Player par1Player) {
   }

   protected void swarmBurst(int amt) {
      if (Platform.getEnvironment() != Env.CLIENT) {
         this.level().playSoundAtEntity(this, "thaumcraft:gore", 1.0F, 0.9F + this.level().rand.nextFloat() * 0.1F);

         for(int a = 0; a < amt; ++a) {
            EntityTaintSwarm swarm = new EntityTaintSwarm(this.level());
            swarm.setLocationAndAngles(this.posX, this.posY + (double)0.5F, this.posZ, this.level().rand.nextFloat() * 360.0F, 0.0F);
            this.level().spawnEntityInWorld(swarm);
         }

         this.level().setEntityState(this, (byte)6);
      }

   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if (par1 == 6) {
         this.spawnCounter = 500;
         this.sploosh(25);

          for (Object o : this.swarm) {
              ((Entity) o).setDead();
          }

         this.swarm.clear();
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public int getTalkInterval() {
       return super.getTalkInterval();
   }

   protected String getLivingSound() {
      return "thaumcraft:roots";
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posZ);
      if (this.level().blockExists(i, 0, j)) {
         double d0 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66;
         int k = MathHelper.floor_double(this.posY - (double)this.yOffset + d0);
         return this.level().getLightBrightnessForSkyBlocks(i, k, j, 0);
      } else {
         return 0;
      }
   }

   public float getBrightness(float par1) {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posZ);
      if (this.level().blockExists(i, 0, j)) {
         double d0 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66;
         int k = MathHelper.floor_double(this.posY - (double)this.yOffset + d0);
         return this.level().getLightBrightness(i, k, j);
      } else {
         return 0.0F;
      }
   }

   protected Item getDropItem() {
       return super.getDropItem();
   }

   protected void dropFewItems(boolean flag, int i) {
      for(int a = 0; a <= 1; ++a) {
         if (this.level().rand.nextBoolean()) {
            this.entityDropItem(new ItemStack(ThaumcraftItems.TAINTED_GOO,1), this.height / 2.0F);
         } else {
            this.entityDropItem(new ItemStack(ThaumcraftItems.TAINT_TENDRIL,1), this.height / 2.0F);
         }
      }

   }
}
