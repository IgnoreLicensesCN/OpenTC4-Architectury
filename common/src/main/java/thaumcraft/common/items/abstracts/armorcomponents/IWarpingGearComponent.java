package thaumcraft.common.items.abstracts.armorcomponents;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IWarpingGearComponent {
    int getWarpAsComponent(ItemStack selfStack,ItemStack parentStack, @Nullable Entity entityEquipped);

    default void addWarpTooltipAsComponent(ItemStack selfStack,ItemStack parentStack, Level level, List<Component> list, TooltipFlag tooltipFlag)
    {
        int warp = getWarpAsComponent(selfStack,parentStack,null);
        if (warp != 0
        ) {
            list.add(Component.translatable("item.warping").withStyle(ChatFormatting.DARK_PURPLE)
                    .append(Component.literal(" " + warp).withStyle(ChatFormatting.DARK_PURPLE)));
        }
    }
}
