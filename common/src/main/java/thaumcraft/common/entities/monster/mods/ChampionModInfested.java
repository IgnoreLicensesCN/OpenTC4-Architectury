package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.damagesource.DamageSource;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityTaintSpider;

public class ChampionModInfested implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      if (boss.level().rand.nextFloat() < 0.4F && Platform.getEnvironment() != Env.CLIENT) {
         EntityTaintSpider spiderling = new EntityTaintSpider(boss.level());
         spiderling.setLocationAndAngles(boss.posX, boss.posY + (double)(boss.height / 2.0F), boss.posZ, boss.level().rand.nextFloat() * 360.0F, 0.0F);
         boss.level().spawnEntityInWorld(spiderling);
         boss.playSound("thaumcraft:gore", 0.5F, 1.0F);
      }

      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (boss.level().rand.nextBoolean()) {
         Thaumcraft.proxy.slimeJumpFX(boss, 0);
      }

   }
}
