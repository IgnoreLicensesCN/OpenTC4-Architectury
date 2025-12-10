package thaumcraft.common.entities.monster.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class ChampionModDummy implements IChampionModifierEffect {
   public float performEffect(EntityLivingBase boss, EntityLivingBase target, DamageSource source, float amount) {
      return amount;
   }

   public void showFX(EntityLivingBase boss) {
   }
}
