package thaumcraft.common.entities.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.Level;
import thaumcraft.common.config.ConfigItems;

import java.util.List;

public class EntityInhabitedZombie extends EntityZombie {
   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0F);
      this.getEntityAttribute(field_110186_bp).setBaseValue(0.0F);
   }

   public EntityInhabitedZombie(World world) {
      super(world);
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityCultist.class, 0, true));
   }

   public void onKillEntity(EntityLivingBase par1EntityLivingBase) {
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
      float diff = this.level().difficultySetting == Difficulty.HARD ? 0.9F : 0.6F;
      this.setCurrentItemOrArmor(4, new ItemStack(ConfigItems.itemHelmetCultistPlate));
      if (this.rand.nextFloat() <= diff) {
         this.setCurrentItemOrArmor(3, new ItemStack(ConfigItems.itemChestCultistPlate));
      }

      if (this.rand.nextFloat() <= diff) {
         this.setCurrentItemOrArmor(2, new ItemStack(ConfigItems.itemLegsCultistPlate));
      }

      return p_110161_1_;
   }

   protected Item getDropItem() {
      return Item.getItemById(0);
   }

   protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
   }

   protected void onDeathUpdate() {
      if (Platform.getEnvironment() != Env.CLIENT) {
         EntityEldritchCrab crab = new EntityEldritchCrab(this.level());
         crab.setPositionAndRotation(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, this.rotationYaw, this.rotationPitch);
         crab.setHelm(true);
         this.level().spawnEntityInWorld(crab);
         if ((this.recentlyHit > 0 || this.isPlayer()) && this.func_146066_aG() && this.level().getGameRules().getGameRuleBooleanValue("doMobLoot")) {
            int i = this.getExperiencePoints(this.attackingPlayer);

            while(i > 0) {
               int j = EntityXPOrb.getXPSplit(i);
               i -= j;
               this.level().spawnEntityInWorld(new EntityXPOrb(this.level(), this.posX, this.posY, this.posZ, j));
            }
         }
      }

      for(int i = 0; i < 20; ++i) {
         double d2 = this.rand.nextGaussian() * 0.02;
         double d0 = this.rand.nextGaussian() * 0.02;
         double d1 = this.rand.nextGaussian() * 0.02;
         this.level().spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1);
      }

      this.setDead();
   }

   public void onDeath(DamageSource p_70645_1_) {
   }

   protected String getLivingSound() {
      return "thaumcraft:crabtalk";
   }

   protected String getHurtSound() {
      return "game.hostile.hurt";
   }

   public boolean getCanSpawnHere() {
      List ents = this.level().getEntitiesWithinAABB(EntityInhabitedZombie.class, AxisAlignedBB.getBoundingBox(this.posX, this.posY, this.posZ, this.posX + (double)1.0F, this.posY + (double)1.0F, this.posZ + (double)1.0F).expand(32.0F, 16.0F, 32.0F));
      return ents.isEmpty() && super.getCanSpawnHere();
   }
}
