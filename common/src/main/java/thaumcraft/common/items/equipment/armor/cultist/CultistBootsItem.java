package thaumcraft.common.items.equipment.armor.cultist;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.abstracts.IVisDiscountGearItem;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;

public class CultistBootsItem extends ArmorItem implements IVisDiscountGearItem, IAugmentationRunicShieldProviderItem, IWarpingGear {
    public CultistBootsItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public CultistBootsItem(){
        this(ArmorMaterials.IRON,Type.BOOTS,new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public int getVisCostPercentDecrease(ItemStack stack, @Nullable LivingEntity living, @Nullable Aspect aspect) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addVisDiscountToolTip(itemStack,level,list,tooltipFlag,null,null);
        addShieldToolTip(itemStack,level,list,tooltipFlag);
        addWarpTooltip(itemStack,level,list,tooltipFlag);
    }

    @Override
    public int getWarp(ItemStack itemstack, @Nullable Entity entityEquipped) {
        return 1;
    }
    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.FEET;
    }
}
