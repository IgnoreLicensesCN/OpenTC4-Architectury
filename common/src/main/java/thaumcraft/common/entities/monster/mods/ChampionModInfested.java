package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityTaintSpider;

public class ChampionModInfested implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      if (boss.worldObj.rand.nextFloat() < 0.4F && !boss.worldObj.isRemote) {
         EntityTaintSpider spiderling = new EntityTaintSpider(boss.worldObj);
         spiderling.setLocationAndAngles(boss.posX, boss.posY + (double)(boss.height / 2.0F), boss.posZ, boss.worldObj.rand.nextFloat() * 360.0F, 0.0F);
         boss.worldObj.spawnEntityInWorld(spiderling);
         boss.playSound("thaumcraft:gore", 0.5F, 1.0F);
      }

      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (boss.worldObj.rand.nextBoolean()) {
         Thaumcraft.proxy.slimeJumpFX(boss, 0);
      }

   }
}
