package thaumcraft.common.entities.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thaumcraft.api.entities.ITaintedMob;

public class EntityTaintacleSmall extends EntityTaintacle implements ITaintedMob {
   int lifetime = 200;

   public EntityTaintacleSmall(World par1World) {
      super(par1World);
      this.setSize(0.22F, 1.0F);
      this.experienceValue = 0;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0F);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0F);
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.lifetime-- <= 0) {
         this.damageEntity(DamageSource.magic, 10.0F);
      }

   }

   public boolean getCanSpawnHere() {
      return false;
   }

   protected Item getDropItem() {
       return super.getDropItem();
   }

   protected void dropFewItems(boolean flag, int i) {
   }
}
