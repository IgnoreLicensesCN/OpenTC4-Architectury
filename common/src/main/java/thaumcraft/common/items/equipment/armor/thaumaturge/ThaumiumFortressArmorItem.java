package thaumcraft.common.items.equipment.armor.thaumaturge;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.items.ThaumcraftToolAndArmorMaterial;
import thaumcraft.common.items.abstracts.ISpecialDamageCalculationEquipmentItem;
import thaumcraft.common.items.abstracts.armorcomponents.IArmorComponentsOwnerItem;
import thaumcraft.common.items.abstracts.armorcomponents.IArmorDamageDecreaseMultiplierComponentItem;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;

public class ThaumiumFortressArmorItem extends ArmorItem implements IAugmentationRunicShieldProviderItem, ISpecialDamageCalculationEquipmentItem {
    public ThaumiumFortressArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public ThaumiumFortressArmorItem(Type type) {
        this(ThaumcraftToolAndArmorMaterial.THAUMIUM_FORTRESS, type, new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addShieldToolTip(itemStack, level, list, tooltipFlag);
    }

    @Override
    public float modifyDamageAfterCalculatedArmorAbsorb(LivingEntity living, ItemStack selfStack, float currentOutput, DamageSource damageSource, float input) {

        double ratio = getDefense() / (double)25.0F;
        if (damageSource.is(DamageTypes.MAGIC)) {//TODO:[maybe wont finished] find out Magic damage today
            ratio = getDefense() / (double)35.0F;
        } else if (
                !(damageSource.is(DamageTypeTags.IS_EXPLOSION) || damageSource.is(DamageTypeTags.IS_FIRE))
        ) {
            if (damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
                ratio = 0.0F;
            }
        } else {
            ratio = getDefense()/ (double)20.0F;
        }


        float ratioMultiplier = 0.875F;

        for (var armorItem:living.getArmorSlots()){
            if (armorItem.getItem() instanceof ThaumiumFortressArmorItem){
                ratioMultiplier += 0.125F;
            }
            if (armorItem.getItem() instanceof IArmorComponentsOwnerItem componentsOwner){
                for (var componentStack:componentsOwner.getArmorComponents(armorItem)){
                    if (componentStack.getItem() instanceof IArmorDamageDecreaseMultiplierComponentItem componentsDecreaseMultiplier){
                        ratioMultiplier += componentsDecreaseMultiplier.getAdditionalDamageDecreaseMultiplier(componentStack);
                    }
                }
            }
        }

        ratio *= ratioMultiplier;
        return (float) (currentOutput * (1-ratio));
    }
}
