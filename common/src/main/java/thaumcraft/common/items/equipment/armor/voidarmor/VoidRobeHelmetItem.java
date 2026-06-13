package thaumcraft.common.items.equipment.armor.voidarmor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.IGoggles;
import thaumcraft.common.items.ThaumcraftToolAndArmorMaterial;

public class VoidRobeHelmetItem extends VoidRobeArmorItem implements IGoggles {
    public VoidRobeHelmetItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }

    public VoidRobeHelmetItem() {
        this(ThaumcraftToolAndArmorMaterial.ARMOR_VOID,Type.HELMET,new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
