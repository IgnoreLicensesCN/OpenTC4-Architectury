package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import thaumcraft.common.Thaumcraft;

public class ChampionModWarp implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      if (boss.level().rand.nextFloat() < 0.33F && target instanceof Player) {
         Thaumcraft.addWarpToPlayer((Player)target, 1 + boss.level().rand.nextInt(3), true);
      }

      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!boss.level().rand.nextBoolean()) {
         float w = boss.level().rand.nextFloat() * boss.width;
         float d = boss.level().rand.nextFloat() * boss.width;
         float h = boss.level().rand.nextFloat() * boss.height;
         Thaumcraft.proxy.drawGenericParticles(boss.level(), boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.0F, 0.0F, 0.0F, 0.8F + boss.level().rand.nextFloat() * 0.2F, 0.0F, 0.9F + boss.level().rand.nextFloat() * 0.1F, 0.7F, true, 72, 8, 1, 10 + boss.level().rand.nextInt(4), 0, 0.6F + boss.level().rand.nextFloat() * 0.4F);
      }
   }
}
