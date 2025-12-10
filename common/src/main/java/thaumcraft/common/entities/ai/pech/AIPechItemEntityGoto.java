package thaumcraft.common.entities.ai.pech;

import net.minecraft.world.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathPoint;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityPech;

import java.util.List;

public class AIPechItemEntityGoto extends EntityAIBase {
   private EntityPech pech;
   private Entity targetEntity;
   float maxTargetDistance = 16.0F;
   private int count;
   private int failedPathFindingPenalty;

   public AIPechItemEntityGoto(EntityPech par1EntityCreature) {
      this.pech = par1EntityCreature;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      if (this.pech.ticksExisted % Config.golemDelay > 0) {
         return false;
      } else if (--this.count > 0) {
         return false;
      } else {
         double range = Double.MAX_VALUE;
         List<Entity> targets = this.pech.level().getEntitiesWithinAABBExcludingEntity(this.pech, this.pech.boundingBox.expand(this.maxTargetDistance, this.maxTargetDistance, this.maxTargetDistance));
         if (targets.isEmpty()) {
            return false;
         } else {
            for(Entity e : targets) {
               if (e instanceof EntityItem && this.pech.canPickup(((EntityItem)e).getEntityItem())) {
                  NBTTagCompound itemData = e.getEntityData();
                  String username = ((EntityItem)e).func_145800_j();
                  if (username == null || !username.equals("PechDrop")) {
                     double distance = e.getDistanceSq(this.pech.posX, this.pech.posY, this.pech.posZ);
                     if (distance < range && distance <= (double)(this.maxTargetDistance * this.maxTargetDistance)) {
                        range = distance;
                        this.targetEntity = e;
                     }
                  }
               }
            }

             return this.targetEntity != null;
         }
      }
   }

   public boolean continueExecuting() {
      return this.targetEntity != null && (this.targetEntity.isEntityAlive() && !this.pech.getNavigator().noPath() && this.targetEntity.getDistanceSqToEntity(this.pech) < (double) (this.maxTargetDistance * this.maxTargetDistance));
   }

   public void resetTask() {
      this.targetEntity = null;
   }

   public void startExecuting() {
      this.pech.getNavigator().setPath(this.pech.getNavigator().getPathToEntityLiving(this.targetEntity), this.pech.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() * (double)1.5F);
      this.count = 0;
   }

   public void updateTask() {
      this.pech.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
      if (this.pech.getEntitySenses().canSee(this.targetEntity) && --this.count <= 0) {
         this.count = this.failedPathFindingPenalty + 4 + this.pech.getRNG().nextInt(4);
         this.pech.getNavigator().tryMoveToEntityLiving(this.targetEntity, this.pech.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue() * (double)1.5F);
         if (this.pech.getNavigator().getPath() != null) {
            PathPoint finalPathPoint = this.pech.getNavigator().getPath().getFinalPathPoint();
            if (finalPathPoint != null && this.targetEntity.getDistanceSq(finalPathPoint.xCoord, finalPathPoint.yCoord, finalPathPoint.zCoord) < (double)1.0F) {
               this.failedPathFindingPenalty = 0;
            } else {
               this.failedPathFindingPenalty += 10;
            }
         } else {
            this.failedPathFindingPenalty += 10;
         }
      }

      double distance = this.pech.getDistanceSq(this.targetEntity.posX, this.targetEntity.boundingBox.minY, this.targetEntity.posZ);
      if (distance <= (double)1.5F) {
         this.count = 0;
         int am = ((EntityItem)this.targetEntity).getEntityItem().stackSize;
         ItemStack is = this.pech.pickupItem(((EntityItem)this.targetEntity).getEntityItem());
         if (is != null && is.stackSize > 0) {
            ((EntityItem)this.targetEntity).setEntityItemStack(is);
         } else {
            this.targetEntity.setDead();
         }

         if (is == null || is.stackSize != am) {
            this.targetEntity.level().playSoundAtEntity(this.targetEntity, "random.pop", 0.2F, ((this.targetEntity.level().rand.nextFloat() - this.targetEntity.level().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
         }
      }

   }
}
