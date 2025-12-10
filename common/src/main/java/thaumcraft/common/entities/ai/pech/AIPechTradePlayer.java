package thaumcraft.common.entities.ai.pech;

import net.minecraft.entity.ai.EntityAIBase;
import thaumcraft.common.entities.monster.EntityPech;

public class AIPechTradePlayer extends EntityAIBase {
   private EntityPech villager;

   public AIPechTradePlayer(EntityPech par1EntityVillager) {
      this.villager = par1EntityVillager;
      this.setMutexBits(5);
   }

   public boolean shouldExecute() {
      if (!this.villager.isEntityAlive()) {
         return false;
      } else if (this.villager.isInWater()) {
         return false;
      } else if (!this.villager.isTamed()) {
         return false;
      } else if (!this.villager.onGround) {
         return false;
      } else {
         return !this.villager.velocityChanged && this.villager.trading;
      }
   }

   public void startExecuting() {
      this.villager.getNavigator().clearPathEntity();
   }

   public void resetTask() {
      this.villager.trading = false;
   }
}
