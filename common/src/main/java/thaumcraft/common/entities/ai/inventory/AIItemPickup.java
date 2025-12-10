package thaumcraft.common.entities.ai.inventory;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.lib.utils.InventoryUtils;

public class AIItemPickup extends EntityAIBase {
   private EntityGolemBase theGolem;
   private Entity targetEntity;
   int count = 0;

   public AIItemPickup(EntityGolemBase par1EntityCreature) {
      this.theGolem = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      return this.theGolem.ticksExisted % Config.golemDelay <= 0 && this.findItem();
   }

   private boolean findItem() {
      double range = Double.MAX_VALUE;
      float dmod = this.theGolem.getRange();
      List<Entity> targets = this.theGolem.worldObj.getEntitiesWithinAABBExcludingEntity(this.theGolem, AxisAlignedBB.getBoundingBox(this.theGolem.getHomePosition().posX, this.theGolem.getHomePosition().posY, this.theGolem.getHomePosition().posZ, this.theGolem.getHomePosition().posX + 1, this.theGolem.getHomePosition().posY + 1, this.theGolem.getHomePosition().posZ + 1).expand(dmod, dmod, dmod));
      if (targets.isEmpty()) {
         return false;
      } else {
         for(Entity e : targets) {
            if (e instanceof EntityItem && ((EntityItem)e).delayBeforeCanPickup < 5 && (this.theGolem.inventory.allEmpty() || this.theGolem.inventory.getAmountNeededSmart(((EntityItem)e).getEntityItem(), this.theGolem.getUpgradeAmount(5) > 0) > 0) && (this.theGolem.getCarried() == null || InventoryUtils.areItemStacksEqualStrict(this.theGolem.getCarried(), ((EntityItem)e).getEntityItem()) && ((EntityItem)e).getEntityItem().stackSize <= this.theGolem.getCarrySpace())) {
               double distance = e.getDistanceSq((float)this.theGolem.getHomePosition().posX + 0.5F, (float)this.theGolem.getHomePosition().posY + 0.5F, (float)this.theGolem.getHomePosition().posZ + 0.5F);
               double distance2 = e.getDistanceSq(this.theGolem.posX, this.theGolem.posY, this.theGolem.posZ);
               if (distance2 < range && distance <= (double)(dmod * dmod)) {
                  range = distance2;
                   if (e.isEntityAlive()) {
                       this.targetEntity = e;
                   }
               }
            }
         }

          return this.targetEntity != null;
      }
   }

   public boolean continueExecuting() {
      return this.count-- > 0 && !this.theGolem.getNavigator().noPath() && this.targetEntity.isEntityAlive();
   }

   public void resetTask() {
      this.count = 0;
      this.targetEntity = null;
      this.theGolem.getNavigator().clearPathEntity();
   }

   public void updateTask() {
      this.theGolem.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
      double dist = this.theGolem.getDistanceSqToEntity(this.targetEntity);
      if (dist <= (double)2.0F) {
         this.pickUp();
      }

   }

   private void pickUp() {
      int amount = 0;
      if (this.targetEntity instanceof EntityItem) {
         ItemStack stack = ((EntityItem)this.targetEntity).getEntityItem().copy();
         if (((EntityItem)this.targetEntity).getEntityItem().stackSize < this.theGolem.getCarrySpace()) {
            amount = ((EntityItem)this.targetEntity).getEntityItem().stackSize;
         } else {
            amount = this.theGolem.getCarrySpace();
         }

         stack.stackSize = amount;
         ItemStack var10000 = ((EntityItem)this.targetEntity).getEntityItem();
         var10000.stackSize -= amount;
         if (this.theGolem.getCarried() == null) {
            this.theGolem.setCarried(stack);
         } else {
            var10000 = this.theGolem.getCarried();
            var10000.stackSize += amount;
         }
      }

      if (amount != 0) {
         this.targetEntity.worldObj.playSoundAtEntity(this.targetEntity, "random.pop", 0.2F, ((this.targetEntity.worldObj.rand.nextFloat() - this.targetEntity.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
      }
   }

   public void startExecuting() {
      this.count = 200;
      this.theGolem.getNavigator().tryMoveToEntityLiving(this.targetEntity, this.theGolem.getAIMoveSpeed());
   }
}
