package thaumcraft.common.entities.monster.mods;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface IChampionModifierEffect {
   float performEffect(LivingEntity var1, LivingEntity var2, DamageSource var3, float var4);

   void showFX(LivingEntity var1);
}
