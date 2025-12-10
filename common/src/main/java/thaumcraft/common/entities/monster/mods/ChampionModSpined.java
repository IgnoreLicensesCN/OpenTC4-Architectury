package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import thaumcraft.common.Thaumcraft;

public class ChampionModSpined implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      if (target != null && !source.damageType.equalsIgnoreCase("thorns")) {
         target.attackEntityFrom(DamageSource.causeThornsDamage(boss), (float)(1 + boss.worldObj.rand.nextInt(3)));
         target.playSound("damage.thorns", 0.5F, 1.0F);
      }
       return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!boss.worldObj.rand.nextBoolean()) {
         float w = boss.worldObj.rand.nextFloat() * boss.width;
         float d = boss.worldObj.rand.nextFloat() * boss.width;
         float h = boss.worldObj.rand.nextFloat() * boss.height;
         int p = 176 + boss.worldObj.rand.nextInt(4) * 3;
         Thaumcraft.proxy.drawGenericParticles(boss.worldObj, boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.0F, 0.0F, 0.0F, 0.5F + boss.worldObj.rand.nextFloat() * 0.2F, 0.1F + boss.worldObj.rand.nextFloat() * 0.2F, 0.1F + boss.worldObj.rand.nextFloat() * 0.2F, 0.7F, false, p, 3, 1, 3, 0, 1.2F + boss.worldObj.rand.nextFloat() * 0.3F);
      }
   }
}
