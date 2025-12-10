package thaumcraft.api.wands;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import thaumcraft.api.aspects.AspectList;

@Deprecated(forRemoval = true)
public interface IWandFocus {
   int getFocusColor();

//   IIcon getFocusDepthLayerIcon();
//
//   IIcon getOrnament();

   WandFocusAnimation getAnimation();

   AspectList getVisCost();

   boolean isVisCostPerTick();

   ItemStack onFocusRightClick(ItemStack var1, Level var2, Player var3, HitResult var4);

   void onUsingFocusTick(ItemStack var1, Player var2, int var3);

   void onPlayerStoppedUsingFocus(ItemStack var1, Level var2, Player var3, int var4);

   String getSortingHelper(ItemStack var1);

   boolean onFocusBlockStartBreak(ItemStack var1, int var2, int var3, int var4, Player var5);

   boolean acceptsEnchant(Enchantment var1);

   enum WandFocusAnimation {
      WAVE,
      CHARGE
   }
}
