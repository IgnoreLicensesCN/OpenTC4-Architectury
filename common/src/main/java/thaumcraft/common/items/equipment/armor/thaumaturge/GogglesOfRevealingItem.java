package thaumcraft.common.items.equipment.armor.thaumaturge;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscountGearItem;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.ThaumcraftToolAndArmorMaterial;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;

public class GogglesOfRevealingItem extends ArmorItem implements IVisDiscountGearItem, IGoggles, IAugmentationRunicShieldProviderItem {
    public GogglesOfRevealingItem(ArmorMaterial armorMaterial, Properties properties) {
        super(armorMaterial, Type.HELMET, properties);
    }
    public GogglesOfRevealingItem() {
        this(ThaumcraftToolAndArmorMaterial.SPECIAL,
                new Properties().durability(350).rarity(Rarity.RARE)
        );
    }

    @Override
    public int getVisCostPercentDecrease(ItemStack stack, @Nullable LivingEntity living, @Nullable Aspect aspect) {
        return 5;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addVisDiscountToolTip(itemStack,level,list,tooltipFlag,null,null);
        addShieldToolTip(itemStack,level,list,tooltipFlag);
    }
    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
