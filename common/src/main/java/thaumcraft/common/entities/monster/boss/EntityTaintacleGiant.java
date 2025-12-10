package thaumcraft.common.entities.monster.boss;

import net.minecraft.world.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.level.Level;
import thaumcraft.api.entities.ITaintedMob;
import thaumcraft.common.entities.monster.EntityTaintacle;
import thaumcraft.common.items.ThaumcraftItems;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.ArrayList;

public class EntityTaintacleGiant extends EntityTaintacle implements ITaintedMob, IBossDisplayData {
   public EntityTaintacleGiant(Level par1World) {
      super(par1World);
      this.setSize(1.1F, 6.0F);
      this.experienceValue = 20;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(125.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(9.0F);
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
      EntityUtils.makeChampion(this, true);
      return data;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.getAnger() > 0) {
         this.setAnger(this.getAnger() - 1);
      }

      if ((Platform.getEnvironment() == Env.CLIENT) && this.rand.nextInt(15) == 0 && this.getAnger() > 0) {
         double d0 = this.rand.nextGaussian() * 0.02;
         double d1 = this.rand.nextGaussian() * 0.02;
         double d2 = this.rand.nextGaussian() * 0.02;
         this.level().spawnParticle("angryVillager", this.posX + (double)(this.rand.nextFloat() * this.width) - (double)this.width / (double)2.0F, this.boundingBox.minY + (double)this.height + (double)this.rand.nextFloat() * (double)0.5F, this.posZ + (double)(this.rand.nextFloat() * this.width) - (double)this.width / (double)2.0F, d0, d1, d2);
      }

      if (Platform.getEnvironment() != Env.CLIENT && this.ticksExisted % 30 == 0) {
         this.heal(1.0F);
      }

   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(14, (short) 0);
   }

   public int getAnger() {
      return this.dataWatcher.getWatchableObjectShort(14);
   }

   public void setAnger(int par1) {
      this.dataWatcher.updateObject(14, (short)par1);
   }

   public boolean getCanSpawnHere() {
      return false;
   }

   protected void dropFewItems(boolean flag, int i) {
      ArrayList<Entity> ents = EntityUtils.getEntitiesInRange(this.level(), this.posX, this.posY, this.posZ, this, EntityTaintacleGiant.class, 48.0F);
      if (ents == null || ents.isEmpty()) {
         EntityUtils.entityDropSpecialItem(this, new ItemStack(ThaumcraftItems.PRIME_PEARL), this.height / 2.0F);
      }

   }

   protected boolean canDespawn() {
      return false;
   }

   public boolean canBreatheUnderwater() {
      return true;
   }

   protected int decreaseAirSupply(int air) {
      return air;
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      if (Platform.getEnvironment() != Env.CLIENT && damage > 35.0F) {
         if (this.getAnger() == 0) {
            try {
               this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, (int)(damage / 15.0F)));
               this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 200, (int)(damage / 40.0F)));
               this.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 200, (int)(damage / 40.0F)));
               this.setAnger(200);
            } catch (Exception ignored) {
            }

            if (source.getEntity() != null && source.getEntity() instanceof Player) {
               ((Player)source.getEntity()).addChatMessage(new ChatComponentText(this.getCommandSenderName() + " " + StatCollector.translateToLocal("tc.boss.enrage")));
            }
         }

         damage = 35.0F;
      }

      return super.attackEntityFrom(source, damage);
   }
}
