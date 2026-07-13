package thaumcraft.common.items.equipment.masks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.abstracts.armorcomponents.IWarpingGearComponent;

import java.util.List;

public class GrinningDevilMaskItem extends Item implements IWarpingGearComponent {
    public GrinningDevilMaskItem(Properties properties) {
        super(properties);
    }
    public GrinningDevilMaskItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public int getWarpAsComponent(ItemStack itemstack,ItemStack parentStack, @Nullable Entity entityEquipped) {
        return 2 + (entityEquipped != null?entityEquipped.level().random: RandomSource.createNewThreadLocalInstance()).nextInt(4);
    }

    @Override
    public void addWarpTooltipAsComponent(ItemStack itemStack,ItemStack parentStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("item.warping").withStyle(ChatFormatting.DARK_PURPLE)
                .append(Component.literal(" -5~-2").withStyle(ChatFormatting.DARK_PURPLE)));
    }
}
