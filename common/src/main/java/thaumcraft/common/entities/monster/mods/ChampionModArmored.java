package thaumcraft.common.entities.monster.mods;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.Thaumcraft;

public class ChampionModArmored implements IChampionModifierEffect {
   public float performEffect(LivingEntity mob, LivingEntity target, DamageSource source, float amount) {
      if (!source.isUnblockable()) {
         float f1 = amount * 19.0F;
         amount = f1 / 25.0F;
      }

      return amount;
   }

   public void showFX(LivingEntity boss) {
      if (boss.level().random.nextInt(4) == 0) {
         float w = boss.level().random.nextFloat() * boss.width;
         float d = boss.level().random.nextFloat() * boss.width;
         float h = boss.level().random.nextFloat() * boss.height;
         Thaumcraft.proxy.drawGenericParticles(boss.level(), boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.0F, 0.0F, 0.0F, 0.9F, 0.9F, 0.9F + boss.level().random.nextFloat() * 0.1F, 0.7F, false, 112, 9, 1, 5 + boss.level().random.nextInt(4), 0, 0.6F + boss.level().random.nextFloat() * 0.2F);
      }
   }
}
