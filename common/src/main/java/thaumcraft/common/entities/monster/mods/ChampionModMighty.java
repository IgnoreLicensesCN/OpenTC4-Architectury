package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.damagesource.DamageSource;
import thaumcraft.common.Thaumcraft;

public class ChampionModMighty implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float ammount) {
      return 0.0F;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!(boss.level().rand.nextFloat() > 0.3F)) {
         float w = boss.level().rand.nextFloat() * boss.width;
         float d = boss.level().rand.nextFloat() * boss.width;
         float h = boss.level().rand.nextFloat() * boss.height;
         int p = 176 + boss.level().rand.nextInt(4) * 3;
         Thaumcraft.proxy.drawGenericParticles(boss.level(), boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.0F, 0.0F, 0.0F, 0.8F + boss.level().rand.nextFloat() * 0.2F, 0.8F + boss.level().rand.nextFloat() * 0.2F, 0.8F + boss.level().rand.nextFloat() * 0.2F, 0.7F, false, p, 3, 1, 4 + boss.level().rand.nextInt(3), 0, 1.0F + boss.level().rand.nextFloat() * 0.3F);
      }
   }
}
