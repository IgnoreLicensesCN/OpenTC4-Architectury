package thaumcraft.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static thaumcraft.api.listeners.warp.WarpEventManager.getFinalWarp;

/**
 * 
 * @author Azanor
 * 
 * Armor, held items or bauble slot items that implement this interface add warp when equipped or held.
 *
 */

public interface IWarpingGear {
	
	/**
	 * returns how much warp this item adds while worn or held. 
	 */
    int getWarp(ItemStack itemstack,@Nullable Entity entityEquipped);

	default void addWarpTooltip(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		int warp = getWarp(itemStack,null);
		if (warp != 0
		) {
			list.add(Component.translatable("item.warping").withStyle(ChatFormatting.DARK_PURPLE)
					.append(Component.literal(" " + warp).withStyle(ChatFormatting.DARK_PURPLE)));
		}
	}
}
