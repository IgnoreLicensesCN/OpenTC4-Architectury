package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import thaumcraft.common.Thaumcraft;

public class ChampionModWarp implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      if (boss.worldObj.rand.nextFloat() < 0.33F && target instanceof EntityPlayer) {
         Thaumcraft.addWarpToPlayer((EntityPlayer)target, 1 + boss.worldObj.rand.nextInt(3), true);
      }

      return amount;
   }

   @SideOnly(Side.CLIENT)
   public void showFX(EntityLivingBase boss) {
      if (!boss.worldObj.rand.nextBoolean()) {
         float w = boss.worldObj.rand.nextFloat() * boss.width;
         float d = boss.worldObj.rand.nextFloat() * boss.width;
         float h = boss.worldObj.rand.nextFloat() * boss.height;
         Thaumcraft.proxy.drawGenericParticles(boss.worldObj, boss.boundingBox.minX + (double)w, boss.boundingBox.minY + (double)h, boss.boundingBox.minZ + (double)d, 0.0F, 0.0F, 0.0F, 0.8F + boss.worldObj.rand.nextFloat() * 0.2F, 0.0F, 0.9F + boss.worldObj.rand.nextFloat() * 0.1F, 0.7F, true, 72, 8, 1, 10 + boss.worldObj.rand.nextInt(4), 0, 0.6F + boss.worldObj.rand.nextFloat() * 0.4F);
      }
   }
}
