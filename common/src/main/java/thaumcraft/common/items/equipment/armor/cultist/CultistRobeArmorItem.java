package thaumcraft.common.items.equipment.armor.cultist;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IVisDiscountGearItem;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;

public class CultistRobeArmorItem extends ArmorItem implements
        IAugmentationRunicShieldProviderItem,
        IWarpingGear,
        IVisDiscountGearItem
{
    public CultistRobeArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public CultistRobeArmorItem(Type type) {
        this(ArmorMaterials.IRON, type, new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addVisDiscountToolTip(itemStack,level,list,tooltipFlag,null,null);
        addShieldToolTip(itemStack, level, list, tooltipFlag);
        addWarpTooltip(itemStack, level, list, tooltipFlag);
    }

    @Override
    public int getVisCostPercentDecrease(ItemStack stack, @Nullable LivingEntity living, @Nullable Aspect aspect) {
        return 1;
    }

    @Override
    public int getWarp(ItemStack itemstack, @Nullable Entity entityEquipped) {
        return 1;
    }
}
