package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import thaumcraft.common.Thaumcraft;

public class ChampionModUndying implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase mob, EntityLivingBase target, DamageSource source, float amount) {
      if (mob.ticksExisted % 20 == 0) {
         mob.heal(1.0F);
      }

      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!boss.worldObj.rand.nextBoolean()) {
         float w = boss.worldObj.rand.nextFloat() * boss.width;
         float d = boss.worldObj.rand.nextFloat() * boss.width;
         float h = boss.worldObj.rand.nextFloat() * boss.height;
         Thaumcraft.proxy.drawGenericParticles(boss.worldObj, boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.0F, 0.03, 0.0F, 0.1F + boss.worldObj.rand.nextFloat() * 0.1F, 0.8F + boss.worldObj.rand.nextFloat() * 0.2F, 0.1F + boss.worldObj.rand.nextFloat() * 0.1F, 0.9F, true, 21, 4, 1, 4 + boss.worldObj.rand.nextInt(4), 0, 0.5F + boss.worldObj.rand.nextFloat() * 0.2F);
      }
   }
}
