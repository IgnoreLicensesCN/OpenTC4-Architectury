package thaumcraft.common.items.equipment.armor.cultist;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.runicshield.IAugmentationRunicShieldProviderItem;

import java.util.List;

public class CultistPlateArmorItem extends ArmorItem implements IAugmentationRunicShieldProviderItem {
    public CultistPlateArmorItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }
    public CultistPlateArmorItem(Type type) {
        this(ArmorMaterials.IRON, type, new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        addShieldToolTip(itemStack, level, list, tooltipFlag);
    }
}
