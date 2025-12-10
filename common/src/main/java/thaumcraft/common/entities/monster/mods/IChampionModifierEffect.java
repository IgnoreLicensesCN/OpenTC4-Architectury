package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public interface IChampionModifierEffect {
   float performEffect(EntityLivingBase var1, EntityLivingBase var2, DamageSource var3, float var4);

   @SideOnly(Side.CLIENT)
   void showFX(EntityLivingBase var1);
}
