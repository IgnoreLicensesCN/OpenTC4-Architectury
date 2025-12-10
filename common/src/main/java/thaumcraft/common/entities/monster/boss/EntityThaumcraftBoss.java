package thaumcraft.common.entities.monster.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import thaumcraft.api.entities.IEldritchMob;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityThaumcraftBoss extends EntityMob implements IBossDisplayData {
   HashMap<Integer,Integer> aggro = new HashMap<>();
   int spawnTimer = 0;

   public EntityThaumcraftBoss(World world) {
      super(world);
      this.experienceValue = 50;
   }

   public void readEntityFromNBT(NBTTagCompound nbt) {
      super.readEntityFromNBT(nbt);
      if (nbt.hasKey("HomeD")) {
         this.setHomeArea(nbt.getInteger("HomeX"), nbt.getInteger("HomeY"), nbt.getInteger("HomeZ"), nbt.getInteger("HomeD"));
      }

   }

   public void writeEntityToNBT(NBTTagCompound nbt) {
      super.writeEntityToNBT(nbt);
      if (this.getHomePosition() != null && this.func_110174_bM() > 0.0F) {
         nbt.setInteger("HomeD", (int)this.func_110174_bM());
         nbt.setInteger("HomeX", this.getHomePosition().posX);
         nbt.setInteger("HomeY", this.getHomePosition().posY);
         nbt.setInteger("HomeZ", this.getHomePosition().posZ);
      }

   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(0.95);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0F);
   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(14, (short) 0);
   }

   protected void updateAITasks() {
      if (this.getSpawnTimer() == 0) {
         super.updateAITasks();
      }

      if (this.getAttackTarget() != null && this.getAttackTarget().isDead) {
         this.setAttackTarget(null);
      }

   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
      this.setHomeArea((int)this.posX, (int)this.posY, (int)this.posZ, 24);
      return data;
   }

   public int getAnger() {
      return this.dataWatcher.getWatchableObjectShort(14);
   }

   public void setAnger(int par1) {
      this.dataWatcher.updateObject(14, (short)par1);
   }

   public int getSpawnTimer() {
      return this.spawnTimer;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.getSpawnTimer() > 0) {
         --this.spawnTimer;
      }

      if (this.getAnger() > 0) {
         this.setAnger(this.getAnger() - 1);
      }

      if (this.worldObj.isRemote && this.rand.nextInt(15) == 0 && this.getAnger() > 0) {
         double d0 = this.rand.nextGaussian() * 0.02;
         double d1 = this.rand.nextGaussian() * 0.02;
         double d2 = this.rand.nextGaussian() * 0.02;
         this.worldObj.spawnParticle("angryVillager", this.posX + (double)(this.rand.nextFloat() * this.width) - (double)this.width / (double)2.0F, this.boundingBox.minY + (double)this.height + (double)this.rand.nextFloat() * (double)0.5F, this.posZ + (double)(this.rand.nextFloat() * this.width) - (double)this.width / (double)2.0F, d0, d1, d2);
      }

      if (!this.worldObj.isRemote) {
         if (this.ticksExisted % 30 == 0) {
            this.heal(1.0F);
         }

         if (this.getAttackTarget() != null && this.ticksExisted % 20 == 0) {
            ArrayList<Integer> dl = new ArrayList<>();
            int players = 0;
            int hei = this.getAttackTarget().getEntityId();
            int ad = this.aggro.getOrDefault(hei, 0);
            int ld = ad;
            Entity newTarget = null;

            for(Integer ei : this.aggro.keySet()) {
               int ca = this.aggro.get(ei);
               if (ca > ad + 25 && (double)ca > (double)ad * 1.1 && ca > ld) {
                  newTarget = this.worldObj.getEntityByID(hei);
                  if (newTarget != null && !newTarget.isDead && !(this.getDistanceSqToEntity(newTarget) > (double)16384.0F)) {
                     hei = ei;
                     ld = ei;
                     if (newTarget instanceof EntityPlayer) {
                        ++players;
                     }
                  } else {
                     dl.add(ei);
                  }
               }
            }

            for(Integer ei : dl) {
               this.aggro.remove(ei);
            }

            if (newTarget != null && hei != this.getAttackTarget().getEntityId()) {
               this.setAttackTarget((EntityLivingBase)newTarget);
            }

            float om = this.getMaxHealth();
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            IAttributeInstance iattributeinstance2 = this.getEntityAttribute(SharedMonsterAttributes.attackDamage);

            for(int a = 0; a < 5; ++a) {
               iattributeinstance2.removeModifier(EntityUtils.DMGBUFF[a]);
               iattributeinstance.removeModifier(EntityUtils.HPBUFF[a]);
            }

            for(int a = 0; a < Math.min(5, players - 1); ++a) {
               iattributeinstance.applyModifier(EntityUtils.HPBUFF[a]);
               iattributeinstance2.applyModifier(EntityUtils.DMGBUFF[a]);
            }

            double mm = this.getMaxHealth() / om;
            this.setHealth((float)((double)this.getHealth() * mm));
         }
      }

   }

   public boolean isEntityInvulnerable() {
      return super.isEntityInvulnerable() || this.getSpawnTimer() > 0;
   }

   public boolean canBreatheUnderwater() {
      return true;
   }

   public boolean canBePushed() {
      return super.canBePushed() && !this.isEntityInvulnerable();
   }

   protected int decreaseAirSupply(int air) {
      return air;
   }

   public void setInWeb() {
   }

   public boolean canPickUpLoot() {
      return false;
   }

   protected boolean isAIEnabled() {
      return true;
   }

   protected void addRandomArmor() {
   }

   protected void enchantEquipment() {
   }

   protected boolean canDespawn() {
      return false;
   }

   public boolean isOnSameTeam(EntityLivingBase el) {
      return el instanceof IEldritchMob;
   }

   protected void dropFewItems(boolean flag, int fortune) {
      EntityUtils.entityDropSpecialItem(this, new ItemStack(ConfigItems.itemEldritchObject, 1, 3), this.height / 2.0F);
      this.entityDropItem(new ItemStack(ConfigItems.itemLootbag, 1, 2), 1.5F);
   }

   protected void dropRareDrop(int fortune) {
      super.dropRareDrop(fortune);
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      if (!this.worldObj.isRemote) {
         if (source.getEntity() != null && source.getEntity() instanceof EntityLivingBase) {
            int target = source.getEntity().getEntityId();
            int ad = (int)damage;
            if (this.aggro.containsKey(target)) {
               ad += this.aggro.get(target);
            }

            this.aggro.put(target, ad);
         }

         if (damage > 35.0F) {
            if (this.getAnger() == 0) {
               try {
                  this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, (int)(damage / 15.0F)));
                  this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 200, (int)(damage / 40.0F)));
                  this.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 200, (int)(damage / 40.0F)));
                  this.setAnger(200);
               } catch (Exception ignored) {
               }

               if (source.getEntity() != null && source.getEntity() instanceof EntityPlayer) {
                  ((EntityPlayer)source.getEntity()).addChatMessage(new ChatComponentText(this.getCommandSenderName() + " " + StatCollector.translateToLocal("tc.boss.enrage")));
               }
            }

            damage = 35.0F;
         }
      }

      return super.attackEntityFrom(source, damage);
   }

   public void generateName() {
   }
}
