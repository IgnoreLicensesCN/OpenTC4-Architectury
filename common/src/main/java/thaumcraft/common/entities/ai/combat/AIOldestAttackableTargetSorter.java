package thaumcraft.common.entities.ai.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITarget;

import java.util.Comparator;

public class AIOldestAttackableTargetSorter implements Comparator {
   private Entity theEntity;
   final EntityAITarget parent;

   public AIOldestAttackableTargetSorter(EntityAITarget par1EntityAIOldestAttackableTarget, Entity par2Entity) {
      this.parent = par1EntityAIOldestAttackableTarget;
      this.theEntity = par2Entity;
   }

   public int compareAge(Entity par1Entity, Entity par2Entity) {
      int var3 = par1Entity.ticksExisted;
      int var5 = par2Entity.ticksExisted;
      return Integer.compare(var5, var3);
   }

   public int compare(Object par1Obj, Object par2Obj) {
      return this.compareAge((Entity)par1Obj, (Entity)par2Obj);
   }
}
