package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import thaumcraft.common.Thaumcraft;

public class ChampionModGrim implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      if (boss.worldObj.rand.nextFloat() < 0.4F) {
         target.addPotionEffect(new PotionEffect(Potion.wither.id, 200));
      }

      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!boss.worldObj.rand.nextBoolean()) {
         float w = boss.worldObj.rand.nextFloat() * boss.width;
         float d = boss.worldObj.rand.nextFloat() * boss.width;
         float h = boss.worldObj.rand.nextFloat() * boss.height;
         Thaumcraft.proxy.drawGenericParticles(boss.worldObj, boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.0F, -0.02, 0.0F, boss.worldObj.rand.nextFloat() * 0.2F, boss.worldObj.rand.nextFloat() * 0.2F, boss.worldObj.rand.nextFloat() * 0.2F, 0.8F, false, 160, 10, 1, 8 + boss.worldObj.rand.nextInt(4), 0, 0.6F + boss.worldObj.rand.nextFloat() * 0.4F);
      }
   }
}
