package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITarget;

import java.util.Comparator;

public class AINearestAttackableTargetSorter implements Comparator {
   private Entity theEntity;
   final EntityAITarget parent;

   public AINearestAttackableTargetSorter(EntityAITarget par1EntityAINearestAttackableTarget, Entity par2Entity) {
      this.parent = par1EntityAINearestAttackableTarget;
      this.theEntity = par2Entity;
   }

   public int compareDistanceSq(Entity par1Entity, Entity par2Entity) {
      double var3 = this.theEntity.getDistanceSqToEntity(par1Entity);
      double var5 = this.theEntity.getDistanceSqToEntity(par2Entity);
      return Double.compare(var3, var5);
   }

   public int compare(Object par1Obj, Object par2Obj) {
      return this.compareDistanceSq((Entity)par1Obj, (Entity)par2Obj);
   }
}
