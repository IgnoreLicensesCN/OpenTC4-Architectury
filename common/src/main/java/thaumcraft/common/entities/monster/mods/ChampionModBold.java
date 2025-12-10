package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.client.Minecraft;
import thaumcraft.client.fx.particles.FXSpark;

public class ChampionModBold implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float ammount) {
      return 0.0F;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!boss.level().rand.nextBoolean()) {
         float w = boss.level().rand.nextFloat() * boss.width;
         float d = boss.level().rand.nextFloat() * boss.width;
         float h = boss.level().rand.nextFloat() * boss.height / 3.0F;
         FXSpark ef = new FXSpark(boss.level(), boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.2F);
         ef.setRBGColorF(0.3F - boss.level().rand.nextFloat() * 0.1F, 0.0F, 0.8F + boss.level().rand.nextFloat() * 0.2F);
         Minecraft.getInstance().particleEngine.add(ef);

      }
   }
}
