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
import thaumcraft.api.IWarpingGear;

import java.util.List;

//used for THAUMIC_FORTRESS_MASK_DISCOUNT
public class GrinningDevilMask extends Item implements IWarpingGear {
    public GrinningDevilMask(Properties properties) {
        super(properties);
    }
    public GrinningDevilMask() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public int getWarp(ItemStack itemstack, @Nullable Entity entityEquipped) {
        return 2 + (entityEquipped != null?entityEquipped.level().random: RandomSource.createNewThreadLocalInstance()).nextInt(4);
    }

    @Override
    public void addWarpTooltip(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("item.warping").withStyle(ChatFormatting.DARK_PURPLE)
                .append(Component.literal(" -5~-2").withStyle(ChatFormatting.DARK_PURPLE)));
    }
}
