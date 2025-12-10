package thaumcraft.api.wands;

import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.api.aspects.AspectList;

public interface IWandFocus {
   int getFocusColor();

   IIcon getFocusDepthLayerIcon();

   IIcon getOrnament();

   WandFocusAnimation getAnimation();

   AspectList getVisCost();

   boolean isVisCostPerTick();

   ItemStack onFocusRightClick(ItemStack var1, World var2, Player var3, MovingObjectPosition var4);

   void onUsingFocusTick(ItemStack var1, Player var2, int var3);

   void onPlayerStoppedUsingFocus(ItemStack var1, World var2, Player var3, int var4);

   String getSortingHelper(ItemStack var1);

   boolean onFocusBlockStartBreak(ItemStack var1, int var2, int var3, int var4, Player var5);

   boolean acceptsEnchant(int var1);

   enum WandFocusAnimation {
      WAVE,
      CHARGE
   }
}
