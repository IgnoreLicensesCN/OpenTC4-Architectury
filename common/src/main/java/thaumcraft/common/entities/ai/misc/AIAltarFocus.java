package thaumcraft.common.entities.ai.misc;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.entities.monster.EntityCultistCleric;

public class AIAltarFocus extends EntityAIBase {
   private final EntityCultistCleric entity;
   private final World world;

   public AIAltarFocus(EntityCultistCleric par1EntityLiving) {
      this.entity = par1EntityLiving;
      this.world = par1EntityLiving.worldObj;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      return this.entity.getIsRitualist() && this.entity.getHomePosition() != null;
   }

    public boolean continueExecuting() {
      return this.entity.getIsRitualist() && this.entity.getHomePosition() != null;
   }

   public void updateTask() {
      if (this.entity.getHomePosition() != null && this.entity.ticksExisted % 40 == 0 && (this.entity.getHomePosition().getDistanceSquared((int)this.entity.posX, (int)this.entity.posY, (int)this.entity.posZ) > 16.0F || this.world.getBlock(this.entity.getHomePosition().posX, this.entity.getHomePosition().posY, this.entity.getHomePosition().posZ) != ConfigBlocks.blockEldritch)) {
         this.entity.setIsRitualist(false);
      }

   }
}
