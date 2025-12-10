package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.damagesource.DamageSource;
import thaumcraft.common.Thaumcraft;

public class ChampionModVampire implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      boss.heal(Math.max(2.0F, amount / 2.0F));
      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!(boss.level().rand.nextFloat() > 0.2F)) {
         float w = boss.level().rand.nextFloat() * boss.width;
         float d = boss.level().rand.nextFloat() * boss.width;
         float h = boss.level().rand.nextFloat() * boss.height;
         Thaumcraft.proxy.drawGenericParticles(boss.level(), boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.0F, 0.0F, 0.0F, 0.9F + boss.level().rand.nextFloat() * 0.1F, 0.0F, 0.0F, 0.9F, false, 147, 4, 1, 8 + boss.level().rand.nextInt(4), 0, 0.5F + boss.level().rand.nextFloat() * 0.2F);
      }
   }
}
